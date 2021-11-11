import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Constants {
    public static final int COLOR_MASK = 0xffffff;
    public static final int KEYED_IMAGE_MIN = 5;
    public static final int KEYED_RED_IDX = 2;
    public static final int KEYED_GREEN_IDX = 3;
    public static final int KEYED_BLUE_IDX = 4;

    public static final int PROPERTY_KEY = 0;

    public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right",
            "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_NUM_PROPERTIES = 4;
    public static final int SAPLING_ID = 1;
    public static final int SAPLING_COL = 2;
    public static final int SAPLING_ROW = 3;
    public static final int SAPLING_HEALTH = 4;

    public static final String BGND_KEY = "background";
    public static final int BGND_NUM_PROPERTIES = 4;
    public static final int BGND_ID = 1;
    public static final int BGND_COL = 2;
    public static final int BGND_ROW = 3;

    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 5;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;
    public static final int OBSTACLE_ANIMATION_PERIOD = 4;

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_NUM_PROPERTIES = 7;
    public static final int DUDE_ID = 1;
    public static final int DUDE_COL = 2;
    public static final int DUDE_ROW = 3;
    public static final int DUDE_LIMIT = 4;
    public static final int DUDE_ACTION_PERIOD = 5;
    public static final int DUDE_ANIMATION_PERIOD = 6;

    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 4;
    public static final int HOUSE_ID = 1;
    public static final int HOUSE_COL = 2;
    public static final int HOUSE_ROW = 3;

    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_NUM_PROPERTIES = 6;
    public static final int FAIRY_ID = 1;
    public static final int FAIRY_COL = 2;
    public static final int FAIRY_ROW = 3;
    public static final int FAIRY_ANIMATION_PERIOD = 4;
    public static final int FAIRY_ACTION_PERIOD = 5;

    public static final String STUMP_KEY = "stump";

    public static final String TREE_KEY = "tree";
    public static final int TREE_NUM_PROPERTIES = 7;
    public static final int TREE_ID = 1;
    public static final int TREE_COL = 2;
    public static final int TREE_ROW = 3;
    public static final int TREE_ANIMATION_PERIOD = 4;
    public static final int TREE_ACTION_PERIOD = 5;
    public static final int TREE_HEALTH = 6;

    public static final int TREE_ANIMATION_MAX = 600;
    public static final int TREE_ANIMATION_MIN = 50;
    public static final int TREE_ACTION_MAX = 1400;
    public static final int TREE_ACTION_MIN = 1000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;


}
