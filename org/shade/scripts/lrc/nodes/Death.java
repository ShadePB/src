package org.shade.scripts.lrc.nodes;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.tab.Summoning;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.shade.scripts.lrc.misc.Magic;
import org.shade.scripts.lrc.misc.Variables;
import sk.action.book.magic.Spell;

public class Death extends Node {

    Area dwarvenMinesArea = new Area(new Tile[] { new Tile(2979, 3459, 0), new Tile(3031, 3460, 0), new Tile(3027, 3383, 0),
            new Tile(2956, 3386, 0), new Tile(2942, 3427, 0) });

    Tile[] dwarvenMinePath = new Tile[] { new Tile(2966, 3404, 0), new Tile(2968, 3409, 0), new Tile(2971, 3413, 0),
            new Tile(2974, 3417, 0), new Tile(2979, 3418, 0), new Tile(2984, 3421, 0),
            new Tile(2987, 3425, 0), new Tile(2989, 3430, 0), new Tile(2994, 3432, 0),
            new Tile(2999, 3433, 0), new Tile(3002, 3433, 0), new Tile(3007, 3433, 0),
            new Tile(3012, 3434, 0), new Tile(3015, 3438, 0), new Tile(3014, 3443, 0),
            new Tile(3015, 3448, 0), new Tile(3018, 3449, 0) };

    @Override
    public boolean activate() {
        return Players.getLocal().getLocation().getY() < 5000 || Players.getLocal().getLocation().getY() > 5300 && !Variables.needToBank;
    }

    @Override
    public void execute() {
        Variables.status = "Traversing [LRC]";
        Item pick = Inventory.getItem(Variables.PICKAXE_IDS);
        if(pick != null) {
            if(pick.getId() == 15259) {
                if(Skills.getLevel(Skills.ATTACK) > 59) {
                    pick.getWidgetChild().click(true);
                    Task.sleep(500, 600);
                }
            } else {
                if(Skills.getLevel(Skills.ATTACK) > 49) {
                    pick.getWidgetChild().click(true);
                    Task.sleep(500, 600);
                }
            }
        }
        if(Players.getLocal().getLocation().getY() > 9000) {
            SceneObject rope = SceneEntities.getNearest(45077);
            if(rope != null) {
                if(rope.isOnScreen()) {
                    if(rope.interact("Climb")) {
                        Task.sleep(500, 600);
                        while(Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1) {
                            Task.sleep(50, 60);
                        }
                    }
                } else {
                    Walking.walk(new Tile(3012, 9832, 0));
                    Task.sleep(500, 600);
                }
            } else {
                Walking.walk(new Tile(3012, 9832, 0));
                Task.sleep(500, 600);
            }
        } else {
            if(dwarvenMinesArea.contains(Players.getLocal().getLocation())) {
                if(Summoning.getPoints() < Skills.getLevel(Skills.SUMMONING)) {
                    SceneObject obelisk = SceneEntities.getNearest(83294);
                    if(obelisk != null) {
                        if(obelisk.isOnScreen()) {
                            if(obelisk.click(true)) {
                                Task.sleep(500, 600);
                                while(Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1) {
                                    Task.sleep(50, 60);
                                }
                            }
                        } else {
                            Walking.walk(obelisk.getLocation());
                            Task.sleep(500, 600);
                        }
                    } else {
                        Walking.findPath(new Tile(2977, 3409, 0)).traverse();
                        Task.sleep(500, 600);
                    }
                } else {
                    SceneObject ladder = SceneEntities.getNearest(30942);
                    if(ladder != null) {
                        if(ladder.isOnScreen()) {
                            if(ladder.interact("Climb")) {
                                Task.sleep(500, 600);
                                while(Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1) {
                                    Task.sleep(50, 60);
                                }
                            }
                        } else {
                            Walking.newTilePath(dwarvenMinePath).traverse();
                            Task.sleep(400, 500);
                        }
                    } else {
                        Walking.newTilePath(dwarvenMinePath).traverse();
                        Task.sleep(400, 500);
                    }
                }
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
        }
    }
}
