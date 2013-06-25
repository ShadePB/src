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

public class Silver {

    public static boolean silverInterface() {
        return Widgets.get(1370, 0).validate();
    }

    public static void make() {
        SceneObject furnaceSilver = SceneEntities.getNearest(Variables.furnaceID);
        if (silverInterface()) {
            if (Variables.urnType.contains("ymbol")) {
                if (Variables.urnType.equals(Widgets.get(1370, 56).getText())) {
                    Crafting.clickStart();
                } else {
                    Widgets.get(1371, 44).getChild(0).click(true);
                    Task.sleep(500, 600);
                    Crafting.clickStart();
                }
            }
            if (Variables.urnType.contains("blem")) {
                if (Variables.urnType.equals(Widgets.get(1370, 56).getText())) {
                    Crafting.clickStart();
                } else {
                    Widgets.get(1371, 44).getChild(4).click(true);
                    Task.sleep(500, 600);
                    Crafting.clickStart();
                }
            }
            if (Variables.urnType.contains("ickle")) {
                if (Variables.urnType.equals(Widgets.get(1370, 56).getText())) {
                    Crafting.clickStart();
                } else {
                    Widgets.get(1371, 44).getChild(8).click(true);
                    Task.sleep(500, 600);
                    Crafting.clickStart();
                }
            }
            if (Variables.urnType.contains("iara")) {
                if (Variables.urnType.equals(Widgets.get(1370, 56).getText())) {
                    Crafting.clickStart();
                } else {
                    Widgets.get(1371, 44).getChild(20).click(true);
                    Task.sleep(500, 600);
                    Crafting.clickStart();
                }
            }
            Variables.status = "Crafting";
            final Timer symbolTimer = new Timer(60000);
            while (symbolTimer.isRunning() && Inventory.getCount(Variables.SILVER_BAR_ID) != 0) {
                Task.sleep(50);
            }
        } else {
            if (furnaceSilver != null) {
                if (furnaceSilver.isOnScreen()) {
                    Variables.status = "Opening furnace";
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
                                if (Calculations.distanceTo(door.getLocation()) > 6) {
                                    Walking.walk(door.getLocation());
                                } else {
                                    Camera.turnTo(door.getLocation());
                                }
                            }
                        }
                    }
                    if (Tiles.furnaceTile.equals(Tiles.neitiznotFurnaceTile)) {
                        if(Bank.isOpen()){
                            Bank.close();
                        }
                        furnaceSilver.click(true);
                        Task.sleep(900, 1000);
                        while (Players.getLocal().isMoving()) {
                            Task.sleep(90);
                        }
                    } else {
                        if(Bank.isOpen()){
                            Bank.close();
                        }
                        furnaceSilver.click(true);
                        Task.sleep(900, 1000);
                        while (Players.getLocal().isMoving()) {
                            Task.sleep(90);
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
                                if (Calculations.distanceTo(door.getLocation()) > 6) {
                                    Walking.walk(door.getLocation());
                                } else {
                                    Camera.turnTo(door.getLocation());
                                }
                            }
                        }
                    }
                    Variables.status = "Crafting";
                    Walking.walk(Tiles.furnaceTile);
                }
            }
        }
    }

}
