package org.shade.scripts.crafter.tasks;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
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

public class Bracelets {

    public static boolean furnaceInterface() {
        return Widgets.get(1371, 0).validate();
    }

    public static void clickStart() {
        Widgets.get(1370, 33).click(true);
        Task.sleep(2500, 2600);
    }

    public static void makeAll() {
        if(furnaceInterface()) {
            Variables.status = "Interface open";
            if(Widgets.get(1370, 56).getText().equals(Variables.urnType)) {
                Variables.status = "Start";
                clickStart();
            } else {
                if(!Variables.amulets && Variables.bracelets && !Variables.necklaces && !Variables.rings) {
                    if(Variables.usingGems) {
                        if(Variables.selectedGem == Variables.EMERALD_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Variables.status = "Selecting bracelet";
                            Widgets.get(1371, 44).getChild(40).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.RUBY_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Variables.status = "Selecting bracelet";
                            Widgets.get(1371, 44).getChild(56).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.SAPPHIRE_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Variables.status = "Selecting bracelet";
                            Widgets.get(1371, 44).getChild(24).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.DIAMOND_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(5).getCentralPoint());
                            Mouse.hold(1000, true);
                            Variables.status = "Selecting bracelet";
                            Widgets.get(1371, 44).getChild(72).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        }  else if(Variables.selectedGem == Variables.DRAGONSTONE_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(5).getCentralPoint());
                            Mouse.hold(1000, true);
                            Variables.status = "Selecting bracelet";
                            Widgets.get(1371, 44).getChild(88).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        }
                    } else {
                        Variables.status = "Finding bracelet";
                        Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                        Mouse.hold(1000, true);
                        Widgets.get(1371, 44).getChild(8).click(true);
                        Task.sleep(500, 600);
                        Crafting.clickStart();
                    }
                } if(Variables.amulets) {
                    if(Variables.usingGems) {
                        if(Variables.selectedGem == Variables.EMERALD_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(44).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.RUBY_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(60).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.SAPPHIRE_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(28).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.DIAMOND_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(5).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(76).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        }  else if(Variables.selectedGem == Variables.DRAGONSTONE_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(5).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(92).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        }
                    } else {
                        Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                        Mouse.hold(1000, true);
                        Widgets.get(1371, 44).getChild(12).click(true);
                        Task.sleep(500, 600);
                        Crafting.clickStart();
                    }
                } if(Variables.rings) {
                    if(Variables.usingGems) {
                        if(Variables.selectedGem == Variables.EMERALD_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(32).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.RUBY_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(48).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.SAPPHIRE_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(16).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.DIAMOND_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(5).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(446, 90).interact("Make All");
                            Task.sleep(2400, 2500);
                            Crafting.clickStart();
                        }  else if(Variables.selectedGem == Variables.DRAGONSTONE_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(5).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(446, 92).interact("Make All");
                            Task.sleep(2400, 2500);
                            Crafting.clickStart();
                        }
                    } else {
                        Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                        Mouse.hold(1000, true);
                        Widgets.get(1371, 44).getChild(0).click(true);
                        Task.sleep(500, 600);
                        Crafting.clickStart();
                    }
                } if(Variables.necklaces) {
                    if(Variables.usingGems) {
                        if(Variables.selectedGem == Variables.EMERALD_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(36).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.RUBY_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(52).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.SAPPHIRE_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(20).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        } else if(Variables.selectedGem == Variables.DIAMOND_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(5).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(68).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        }  else if(Variables.selectedGem == Variables.DRAGONSTONE_ID) {
                            Mouse.move(Widgets.get(1371, 47).getChild(5).getCentralPoint());
                            Mouse.hold(1000, true);
                            Widgets.get(1371, 44).getChild(84).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                        }
                    } else {
                        Mouse.move(Widgets.get(1371, 47).getChild(4).getCentralPoint());
                        Mouse.hold(1000, true);
                        Widgets.get(1371, 44).getChild(4).click(true);
                        Task.sleep(500, 600);
                        Crafting.clickStart();
                    }
                }
            }
        }
    }

    public static void make() {
        SceneObject furnace = SceneEntities.getNearest(Variables.furnaceID);
        if (Players.getLocal().getAnimation() == -1) {
            if (furnace != null) {
                if (furnace.isOnScreen()) {
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
                                while (Players.getLocal()
                                        .isMoving()) {
                                    Task.sleep(90, 100);
                                }
                            } else {
                                if (Calculations.distanceTo(door
                                        .getLocation()) > 6) {
                                    Walking.walk(door.getLocation());
                                } else {
                                    Camera.turnTo(door
                                            .getLocation());
                                }
                            }
                        }
                    }
                    Variables.status = "Crafting";
                    if (furnaceInterface()) {
                        makeAll();
                        final Timer braceletTimer = new Timer(60000);
                        while (braceletTimer.isRunning()
                                && Inventory.getCount(Variables.GOLD_BAR_ID) != 0) {
                            Task.sleep(50);
                        }
                    } else {
                        Variables.status = "Opening furnace";
                        if (Tiles.furnaceTile.equals(Tiles.neitiznotFurnaceTile)) {
                            if(Bank.isOpen()){
                                Bank.close();
                            }
                            furnace.click(true);
                            Task.sleep(900, 1000);
                            while (Players.getLocal().isMoving()) {
                                Task.sleep(90);
                            }
                        } else {
                            if(Bank.isOpen()){
                                Bank.close();
                            }
                            furnace.click(true);
                            Task.sleep(900, 1000);
                            while (Players.getLocal().isMoving()) {
                                Task.sleep(90);
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
                                while (Players.getLocal()
                                        .isMoving()) {
                                    Task.sleep(90, 100);
                                }
                            } else {
                                if (Calculations.distanceTo(door
                                        .getLocation()) > 6) {
                                    Walking.walk(door.getLocation());
                                } else {
                                    Camera.turnTo(door
                                            .getLocation());
                                }
                            }
                        }
                    }
                    Variables.status = "Crafting";
                    Walking.walk(Tiles.furnaceTile);
                    Task.sleep(500, 600);
                }
            }
    }
    }

}
