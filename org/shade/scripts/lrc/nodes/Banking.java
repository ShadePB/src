package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.tab.Summoning;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Entity;
import org.shade.scripts.lrc.misc.Magic;
import org.shade.scripts.lrc.misc.Variables;
import sk.action.book.magic.Spell;

public class Banking extends Node{
    @Override
    public boolean activate() {
        if(!Players.getLocal().isInCombat()) {
            if(Variables.usingUrns) {
                return Inventory.getItem(Variables.FINISHED_URN_ID) == null && Inventory.getItem(Variables.URN_ID) == null && Inventory.getItem(Variables.CURRENT_URN_ID) == null;
            } if(Variables.usingSummoning) {
                if(!Summoning.isFamiliarSummoned()) {
                    if(Inventory.getItem(Variables.ALL_POUCHES_ID) == null) {
                        return true;
                    } else {
                        if(Summoning.getPoints() < 8) {
                            if(Inventory.getItem(Variables.SUMMONING_POTION_ID) == null) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void execute() {
        Variables.status = "Banking";
        Variables.needToBank = true;
        if(!Bank.isOpen()) {
            Entity b = Bank.getNearest();
            if(b != null) {
                Bank.open();
            } else {
                if (!Widgets.get(1092).validate()) {
                    Spell.HOME_TELEPORT.show();
                    Spell.HOME_TELEPORT.getChild().click(true);
                    Task.sleep(600, 700);
                } else {
                    Magic.clickLodestone(46);
                    Task.sleep(2200, 2300);
                    while(Players.getLocal().getAnimation() != -1) {
                        Task.sleep(90);
                    }
                }
            }
        } else {
            //Can definitely be improved :3
            Bank.depositInventory();
            if(Variables.usingUrns) {
                if(Bank.getItem(Variables.CURRENT_URN_ID) != null) {
                    Bank.withdraw(Variables.CURRENT_URN_ID, 1);
                    Task.sleep(500, 600);
                } if(Bank.getItem(Variables.URN_ID) != null) {
                    Bank.withdraw(Variables.URN_ID, 10);
                    Task.sleep(500, 600);
                } else {
                    Variables.usingUrns = false;
                }
            } if(Variables.usingSummoning) {
                if(Skills.getLevel(Skills.SUMMONING) >= 83) {
                    if(Bank.getItem(Variables.LAVA_TITAN_POUCH_ID) != null) {
                        Bank.withdraw(Variables.LAVA_TITAN_POUCH_ID, 4);
                        Task.sleep(500, 600);
                    } else {
                        Variables.usingSummoning = false;
                    }
                } else {
                    if(Bank.getItem(Variables.OBSIDIAN_GOLEM_POUCH_ID) != null) {
                        Bank.withdraw(Variables.OBSIDIAN_GOLEM_POUCH_ID, 4);
                        Task.sleep(500, 600);
                    } else {
                        Variables.usingSummoning = false;
                    }
                } if(Bank.getItem(12140) != null) {
                    Bank.withdraw(12140, 1);
                    Task.sleep(500, 600);
                } else {
                    Variables.usingSummoning = false;
                }
            }
            Bank.close();
            Variables.needToBank = false;
        }
    }
}
