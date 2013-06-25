package org.shade.scripts.crafter.tasks;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.shade.scripts.crafter.misc.Variables;
import org.shade.scripts.crafter.nodes.Crafting;

public class Battlestaff {

    public static void make() {
        if(!Bank.isOpen()) {
            if(!Widgets.get(1370, 0).validate()) {
                if(!Inventory.isItemSelected()) {
                    Inventory.getItem(Variables.selectedOrbID).getWidgetChild().interact("Use");
                    Timer failsafe = new Timer(1000);
                    while(!Inventory.isItemSelected() && failsafe.isRunning()) {
                        Task.sleep(50, 60);
                    }
                } if(Inventory.isItemSelected()) {
                    Inventory.getItem(Variables.BATTLE_STAFF_ID).getWidgetChild().interact("Use");
                    Timer failsafe = new Timer(1000);
                    while(!Widgets.get(1370, 0).validate() && failsafe.isRunning()) {
                        Task.sleep(50, 60);
                    }
                }
            } else {
                Crafting.clickStart();
                Timer failsafe = new Timer(20000);
                while(Inventory.getItem(Variables.BATTLE_STAFF_ID) != null && Inventory.getItem(Variables.selectedOrbID) != null && failsafe.isRunning()) {
                    Task.sleep(50, 60);
                }
            }
        } else {
            Bank.close();
        }
    }

}
