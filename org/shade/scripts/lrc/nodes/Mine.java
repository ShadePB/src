package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import org.shade.scripts.lrc.ShadeLRC;
import org.shade.scripts.lrc.misc.Variables;
import sk.action.ActionBar;

public class Mine extends Node {

	public static Timer gettingOre = new Timer(5000);
	public String nextDeposit = null;
	
	public boolean isMining() {
		SceneObject i = SceneEntities.getNearest(Variables.BOTH_DEPOSITS_ID);
		if(i != null)
			return (Players.getLocal().getAnimation() != -1 || gettingOre.isRunning()) && Calculations.distanceTo(i) < 5;
		return false;
	}
	
	public SceneObject getDeposit(final String deposit) {
		Filter<SceneObject> i = new Filter<SceneObject>() {
			public boolean accept(SceneObject entity) {
				if(deposit.equals("West")) {
					return entity.getId() == Variables.GOLD_DEPOSIT_ID && Variables.WEST_TILE.equals(entity.getLocation());
				} if(deposit.equals("East")) {
					return entity.getId() == Variables.GOLD_DEPOSIT_ID && Variables.EAST_TILE.equals(entity.getLocation());
				} if(deposit.equals("Coal")) {
					return entity.getId() == Variables.COAL_DEPOSIT_ID && Variables.COAL_TILE.equals(entity.getLocation());
				} if(deposit.equals("Coal 2")) {
                    return entity.getId() == Variables.COAL_DEPOSIT_ID && Variables.COAL_TILE_2.equals(entity.getLocation());
                }
				return false;
			}
		};
		SceneObject b = SceneEntities.getNearest(i);
		return b;
	}

	@Override
	public boolean activate() {
        if(Variables.banking) {
            return !ShadeLRC.fullInventory() && (Combat.getRock() == null || !Players.getLocal().isInCombat());
        } else {
            return Inventory.getCount() < 21 && (Combat.getRock() == null || !Players.getLocal().isInCombat());
        }
	}

	@Override
	public void execute() {
		if(ActionBar.isExpanded()) {
			ActionBar.setExpanded(false);
		}
		if(!isMining()) {
			if(Skills.getRealLevel(Skills.MINING) <= 79) {
				nextDeposit = "Coal";
			}
			SceneObject deposit = getDeposit("East");
			SceneObject deposit2 = getDeposit("West");
			SceneObject deposit3 = getDeposit("Coal");
            SceneObject deposit4 = getDeposit("Coal 2");
			if(deposit != null && Skills.getRealLevel(Skills.MINING) > 79) {
				nextDeposit = "West";
				if(deposit.isOnScreen()) {
					Variables.status = "Clicking deposit [West]";
					deposit.interact("Mine");
					Task.sleep(500, 600);
					while(Players.getLocal().isMoving() && !Players.getLocal().isInCombat()) {
						Task.sleep(50, 60);
					}
				} else {
					Variables.status = "Walking [West]";
					Walking.walk(deposit.getLocation());
				}
			} else if(deposit2 != null && Skills.getRealLevel(Skills.MINING) > 79) {
				nextDeposit = "East";
				if(deposit2.isOnScreen()) {
					Variables.status = "Clicking deposit [East]";
					deposit2.interact("Mine");
					Task.sleep(500, 600);
					while(Players.getLocal().isMoving() && !Players.getLocal().isInCombat()) {
						Task.sleep(50, 60);
					}
				} else {
					Variables.status = "Walking [East]";
					Walking.walk(deposit2.getLocation());
					Task.sleep(500, 600);
				}
			} else if(deposit3 != null) {
				if(deposit3.isOnScreen()) {
					Variables.status = "Clicking deposit [Coal]";
					deposit3.interact("Mine");
					Task.sleep(500, 600);
					while(Players.getLocal().isMoving() && !Players.getLocal().isInCombat()) {
						Task.sleep(50, 60);
					}
				} else {
					Variables.status = "Walking [Coal]";
					Walking.walk(deposit3.getLocation());
					Task.sleep(500, 600);
				}
			} else if(deposit4 != null) {
                if(deposit4.isOnScreen()) {
                    Variables.status = "Clicking deposit [Coal 2]";
                    deposit4.interact("Mine");
                    Task.sleep(500, 600);
                    while(Players.getLocal().isMoving() && !Players.getLocal().isInCombat()) {
                        Task.sleep(50, 60);
                    }
                } else {
                    if(Players.getLocal().getLocation().getY() > 5096 && Players.getLocal().getLocation().getX() > 3665) {
                        Variables.status = "Walking [Coal 2]";
                        Walking.walk(new Tile(3668, 5091, 0));
                        Task.sleep(500, 600);
                    } else {
                        Variables.status = "Walking [Coal 2]";
                        Walking.walk(deposit4.getLocation());
                        Task.sleep(500, 600);
                    }
                }
            } else {
				if(nextDeposit != null) {
					if(nextDeposit.equals("West")) {
						if(Calculations.distanceTo(new Tile(3640, 5095, 0)) > 2) {
							Variables.status = "Walking [West]";
							Walking.walk(new Tile(3640, 5095, 0));
							Task.sleep(500, 600);
						} else {
							Variables.status = "Waiting [West]";
						}
					} else if(nextDeposit.equals("East")) {
						if(Calculations.distanceTo(new Tile(3669, 5076, 0)) > 2) {
							Variables.status = "Walking [East]";
							Walking.walk(new Tile(3669, 5076, 0));
							Task.sleep(500, 600);
						} else {
							Variables.status = "Waiting [East]";
						}
					} else if(nextDeposit.equals("Coal")) {
						if(Calculations.distanceTo(new Tile(3666, 5092, 0)) > 2) {
							Variables.status = "Walking [Coal]";
							Walking.walk(new Tile(3666, 5092, 0));
							Task.sleep(500, 600);
						} else {
							Variables.status = "Waiting [Coal]";
						}
					}
				} else {
					if(Calculations.distanceTo(new Tile(3669, 5076, 0)) > 2) {
						Variables.status = "Walking [East]";
						Walking.walk(new Tile(3669, 5076, 0));
						Task.sleep(500, 600);
					} else {
						Variables.status = "Waiting [East]";
					}
				}
			}
		} else {
			Variables.status = "Mining";
            if(Variables.usingSuperheat) {
                if(Inventory.getItem(Variables.GOLD_ORE_ID) != null) {
                    Superheat.superheat();
                }
            }
			int randomNum = Random.nextInt(1, 22);
			if(randomNum == 10) {
				//Rewriting antiban
			}
		}
	}

}
