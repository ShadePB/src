package org.shade.scripts.lrc.misc;

import org.powerbot.game.api.wrappers.Tile;

public class Variables {
	
	//SceneObject IDs
	public static final int COAL_DEPOSIT_ID = 5999;
    public static final int GOLD_DEPOSIT_ID = 45076;
    public static final int[] BOTH_DEPOSITS_ID = { 5999, 45076 };
    public static final int TROLLEY_ID = 45079;
	
	//Item IDs
    public static final int GOLD_ORE_ID = 444;
    public static final int COAL_ORE_ID = 453;
    public static final int GOLD_BAR_ID = 2357;
    public static final int[] SUMMONING_POTION_ID = { 12140, 12142, 12144, 12146 };
    public static final int[] PICKAXE_IDS = { 15259, 1275 };
    public static final int OBSIDIAN_GOLEM_POUCH_ID = 12792;
    public static final int LAVA_TITAN_POUCH_ID = 12788;
    public static final int[] ALL_POUCHES_ID = { 12792, 12788 };
    public static final int FINISHED_URN_ID = 20408;
    public static final int URN_ID = 20406 ;
    public static final int CURRENT_URN_ID = 20407 ;
    public static final int[] RUNE_IDS = { 561, 554 };
    public static final int[] SCRIMSHAW_IDS =  { 26286, 26288, 26287, 26289 };
	
	//Other
    public static final Tile WEST_TILE = new Tile(3638, 5095, 0);
    public static final Tile EAST_TILE = new Tile(3668, 5076, 0);
    public static final Tile COAL_TILE = new Tile(3665, 5091, 0);
    public static final Tile COAL_TILE_2 = new Tile(3675, 5099, 0);

    public static boolean banking = true;
    public static boolean pickaxeEquipped = true;
    public static boolean usingSummoning = false;
    public static boolean usingUrns = false;
    public static boolean usingSuperheat = false;
    public static boolean usingScrimshaws = false;
    public static boolean useAura = false;
	
	//Paint
    public static String status = "Welcome";
    public static int startExp = 0;
    public static int startExpSmith = 0;
    public static int orePrice = 0;
    public static int oresMined = 0;
    public static long startTime = 0;
	public static int expGained = 0;
    public static int profitGained = 0;
	
}