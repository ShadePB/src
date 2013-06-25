package org.shade.scripts.dagonhai;

import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.SkillData;
import org.powerbot.game.client.Client;
import org.shade.scripts.dagonhai.misc.Variables;
import org.shade.scripts.dagonhai.nodes.Attack;
import org.shade.scripts.dagonhai.nodes.Escape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

@Manifest(authors = { "Shade" }, description = "Kills Dagon hai monks in the chaos tunnels for massive exp, have a Varrock teletab in your inventory", name = "ShadeDagonhai", version = 0.1, instances = 99)
public class ShadeDagonhai extends ActiveScript implements PaintListener {

    private Tree jobContainer = null;

    public final Timer runTime = new Timer(0);

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

    public void onStart() {
        try {
            Variables.foodID = Integer.parseInt(JOptionPane.showInputDialog("Enter food ID (View > Inventory). Enter 1 if using soul split (quick prayers).").toString());
        } catch (Exception e) {
            stop();
        }
        while(Variables.foodID == 0) {
            Task.sleep(50, 60);
        }
        Mouse.setSpeed(Mouse.Speed.FAST);
        provide(new Attack(), new Escape());
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

    private final RenderingHints antialiasing = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    private final Color color1 = new Color(255, 255, 255);

    private final Font font1 = new Font("Trebuchet MS", 1, 9);

    private final Image img1 = getImage("http://i.imgur.com/UteH1KP.png");

    SkillData sd = new SkillData();

    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        g.setRenderingHints(antialiasing);

        int expGained = sd.experience(Skills.ATTACK) + sd.experience(Skills.STRENGTH) + sd.experience(Skills.DEFENSE)
                + sd.experience(Skills.MAGIC) + sd.experience(Skills.RANGE);
        int expHour = sd.experience(SkillData.Rate.HOUR, Skills.ATTACK) + sd.experience(SkillData.Rate.HOUR, Skills.STRENGTH)
                + sd.experience(SkillData.Rate.HOUR, Skills.DEFENSE) + sd.experience(SkillData.Rate.HOUR, Skills.MAGIC)
                + sd.experience(SkillData.Rate.HOUR, Skills.RANGE);

        g.drawImage(img1, -60, -4, null);
        g.setFont(font1);
        g.setColor(color1);
        g.drawString("" + runTime.toElapsedString(), 125, 34);
        g.drawString("" + Variables.status, 261, 34);
        g.drawString("" + (expGained / 1000) + "k" + " - " + (sd.experience(Skills.CONSTITUTION) / 1000) + "k", 438, 35);
        g.drawString("" + (expHour / 1000) + "k" + " - " + (sd.experience(SkillData.Rate.HOUR, Skills.CONSTITUTION) / 1000) + "k", 595, 34);
    }


}
