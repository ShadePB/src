package org.shade.scripts.crafter.tasks;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import org.shade.scripts.crafter.misc.Variables;
import org.shade.scripts.crafter.nodes.Crafting;

public class Glass {

    public static void make() {
        if (Bank.isOpen()) {
            Bank.close();
        } else {
            if (Inventory.getCount(Variables.glassID) != 0) {
                    Variables.status = "Crafting";
                    Item glass = Inventory.getItem(Variables.glassID);
                    if (Widgets.get(1371, 0).validate()) {
                        if (Widgets.get(1370, 56).getText().equals(Variables.urnType)) {
                            Crafting.clickStart();
                            final Timer glassTimer = new Timer(
                                    60000);
                            while (glassTimer.isRunning()
                                    && Inventory.getCount(Variables.glassID) != 0) {
                                Task.sleep(50);
                            }
                        } else {
                            Widgets.get(1371, 44).getChild(20).click(true);
                            Task.sleep(500, 600);
                            Crafting.clickStart();
                            final Timer glassTimer = new Timer(
                                    60000);
                            while (glassTimer.isRunning()
                                    && Inventory.getCount(Variables.glassID) != 0) {
                                Task.sleep(50);
                            }
                        }
                    } else {
                        if (glass != null) {
                            if (glass.getWidgetChild() != null) {
                                Variables.status = "Crafting";
                                glass.getWidgetChild().click(true);
                                Task.sleep(800, 900);
                            }
                        }
                    }
            }
        }
    }

}
