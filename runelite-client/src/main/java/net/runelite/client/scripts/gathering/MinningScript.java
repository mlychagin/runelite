package net.runelite.client.scripts.gathering;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.client.plugins.zCyprus.RealisticCamera;
import net.runelite.client.plugins.zCyprus.RealisticClicker;
import net.runelite.client.ui.overlay.OverlayManager;

import static net.runelite.api.ItemID.*;
import static net.runelite.api.ItemID.COAL;
import static net.runelite.api.ObjectID.*;

public class MinningScript extends GatheringScript {
    public static byte TIN_MODE = 0;
    public static byte COPPER_MODE = 1;
    public static byte IRON_MODE = 2;
    public static byte MITHRIL_MODE = 3;
    public static byte ADAMANTITE_MODE = 4;
    public static byte RUNITE_MODE = 5;
    public static byte SILVER_MODE = 6;
    public static byte GOLD_MODE = 7;
    public static byte COAL_MODE = 8;

    private byte oreType;

    public MinningScript(Client client, OverlayManager overlayManager, RealisticCamera camera, RealisticClicker clicker,
            byte oreType) {
        super(client, overlayManager, camera, clicker);
        this.oreType = oreType;
        this.maxIdleDuration = 200;
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
            case ROCKS_11360:
            case ROCKS_11361:
                return oreType == TIN_MODE;
            case ROCKS_10943:
            case ROCKS_11161:
                return oreType == COPPER_MODE;
            case ROCKS_11364:
            case ROCKS_11365:
            case ROCKS_36203:
                return oreType == IRON_MODE;
            case MITHRIL_ORE:
                return oreType == MITHRIL_MODE;
            case ADAMANTITE_ORE:
                return oreType == ADAMANTITE_MODE;
            case ROCKS_11376:
            case ROCKS_11377:
            case ROCKS_36209:
                return oreType == RUNITE_MODE;
            case ROCKS_11368:
            case ROCKS_11369:
            case ROCKS_36205:
                return oreType == SILVER_MODE;
            case ROCKS_11370:
            case ROCKS_11371:
            case ROCKS_36206:
                return oreType == GOLD_MODE;
            case ROCKS_11366:
            case ROCKS_11367:
            case ROCKS_36204:
                return oreType == COAL_MODE;
            default:
                return false;
        }
    }

    @Override protected boolean isLootItem(int id) {
        switch (id) {
            case COPPER_ORE:
            case TIN_ORE:
            case IRON_ORE:
            case SILVER_ORE:
            case GOLD_ORE:
            case PERFECT_GOLD_ORE:
            case MITHRIL_ORE:
            case ADAMANTITE_ORE:
            case RUNITE_ORE:
            case COAL:
                return true;
            default:
                return false;
        }
    }
}
