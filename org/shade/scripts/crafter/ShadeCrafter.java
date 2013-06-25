package org.shade.scripts.crafter;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.powerbot.core.Bot;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;

import org.powerbot.game.client.Client;
import org.shade.scripts.crafter.misc.MousePaint;
import org.shade.scripts.crafter.misc.Tiles;
import org.shade.scripts.crafter.misc.Variables;
import org.shade.scripts.crafter.nodes.Banking;
import org.shade.scripts.crafter.nodes.Crafting;
import org.shade.scripts.crafter.nodes.RandomBox;
import sk.action.ActionBar;
import sk.action.book.magic.Spell;

@Manifest(name = "ShadeCrafter", description = "AIO Crafter. Lunar spells, Gold bar smelting, Jewellery, Leather crafting, Glass blowing, Battlestaff crafting, flax spinning and more!", version = 3.00, authors = { "Shade" }, instances = 99)
public class ShadeCrafter extends ActiveScript implements PaintListener, MouseListener, MouseMotionListener, MessageListener {
	
	public double version = 3.00;

    private Tree jobContainer = null;

    private Client client = Bot.client();

    public synchronized final void provide(final Node... jobs) {
        java.util.List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
        for (final Node job : jobs) {
            if(!jobsCollection.contains(job)) {
                jobsCollection.add(job);
            }
        }
        jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }
	
	MousePaint mousePaint = new MousePaint();
	
	long startTime = System.currentTimeMillis();
    long millis = 0;
    long hours = 0;
    long minutes = 0;
    long seconds = 0;
	
    int craftingStartExp;
    int craftingExpGained;
    int craftingExpHour;
    int smithingStartExp;
    int smithingExpHour;
    int magicExpGained;
    int magicExpHour;
    int magicStartExp;
    int made = 0;
    int madeHour = 0;
    int profitGained = 0;
    int profitHour = 0;
    public static String urnType;

	Timer runTime = new Timer(0);

	public void onStart() {
		guiStoof();
		craftingStartExp = Skills.getExperience(Skills.CRAFTING);
		smithingStartExp = Skills.getExperience(Skills.SMITHING);
		magicStartExp = Skills.getExperience(Skills.MAGIC);
		while(!Variables.startScript) {
            Variables.status = "Waiting on GUI";
			Task.sleep(50);
		}
		if(Variables.glassMake) {
			if(Game.isLoggedIn()) {
				ActionBar.makeReadyForInteract();
				ActionBar.dragToSlot(Spell.SUPERGLASS_MAKE.getChild(), 1);
			}
		} if(Variables.leatherMake) {
			if(Game.isLoggedIn()) {
				ActionBar.makeReadyForInteract();
				ActionBar.dragToSlot(Spell.MAKE_LEATHER.getChild(), 1);
			}
		}
        Variables.status = "Grabbing prices";
        if(Variables.leatherMake) {
            Variables.bodyRunePrice = lookup(559);
            Variables.hidePrice = lookup(Variables.leatherUsing);
            Variables.leatherPrice = lookup(Variables.madeLeather);
            Variables.astralRunePrice = lookup(9075);
        } if(Variables.bracelets && !Variables.usingGems) {
            if(Variables.amulets) {
                Variables.goldBarPrice = lookup(Variables.GOLD_BAR_ID);
                Variables.braceletPrice = lookup(1673);
            } if (!Variables.amulets && Variables.bracelets) {
                Variables.goldBarPrice = lookup(Variables.GOLD_BAR_ID);
                Variables.braceletPrice = lookup(Variables.BRACELET_ID);
            }
        } if(Variables.flax) {
            Variables.flaxPrice = lookup(Variables.FLAX_ID);
            Variables.bowStringPrice = lookup(Variables.BOW_STRING_ID);
        } if(Variables.glassMake) {
            Variables.sandPrice = lookup(Variables.SAND_ID);
            Variables.seaweedPrice = lookup(Variables.SEAWEED_ID);
            Variables.airRunePrice = lookup(556);
            Variables.astralRunePrice = lookup(9075);
            Variables.moltenGlassPrice = lookup(Variables.MOLTEN_GLASS_ID);
        }
        Variables.urnType = urnType;
        provide(new RandomBox(), new Crafting(), new Banking());
	}

