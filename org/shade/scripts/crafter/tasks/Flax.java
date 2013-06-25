package org.shade.scripts.crafter.tasks;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.shade.scripts.crafter.misc.Variables;
import org.shade.scripts.crafter.nodes.Crafting;

public class Flax {

    public static boolean spinInterface() {
        return Widgets.get(1370, 0).validate();
    }

    public static void make() {
        try {
            SceneObject spinner = SceneEntities.getNearest(Variables.SPINNING_WHEEL_ID);
            if (spinner != null) {
                spinAll();
            } else {
                SceneObject stairs = SceneEntities.getNearest(Variables.STAIRS_DOWN_ID);
                if (stairs != null) {
                    if (stairs.isOnScreen()) {
                        if(Bank.isOpen()){
                            Bank.close();
                        }
                        if (stairs.interact("Climb-down")) {
                            Task.sleep(500);
                            while (Players.getLocal().isMoving()) {
                                Task.sleep(50);
                            }
                        }
                    } else {
                        Walking.walk(stairs.getLocation());
                        Task.sleep(500);
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void spinAll() {
        Variables.status = "Crafting";
        try {
            if(spinInterface()) {
                if(Variables.urnType.equals(Widgets.get(1370, 56).getText())) {
                    Crafting.clickStart();
                    final Timer flaxTimer = new Timer(60000);
                    while(flaxTimer.isRunning() && Inventory.getCount(Variables.FLAX_ID) != 0) {
                        Task.sleep(50);
                    }
                } else {
                    Widgets.get(1371, 44).getChild(4).click(true);
                    Crafting.clickStart();
                    Variables.status = "Crafting";
                    final Timer flaxTimer = new Timer(60000);
                    while(flaxTimer.isRunning() && Inventory.getCount(Variables.FLAX_ID) != 0) {
                        Task.sleep(50);
                    }
                }
            } else {
                SceneObject spinner = SceneEntities.getNearest(Variables.SPINNING_WHEEL_ID);
                if(spinner != null) {
                    if(spinner.isOnScreen()) {
                        if(spinner.interact("Spin")) {
                            Task.sleep(500, 900);
                            while(Players.getLocal().isMoving()) {
                                Task.sleep(50);
                            }
                        }
                    } else {
                        Walking.walk(spinner.getLocation());
                        Task.sleep(900);
                    }
                }
            }
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

}
