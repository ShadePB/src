package org.shade.scripts.dagonhai.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.shade.scripts.dagonhai.misc.Variables;
import sk.action.ActionBar;

public class Attack extends Node {

    public boolean isInCombat() {
        return getCurrent() != null;
    }

    public NPC getCurrent() {
        Filter<NPC> i = new Filter<NPC>() {
            public boolean accept(NPC entity) {
                return (entity.getId() == 7138 || entity.getId() == 7139 || entity.getId() == 7140)
                     && entity.getInteracting() != null && entity.getInteracting().equals(Players.getLocal()) && entity.getAnimation() != 836;
            }
        };
        return NPCs.getNearest(i);
    }

    public NPC getNext() {
        Filter<NPC> i = new Filter<NPC>() {
            public boolean accept(NPC entity) {
                return (entity.getId() == 7138 || entity.getId() == 7139 || entity.getId() == 7140)
                     && entity.getInteracting() == null && entity.getAnimation() != 836;
            }
        };
        return NPCs.getNearest(i);
    }

    @Override
    public boolean activate() {
        if(Variables.foodID != 1) {
            return Inventory.getItem(Variables.foodID) != null;
        } else {
            return Inventory.getItem(Variables.PRAYER_POTION_ID) != null;
        }
    }

    @Override
    public void execute() {
        if(Settings.get(1769) != 0 && Players.getLocal().getHealthRatio() > 100) {
            Widgets.get(749, 0).click(true);
            Task.sleep(500, 600);
        }
        if(Players.getLocal().getHealthRatio() < 80) {
            if(Variables.foodID == 1) {
                Variables.status = "Activating prayer";
                if(Integer.parseInt(Widgets.get(749, 6).getText()) < 100) {
                    Item pot = Inventory.getItem(Variables.PRAYER_POTION_ID);
                    if(pot != null) {
                        Variables.status = "Drinking pot";
                        pot.getWidgetChild().click(true);
                        Task.sleep(1500, 1600);
                    }
                } else {
                    if(Settings.get(1769) == 0) {
                        Widgets.get(749, 0).click(true);
                        Task.sleep(500, 600);
                    }
                }
            } else {
                Item food = Inventory.getItem(Variables.foodID);
                if(food != null) {
                    Variables.status = "Eating " + food.getName();
                    food.getWidgetChild().click(true);
                    Task.sleep(1500, 1600);
                }
            }
        }
        if(ActionBar.isExpanded()) {
            Variables.status = "Closing action bar";
            ActionBar.setExpanded(false);
        }
        if(isInCombat()) {
            if(Players.getLocal().getInteracting() == null) {
                NPC dagonhai = getCurrent();
                if(dagonhai != null) {
                    if(dagonhai.isOnScreen()) {
                        Variables.status = "Attacking Dagon'hai";
                        dagonhai.interact("Attack", dagonhai.getName());
                        Timer failSafe = new Timer(5000);
                        while((Players.getLocal().getInteracting() == null || Players.getLocal().isMoving()) && failSafe.isRunning()) {
                            Task.sleep(50, 60);
                        }
                    } else {
                        if(Calculations.distanceTo(dagonhai.getLocation()) > 5) {
                            Variables.status = "Walking to Dagon'hai";
                            Walking.walk(dagonhai.getLocation());
                            Task.sleep(500, 600);
                        } else {
                            Variables.status = "Turning to Dagon'hai";
                            Camera.turnTo(dagonhai.getLocation());
                        }
                    }
                }
            } else {
                NPC next = getNext();
                if(next != null) {
                    if(Calculations.distanceTo(next.getLocation()) < 13) {
                        if(next.isOnScreen()) {
                            if(!next.getModel().contains(Mouse.getLocation())) {
                                next.hover();
                            }
                        } else {
                            Camera.turnTo(next.getLocation());
                        }
                    }
                }
            }
        } else {
            NPC dagonhai = getNext();
            if(dagonhai != null) {
                if(dagonhai.isOnScreen()) {
                    Variables.status = "Attacking Dagon'hai";
                    dagonhai.interact("Attack", dagonhai.getName());
                    Timer failSafe = new Timer(5000);
                    while((Players.getLocal().getInteracting() == null || Players.getLocal().isMoving()) && failSafe.isRunning()) {
                        Task.sleep(50, 60);
                    }
                } else {
                    if(Calculations.distanceTo(dagonhai.getLocation()) > 5) {
                        Variables.status = "Walking to Dagon'hai";
                        Walking.walk(dagonhai.getLocation());
                        Task.sleep(500, 600);
                    } else {
                        Variables.status = "Turning to Dagon'hai";
                        Camera.turnTo(dagonhai.getLocation());
                    }
                }
            } else {
                Tile middle = new Tile(3255, 5463, 0);
                if(Calculations.distanceTo(middle) > 5) {
                    Variables.status = "Walking to middle";
                    Walking.walk(middle.randomize(2, 2));
                    Task.sleep(500, 600);
                }
            }
        }
    }
}
