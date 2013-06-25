package org.shade.scripts.crafter.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.shade.scripts.crafter.misc.Variables;
import org.shade.scripts.crafter.tasks.*;
import org.shade.scripts.crafter.tasks.spells.GlassMake;
import org.shade.scripts.crafter.tasks.spells.LeatherMake;

public class Crafting extends Node {

    public static void clickStart() {
        Widgets.get(1370, 33).getChild(0).click(true);
        Task.sleep(2500, 2600);
    }

    @Override
    public void execute() {
        try {
            if (Tabs.getCurrent() != Tabs.INVENTORY) {
                Tabs.INVENTORY.open();
            }
            Variables.status = "Crafting";
            if (Variables.leatherMake) {
                LeatherMake.cast();
            } if(Variables.battlestaff) {
                Battlestaff.make();
            } if(Variables.glassMake) {
                GlassMake.cast();
            } if(Variables.silver) {
                Silver.make();
            } if (Variables.needleAndThread) {
                NeedleAndThread.make();
            } if (Variables.gemCutting) {
                GemCutting.make();
            } if (Variables.flax) {
                Flax.make();
            } if (Variables.bars) {
                Bars.make();
            } if (Variables.bracelets) {
                Bracelets.make();
            } if (Variables.glass) {
                Glass.make();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean activate() {
        if(Variables.urnType != null) {
        try {
        if(Variables.bracelets) {
            if(Variables.usingGems) {
                return (Inventory.getCount(Variables.selectedGem) <= 14 && Inventory.getCount(Variables.GOLD_BAR_ID) <= 14 && Inventory.getCount(Variables.GOLD_BAR_ID) != 0 && Inventory.getCount(Variables.selectedGem) != 0);
            } else {
                return Inventory.getCount(Variables.GOLD_BAR_ID) <= 28 && Inventory.getCount(Variables.GOLD_BAR_ID) != 0;
            }
        } if(Variables.glass) {
            return Inventory.getCount(Variables.glassID) != 0;
        } if(Variables.bars) {
            return Inventory.getCount(Variables.GOLD_ORE_ID) != 0;
        } if(Variables.flax) {
            return Inventory.getCount(Variables.FLAX_ID) != 0;
        } if(Variables.gemCutting) {
            return Inventory.getCount(Variables.gemToCut) != 0;
        } if(Variables.needleAndThread) {
            if(Variables.useSacredClay) {
                if(Variables.urnType.contains("body") || Variables.urnType.contains("torso"))
                    return Inventory.getCount(Variables.leatherID) >= 3 && Inventory.getCount(Variables.SACRED_CLAY_NEEDLE_ID) != 0;
                if(Variables.urnType.contains("shield"))
                    return Inventory.getCount(Variables.leatherID) > 3 && Inventory.getCount(Variables.SACRED_CLAY_NEEDLE_ID) != 0;
                if(Variables.urnType.contains("chaps")  || Variables.urnType.contains("legs") || Variables.urnType.contains("helm"))
                    return Inventory.getCount(Variables.leatherID) >= 2 && Inventory.getCount(Variables.SACRED_CLAY_NEEDLE_ID) != 0;
                if(Variables.urnType.contains("vambraces") || Variables.urnType.contains("boots") || Variables.urnType.contains("gloves"))
                    return Inventory.getCount(Variables.leatherID) >= 1 && Inventory.getCount(Variables.SACRED_CLAY_NEEDLE_ID) != 0;
            } else {
                if(Variables.urnType.contains("body") || Variables.urnType.contains("torso")) {
                    return Inventory.getCount(Variables.leatherID) >= 3;
                } else if(Variables.urnType.contains("shield")) {
                    return Inventory.getCount(Variables.leatherID) > 3;
                } else if(Variables.urnType.contains("chaps")  || Variables.urnType.contains("legs") || Variables.urnType.contains("helm")) {
                    return Inventory.getCount(Variables.leatherID) >= 2;
                } else if(Variables.urnType.contains("vambraces") || Variables.urnType.contains("boots") || Variables.urnType.contains("gloves")) {
                    return Inventory.getCount(Variables.leatherID) >= 1;
                }
            }
        } if(Variables.silver) {
            return Inventory.getCount(Variables.SILVER_BAR_ID) != 0;
        } if(Variables.glassMake) {
            return (Inventory.getCount(Variables.SAND_ID) > 0 && Inventory.getCount(Variables.SEAWEED_ID) > 0)
                    && (Inventory.getCount(Variables.SAND_ID) == Inventory.getCount(Variables.SEAWEED_ID));
        } if(Variables.leatherMake) {
            return Inventory.getCount(Variables.leatherUsing) > 4;
        } if(Variables.battlestaff) {
            return (Inventory.getCount(Variables.selectedOrbID) > 0 && Inventory.getCount(Variables.BATTLE_STAFF_ID) > 0);
        }
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        } else {
            System.out.println(Variables.urnType);
        }
        return false;
    }


}
