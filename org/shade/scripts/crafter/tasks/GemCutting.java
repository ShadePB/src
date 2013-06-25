package org.shade.scripts.crafter.tasks;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.shade.scripts.crafter.misc.Variables;
import org.shade.scripts.crafter.nodes.Crafting;

public class GemCutting {

    public static boolean gemInterface() {
        return Widgets.get(1370, 0).validate();
    }

    public static void make() {
        if (!Bank.isOpen()) {
            if (Inventory.getCount(Variables.gemToCut) > 0) {
                if (gemInterface()) {
                    Crafting.clickStart();
                    final Timer gemTimer = new Timer(20000);
                    while (gemTimer.isRunning()
                            && Inventory.getCount(Variables.gemToCut) != 0) {
                        Task.sleep(50);
                    }
                } else {
                    WidgetChild gem = Inventory.getItem(Variables.gemToCut)
                            .getWidgetChild();
                    if (gem != null) {
                        gem.interact("Craft");
                        Task.sleep(600, 700);
                    }
                }
            }
        } else {
            Bank.close();
        }
    }

}
