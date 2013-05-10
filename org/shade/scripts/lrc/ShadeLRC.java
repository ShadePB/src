package org.shade.scripts.lrc;

import org.powerbot.core.Bot;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.core.script.methods.Game;
import org.powerbot.core.script.util.Timer;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.tab.Summoning;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.DepositBox;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;
import org.shade.scripts.lrc.misc.MousePaint;
import org.shade.scripts.lrc.misc.Variables;
import org.shade.scripts.lrc.nodes.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

@Manifest(authors = { "Shade" }, description = "Mines in the LRC. Coal/Gold. Bank/Drop. Urns, Summoning and death walk.", name = "ShadeLRC", version = 0.5, singleinstance = true)
public class ShadeLRC extends ActiveScript implements PaintListener, MessageListener, MouseListener {

    private Tree jobContainer = null;

    private Client client = Bot.client();

    private final Timer RUN_TIME = new Timer(0);
    private static final RenderingHints antialiasing = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    public synchronized final void provide(final Node... jobs) {
        List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
        for (final Node job : jobs) {
            if(!jobsCollection.contains(job)) {
                jobsCollection.add(job);
            }
        }
        jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

	public void onStart() {
        if(Game.isLoggedIn()) {
            Context.setLoginWorld(84);
            if(Inventory.getItem(Variables.PICKAXE_IDS) != null) {
                Variables.pickaxeEquipped = false;
            } if(Inventory.getItem(Variables.URN_ID) != null || Inventory.getItem(Variables.FINISHED_URN_ID) != null
                    || Inventory.getItem(Variables.CURRENT_URN_ID) != null) {
                Variables.usingUrns = true;
                Variables.pickaxeEquipped = false;
            }
            Variables.startTime = System.currentTimeMillis();
            Variables.startExp = Skills.getExperience(Skills.MINING);
            Variables.orePrice = lookup(Variables.GOLD_ORE_ID);
            if(Inventory.getItem(Variables.ALL_POUCHES_ID) != null || Summoning.isFamiliarSummoned()) {
                Variables.usingSummoning = true;
                Variables.pickaxeEquipped = false;
            }
            provide(new InterfaceCloser(), new Urns(), new Banking(), new Death(), new Summon(), new Combat(), new Mine(), new Deposit());
        } else {
            stop();
        }
	}

    //Paint fix, X303
	
	@Override
	public int loop() {
		if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
	        return 1000;
	    }
		if (client != Bot.client()) {
	       WidgetCache.purge();
	       Bot.context().getEventManager().addListener(this);
	       client = Bot.client();
	    }
		Camera.setPitch(99);
        if (jobContainer != null) {
            final Node job = jobContainer.state();
            if (job != null) {
                jobContainer.set(job);
                getContainer().submit(job);
                job.join();
            }
        }
        return Random.nextInt(100, 150);
    }

	public static boolean fullInventory() {
		if(DepositBox.isOpen()) {
            WidgetChild w = Widgets.get(11, 17).getChild(11);
            if(w.validate()) {
			    if(w.getModelId() != -1) {
					return true;
				}
            }
		} else {
			if(Inventory.isFull()) {
				return true;
			}
		}
		return false;
	}

    //Credits Enfilade's easel

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    private final Color color1 = new Color(0, 255, 255);
    private final Font font1 = new Font("Arial", 1, 11);
    private final Image img1 = getImage("http://i.imgur.com/xPJKvkG.png");
    private final Image img2 = getImage("http://i.imgur.com/EUudAwg.png");
    MousePaint mPaint = new MousePaint();

    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        g.setRenderingHints(antialiasing);
        
        int expGained = Skills.getExperience(Skills.MINING) - Variables.startExp;
        int expHour = (int) ((expGained) * 3600000D / (System.currentTimeMillis() - Variables.startTime));
        int profitGained = Variables.oresMined * Variables.orePrice;
        int profitHour = (int) ((profitGained) * 3600000D / (System.currentTimeMillis() - Variables.startTime));
        int oresHour = (int) ((Variables.oresMined) * 3600000D / (System.currentTimeMillis() - Variables.startTime));

        if(Variables.banking) {
        g.drawImage(img1, 73, -47 + 50, null);
        } else {
        	g.drawImage(img2, 73, -47 + 50, null);
        }
        g.setFont(font1);
        g.setColor(color1);
        g.drawString("" + RUN_TIME.toElapsedString(), 406, 64 + 50);
        g.drawString("" + Variables.status, 369, 76 + 50);
        g.drawString("" + NumberFormat.getInstance().format(expGained) + " (" + NumberFormat.getInstance().format(expHour) + ")", 395, 88 + 50);
        if(Variables.banking) {
        	g.drawString("" + NumberFormat.getInstance().format(profitGained) + " (" + NumberFormat.getInstance().format(profitHour) + ")", 403, 100 + 50);
        } else {
        	g.drawString("N/A", 403, 100 + 50);
        }
        g.drawString("" + NumberFormat.getInstance().format(Variables.oresMined) + " (" + NumberFormat.getInstance().format(oresHour) + ")", 398, 112 + 50);
        mPaint.Draw(g);
    }

	@Override
	public void messageReceived(MessageEvent e) {
		if(e.getMessage().contains("manage")) {
			Mine.gettingOre.reset();
			Variables.oresMined++;
		} if(e.getMessage().contains("additional")) {
			Mine.gettingOre.reset();
			Variables.oresMined++;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Rectangle b = new Rectangle(300, 7 + 50, 17, 57);
		Rectangle d = new Rectangle(300, 72 + 50, 17, 57);
		if(b.contains(e.getPoint())) {
			Variables.banking = true;
		} else if(d.contains(e.getPoint())) {
			Variables.banking = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

    //Credits Coma
    public static int lookup(final int id) {
        final String add = "http://scriptwith.us/api/?return=text&item=" + id;
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(
                new URL(add).openConnection().getInputStream()))) {
            final String line = in.readLine();
            return Integer.parseInt(line.split("[:]")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
	
}