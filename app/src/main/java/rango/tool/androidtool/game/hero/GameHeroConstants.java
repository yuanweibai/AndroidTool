package rango.tool.androidtool.game.hero;

import rango.tool.common.utils.ScreenUtils;

class GameHeroConstants {

    /**
     * The ratio of the width of the PerfectRect to the width of the screen
     */
    static final float PERFECT_RECT_WIDTH_RATIO = 1 / 60f;

    /**
     * The ratio of the height of the PerfectRect to the width of the PerfectRect
     */
    static final float PERFECT_RECT_HEIGHT_WIDTH_RATIO = 3 / 4f;

    /**
     * The score passed one level.
     */
    static final float GAME_SCORE_EVERY_LEVEL = 1;

    /**
     * The score passed perfectly
     */
    static final float GAME_SCORE_PERFECTLY = 3;

    /**
     * The hierarchy of game
     * The game score is divided into several stages, such as: [0,10]、(10,20]、(20,30]、30+
     */
    static final int[] GAME_HIERARCHY = new int[]{
            30,
    };

    /**
     * The multiple of the width of pillar than the width of PrefectRect int every hierarchy
     */
    static final int[][] PILLAR_WIDTH_MULTIPLE = new int[][]{
            {6, 8, 10},
            {6, 8, 10},
            {6, 8, 10},
            {6, 8, 10}
    };

    /**
     * The multiple of the width of interstice than the width of PrefectRect int every hierarchy
     */
    static final int[][] INTERSTICE_WIDTH_MULTIPLE = new int[][]{
            {3, 4, 8, 15, 24,},
            {3, 4, 8, 15, 24, 30, 40},
            {3, 4, 8, 15, 24, 30, 40},
            {3, 4, 8, 15, 24, 30, 40}
    };

    /**
     * The ratio of the height of the init pillar
     */
    static final float PILLAR_INIT_HEIGHT_RATIO = 1 / 5f;

    /**
     * The multiple of the width of init pillar
     */
    static final float PILLAR_INIT_WIDTH_MULTIPLE = 10;

    /**
     * The ratio of the height of the pillar to the height of screen
     */
    static final float PILLAR_HEIGHT_RATIO = 1 / 3f;

    /**
     * The min ratio of the distance of first pillar left
     */
    static final float FIRST_PILLAR_LEFT_MIN_RATIO = 1 / 5f;

    /**
     * The speed of bridge growing
     */
    static final float BRIDGE_GROWING_SPEED = ScreenUtils.dp2px(7);

    /**
     * The duration of bridge rotate anim
     */
    static final long BRIDGE_ROTATE_DURATION = 300;

    /**
     * The speed of walking: px per second
     */
    static final float WALK_SPEED = ScreenUtils.dp2px(200);

    /**
     * The speed of go next level: px per second
     */
    static final float GO_TO_NEXT_LEVEL_SPEED = ScreenUtils.dp2px(500);

    /**
     * The duration of fall anim
     */
    static final long FALL_DURATION = 300;

    /**
     * The duration of start anim
     */
    static final long START_DURATION = 300;

}
