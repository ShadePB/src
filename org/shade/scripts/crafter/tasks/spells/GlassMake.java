package org.shade.scripts.crafter.tasks.spells;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.widget.Bank;
import sk.action.ActionBar;

public class GlassMake {

    public static void cast() {
        if (!Bank.isOpen()) {
            if(!ActionBar.isReadyForInteract()) {
                ActionBar.makeReadyForInteract();
            }
            Keyboard.sendKey('1');
            Task.sleep(2500, 2600);
        } else {
            Bank.close();
        }
    }

}
