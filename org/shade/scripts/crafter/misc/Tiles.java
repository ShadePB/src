package org.shade.scripts.crafter.misc;

import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

public class Tiles {

    public static final Tile[] PATH_TO_BANK_ALKHARID = {
            new Tile(3271, 3190, 0), new Tile(3273, 3190, 0),
            new Tile(3275, 3189, 0), new Tile(3277, 3189, 0),
            new Tile(3279, 3189, 0), new Tile(3280, 3187, 0),
            new Tile(3280, 3185, 0), new Tile(3280, 3183, 0),
            new Tile(3279, 3181, 0), new Tile(3278, 3179, 0),
            new Tile(3277, 3177, 0), new Tile(3277, 3175, 0),
            new Tile(3276, 3173, 0), new Tile(3276, 3171, 0),
            new Tile(3276, 3169, 0), new Tile(3275, 3167, 0),
            new Tile(3273, 3166, 0), new Tile(3271, 3165, 0),
            new Tile(3269, 3166, 0), new Tile(3269, 3168, 0)
    };

    public static final Tile[] PATH_TO_BANK_EDGEVILLE = {
            new Tile(3108, 3499, 0), new Tile(3103, 3498, 0),
            new Tile(3098, 3497, 0), new Tile(3097, 3496, 0)
    };

    public static final Tile[] PATH_TO_BANK_PHASMATY = {
            new Tile(3685, 3478, 0), new Tile(3685, 3473, 0),
            new Tile(3689, 3473, 0), new Tile(3689, 3469, 0),
            new Tile(3688, 3466, 0)
    };

    public static final Tile[] PATH_TO_BANK_NEITIZNOT = {
            new Tile(2342, 3809, 0), new Tile(2341, 3804, 0),
            new Tile(2336, 3803, 0), new Tile(2336, 3807, 0)
    };

    public static Tile[] pathToBank;
    public static Area bankArea;
    public static Tile furnaceTile;

    public static final Tile edgevilleFurnaceTile = new Tile(3109, 3501, 0);
    public static final Tile alKharidFurnaceTile = new Tile(3274, 3190, 0);
    public static final Tile neitiznotFurnaceTile = new Tile(2342, 3808, 0);
    public static final Tile phasmatyFurnaceTile = new Tile(3684, 3478, 0);

    public static final Area BANK_AREA_ALKHARID = new Area(new Tile[] {
            new Tile(3263, 3175, 0), new Tile(3268, 3175, 0),
            new Tile(3273, 3175, 0), new Tile(3274, 3170, 0),
            new Tile(3274, 3165, 0), new Tile(3271, 3160, 0),
            new Tile(3266, 3159, 0), new Tile(3261, 3162, 0)
    });

    public static final Area BANK_AREA_EDGEVILLE = new Area(new Tile[] {
            new Tile(3090, 3502, 0), new Tile(3095, 3502, 0),
            new Tile(3100, 3501, 0), new Tile(3100, 3496, 0),
            new Tile(3100, 3491, 0), new Tile(3097, 3487, 0),
            new Tile(3092, 3486, 0)
    });

    public static final Area BANK_AREA_PHASMATY = new Area(new Tile[] {
            new Tile(3684, 3472, 0), new Tile(3691, 3472, 0),
            new Tile(3691, 3460, 0), new Tile(3684, 3460, 0)
    });

    public static final Area BANK_AREA_NEITIZNOT = new Area(new Tile[] {
            new Tile(2339, 3804, 0), new Tile(2332, 3804, 0),
            new Tile(2332, 3809, 0), new Tile(2339, 3809, 0)
    });

}
