package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.shade.scripts.lrc.misc.Variables;

public class Combat extends Node {

	public static NPC getRock() {
		Filter<NPC> i = new Filter<NPC>() {
			public boolean accept(NPC entity) {
				return entity.getName().contains("rock") && entity.getInteracting().equals(Players.getLocal());
			}
		};
		NPC b = NPCs.getNearest(i);
		return b;
	}
	
	@Override
	public boolean activate() {
		NPC mob = getRock();
		return Players.getLocal().isInCombat() || mob != null;
	}

	@Override
	public void execute() {
		Variables.status = "Running from combat";
		Walking.walk(new Tile(3653, 5115, 0));
		Task.sleep(500, 600);
	}

}
