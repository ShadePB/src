package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.tab.Summoning;
import org.powerbot.game.api.wrappers.node.Item;
import org.shade.scripts.lrc.misc.Variables;

public class Summon extends Node {

	@Override
	public boolean activate() {
		return Variables.usingSummoning && !Summoning.isFamiliarSummoned();
	}

	@Override
	public void execute() {
		Variables.status = "Summoning";
		if(Skills.getRealLevel(Skills.SUMMONING) >= 83) {
			if(Summoning.getPoints() > 8 && Inventory.getItem(Variables.LAVA_TITAN_POUCH_ID) != null) {
				Summoning.summonFamiliar(Summoning.Familiar.LAVA_TITAN);
				Task.sleep(1000, 1500);
			} else {
				Item potion = Inventory.getItem(Variables.SUMMONING_POTION_ID);
				if(potion != null) {
					potion.getWidgetChild().click(true);
					Task.sleep(1200, 1600);
					while(Players.getLocal().getAnimation() != -1) {
						Task.sleep(50, 60);
					}
				} else {
					Variables.usingSummoning = false;
				}
			}
		} else {
			if(Summoning.getPoints() > 7 && Inventory.getItem(Variables.OBSIDIAN_GOLEM_POUCH_ID) != null) {
				Summoning.summonFamiliar(Summoning.Familiar.OBSIDIAN_GOLEM);
				Task.sleep(1000, 1500);
			} else {
				Item potion = Inventory.getItem(Variables.SUMMONING_POTION_ID);
				if(potion != null) {
					potion.getWidgetChild().click(true);
					Task.sleep(1200, 1600);
					while(Players.getLocal().getAnimation() != -1) {
						Task.sleep(50, 60);
					}
				} else {
					Variables.usingSummoning = false;
				}
			}
		}
	}

}
