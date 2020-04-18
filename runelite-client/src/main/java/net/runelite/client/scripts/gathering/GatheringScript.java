package net.runelite.client.scripts.gathering;

import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.zCyprus.RealisticCamera;
import net.runelite.client.plugins.zCyprus.RealisticClicker;
import net.runelite.client.robot.Flexo;
import net.runelite.client.scripts.Script;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.event.KeyEvent;

import static net.runelite.api.AnimationID.IDLE;

public abstract class GatheringScript extends Script {
    protected Client client;
    protected OverlayManager overlayManager;
    protected RealisticCamera camera;
    protected RealisticClicker clicker;

    protected boolean idle;
    protected long idleStart;
    protected long maxIdleDuration = 2000;

    public GatheringScript(Client client, OverlayManager overlayManager, RealisticCamera camera,
            RealisticClicker clicker) {
        this.client = client;
        this.overlayManager = overlayManager;
        this.camera = camera;
        this.clicker = clicker;
        this.idle = false;
    }

    @SneakyThrows @Override public void run() {
        while (true) {
            processFullInventory();
            proccessIdle();
            Thread.sleep(200);
        }
    }

    private void processFullInventory() throws InterruptedException {
        if (idle && isInventoryFull()) {
            clicker.stopClicker();
            Flexo f = clicker.getFlexo();
            f.keyPress(KeyEvent.VK_SHIFT);
            for (WidgetItem i : overlayManager.getItemWidgets()) {
                if (isLootItem(i.getId())) {
                    clicker.addInventoryClick(i);
                }
            }
            clicker.startClicker();
            clicker.waitClicker();
            f.keyRelease(KeyEvent.VK_SHIFT);
            clickGatheringSpot();
            Thread.sleep(10000);
            idle = false;
        }
    }

    private boolean isInventoryFull(){
        ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);
        if (itemContainer == null) {
            return false;
        }
        Item[] items = itemContainer.getItems();
        int count = 0;
        for (Item i : items) {
            if (i.getId() != -1) {
                count++;
            }
        }
        return count == 28;
    }

    private void proccessIdle() throws InterruptedException {
        if (!idle && client.getLocalPlayer().getAnimation() == IDLE) {
            idleStart = System.currentTimeMillis();
            idle = true;
        }
        if (client.getLocalPlayer().getAnimation() != IDLE) {
            idle = false;
        }
        if (idle) {
            if (System.currentTimeMillis() - idleStart > maxIdleDuration) {
                clickGatheringSpot();
                clicker.waitClicker();
                Thread.sleep(10000);
            }
        }
    }

    protected void clickGatheringSpot() {
        NPC spot = null;
        double shortestDistance = Double.MAX_VALUE;
        LocalPoint p = client.getLocalPlayer().getLocalLocation();
        for (NPC n : client.getNpcs()) {
            if (!isGatheringSpot(n.getId())) {
                continue;
            }
            double d = p.distanceTo(n.getLocalLocation());
            if (d < shortestDistance) {
                shortestDistance = d;
                spot = n;
            }
        }
        if (spot != null) {
            clicker.addActorClick(spot);
        }
    }

    protected abstract boolean isGatheringSpot(int id);

    protected abstract boolean isLootItem(int id);

}
