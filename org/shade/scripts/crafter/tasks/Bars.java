package org.shade.scripts.crafter.tasks;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.shade.scripts.crafter.misc.Tiles;
import org.shade.scripts.crafter.misc.Variables;
import org.shade.scripts.crafter.nodes.Crafting;

public class Bars {

    public static void make() {
        SceneObject furnace = SceneEntities.getNearest(Variables.furnaceID);
        if (furnace != null) {
            if (furnace.isOnScreen()) {
                Variables.status = "Crafting";
                if (Tiles.furnaceTile.equals(Tiles.phasmatyFurnaceTile)) {
                    Filter<SceneObject> i = new Filter<SceneObject>() {
                        public boolean accept(SceneObject entity) {
                            for(int d : Variables.DOOR_ID)
                                return entity.getId() == d && entity.getLocation().distance(new Tile(3684, 3476, 0)) < 3;
                            return false;
                        }
                    };
                    SceneObject door = SceneEntities.getNearest(i);
                    if (door != null) {
                        if (door.isOnScreen()) {
                            if(Bank.isOpen()){
                                Bank.close();
                            }
                            door.click(true);
                            Task.sleep(900, 1000);
                            while (Players.getLocal().isMoving()) {
                                Task.sleep(90, 100);
                            }
                        } else {
                            if (Calculations.distanceTo(door
                                    .getLocation()) > 6) {
                                Walking.walk(door.getLocation());
                            } else {
                                Camera.turnTo(door.getLocation());
                            }
                        }
                    }
                }
                if (Widgets.get(1371, 0).validate()) {
                    Crafting.clickStart();
                    Task.sleep(2500, 2600);
                    final Timer barTimer = new Timer(75000);
                    while (barTimer.isRunning()
                            && Inventory.getCount(Variables.GOLD_ORE_ID) != 0) {
                        Task.sleep(50);
                    }
                } else {
                    Variables.status = "Clicking furnace";
                    if(Bank.isOpen()){
                        Bank.close();
                    }
                    if (furnace.interact("Smelt")) {
                        Task.sleep(1300, 1400);
                        while (Players.getLocal().isMoving()) {
                            Task.sleep(50);
                        }
                    }
                }
            } else {
                if (Tiles.furnaceTile.equals(Tiles.phasmatyFurnaceTile)) {
                    Filter<SceneObject> i = new Filter<SceneObject>() {
                        public boolean accept(SceneObject entity) {
                            for(int d : Variables.DOOR_ID)
                                return entity.getId() == d && entity.getLocation().distance(new Tile(3684, 3476, 0)) < 3;
                            return false;
                        }
                    };
                    SceneObject door = SceneEntities.getNearest(i);
                    if (door != null) {
                        if (door.isOnScreen()) {
                            if(Bank.isOpen()){
                                Bank.close();
                            }
                            door.click(true);
                            Task.sleep(900, 1000);
                            while (Players.getLocal().isMoving()) {
                                Task.sleep(90, 100);
                            }
                        } else {
                            if (Calculations.distanceTo(door
                                    .getLocation()) > 6) {
                                Walking.walk(door.getLocation());
                            } else {
                                Camera.turnTo(door.getLocation());
                            }
                        }
                    }
                }
                Walking.walk(Tiles.furnaceTile);
                Task.sleep(500, 600);
            }
        }
    }

}
