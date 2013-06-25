package org.shade.scripts.crafter.nodes;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.node.Item;

public class RandomBox extends Node {

    private final int spinTicketID = 24154;

    private void claim(int spinTicketID) {
        for (Item spinTicket : Inventory.getItems()) {
            if (spinTicket != null && spinTicket.getId() == (spinTicketID)) {
                spinTicket.getWidgetChild().interact("Claim");
            }
        }
    }

    @Override
    public void execute() {
        if(Bank.isOpen()) {
            Bank.close();
        }
        claim(spinTicketID);
    }

    @Override
    public boolean activate() {
        return Inventory.getItem(spinTicketID) != null;
    }

}
