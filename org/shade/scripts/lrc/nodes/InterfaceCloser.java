package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.DepositBox;
import org.powerbot.game.api.wrappers.node.Item;

public class InterfaceCloser extends Node {

	@Override
	public boolean activate() {
		if(Widgets.get(1107, 0).validate()) {
			return true;
		} if(!DepositBox.isOpen()) {
			if(Inventory.getItem(spinTicketID) != null) {
				return true;
			}
		} if(Widgets.get(266, 0).validate()) {
			return true;
		}
		return false;
	}

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
		if(Inventory.getItem(spinTicketID) != null) {
			claim(spinTicketID);
		}
		if(Widgets.get(1107, 0).validate()) {
			Widgets.get(1107, 158).click(true);
			Task.sleep(500, 600);
		} if(Widgets.get(266, 0).validate()) {
			Widgets.get(266, 4).click(true);
			Task.sleep(500, 600);
		}
	}

}
