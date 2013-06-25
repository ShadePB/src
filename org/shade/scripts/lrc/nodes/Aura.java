package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.wrappers.node.Item;
import org.shade.scripts.lrc.misc.Variables;

public class Aura extends Node {

    @Override
    public boolean activate() {
        return Variables.useAura;
    }

    @Override
    public void execute() {
        Item i = Equipment.getItem(Equipment.Slot.AURA);
        if(i != null) {
            if(Equipment.getItem(Equipment.Slot.AURA).getWidgetChild().interact("Activate")) {
                Task.sleep(1000, 2000);
                Variables.useAura = false;
            }
        }
    }
}
