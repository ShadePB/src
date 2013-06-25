package org.shade.scripts.crafter.tasks;

import org.powerbot.core.Bot;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.shade.scripts.crafter.misc.Variables;
import org.shade.scripts.crafter.nodes.Crafting;

public class NeedleAndThread {

    public static boolean leatherInterface1() {
        return Widgets.get(1179, 0).validate();
    }

    public static boolean leatherInterface2() {
        return Widgets.get(1370, 0).validate();
    }

    public static void make() {
        if (!Bank.isOpen()) {
            if (Inventory.getCount(Variables.THREAD_ID) > 0) {
                if (Inventory.getCount(Variables.leatherID) > 0) {
                    if (leatherInterface1()) {
                        Widgets.get(1179, 16).click(true);
                        Task.sleep(900, 1000);
                    }
                    if (leatherInterface2()) {
                        if (Variables.urnType.equals(Widgets.get(1370, 56).getText())) {
                            Crafting.clickStart();
                            final Timer leatherTime = new Timer(60000);
                            if(Variables.urnType.contains("body") || Variables.urnType.contains("torso")) {
                                while (leatherTime.isRunning()&& Inventory.getCount(Variables.leatherID) > 2
                                        || Players.getLocal().getAnimation() != -1) {
                                    Task.sleep(100, 120);
                                }
                            } if(Variables.urnType.contains("shield")) {
                                while (leatherTime.isRunning()&& Inventory.getCount(Variables.leatherID) > 3
                                        || Players.getLocal().getAnimation() != -1) {
                                    Task.sleep(100, 120);
                                }
                            } if(Variables.urnType.contains("chaps") || Variables.urnType.contains("legs") || Variables.urnType.contains("helm")) {
                                while (leatherTime.isRunning()&& Inventory.getCount(Variables.leatherID) > 1
                                        || Players.getLocal().getAnimation() != -1) {
                                    Task.sleep(100, 120);
                                }
                            } if(Variables.urnType.contains("vambraces") || Variables.urnType.contains("gloves") || Variables.urnType.contains("boots")) {
                                while (leatherTime.isRunning()&& Inventory.getCount(Variables.leatherID) > 0
                                        || Players.getLocal().getAnimation() != -1) {
                                    Task.sleep(100, 120);
                                }
                            }
                        }
                        if (Variables.leatherID == Variables.HARD_LEATHER_ID) {
                            if(Variables.urnType.contains("boots")) {
                                Widgets.get(1371, 44).getChild(32) .click(true);
                            } else {
                                Widgets.get(1371, 44).getChild(36).click(true);
                            }
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                            final Timer leatherTime = new Timer(60000);
                            while (leatherTime.isRunning() && Inventory.getCount(Variables.leatherID) != 0
                                    || Players.getLocal() .getAnimation() != -1) {
                                Task.sleep(100, 120);
                            }
                        }
                        if (Variables.leatherID == Variables.SOFT_LEATHER_ID) {
                            Widgets.get(1371, 44).getChild(16).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                            final Timer leatherTime = new Timer(60000);
                            while (leatherTime.isRunning() && Inventory.getCount(Variables.leatherID) != 0
                                    || Players.getLocal().getAnimation() != -1) {
                                Task.sleep(100, 120);
                            }
                        }
                        if (Variables.leatherID != Variables.HARD_LEATHER_ID && Variables.leatherID != Variables.SOFT_LEATHER_ID) {
                            if(!Widgets.get(1370, 56).getText().equals(Variables.urnType)) {
                                if(Variables.urnType.contains("body") || Variables.urnType.contains("helm")) {
                                    Widgets.get(1371, 44).getChild(8).click(true);
                                    Task.sleep(500, 600);
                                } if(Variables.urnType.contains("vambraces") || Variables.urnType.contains("gloves")) {
                                    Widgets.get(1371, 44).getChild(0).click(true);
                                    Task.sleep(500, 600);
                                } if(Variables.urnType.contains("chaps") || Variables.urnType.contains("boots")) {
                                    Widgets.get(1371, 44).getChild(4).click(true);
                                    Task.sleep(500, 600);
                                } if(Variables.urnType.contains("shield") || Variables.urnType.contains("legs")) {
                                    Widgets.get(1371, 44).getChild(12).click(true);
                                    Task.sleep(500, 600);
                                } if(Variables.urnType.contains("torso")) {
                                    Widgets.get(1371, 44).getChild(16).click(true);
                                    Task.sleep(500, 600);
                                }
                            } else {
                                Crafting.clickStart();
                                final Timer leatherTime = new Timer(60000);
                                if(Variables.urnType.contains("body") || Variables.urnType.contains("torso")) {
                                    while (leatherTime.isRunning()&& Inventory.getCount(Variables.leatherID) > 2
                                            || Players.getLocal().getAnimation() != -1) {
                                        Task.sleep(100, 120);
                                    }
                                } if(Variables.urnType.contains("shield")) {
                                    while (leatherTime.isRunning()&& Inventory.getCount(Variables.leatherID) > 3
                                            || Players.getLocal().getAnimation() != -1) {
                                        Task.sleep(100, 120);
                                    }
                                } if(Variables.urnType.contains("chaps") || Variables.urnType.contains("legs") || Variables.urnType.contains("helm")) {
                                    while (leatherTime.isRunning()&& Inventory.getCount(Variables.leatherID) > 1
                                            || Players.getLocal().getAnimation() != -1) {
                                        Task.sleep(100, 120);
                                    }
                                } if(Variables.urnType.contains("vambraces") || Variables.urnType.contains("gloves") || Variables.urnType.contains("boots")) {
                                    while (leatherTime.isRunning()&& Inventory.getCount(Variables.leatherID) > 0
                                            || Players.getLocal().getAnimation() != -1) {
                                        Task.sleep(100, 120);
                                    }
                                }
                            }
                        }
                    }
                    if (!leatherInterface1()
                            && !leatherInterface2()) {
                        WidgetChild leather = Inventory.getItem(Variables.leatherID).getWidgetChild();
                        if (leather != null) {
                            if (leather.interact("Craft")) {
                                int randomNum = Random.nextInt(0, 10);
                                int randomNum2 = Random.nextInt(1, 2);
                                if(randomNum2 == 1) {
                                    Mouse.move(404 + randomNum, 352 + randomNum);
                                } else {
                                    Mouse.move(404 - randomNum, 352 -randomNum);
                                }
                                Timer failSafe = new Timer(2500);
                                while(failSafe.isRunning() && !Widgets.get(1370, 0).validate()) {
                                    Task.sleep(5);
                                }
                            }
                        }
                    }
                }
            } else {
                Bot.context().getScriptHandler().shutdown();
            }
        } else {
            Bank.close();
        }
    }

}
