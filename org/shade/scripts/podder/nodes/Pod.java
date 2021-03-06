package org.shade.scripts.podder.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;
import org.shade.scripts.podder.misc.Variables;

public class Pod extends Node {
    @Override
    public boolean activate() {
        return Players.getLocal().getLocation().getPlane() != 3;
    }

    @Override
    public void execute() {
        Item pod = Inventory.getItem(Variables.POD_ID);
        if(pod != null) {
            if(pod.getWidgetChild().interact("Launch")) {
                Timer failSafe = new Timer(10000);
                while(Players.getLocal().getLocation().getPlane() != 3 && failSafe.isRunning()) {
                    Task.sleep(500, 600);
                }
            }
        }
    }
}
