package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.util.Timer;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.DepositBox;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import org.shade.scripts.lrc.ShadeLRC;
import org.shade.scripts.lrc.misc.Variables;
import sk.action.ActionBar;

public class Deposit extends Node {

	@Override
	public boolean activate() {
		if(Variables.banking) {
			return ShadeLRC.fullInventory();
		} else {
			return Inventory.getCount() > 20;
		}
	}

	@Override
	public void execute() {
		if(Variables.banking) {
			SceneObject trolley = SceneEntities.getNearest(Variables.TROLLEY_ID);
			if(DepositBox.isOpen()) {
				Variables.status = "Depositing ore";
				if(!Variables.pickaxeEquipped) {
					DepositBox.deposit(Variables.COAL_ORE_ID, 0);
					Task.sleep(500, 600);
					DepositBox.deposit(Variables.GOLD_ORE_ID, 0);
					Task.sleep(500, 600);
				} else {
					DepositBox.depositInventory();
					Task.sleep(500, 600);
				}
			} else {
				if(trolley != null) {
					if(trolley.isOnScreen()) {
						Variables.status = "Clicking trolley";
						trolley.interact("Deposit");
						Task.sleep(500, 600);
						Timer failSafe = new Timer(3000);
						while((Players.getLocal().isMoving() || !DepositBox.isOpen()) && failSafe.isRunning()) {
							Task.sleep(500, 600);
						}
					} else {
						Variables.status = "Walking [Bank]";
						if(Players.getLocal().getLocation().getX() < 3652) {
							Walking.walk(new Tile(3656, 5093, 0));
							Task.sleep(500, 600);
						} else {
							Walking.walk(trolley.getLocation());
							Task.sleep(500, 600);
						}
					}
				}
			}
		} else {
			Variables.status = "Dropping";
			ActionBar.makeReadyForInteract();
			for(int i = 0; i < 12; i++){
				if(Inventory.getCount(Variables.GOLD_ORE_ID) > 0) {
	                if(ActionBar.getItemId(i) == Variables.GOLD_ORE_ID){
	                	Timer failSafe = new Timer(15000);
	                	while(Inventory.getCount(Variables.GOLD_ORE_ID) != 0 && failSafe.isRunning()) {
	                		ActionBar.useSlot(i);
	                		Task.sleep(50, 60);
	                	}
	                }
				} if(Inventory.getCount(Variables.COAL_ORE_ID) > 0) {
	                if(ActionBar.getItemId(i) == Variables.COAL_ORE_ID){
	                	Timer failSafe = new Timer(15000);
	                	while(Inventory.getCount(Variables.COAL_ORE_ID) != 0 && failSafe.isRunning()) {
	                		ActionBar.useSlot(i);
	                		Task.sleep(50, 60);
	                	}
	                }
				}
			}
		}
	}

}
