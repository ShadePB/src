package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Magic;
import org.powerbot.game.api.wrappers.node.Item;
import org.shade.scripts.lrc.misc.Variables;
import sk.action.ActionBar;

public class Superheat extends Node {

    public static void superheat() {
        Variables.status = "Superheating";
        if(!ActionBar.isReadyForInteract())
            ActionBar.makeReadyForInteract();
            Item gold = Inventory.getItem(Variables.GOLD_ORE_ID);
            if(Magic.isSpellSelected()) {
                if(gold != null) {
                    if(gold.getWidgetChild().click(true)) {
                        Task.sleep(400, 500);
                    }
                }
            } else {
                ActionBar.useSlot(2);
            }
    }

    @Override
    public boolean activate() {
        return Variables.usingSuperheat && Inventory.contains(Variables.GOLD_ORE_ID) && !Players.getLocal().isInCombat();
    }

    @Override
    public void execute() {
        superheat();
    }
}
