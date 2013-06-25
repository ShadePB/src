package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;
import org.shade.scripts.lrc.misc.Variables;

public class Urns extends Node {

    @Override
    public boolean activate() {
        return Variables.usingUrns && Inventory.getItem(Variables.FINISHED_URN_ID) != null;
    }

    @Override
    public void execute() {
        Variables.status = "Teleporting urn";
        Item urn = Inventory.getItem(Variables.FINISHED_URN_ID);
        if(urn != null) {
            urn.getWidgetChild().click(true);
            Timer failSafe = new Timer(1300);
            while(urn != null && failSafe.isRunning()) {
                Task.sleep(50, 60);
            }
        }
    }
}
