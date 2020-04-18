package net.runelite.client.scripts.gathering;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.client.plugins.zCyprus.RealisticCamera;
import net.runelite.client.plugins.zCyprus.RealisticClicker;
import net.runelite.client.ui.overlay.OverlayManager;

import static net.runelite.api.ItemID.LOGS;
import static net.runelite.api.ItemID.*;
import static net.runelite.api.NullObjectID.NULL_10823;
import static net.runelite.api.NullObjectID.NULL_10835;
import static net.runelite.api.ObjectID.MAPLE_TREE;
import static net.runelite.api.ObjectID.OAK_TREE;
import static net.runelite.api.ObjectID.TREE;
import static net.runelite.api.ObjectID.*;

public class WoodcuttingScript extends GatheringScript {
    public static byte NORMAL_MODE = 0;
    public static byte ACHEY_MODE = 1;
    public static byte OAK_MODE = 2;
    public static byte WILLOW_MODE = 3;
    public static byte TEAK_MODE = 4;
    public static byte MAPLE_MODE = 5;
    public static byte HOLLOW_MODE = 6;
    public static byte MAHOGANY_MODE = 7;
    public static byte ARCTIC_PINE_MODE = 8;
    public static byte YEW_MODE = 9;
    public static byte SULLIUSCEP_MODE = 10;
    public static byte MAGIC_MODE = 11;
    public static byte REDWOOD_MODE = 12;

    private byte treeType;

    public WoodcuttingScript(Client client, OverlayManager overlayManager, RealisticCamera camera,
            RealisticClicker clicker, byte treeType) {
        super(client, overlayManager, camera, clicker);
        this.treeType = treeType;
        this.maxIdleDuration = 1000;
    }

    @Override protected void clickGatheringSpot() {
        GameObject spot = null;
        double shortestDistance = Double.MAX_VALUE;
        LocalPoint p = client.getLocalPlayer().getLocalLocation();
        for (GameObject o : new GameObjectQuery().result(client)) {
            if (!isGatheringSpot(o.getId())) {
                continue;
            }
            double d = p.distanceTo(o.getLocalLocation());
            if (d < shortestDistance) {
                shortestDistance = d;
                spot = o;
            }
        }
        if (spot != null) {
            clicker.addObjectClick(spot);
        }
    }

    @Override protected boolean isGatheringSpot(int id) {
        switch (id) {
            case TREE:
            case TREE_1277:
            case TREE_1278:
            case TREE_1279:
            case TREE_1280:
                return treeType == NORMAL_MODE;
            case OAK_TREE:
            case OAK_TREE_4540:
            case OAK_10820:
                return treeType == OAK_MODE;
            case WILLOW:
            case WILLOW_10829:
            case WILLOW_10831:
            case WILLOW_10833:
                return treeType == WILLOW_MODE;
            case MAPLE_TREE:
            case MAPLE_TREE_10832:
            case MAPLE_TREE_36681:
                return treeType == MAPLE_MODE;
            case TEAK:
            case TEAK_36686:
                return treeType == TEAK_MODE;
            case MAHOGANY:
            case MAHOGANY_36688:
                return treeType == MAHOGANY_MODE;
            case YEW:
            case NULL_10823:
            case YEW_36683:
                return treeType == YEW_MODE;
            case MAGIC_TREE_10834:
            case NULL_10835:
                return treeType == MAGIC_MODE;
            case REDWOOD:
            case REDWOOD_29670:
                return treeType == REDWOOD_MODE;
            default:
                return false;
        }
    }

    @Override protected boolean isLootItem(int id) {
        switch (id) {
            case LOGS:
            case MAGIC_LOGS:
            case YEW_LOGS:
            case MAPLE_LOGS:
            case WILLOW_LOGS:
            case OAK_LOGS:
            case ARCTIC_PINE_LOGS:
            case WINDSWEPT_LOGS:
            case SCRAPEY_TREE_LOGS:
            case TEAK_LOGS:
            case MAHOGANY_LOGS:
            case ACHEY_TREE_LOGS:
            case REDWOOD_LOGS:
                return true;
            default:
                return false;
        }
    }
}