	@Override
	public int loop() {
        if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
            return 1000;
        }
        if (client != Bot.client()) {
            WidgetCache.purge();
            Bot.context().getEventManager().addListener(this);
            client = Bot.client();
        } if (jobContainer != null) {
            final Node job = jobContainer.state();
            if (job != null) {
                jobContainer.set(job);
                getContainer().submit(job);
                job.join();
            }
        }
		if(Variables.glassMake || Variables.leatherMake) {
			Mouse.setSpeed(Mouse.Speed.VERY_FAST);
		} else {
			if(Random.nextInt(1, 4) > 2) {
				Mouse.setSpeed(Mouse.Speed.FAST);
			} else {
				Mouse.setSpeed(Mouse.Speed.NORMAL);
			}
		}
		if(Widgets.get(205, 0).validate()) {
			if(Widgets.get(205, 62).click(true)) {
				Task.sleep(900, 1000);
			}
		} if(Settings.get(1173) == 805306368) {
			Widgets.get(548, 43).interact("Toggle Production Dialog");
			Task.sleep(1300, 1400);
		}
        return Random.nextInt(300, 400);
    }
	
	public static void walk(final Tile... path) {
		if(!Walking.isRunEnabled()) {
			if (Walking.getEnergy() > Random.nextInt(30, 50)) {
				Walking.setRun(true);
				Task.sleep(400, 500);
			}
		}
		for (int i = path.length - 1; i >= 0; i --) {
			if (Calculations.distanceTo(path[i]) >= 15) {
				continue;
			}
			if (Walking.walk(path[i])) {
				break;
			}
		}
	}
	
	public void messageReceived(MessageEvent e) {
		if(e.getMessage().contains("You remove")){
			made++;
		} if(Variables.glass || Variables.needleAndThread) {
			if(e.getMessage().contains("make")) {
				made++;
			}
		} if(Variables.bars) {
			if(e.getMessage().contains("retrieve")) {
				made++;
			}
		} if(Variables.gemCutting) {
			if(e.getMessage().contains("cut")) {
				made++;
			}
		}
	}

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    private final Color color1 = new Color(255, 255, 255);

    private final Font font1 = new Font("Tahoma", 0, 9);

    private final Image img1 = getImage("http://i.imgur.com/934TY.png");
    private final Image img2 = getImage("http://i.imgur.com/psNS5.png");

   private final RenderingHints antialiasing = new RenderingHints(
           RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
	        
	    millis = System.currentTimeMillis() - startTime;
        hours = millis / (1000 * 60 * 60);
        millis -= hours * (1000 * 60 * 60);
        minutes = millis / (1000 * 60);
        millis -= minutes * (1000 * 60);
        seconds = millis / 1000;
            
        craftingExpGained = Skills.getExperience(Skills.CRAFTING) - craftingStartExp;
        craftingExpHour = (int) ((craftingExpGained) * 3600000D / (System.currentTimeMillis() - startTime));
        magicExpGained = Skills.getExperience(Skills.MAGIC) - magicStartExp;
        magicExpHour = (int) ((magicExpGained) * 3600000D / (System.currentTimeMillis() - startTime));
        int smithingExpGained = Skills.getExperience(Skills.SMITHING) - smithingStartExp;
        smithingExpHour = (int) ((smithingExpGained) * 3600000D / (System.currentTimeMillis() - startTime));
        
        if(Variables.flax) {
        	if(craftingExpGained != 0) {
        		made = (int) (craftingExpGained / 15);
        	}
        } 
        if(Variables.startScript) {
        	if(Variables.bracelets) {
		        if(Variables.urnType.equals("Sapphire bracelet")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 60);
		        	}
		        } if(Variables.urnType.equals("Emerald bracelet")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 65);
		        	}
		        } if(Variables.urnType.equals("Ruby bracelet")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 80);
		        	}
		        } if(Variables.urnType.equals("Gold bracelet")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 25);
	        		}
	        	} if(Variables.urnType.equals("Diamond bracelet")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 95);
	        		}
	        	}  if(Variables.urnType.equals("Dragonstone bracelet")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 110);
	        		}
	        	}
        	} if(Variables.amulets) {
	        	if(Variables.urnType.equals("Sapphire amulet")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 65);
		        	}
		        } if(Variables.urnType.equals("Emerald amulet")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 70);
		        	}
		        } if(Variables.urnType.equals("Ruby amulet")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 85);
		        	}
		        } if(Variables.urnType.equals("Gold amulet")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 30);
	        		}
	        	} if(Variables.urnType.equals("Diamond amulet")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 100);
	        		}
	        	}  if(Variables.urnType.equals("Dragonstone amulet")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 150);
	        		}
	        	}
        	} if(Variables.silver) {
        		if(Variables.urnType.equals("Unstrung symbol")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 50);
		        	}
		        } if(Variables.urnType.equals("Tiara")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 52.5);
		        	}
		        } if(Variables.urnType.equals("Unstrung emblem")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 50);
		        	}
		        } if(Variables.urnType.equals("Silver sickle")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 50);
		        	}
		        }
        	} if(Variables.necklaces) {
	        	if(Variables.urnType.equals("Sapphire necklace")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 55);
		        	}
		        } if(Variables.urnType.equals("Emerald necklace")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 60);
		        	}
		        } if(Variables.urnType.equals("Ruby necklace")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 75);
		        	}
		        } if(Variables.urnType.equals("Gold necklace")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 20);
	        		}
	        	} if(Variables.urnType.equals("Diamond necklace")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 90);
	        		}
	        	}  if(Variables.urnType.equals("Dragonstone necklace")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 105);
	        		}
	        	}
        	} if(Variables.rings) {
	        	if(urnType.equals("Sapphire ring")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 40);
		        	}
		        } if(urnType.equals("Emerald ring")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 55);
		        	}
		        } if(urnType.equals("Ruby ring")) {
		        	if(craftingExpGained != 0) {
		        		made = (int) (craftingExpGained / 70);
		        	}
		        } if(urnType.equals("Gold ring")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 15);
	        		}
	        	} if(urnType.equals("Diamond ring")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 85);
	        		}
	        	}  if(urnType.equals("Dragonstone ring")) {
	        		if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 100);
	        		}
	        	}
        	} if(Variables.battlestaff) {
        		if(urnType.contains("Earth")) {
        			if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 112.5);
	        		}
        		} if(urnType.contains("Water")) {
        			if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 100);
	        		}
        		} if(urnType.contains("Air")) {
        			if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 137.5);
	        		}
        		} if(urnType.contains("Fire")) {
        			if(craftingExpGained != 0) {
	        			made = (int) (craftingExpGained / 125);
	        		}
        		}
        	}
        }
        madeHour = (int) ((made) * 3600000D / (System.currentTimeMillis() - startTime));
        if(Variables.bracelets) {
        	profitGained = (int) (((Variables.braceletPrice - Variables.goldBarPrice)) * made);
        } if(Variables.flax) {
        	profitGained = (int) ((Variables.bowStringPrice - (Variables.flaxPrice)) * made);
        } if(Variables.glassMake) {
        	int amountOfGlass = (craftingExpGained / 130) * 17;
        	int costPerCast = Variables.seaweedPrice * 13 + Variables.sandPrice * 13 + Variables.astralRunePrice * 2 + Variables.airRunePrice * 10;
        	made = craftingExpGained / 130;
        	profitGained = (int) ((Variables.moltenGlassPrice * amountOfGlass) - (costPerCast * made));
        } if(Variables.leatherMake) {
        	made = (int) magicExpGained / 87;
        	profitGained = (int) (((Variables.leatherPrice - Variables.hidePrice) * 5) - 2 * (Variables.astralRunePrice + Variables.bodyRunePrice)) * made;
        }
        profitHour = (int) ((profitGained) * 3600000D / (System.currentTimeMillis() - startTime));
        
        g.setRenderingHints(antialiasing);
        
        if(!Variables.hide) {
        	if(Variables.needleAndThread) {
	        	g.setColor(Color.BLACK);
	            g.fillRect(351, 288, 166, 15);
	        	g.setColor(Color.WHITE);
	        	g.drawRect(351, 288, 166, 15);
	            g.setColor(Color.WHITE);
	            g.drawString("Use Sacred clay: " + Variables.useSacredClay, 355, 299);
        	}
            g.drawImage(img1, 6, 344 + 50, null);
            g.setFont(font1);
            g.setColor(color1);
            g.drawString("" + runTime.toElapsedString(), 28, 392 + 50);
            g.drawString("" + Variables.status, 28, 421 + 50);
            g.drawString("Profit: " + NumberFormat.getInstance().format(profitGained) + " (" + NumberFormat.getInstance().format(profitHour) + "/hour)", 28, 451 + 50);
            if(Variables.bars) {
            	g.drawString("Exp: " + NumberFormat.getInstance().format(smithingExpGained) + " (" + NumberFormat.getInstance().format(smithingExpHour) + "/hour)", 325, 368 + 50);
            	g.drawString("Time til level " + (Skills.getLevel(Skills.SMITHING) + 1) + ": " + getTimeTillLevel(Skills.SMITHING, smithingExpGained, smithingExpHour), 325, 396 + 50);
            } if(!Variables.bars && !Variables.leatherMake) {
            	g.drawString("Exp: " + NumberFormat.getInstance().format(craftingExpGained) + " (" + NumberFormat.getInstance().format(craftingExpHour) + "/hour)", 325, 368 + 50);
            	g.drawString("Time til level " + (Skills.getLevel(Skills.CRAFTING) + 1) + ": " + getTimeTillLevel(Skills.CRAFTING, craftingExpGained, craftingExpHour), 325, 396 + 50);
            } if(Variables.leatherMake) {
            	g.drawString("Exp: " + NumberFormat.getInstance().format(magicExpGained) + " (" + NumberFormat.getInstance().format(magicExpHour) + "/hour)", 325, 368 + 50);
            	g.drawString("Time til level " + (Skills.getLevel(Skills.CRAFTING) + 1) + ": " + getTimeTillLevel(Skills.MAGIC, magicExpGained, magicExpHour), 325, 396 + 50);
            }
            g.drawString("Made: " + NumberFormat.getInstance().format(made) + " (" + NumberFormat.getInstance().format(madeHour) + "/hour)", 325, 426 + 50);
            g.drawString("" + urnType, 325, 457 + 50);
        } else {
        	g.drawImage(img2, 6, 344 + 50, null);
        }
        mousePaint.Draw(g);
    }
    
    private String getTimeTillLevel(int skill, int expGained, int expPerHour) {
    	int currentLevel = Skills.getLevel(skill);
    	if(currentLevel != 99) {
	    	int expTillNextLevel = Skills.getExperienceToLevel(skill, currentLevel + 1);
	    	if (expGained > 0) {
	    		return Time.format((long) ((double) expTillNextLevel / (double) expPerHour * 3600000));
	    	}
    	}
    		return "0:0:0";
    } 

    public void mouseClicked(MouseEvent e) {
    	Rectangle needle = new Rectangle(351, 288, 166, 15);
 		Rectangle hidePaint = new Rectangle(484, 343 + 50, 15, 130);
	    if(hidePaint.contains(e.getPoint())) {
	   	 if(Variables.hide) {
             Variables.hide = false;
	   	 } else {
             Variables.hide = true;
	   	 }
	    } if(needle.contains(e.getPoint())) {
 			if(Variables.useSacredClay) {
                Variables.useSacredClay = false;
			} else {
                Variables.useSacredClay = true;
			}
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
		public void mouseReleased(MouseEvent e) {
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {	
		}
		
		public void guiStoof() {
			try {
		        SwingUtilities.invokeLater(new Runnable() {
		                        public void run() {
		                        	 final gui gui = new gui();
		             	            gui.setVisible(true);
		                        }
		        });
			} catch (Throwable ignore) {
		        log.info("Could not load GUI.");
		        return ;
			}
		}
	 
	 @SuppressWarnings("serial")
	 public class gui extends JFrame {
			public gui() {
				initComponents();
			}
			
			private void startButtonActionPerformed(ActionEvent e) {
				String whatToMake = tree1.getSelectionPath().toString().replace("(root), ", "");
				if(whatToMake.contains("Furnace")) {
					if(whatToMake.contains("Bracelets")) {
						if(whatToMake.contains("Gold")) {
                            Variables.usingGems = false;
							urnType = "Gold bracelet";
                            Variables.bracelets = true;
						} if(whatToMake.contains("Sapphire")) {
                            Variables.usingGems = true;
                            Variables.selectedGem = Variables.SAPPHIRE_ID;
							urnType = "Sapphire bracelet";
                            Variables.bracelets = true;
						} if(whatToMake.contains("Emerald")) {
                            Variables.usingGems = true;
                            Variables.selectedGem = Variables.EMERALD_ID;
							urnType = "Emerald bracelet";
                            Variables.bracelets = true;
						} if(whatToMake.contains("Ruby")) {
                            Variables.usingGems = true;
                            Variables.selectedGem = Variables.RUBY_ID;
							urnType = "Ruby bracelet";
                            Variables.bracelets = true;
						} if(whatToMake.contains("Diamond")) {
                            Variables.usingGems = true;
							Variables.selectedGem = Variables.DIAMOND_ID;
							urnType = "Diamond bracelet";
                            Variables.bracelets = true;
						} if(whatToMake.contains("Dragonstone")) {
                            Variables.usingGems = true;
                            Variables.selectedGem = Variables.DRAGONSTONE_ID;
							urnType = "Dragonstone bracelet";
                            Variables.bracelets = true;
						}
					} if(whatToMake.contains("Amulets")) {
                        Variables.bracelets = true;
                        Variables.amulets = true;
						if(whatToMake.contains("Gold")) {
							urnType = "Gold amulet";
                            Variables.usingGems = false;
                            Variables.amulets = true;
                            Variables.bracelets = true;
							System.out.println(whatToMake);
						} if(whatToMake.contains("Sapphire")) {
                            Variables.usingGems = true;
                            Variables.selectedGem = Variables.SAPPHIRE_ID;
							urnType = "Sapphire amulet";
                            Variables.bracelets = true;
						} if(whatToMake.contains("Emerald")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.EMERALD_ID;
							urnType = "Emerald amulet";
						} if(whatToMake.contains("Ruby")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.RUBY_ID;
							urnType = "Ruby amulet";
						} if(whatToMake.contains("Diamond")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.DIAMOND_ID;
							urnType = "Diamond amulet";
						} if(whatToMake.contains("Dragonstone")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.DRAGONSTONE_ID;
							urnType = "Dragonstone amulet";
						}
					} if(whatToMake.contains("Necklaces")) {
                        Variables.necklaces = true;
                        Variables.bracelets = true;
						if(whatToMake.contains("Gold")) {
                            Variables.usingGems = false;
                            Variables.bracelets = true;
							urnType = "Gold necklace";
						} if(whatToMake.contains("Sapphire")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.SAPPHIRE_ID;
							urnType = "Sapphire necklace";
						} if(whatToMake.contains("Emerald")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.EMERALD_ID;
							urnType = "Emerald necklace";
						} if(whatToMake.contains("Ruby")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.RUBY_ID;
							urnType = "Ruby necklace";
						} if(whatToMake.contains("Diamond")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.DIAMOND_ID;
							urnType = "Diamond necklace";
						} if(whatToMake.contains("Dragonstone")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.DRAGONSTONE_ID;
							urnType = "Dragonstone necklace";
						}
					} if(whatToMake.contains("Rings")) {
                        Variables.rings = true;
                        Variables.bracelets = true;
						if(whatToMake.contains("Gold")) {
                            Variables.usingGems = false;
                            Variables.bracelets = true;
							urnType = "Gold ring";
						} if(whatToMake.contains("Sapphire")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.SAPPHIRE_ID;
							urnType = "Sapphire ring";
						} if(whatToMake.contains("Emerald")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.EMERALD_ID;
							urnType = "Emerald ring";
						} if(whatToMake.contains("Ruby")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.RUBY_ID;
							urnType = "Ruby ring";
						} if(whatToMake.contains("Diamond")) {
                            Variables.usingGems = true;
                            Variables.selectedGem = Variables.DIAMOND_ID;
                            Variables.bracelets = true;
							urnType = "Diamond ring";
						} if(whatToMake.contains("Dragonstone")) {
                            Variables.usingGems = true;
                            Variables.bracelets = true;
                            Variables.selectedGem = Variables.DRAGONSTONE_ID;
							urnType = "Dragonstone ring";
						}
					} if(whatToMake.contains("Silver")) {
						 if(whatToMake.contains("Unstrung symbol")) {
							urnType = "Unstrung symbol";
                             Variables.silver = true;
						} if(whatToMake.contains("Unstrung emblem")) {
							urnType = "Unstrung emblem";
                            Variables.silver = true;
						} if(whatToMake.contains("Silver sickle")) {
							urnType = "Silver sickle";
                            Variables.silver = true;
						} if(whatToMake.contains("Tiara")) {
							urnType = "Tiara";
                            Variables.silver = true;
						}
					} if(whatToMake.contains("Other")) {
						if(whatToMake.contains("Gold bar")) {
							urnType = "Gold bar";
                            Variables.bars = true;
						} if(whatToMake.contains("Silver bar")) {
                            urnType = "Silver bar";
                            Variables.bars = true;
                            Variables.GOLD_ORE_ID = Variables.SILVER_ORE_ID;
                            Variables.GOLD_BAR_ID = Variables.SILVER_BAR_ID;
                        }
					}
					if(whatToMake.contains("Edgeville")) {
						Tiles.bankArea = Tiles.BANK_AREA_EDGEVILLE;
                        Tiles.pathToBank = Tiles.PATH_TO_BANK_EDGEVILLE;
                        Tiles.furnaceTile = Tiles.edgevilleFurnaceTile;
                        Variables.furnaceID = Variables.EDGEVILLE_FURNACE_ID;
					} if(whatToMake.contains("Al Kharid")) {
                        Tiles.pathToBank = Tiles.PATH_TO_BANK_ALKHARID;
                        Tiles.bankArea = Tiles.BANK_AREA_ALKHARID;
                        Tiles.furnaceTile = Tiles.alKharidFurnaceTile;
						Variables.furnaceID = 76293;
					} if(whatToMake.contains("Port Phasmaty")) {
                        Tiles.pathToBank = Tiles.PATH_TO_BANK_PHASMATY;
                        Tiles.bankArea = Tiles.BANK_AREA_PHASMATY;
                        Tiles.furnaceTile = Tiles.phasmatyFurnaceTile;
						Variables.furnaceID = 11666;
					} if(whatToMake.contains("Neitiznot")) {
                        Tiles.pathToBank = Tiles.PATH_TO_BANK_NEITIZNOT;
                        Tiles.bankArea = Tiles.BANK_AREA_NEITIZNOT;
                        Tiles.furnaceTile = Tiles.neitiznotFurnaceTile;
						Variables.furnaceID = 21303;
					}
				} if(whatToMake.contains("Glass blowing")) {
					if(whatToMake.contains("Unpowered orb")) {
						urnType = "Unpowered orb";
                        Variables.glass = true;
                        Variables.glassID = Variables.MOLTEN_GLASS_ID;
					} else if(whatToMake.contains("Flask")) {
						urnType = "Potion flask";
                        Variables.glass = true;
                        Variables.glassID = Variables.ROBUST_GLASS_ID;
					}
				} if(whatToMake.contains("Spinning")) {
                    Variables.flax = true;
					urnType = "Flax";
				} if(whatToMake.contains("Leather crafting")) {
                    Variables.needleAndThread = true;
					if(whatToMake.contains("Carapace")) {
						if(whatToMake.contains("torso")) { 
							urnType = "Carapace torso";
                            Variables.bodyType = 25859;
                            Variables.leatherID = Variables.CARAPACE_LEATHER_ID;
						} if(whatToMake.contains("legs")) { 
							urnType = "Carapace legs";
                            Variables.bodyType = 25861;
                            Variables.leatherID = Variables.CARAPACE_LEATHER_ID;
						} if(whatToMake.contains("helm")) { 
							urnType = "Carapace helm";
                            Variables.bodyType = 25857;
                            Variables.leatherID = Variables.CARAPACE_LEATHER_ID;
						} if(whatToMake.contains("gloves")) { 
							urnType = "Carapace gloves";
                            Variables.bodyType = 25865;
                            Variables.leatherID = Variables.CARAPACE_LEATHER_ID;
						} if(whatToMake.contains("boots")) { 
							urnType = "Carapace boots";
                            Variables.bodyType = 25863;
                            Variables.leatherID = Variables.CARAPACE_LEATHER_ID;
						}
					} if(whatToMake.contains("Soft")) {
						urnType = "Soft leather body";
                        Variables.bodyType = 1129;
                        Variables.leatherID = Variables.SOFT_LEATHER_ID;
					} if(whatToMake.contains("Hard")) {
						if(whatToMake.contains("oots")) {
							urnType = "Hard leather boots";
                            Variables.bodyType = 25821;
                            Variables.leatherID = Variables.HARD_LEATHER_ID;
						} else {
							urnType = "Hard leather body";
                            Variables.bodyType = 1131;
                            Variables.leatherID = Variables.HARD_LEATHER_ID;
						}
					} if(whatToMake.contains("Green")) {
						if(whatToMake.contains("ody")) { 
							urnType = "Green d'hide body";
                            Variables.bodyType = 1135;
                            Variables.leatherID = Variables.GREEN_LEATHER_ID;
						} if(whatToMake.contains("haps")) { 
							urnType = "Green d'hide chaps";
                            Variables.bodyType = 1099;
                            Variables.leatherID = Variables.GREEN_LEATHER_ID;
						} if(whatToMake.contains("ambraces")) { 
							urnType = "Green d'hide vambraces";
                            Variables.bodyType = 1065;
                            Variables.leatherID = Variables.GREEN_LEATHER_ID;
						} if(whatToMake.contains("hield")) { 
							urnType = "Green d'hide shield";
                            Variables.bodyType = 25794;
							Variables.leatherID = Variables.GREEN_LEATHER_ID;
						}
					} if(whatToMake.contains("Blue")) {
						if(whatToMake.contains("ody")) { 
							urnType = "Blue d'hide body";
                            Variables.bodyType = 2499;
                            Variables.leatherID = Variables.BLUE_LEATHER_ID;
						} if(whatToMake.contains("haps")) { 
							urnType = "Blue d'hide chaps";
                            Variables.bodyType = 2493;
                            Variables.leatherID = Variables.BLUE_LEATHER_ID;
						} if(whatToMake.contains("ambraces")) { 
							urnType = "Blue d'hide vambraces";
                            Variables.bodyType = 2487;
                            Variables.leatherID = Variables.BLUE_LEATHER_ID;
						} if(whatToMake.contains("hield")) { 
							urnType = "Blue d'hide shield";
                            Variables.bodyType = 25796;
                            Variables.leatherID = Variables.BLUE_LEATHER_ID;
						}
					} if(whatToMake.contains("Red")) {
						if(whatToMake.contains("ody")) { 
							urnType = "Red d'hide body";
                            Variables.bodyType = 2501;
                            Variables.leatherID = Variables.RED_LEATHER_ID;
						} if(whatToMake.contains("haps")) { 
							urnType = "Red d'hide chaps";
                            Variables.bodyType = 2495;
                            Variables.leatherID = Variables.RED_LEATHER_ID;
						} if(whatToMake.contains("ambraces")) { 
							urnType = "Red d'hide vambraces";
                            Variables.bodyType = 2489;
                            Variables.leatherID = Variables.RED_LEATHER_ID;
						} if(whatToMake.contains("hield")) { 
							urnType = "Red d'hide shield";
                            Variables.bodyType = 25798;
                            Variables.leatherID = Variables.RED_LEATHER_ID;
						}
					} if(whatToMake.contains("Black")) {
						if(whatToMake.contains("ody")) { 
							urnType = "Black d'hide body";
                            Variables.bodyType = 2503;
                            Variables.leatherID = Variables.BLACK_LEATHER_ID;
						} if(whatToMake.contains("haps")) { 
							urnType = "Black d'hide chaps";
                            Variables.bodyType = 2497;
                            Variables.leatherID = Variables.BLACK_LEATHER_ID;
						} if(whatToMake.contains("ambraces")) { 
							urnType = "Black d'hide vambraces";
                            Variables.bodyType = 2491;
                            Variables.leatherID = Variables.BLACK_LEATHER_ID;
						} if(whatToMake.contains("hield")) { 
							urnType = "Black d'hide shield";
                            Variables.bodyType = 25800;
                            Variables.leatherID = Variables.BLACK_LEATHER_ID;
						}
					} if(whatToMake.contains("Royal")) {
						if(whatToMake.contains("ody")) { 
							urnType = "Royal d'hide body";
                            Variables.bodyType = 24382;
                            Variables.leatherID = Variables.ROYAL_LEATHER_ID;
						} if(whatToMake.contains("haps")) { 
							urnType = "Royal d'hide chaps";
                            Variables.bodyType = 24379;
                            Variables.leatherID = Variables.ROYAL_LEATHER_ID;
						} if(whatToMake.contains("ambraces")) { 
							urnType = "Royal d'hide vambraces";
                            Variables.bodyType = 24376;
                            Variables.leatherID = Variables.ROYAL_LEATHER_ID;
						}
					}
				} if(whatToMake.contains("Gem cutting")) {
                    Variables.gemCutting = true;
					if(whatToMake.contains("Jade")) {
                        Variables.gemToCut = Variables.UNCUT_JADE_ID;
						urnType = "Cutting Jade";
					} if(whatToMake.contains("Opal")) {
                        Variables.gemToCut = Variables.UNCUT_OPAL_ID;
						urnType = "Cutting Opal";
					} if(whatToMake.contains("Red topaz")) {
                        Variables.gemToCut = Variables.UNCUT_TOPAZ_ID;
						urnType = "Cutting Red topaz";
					} if(whatToMake.contains("Sapphire")) {
                        Variables.gemToCut = Variables.UNCUT_SAPPHIRE_ID;
						urnType = "Cutting Sapphire";
					} if(whatToMake.contains("Emerald")) {
                        Variables.gemToCut = Variables.UNCUT_EMERALD_ID;
						urnType = "Cutting Emerald";
					} if(whatToMake.contains("Ruby")) {
                        Variables.gemToCut = Variables.UNCUT_RUBY_ID;
						urnType = "Cutting Ruby";
					} if(whatToMake.contains("Diamond")) {
                        Variables.gemToCut = Variables.UNCUT_DIAMOND_ID;
						urnType = "Cutting Diamond";
					} if(whatToMake.contains("Dragonstone")) {
                        Variables.gemToCut = Variables.UNCUT_DRAGONSTONE_ID;
						urnType = "Cutting Dragonstone";
					}
				} if(whatToMake.contains("Lunar spells")) {
					if(whatToMake.contains("lass")) {
                        Variables.glassMake = true;
						urnType = "Glass make [Lunars]";
					} if(whatToMake.contains("Make leather (Green)")) {
                        Variables.leatherMake = true;
                        Variables.leatherUsing = Variables.GREEN_HIDE_ID;
                        Variables.madeLeather = Variables.GREEN_LEATHER_ID;
						urnType = "Make leather [Lunars]";
					} if(whatToMake.contains("Make leather (Blue)")) {
                        Variables.leatherMake = true;
                        Variables.leatherUsing = Variables.BLUE_HIDE_ID;
                        Variables.madeLeather = Variables.BLUE_LEATHER_ID;
						urnType = "Make leather [Lunars]";	
					} if(whatToMake.contains("Make leather (Red)")) {
                        Variables.leatherMake = true;
                        Variables.leatherUsing = Variables.RED_HIDE_ID;
                        Variables.madeLeather = Variables.RED_LEATHER_ID;
						urnType = "Make leather [Lunars]";
					} if(whatToMake.contains("Make leather (Black)")) {
                        Variables.leatherMake = true;
                        Variables.leatherUsing = Variables.BLACK_HIDE_ID;
                        Variables.madeLeather = Variables.BLACK_LEATHER_ID;
						urnType = "Make leather [Lunars]";	
					} if(whatToMake.contains("Make leather (Royal)")) {
                        Variables.leatherMake = true;
                        Variables.leatherUsing = Variables.ROYAL_HIDE_ID;
                        Variables.madeLeather = Variables.ROYAL_LEATHER_ID;
						urnType = "Make leather [Lunars]";	
					} 
				} if(whatToMake.contains("Battlestaves")) {
                    Variables.battlestaff = true;
					if(whatToMake.contains("Air")) {
                        Variables.selectedOrbID = Variables.AIR_ORB_ID;
						urnType = "Air battlestaves";
					} else if(whatToMake.contains("Earth")) {
                        Variables.selectedOrbID = Variables.EARTH_ORB_ID;
						urnType = "Earth battlestaves";
					} else if(whatToMake.contains("Water")) {
                        Variables.selectedOrbID = Variables.WATER_ORB_ID;
						urnType = "Water battlestaves";
					} else if(whatToMake.contains("Fire")) {
                        Variables.selectedOrbID = Variables.FIRE_ORB_ID;
						urnType = "Fire battlestaves";
					}
				}
                Variables.startScript = true;
				this.setVisible(false);
				this.dispose();
			}

			private void initComponents() {
				startButton = new JButton();
				textPane1 = new JTextPane();
				scrollPane1 = new JScrollPane();
				tree1 = new JTree();

				//======== this ========
				setTitle("ShadeCrafter " + version);
				setAlwaysOnTop(true);
				setResizable(false);
				Container contentPane = getContentPane();

				//---- startButton ----
				startButton.setText("Start");
 				startButton.addActionListener(new ActionListener() {
 					@Override
 					public void actionPerformed(ActionEvent e) {
 						startButtonActionPerformed(e);
 					}
 				});

				//---- textPane1 ----
				textPane1.setText("Welcome to ShadeCrafter, the most popular AIO Crafter. Please highlight your desired method to the left, and click start. Please post any bugs on the thread and also feel free to suggest more methods!\n\nEnjoy, Shade.");
				textPane1.setEditable(false);
				textPane1.setBackground(SystemColor.menu);
				textPane1.setFont(new Font("Verdana", Font.PLAIN, 14));

				//======== scrollPane1 ========
				{

					//---- tree1 ----
					tree1.setModel(new DefaultTreeModel(
						new DefaultMutableTreeNode("(root)") {
							{
								DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Furnace");
									DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Edgeville");
										DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("Bracelets");
											node3.add(new DefaultMutableTreeNode("Gold bracelets"));
											node3.add(new DefaultMutableTreeNode("Sapphire bracelets"));
											node3.add(new DefaultMutableTreeNode("Emerald bracelets"));
											node3.add(new DefaultMutableTreeNode("Ruby bracelets"));
											node3.add(new DefaultMutableTreeNode("Diamond bracelets"));
											node3.add(new DefaultMutableTreeNode("Dragonstone bracelets"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Necklaces");
											node3.add(new DefaultMutableTreeNode("Gold necklaces"));
											node3.add(new DefaultMutableTreeNode("Sapphire necklaces"));
											node3.add(new DefaultMutableTreeNode("Emerald necklaces"));
											node3.add(new DefaultMutableTreeNode("Ruby necklaces"));
											node3.add(new DefaultMutableTreeNode("Diamond necklaces"));
											node3.add(new DefaultMutableTreeNode("Dragonstone necklaces"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Amulets");
											node3.add(new DefaultMutableTreeNode("Gold amulets"));
											node3.add(new DefaultMutableTreeNode("Sapphire amulets"));
											node3.add(new DefaultMutableTreeNode("Emerald amulets"));
											node3.add(new DefaultMutableTreeNode("Ruby amulets"));
											node3.add(new DefaultMutableTreeNode("Diamond amulets"));
											node3.add(new DefaultMutableTreeNode("Dragonstone amulets"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Rings");
											node3.add(new DefaultMutableTreeNode("Gold rings"));
											node3.add(new DefaultMutableTreeNode("Sapphire rings"));
											node3.add(new DefaultMutableTreeNode("Emerald rings"));
											node3.add(new DefaultMutableTreeNode("Ruby rings"));
											node3.add(new DefaultMutableTreeNode("Diamond rings"));
											node3.add(new DefaultMutableTreeNode("Dragonstone rings"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Silver");
											node3.add(new DefaultMutableTreeNode("Tiara"));
											node3.add(new DefaultMutableTreeNode("Silver sickle"));
											node3.add(new DefaultMutableTreeNode("Unstrung emblem"));
											node3.add(new DefaultMutableTreeNode("Unstrung symbol"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Other");
											node3.add(new DefaultMutableTreeNode("Gold bars"));
                                            node3.add(new DefaultMutableTreeNode("Silver bars"));
										node2.add(node3);
									node1.add(node2);
									node2 = new DefaultMutableTreeNode("Port Phasmaty");
										node3 = new DefaultMutableTreeNode("Bracelets");
											node3.add(new DefaultMutableTreeNode("Gold bracelets"));
											node3.add(new DefaultMutableTreeNode("Sapphire bracelets"));
											node3.add(new DefaultMutableTreeNode("Emerald bracelets"));
											node3.add(new DefaultMutableTreeNode("Ruby bracelets"));
											node3.add(new DefaultMutableTreeNode("Diamond bracelets"));
											node3.add(new DefaultMutableTreeNode("Dragonstone bracelets"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Necklaces");
											node3.add(new DefaultMutableTreeNode("Gold necklaces"));
											node3.add(new DefaultMutableTreeNode("Sapphire necklaces"));
											node3.add(new DefaultMutableTreeNode("Emerald necklaces"));
											node3.add(new DefaultMutableTreeNode("Ruby necklaces"));
											node3.add(new DefaultMutableTreeNode("Diamond necklaces"));
											node3.add(new DefaultMutableTreeNode("Dragonstone necklaces"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Amulets");
											node3.add(new DefaultMutableTreeNode("Gold amulets"));
											node3.add(new DefaultMutableTreeNode("Sapphire amulets"));
											node3.add(new DefaultMutableTreeNode("Emerald amulets"));
											node3.add(new DefaultMutableTreeNode("Ruby amulets"));
											node3.add(new DefaultMutableTreeNode("Diamond amulets"));
											node3.add(new DefaultMutableTreeNode("Dragonstone amulets"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Rings");
											node3.add(new DefaultMutableTreeNode("Gold rings"));
											node3.add(new DefaultMutableTreeNode("Sapphire rings"));
											node3.add(new DefaultMutableTreeNode("Emerald rings"));
											node3.add(new DefaultMutableTreeNode("Ruby rings"));
											node3.add(new DefaultMutableTreeNode("Diamond rings"));
											node3.add(new DefaultMutableTreeNode("Dragonstone rings"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Silver");
											node3.add(new DefaultMutableTreeNode("Tiara"));
											node3.add(new DefaultMutableTreeNode("Silver sickle"));
											node3.add(new DefaultMutableTreeNode("Unstrung emblem"));
											node3.add(new DefaultMutableTreeNode("Unstrung symbol"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Other");
											node3.add(new DefaultMutableTreeNode("Gold bars"));
                                            node3.add(new DefaultMutableTreeNode("Silver bars"));
										node2.add(node3);
									node1.add(node2);
									node2 = new DefaultMutableTreeNode("Al Kharid");
										node3 = new DefaultMutableTreeNode("Bracelets");
											node3.add(new DefaultMutableTreeNode("Gold bracelets"));
											node3.add(new DefaultMutableTreeNode("Sapphire bracelets"));
											node3.add(new DefaultMutableTreeNode("Emerald bracelets"));
											node3.add(new DefaultMutableTreeNode("Ruby bracelets"));
											node3.add(new DefaultMutableTreeNode("Diamond bracelets"));
											node3.add(new DefaultMutableTreeNode("Dragonstone bracelets"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Necklaces");
											node3.add(new DefaultMutableTreeNode("Gold necklaces"));
											node3.add(new DefaultMutableTreeNode("Sapphire necklaces"));
											node3.add(new DefaultMutableTreeNode("Emerald necklaces"));
											node3.add(new DefaultMutableTreeNode("Ruby necklaces"));
											node3.add(new DefaultMutableTreeNode("Diamond necklaces"));
											node3.add(new DefaultMutableTreeNode("Dragonstone necklaces"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Amulets");
											node3.add(new DefaultMutableTreeNode("Gold amulets"));
											node3.add(new DefaultMutableTreeNode("Sapphire amulets"));
											node3.add(new DefaultMutableTreeNode("Emerald amulets"));
											node3.add(new DefaultMutableTreeNode("Ruby amulets"));
											node3.add(new DefaultMutableTreeNode("Diamond amulets"));
											node3.add(new DefaultMutableTreeNode("Dragonstone amulets"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Rings");
											node3.add(new DefaultMutableTreeNode("Gold rings"));
											node3.add(new DefaultMutableTreeNode("Sapphire rings"));
											node3.add(new DefaultMutableTreeNode("Emerald rings"));
											node3.add(new DefaultMutableTreeNode("Ruby rings"));
											node3.add(new DefaultMutableTreeNode("Diamond rings"));
											node3.add(new DefaultMutableTreeNode("Dragonstone rings"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Silver");
											node3.add(new DefaultMutableTreeNode("Tiara"));
											node3.add(new DefaultMutableTreeNode("Silver sickle"));
											node3.add(new DefaultMutableTreeNode("Unstrung emblem"));
											node3.add(new DefaultMutableTreeNode("Unstrung symbol"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Other");
											node3.add(new DefaultMutableTreeNode("Gold bars"));
                                             node3.add(new DefaultMutableTreeNode("Silver bars"));
										node2.add(node3);
									node1.add(node2);
									node2 = new DefaultMutableTreeNode("Neitiznot");
										node3 = new DefaultMutableTreeNode("Bracelets");
											node3.add(new DefaultMutableTreeNode("Gold bracelets"));
											node3.add(new DefaultMutableTreeNode("Sapphire bracelets"));
											node3.add(new DefaultMutableTreeNode("Emerald bracelets"));
											node3.add(new DefaultMutableTreeNode("Ruby bracelets"));
											node3.add(new DefaultMutableTreeNode("Diamond bracelets"));
											node3.add(new DefaultMutableTreeNode("Dragonstone bracelets"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Necklaces");
											node3.add(new DefaultMutableTreeNode("Gold necklaces"));
											node3.add(new DefaultMutableTreeNode("Sapphire necklaces"));
											node3.add(new DefaultMutableTreeNode("Emerald necklaces"));
											node3.add(new DefaultMutableTreeNode("Ruby necklaces"));
											node3.add(new DefaultMutableTreeNode("Diamond necklaces"));
											node3.add(new DefaultMutableTreeNode("Dragonstone necklaces"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Amulets");
											node3.add(new DefaultMutableTreeNode("Gold amulets"));
											node3.add(new DefaultMutableTreeNode("Sapphire amulets"));
											node3.add(new DefaultMutableTreeNode("Emerald amulets"));
											node3.add(new DefaultMutableTreeNode("Ruby amulets"));
											node3.add(new DefaultMutableTreeNode("Diamond amulets"));
											node3.add(new DefaultMutableTreeNode("Dragonstone amulets"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Rings");
											node3.add(new DefaultMutableTreeNode("Gold rings"));
											node3.add(new DefaultMutableTreeNode("Sapphire rings"));
											node3.add(new DefaultMutableTreeNode("Emerald rings"));
											node3.add(new DefaultMutableTreeNode("Ruby rings"));
											node3.add(new DefaultMutableTreeNode("Diamond rings"));
											node3.add(new DefaultMutableTreeNode("Dragonstone rings"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Silver");
											node3.add(new DefaultMutableTreeNode("Tiara"));
											node3.add(new DefaultMutableTreeNode("Silver sickle"));
											node3.add(new DefaultMutableTreeNode("Unstrung emblem"));
											node3.add(new DefaultMutableTreeNode("Unstrung symbol"));
										node2.add(node3);
										node3 = new DefaultMutableTreeNode("Other");
											node3.add(new DefaultMutableTreeNode("Gold bars"));
                                            node3.add(new DefaultMutableTreeNode("Silver bars"));
										node2.add(node3);
									node1.add(node2);
								add(node1);
								node1 = new DefaultMutableTreeNode("Glass blowing");
									node1.add(new DefaultMutableTreeNode("Unpowered orbs"));
									node1.add(new DefaultMutableTreeNode("Flasks"));
								add(node1);
								node1 = new DefaultMutableTreeNode("Spinning");
									node1.add(new DefaultMutableTreeNode("Flax (Lumbridge)"));
								add(node1);
								node1 = new DefaultMutableTreeNode("Leather crafting");
									node1.add(new DefaultMutableTreeNode("Soft leather body"));
									node1.add(new DefaultMutableTreeNode("Hard leather body"));
									node1.add(new DefaultMutableTreeNode("Hard leather boots"));
									node1.add(new DefaultMutableTreeNode("Carapace torso"));
									node1.add(new DefaultMutableTreeNode("Carapace legs"));
									node1.add(new DefaultMutableTreeNode("Carapace helm"));
									node1.add(new DefaultMutableTreeNode("Carapace boots"));
									node1.add(new DefaultMutableTreeNode("Carapace gloves"));
									node1.add(new DefaultMutableTreeNode("Green d'hide body"));
									node1.add(new DefaultMutableTreeNode("Green d'hide chaps"));
									node1.add(new DefaultMutableTreeNode("Green d'hide shield"));
									node1.add(new DefaultMutableTreeNode("Green d'hide vambraces"));
									node1.add(new DefaultMutableTreeNode("Blue d'hide body"));
									node1.add(new DefaultMutableTreeNode("Blue d'hide chaps"));
									node1.add(new DefaultMutableTreeNode("Blue d'hide shield"));
									node1.add(new DefaultMutableTreeNode("Blue d'hide vambraces"));
									node1.add(new DefaultMutableTreeNode("Red d'hide body"));
									node1.add(new DefaultMutableTreeNode("Red d'hide chaps"));
									node1.add(new DefaultMutableTreeNode("Red d'hide shield"));
									node1.add(new DefaultMutableTreeNode("Red d'hide vambraces"));
									node1.add(new DefaultMutableTreeNode("Black d'hide body"));
									node1.add(new DefaultMutableTreeNode("Black d'hide chaps"));
									node1.add(new DefaultMutableTreeNode("Black d'hide shield"));
									node1.add(new DefaultMutableTreeNode("Black d'hide vambraces"));
									node1.add(new DefaultMutableTreeNode("Royal d'hide body"));
									node1.add(new DefaultMutableTreeNode("Royal d'hide chaps"));
									node1.add(new DefaultMutableTreeNode("Royal d'hide vambraces"));
								add(node1);
								node1 = new DefaultMutableTreeNode("Gem cutting");
									node1.add(new DefaultMutableTreeNode("Red topaz"));
									node1.add(new DefaultMutableTreeNode("Jade"));
									node1.add(new DefaultMutableTreeNode("Opal"));
									node1.add(new DefaultMutableTreeNode("Sapphire"));
									node1.add(new DefaultMutableTreeNode("Emerald"));
									node1.add(new DefaultMutableTreeNode("Ruby"));
									node1.add(new DefaultMutableTreeNode("Diamond"));
									node1.add(new DefaultMutableTreeNode("Dragonstone"));
								add(node1);
								node1 = new DefaultMutableTreeNode("Lunar spells");
									node1.add(new DefaultMutableTreeNode("Glass make"));
									node1.add(new DefaultMutableTreeNode("Make leather (Green)"));
									node1.add(new DefaultMutableTreeNode("Make leather (Blue)"));
									node1.add(new DefaultMutableTreeNode("Make leather (Red)"));
									node1.add(new DefaultMutableTreeNode("Make leather (Black)"));
									node1.add(new DefaultMutableTreeNode("Make leather (Royal)"));
								add(node1);
								node1 = new DefaultMutableTreeNode("Battlestaves");
									node1.add(new DefaultMutableTreeNode("Air"));
									node1.add(new DefaultMutableTreeNode("Water"));
									node1.add(new DefaultMutableTreeNode("Earth"));
									node1.add(new DefaultMutableTreeNode("Fire"));
								add(node1);
							}
						}));
					tree1.setRootVisible(false);
					tree1.setShowsRootHandles(true);
					scrollPane1.setViewportView(tree1);
				}

				GroupLayout contentPaneLayout = new GroupLayout(contentPane);
				contentPane.setLayout(contentPaneLayout);
				contentPaneLayout.setHorizontalGroup(
					contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addComponent(startButton, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
								.addComponent(textPane1, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE))
							.addContainerGap())
				);
				contentPaneLayout.setVerticalGroup(
					contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
									.addComponent(textPane1, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
									.addComponent(startButton, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
								.addComponent(scrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
							.addGap(11, 11, 11))
				);
				pack();
				setLocationRelativeTo(getOwner());
			}

			private JButton startButton;
			private JTextPane textPane1;
			private JScrollPane scrollPane1;
			private JTree tree1;
		}

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

