package org.shade.scripts.crafter.nodes;

import org.powerbot.core.Bot;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.shade.scripts.crafter.ShadeCrafter;
import org.shade.scripts.crafter.misc.Tiles;
import org.shade.scripts.crafter.misc.Variables;

public class Banking extends Node {

    String test = Variables.urnType;

    public void stop() {
        Bot.context().getScriptHandler().shutdown();
    }

    @Override
    public void execute() {
        if(!Variables.glassMake) {
            while(Players.getLocal().getAnimation() != -1) {
                Variables.status = "Waiting for animation";
                Task.sleep(200, 300);
            }
        }
        Variables.status = "Banking";
        if(Variables.leatherMake) {
            if(Bank.isOpen()) {
                Task.sleep(90, 90);
                if(Inventory.getCount(Variables.madeLeather) > 0) {
                    Bank.deposit(Variables.madeLeather, 0);
                } else {
                    Task.sleep(290, 300);
                    Bank.withdraw(Variables.leatherUsing, 0);
                    Task.sleep(90, 100);
                } if(Inventory.getCount(Variables.GREEN_HIDE_ID) > 1) {
                    Bank.close();
                }
            } else {
                if(Menu.isOpen()) {
                    Menu.select("Bank");
                    Task.sleep(400, 500);
                } else {
                    Bank.open();
                    while(Players.getLocal().isMoving()) {
                        Task.sleep(10, 50);
                    }
                }
            }
        } if(Variables.battlestaff) {
            if(Bank.isOpen()) {
                Task.sleep(50, 90);
                Bank.depositInventory();
                if(Bank.getItemCount(Variables.selectedOrbID) > 0) {
                    Bank.withdraw(Variables.selectedOrbID, 14);
                    Task.sleep(400, 500);
                } else {
                    System.out.println("Out of supplies");
                    Bot.context().getScriptHandler().shutdown();
                } if(Bank.getItemCount(Variables.BATTLE_STAFF_ID) > 0) {
                    Bank.withdraw(Variables.BATTLE_STAFF_ID, 14);
                    Task.sleep(400, 500);
                } else {
                    System.out.println("Out of supplies");
                    stop();
                }
                Bank.close();
            } else {
                if(Menu.isOpen()) {
                    Menu.select("Bank");
                    Task.sleep(400, 500);
                    while(Players.getLocal().isMoving()) {
                        Task.sleep(50);
                    }
                } else {
                    Bank.open();
                }
            }
        }
        if(Variables.glassMake) {
            if(Bank.isOpen()) {
                Task.sleep(50, 90);
                if(Inventory.getCount(Variables.MOLTEN_GLASS_ID) > 0) {
                    Bank.deposit(Variables.MOLTEN_GLASS_ID, 0);
                } else {
                    Task.sleep(290, 300);
                    if(Bank.getItemCount(Variables.SAND_ID) != 0) {
                        Bank.withdraw(Variables.SAND_ID, 13);
                        Task.sleep(90, 100);
                    } else {
                        System.out.println("Out of supplies");
                        stop();
                    } if(Bank.getItemCount(Variables.SEAWEED_ID) != 0) {
                        Bank.withdraw(Variables.SEAWEED_ID, 13);
                        Task.sleep(90, 100);
                    } else {
                        System.out.println("Out of supplies");
                        stop();
                    } if(Inventory.getCount(Variables.SAND_ID) == Inventory.getCount(Variables.SEAWEED_ID)) {
                        Bank.close();
                    } else {
                        Bank.deposit(Variables.SAND_ID, 0);
                        Bank.deposit(Variables.SEAWEED_ID, 0);
                    }
                }
            } else {
                if(Menu.isOpen()) {
                    Menu.select("Bank");
                    Task.sleep(400, 500);
                    while(Players.getLocal().isMoving()) {
                        Task.sleep(50);
                    }
                } else {
                    Bank.open();
                }
            }
        }
        if(Variables.silver) {
            if(Bank.isOpen()) {
                Task.sleep(50, 90);
                Bank.depositInventory();
                Task.sleep(290, 300);
                if(Bank.getItemCount(Variables.SILVER_BAR_ID) > 0) {
                    Bank.withdraw(Variables.SILVER_BAR_ID, 0);
                    Task.sleep(90, 100);
                } else {
                    stop();
                }
            } else {
                if(Tiles.bankArea.contains(Players.getLocal().getLocation())) {
                    Variables.status = "Banking";
                    Bank.open();
                } else {
                    Variables.status = "Walking to bank";
                    Filter<SceneObject> i = new Filter<SceneObject>() {
                        public boolean accept(SceneObject entity) {
                            for(int d : Variables.DOOR_ID)
                                return entity.getId() == d && entity.getLocation().distance(new Tile(3684, 3476, 0)) < 3;
                            return false;
                        }
                    };
                    SceneObject door = SceneEntities.getNearest(i);
                    if(door != null) {
                        if(door.isOnScreen()) {
                            door.click(true);
                            Task.sleep(900, 1000);
                            while(Players.getLocal().isMoving()) {
                                Task.sleep(90, 100);
                            }
                        }
                    }
                    ShadeCrafter.walk(Tiles.pathToBank);
                    Task.sleep(700, 800);
                }
            }
        }
        if(Variables.needleAndThread) {
            if(Bank.isOpen()) {
                Task.sleep(50, 90);
                Bank.deposit(Variables.bodyType, 0);
                Task.sleep(290, 300);
                if(Bank.getItemCount(Variables.leatherID) > 0) {
                    if(Inventory.getCount(Variables.bodyType) > 0) {
                        Bank.deposit(Variables.bodyType, 0);
                    } else {
                        if(Variables.useSacredClay) {
                            if(Inventory.getItem(Variables.SACRED_CLAY_NEEDLE_ID) == null) {
                                Bank.withdraw(Variables.SACRED_CLAY_NEEDLE_ID, 1);
                                Task.sleep(290, 300);
                            }
                        }
                        Bank.withdraw(Variables.leatherID, 0);
                        Task.sleep(90, 100);
                    }
                } else {
                    stop();
                }
                if(Inventory.getCount(Variables.leatherID) > 20 && Inventory.getCount(Variables.bodyType) == 0) {
                    Bank.close();
                }
            } else {
                if(Menu.isOpen()) {
                    Menu.select("Bank");
                    Task.sleep(400, 500);
                    while(Players.getLocal().isMoving()) {
                        Task.sleep(50);
                    }
                } else {
                    Bank.open();
                }
            }
        }
        if(Variables.gemCutting) {
            if(Bank.isOpen()) {
                Bank.depositInventory();
                Task.sleep(90, 100);
                if(Bank.getItemCount(Variables.gemToCut) > 0) {
                    Bank.withdraw(Variables.gemToCut, 0);
                    Task.sleep(90, 100);
                    if(Inventory.getCount(Variables.gemToCut) != 0) {
                        Bank.close();
                    }
                } else {
                    stop();
                }
            } else {
                if(Menu.isOpen()) {
                    Menu.select("Bank");
                    Task.sleep(400, 500);
                    while(Players.getLocal().isMoving()) {
                        Task.sleep(50);
                    }
                } else {
                    Bank.open();
                }
            }
        }
        if(Variables.flax) {
            try {
                if(Bank.isOpen()) {
                    Bank.depositInventory();
                    Task.sleep(90, 100);
                    if(Bank.getItemCount(Variables.FLAX_ID) > 0) {
                        Bank.withdraw(Variables.FLAX_ID, 0);
                        Task.sleep(200, 300);
                    } else {
                        stop();
                    }
                } else {
                    SceneObject banker = SceneEntities.getNearest(Variables.LUMBRIDGE_BANKER_ID);
                    if(banker != null) {
                        try {
                            if(banker.isOnScreen()) {
                                banker.interact("Bank");
                                Task.sleep(400, 5000);
                                while(Players.getLocal().isMoving()) {
                                    Task.sleep(50);
                                }
                            } else {
                                Tile bankTile = new Tile(3208, 3220, 2);
                                Walking.walk(bankTile);
                                Task.sleep(90, 100);
                                while(Players.getLocal().isMoving()) {
                                    Task.sleep(90, 100);
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            SceneObject stairs = SceneEntities.getNearest(Variables.STAIRS_MIDDLE_ID);
                            if(stairs != null) {
                                if(stairs.isOnScreen()) {
                                    if(stairs.interact("Climb-up")) {
                                        Task.sleep(500);
                                        while(Players.getLocal().isMoving()) {
                                            Task.sleep(50);
                                        }
                                    }
                                } else {
                                    Walking.walk(stairs.getLocation());
                                    Task.sleep(900, 1000);
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        if(Variables.bracelets || Variables.urns) {
            if(Tiles.bankArea.contains(Players.getLocal().getLocation())) {
                if(Bank.isOpen()) {
                    Variables.status = "Banking";
                    Bank.depositInventory();
                    if(Variables.bracelets) {
                        if(Variables.usingGems) {
                            if(Bank.getItemCount(Variables.GOLD_BAR_ID) > 0) {
                                Bank.withdraw(Variables.GOLD_BAR_ID, 14);
                                Task.sleep(900, 1000);
                            } else {
                                stop();
                            } if(Bank.getItemCount(Variables.selectedGem) > 0) {
                                Bank.withdraw(Variables.selectedGem, 14);
                                Task.sleep(900, 1000);
                            } else {
                                stop();
                            }
                        } else {
                            if(Bank.getItemCount(Variables.GOLD_BAR_ID) > 0) {
                                Bank.withdraw(Variables.GOLD_BAR_ID, 0);
                            } else {
                                stop();
                            }
                        }
                    }
                } else {
                    Bank.open();
                }
            } else {
                Variables.status = "Walking to bank";
                Filter<SceneObject> i = new Filter<SceneObject>() {
                    public boolean accept(SceneObject entity) {
                        for(int d : Variables.DOOR_ID)
                            return entity.getId() == d && entity.getLocation().distance(new Tile(3684, 3476, 0)) < 3;
                        return false;
                    }
                };
                SceneObject door = SceneEntities.getNearest(i);
                if(door != null) {
                    if(door.isOnScreen()) {
                        door.click(true);
                        Task.sleep(900, 1000);
                        while(Players.getLocal().isMoving()) {
                            Task.sleep(90, 100);
                        }
                    }
                }
                Variables.status = "Banking";
                ShadeCrafter.walk(Tiles.pathToBank);
                Task.sleep(700, 800);
            }
        } if(Variables.glass) {
            if(Bank.isOpen()) {
                Variables.status = "Banking";
                if(Inventory.getCount() > 0) {
                    Bank.depositInventory();
                } if(Bank.getItemCount(Variables.glassID) > 0) {
                    Bank.withdraw(Variables.glassID, 0);
                } else {
                    System.out.println("Out of supplies");
                    stop();
                }
            } else {
                Variables.status = "Banking";
                Bank.open();
            }
        } if(Variables.bars) {
            if(Tiles.bankArea.contains(Players.getLocal().getLocation())) {
                if(Bank.isOpen()) {
                    Variables.status = "Banking";
                    if(Inventory.getCount() > 0) {
                        Bank.depositInventory();
                    } if(Bank.getItemCount(Variables.GOLD_ORE_ID) > 0) {
                        Bank.withdraw(Variables.GOLD_ORE_ID, 0);
                    } else {
                        System.out.println("Out of supplies");
                        stop();
                    }
                } else {
                    Bank.open();
                }
            } else {
                Filter<SceneObject> i = new Filter<SceneObject>() {
                    public boolean accept(SceneObject entity) {
                        for(int d : Variables.DOOR_ID)
                            return entity.getId() == d && entity.getLocation().distance(new Tile(3684, 3476, 0)) < 3;
                        return false;
                    }
                };
                SceneObject door = SceneEntities.getNearest(i);
                if(door != null) {
                    if(door.isOnScreen()) {
                        door.click(true);
                        Task.sleep(900, 1000);
                        while(Players.getLocal().isMoving()) {
                            Task.sleep(90, 100);
                        }
                    }
                }
                Variables.status = "Walking to bank";
                ShadeCrafter.walk(Tiles.pathToBank);
            }
        }
    }

    public boolean activate() {
        if(Variables.urnType != null) {
        try {
            if(Variables.bracelets) {
                if(Variables.usingGems) {
                    return (Inventory.getCount(Variables.selectedGem) == 0 && Inventory.getCount(Variables.GOLD_BAR_ID) == 0) || Inventory.getCount(Variables.selectedGem) == 0 || Inventory.getCount(Variables.GOLD_BAR_ID) == 0;
                } else {
                    return Inventory.getCount(Variables.GOLD_BAR_ID) == 0;
                }
            } if(Variables.glass) {
                return Inventory.getCount(Variables.glassID) == 0;
            } if(Variables.bars) {
                return Inventory.getCount(Variables.GOLD_ORE_ID) == 0;
            } if(Variables.flax) {
                return Inventory.getCount(Variables.FLAX_ID) == 0;
            } if(Variables.gemCutting) {
                return Inventory.getCount(Variables.gemToCut) == 0;
            } if(Variables.needleAndThread) {
                if(!Variables.useSacredClay) {
                    if(Variables.urnType.contains("torso") || Variables.urnType.contains("body")) {
                        return Inventory.getCount(Variables.leatherID) < 3;
                    } else if(Variables.urnType.contains("shield")) {
                        return Inventory.getCount(Variables.leatherID) < 4;
                    } else if(Variables.urnType.contains("chaps") || Variables.urnType.contains("legs") || Variables.urnType.contains("helm")) {
                        return Inventory.getCount(Variables.leatherID) < 2;
                    } else if(Variables.urnType.contains("vambraces") || Variables.urnType.contains("boots") || Variables.urnType.contains("gloves"))
                        return Inventory.getCount(Variables.leatherID) < 1;
                } else {
                    if(Variables.urnType.contains("body") || Variables.urnType.contains("torso"))
                        return Inventory.getCount(Variables.leatherID) < 3 || Inventory.getItem(Variables.SACRED_CLAY_NEEDLE_ID) == null;
                    if(Variables.urnType.contains("shield"))
                        return Inventory.getCount(Variables.leatherID) < 4 || Inventory.getItem(Variables.SACRED_CLAY_NEEDLE_ID) == null;
                    if(Variables.urnType.contains("chaps") || Variables.urnType.contains("legs") || Variables.urnType.contains("helm"))
                        return Inventory.getCount(Variables.leatherID) < 2 || Inventory.getItem(Variables.SACRED_CLAY_NEEDLE_ID) == null;
                    if(Variables.urnType.contains("vambraces") || Variables.urnType.contains("boots") || Variables.urnType.contains("globes"))
                        return Inventory.getCount(Variables.leatherID) < 1 || Inventory.getItem(Variables.SACRED_CLAY_NEEDLE_ID) == null;
                }
            } if(Variables.silver) {
                return Inventory.getCount(Variables.SILVER_BAR_ID) == 0;
            } if(Variables.glassMake) {
                return (Inventory.getCount(Variables.SAND_ID) == 0 && Inventory.getCount(Variables.SEAWEED_ID) == 0)
                        || (Inventory.getCount(Variables.SAND_ID) > 0 && Inventory.getCount(Variables.SEAWEED_ID) == 0)
                        || (Inventory.getCount(Variables.SAND_ID) == 0 && Inventory.getCount(Variables.SEAWEED_ID) > 0)
                        || (Inventory.getCount(Variables.SAND_ID) != Inventory.getCount(Variables.SEAWEED_ID));
            } if(Variables.leatherMake) {
                return Inventory.getCount(Variables.leatherUsing) < 5;
            } if(Variables.battlestaff) {
                return (Inventory.getCount(Variables.BATTLE_STAFF_ID) == 0 && Inventory.getCount(Variables.selectedOrbID) == 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        }
        return false;
    }

}
