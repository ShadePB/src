package org.shade.scripts.dagonhai.nodes;

import org.powerbot.core.Bot;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;
import org.shade.scripts.dagonhai.misc.Variables;

public class Escape extends Node {
    @Override
    public boolean activate() {
        if(Variables.foodID != 1) {
            return Inventory.getItem(Variables.foodID) == null;
        } else {
            return Inventory.getItem(Variables.PRAYER_POTION_ID) == null;
        }
    }

    @Override
    public void execute() {
        Item tab = Inventory.getItem(8007);
        Variables.status = "Out of food";
        if(tab != null) {
            if(tab.getWidgetChild().interact("Break")) {
                Bot.context().getScriptHandler().shutdown();
            }
        }
    }
}
