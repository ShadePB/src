package org.shade.scripts.podder;

import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.client.Client;
import org.shade.scripts.podder.misc.Variables;
import org.shade.scripts.podder.nodes.Pod;
import org.shade.scripts.podder.nodes.Ring;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

@Manifest(authors = { "Shade" }, description = "Good for starter farmers", name = "ShadePodder", version = 0.1)
public class ShadePodder extends ActiveScript implements PaintListener {

    Timer runTime = new Timer(0);
    long startTime;

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

    public void onStart() {
        startTime = System.currentTimeMillis();
        Mouse.setSpeed(Mouse.Speed.VERY_FAST);
        Variables.startExp = Skills.getExperience(Skills.FARMING);
        provide(new Pod(), new Ring());
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

    //START: Code generated using Enfilade's Easel
    private final Color color1 = new Color(0, 255, 255);

    private final Font font1 = new Font("Verdana", 1, 9);

    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;

        Variables.expGained = Skills.getExperience(Skills.CRAFTING) - Variables.startExp;
        Variables.expHour = (int) ((Variables.expGained) * 3600000D / (System.currentTimeMillis() - startTime));

        g.setFont(font1);
        g.setColor(color1);
        g.drawString("Time running: " + runTime.toElapsedString(), 369, 32);
        g.drawString("Farming exp: " + Variables.expGained, 369, 46);
        g.drawString("/hour: " + Variables.expHour, 402, 60);
    }
    //END: Code generated using Enfilade's Easel

}

