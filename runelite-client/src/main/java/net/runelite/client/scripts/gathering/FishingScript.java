package net.runelite.client.scripts.gathering;

import net.runelite.api.Client;
import net.runelite.client.plugins.zCyprus.RealisticCamera;
import net.runelite.client.plugins.zCyprus.RealisticClicker;
import net.runelite.client.ui.overlay.OverlayManager;

import static net.runelite.api.ItemID.*;
import static net.runelite.api.NpcID.*;

public class FishingScript extends GatheringScript {

    public FishingScript(Client client, OverlayManager overlayManager, RealisticCamera camera,
            RealisticClicker clicker) {
        super(client, overlayManager, camera, clicker);
    }

    @Override protected boolean isGatheringSpot(int id) {
        switch (id) {
            case FISHING_SPOT_1514:
            case FISHING_SPOT_1518:
            case FISHING_SPOT_1521:
            case FISHING_SPOT_1523:
            case FISHING_SPOT_1524:
            case FISHING_SPOT_1525:
            case FISHING_SPOT_1528:
            case FISHING_SPOT_1530:
            case FISHING_SPOT_1544:
            case FISHING_SPOT_3913:
            case FISHING_SPOT_7155:
            case FISHING_SPOT_7459:
            case FISHING_SPOT_7462:
            case FISHING_SPOT_7467:
            case FISHING_SPOT_7469:
            case FISHING_SPOT_7947:
            case FISHING_SPOT_1510:
            case FISHING_SPOT_1519:
            case FISHING_SPOT_1522:
            case FISHING_SPOT_3914:
            case FISHING_SPOT_5820:
            case FISHING_SPOT_7199:
            case FISHING_SPOT_7460:
            case FISHING_SPOT_7465:
            case FISHING_SPOT_7470:
            case FISHING_SPOT_7946:
            case FISHING_SPOT_1511:
            case FISHING_SPOT_1520:
            case FISHING_SPOT_3915:
            case FISHING_SPOT_4476:
            case FISHING_SPOT_4477:
            case FISHING_SPOT_5233:
            case FISHING_SPOT_5234:
            case FISHING_SPOT_5821:
            case FISHING_SPOT_7200:
            case FISHING_SPOT_7461:
            case FISHING_SPOT_7466:
            case FISHING_SPOT_8525:
            case FISHING_SPOT_8526:
            case FISHING_SPOT_8527:
            case FISHING_SPOT_4316:
            case ROD_FISHING_SPOT:
            case ROD_FISHING_SPOT_1506:
            case ROD_FISHING_SPOT_1508:
            case ROD_FISHING_SPOT_1509:
            case ROD_FISHING_SPOT_1513:
            case ROD_FISHING_SPOT_1515:
            case ROD_FISHING_SPOT_1516:
            case ROD_FISHING_SPOT_1526:
            case ROD_FISHING_SPOT_1527:
            case ROD_FISHING_SPOT_7463:
            case ROD_FISHING_SPOT_7464:
            case ROD_FISHING_SPOT_7468:
            case ROD_FISHING_SPOT_8524:
            case FISHING_SPOT_4928:
            case FISHING_SPOT_6784:
            case FISHING_SPOT_1542:
            case FISHING_SPOT_7323:
            case ROD_FISHING_SPOT_6825:
            case FISHING_SPOT_7730:
            case FISHING_SPOT_7731:
            case FISHING_SPOT_7732:
            case FISHING_SPOT_7733:
            case ROD_FISHING_SPOT_7676:
            case FISHING_SPOT_4712:
            case FISHING_SPOT_4713:
            case FISHING_SPOT_4710:
            case FISHING_SPOT_6488:
            case FISHING_SPOT_1497:
            case FISHING_SPOT_1498:
            case FISHING_SPOT_2653:
            case FISHING_SPOT_2654:
            case FISHING_SPOT_2655:
            case FISHING_SPOT_1536:
            case FISHING_SPOT_8523:
                return true;
            default:
                return false;
        }
    }

    @Override protected boolean isLootItem(int id) {
        switch (id) {
            case RAW_SHRIMPS:
            case RAW_ANCHOVIES:
            case RAW_SARDINE:
            case RAW_SALMON:
            case RAW_TROUT:
            case RAW_GIANT_CARP:
            case RAW_COD:
            case RAW_HERRING:
            case RAW_PIKE:
            case RAW_MACKEREL:
            case RAW_TUNA:
            case RAW_BASS:
            case RAW_SWORDFISH:
            case RAW_LOBSTER:
            case RAW_SHARK:
            case RAW_MANTA_RAY:
            case RAW_SEA_TURTLE:
                return true;
            default:
                return false;
        }
    }
}
