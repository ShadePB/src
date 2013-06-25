package org.shade.scripts.crafter.tasks.spells;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import org.shade.scripts.crafter.misc.Variables;
import sk.action.ActionBar;

public class LeatherMake {

    public static void cast() {
        if (!Bank.isOpen()) {
            if (Widgets.get(1370, 0).validate()) {
                Widgets.get(1370, 20).click(true);
                Task.sleep(700, 800);
                Timer failSafe = new Timer(25000);
                while ((Inventory.getCount(Variables.leatherUsing) > 1 || Players
                        .getLocal().getAnimation() != -1)
                        && failSafe.isRunning()) {
                    Task.sleep(50, 60);
                }
            } else {
                Item i = Inventory.getItem(Variables.leatherUsing);
                if (i != null) {
                    if(!ActionBar.isReadyForInteract()) {
                        ActionBar.makeReadyForInteract();
                    }
                    ActionBar.useSlot(0);
                    Task.sleep(400, 500);
                    i.getWidgetChild().interact("Cast");
                    Task.sleep(1200, 1300);
                }
            }
        } else {
            Bank.close();
        }
    }

}
