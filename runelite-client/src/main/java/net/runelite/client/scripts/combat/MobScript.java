package net.runelite.client.scripts.combat;

import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.plugins.zCyprus.RealisticCamera;
import net.runelite.client.plugins.zCyprus.RealisticClicker;
import net.runelite.client.scripts.Script;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.HashSet;

public class MobScript extends Script {
    protected Client client;
    protected OverlayManager overlayManager;
    protected RealisticCamera camera;
    protected RealisticClicker clicker;

    private HashSet<Integer> mobTypes = new HashSet<>();
    private NPC currentMob;

    public MobScript(Client client, OverlayManager overlayManager, RealisticCamera camera, RealisticClicker clicker) {
        this.client = client;
        this.overlayManager = overlayManager;
        this.camera = camera;
        this.clicker = clicker;
    }

    public void addMobType(int mobType) {
        mobTypes.add(mobType);
    }

    @SneakyThrows @Override public void run() {
        while (true) {
            if (queryNewMob()) {
                clickNewMob();
                Thread.sleep(500);
            } else {
                Thread.sleep(200);
            }
        }
    }

    private boolean queryNewMob() {
        if (currentMob == null) {
            return true;
        }
        Player p = client.getLocalPlayer();
        if (p.getInteracting() == null) {
            return true;
        }
        if (currentMob.getInteracting() != null && currentMob.getInteracting() != p) {
            return true;
        }
        if (currentMob.getHealthRatio() == 0) {
            return true;
        }
        return false;
    }

    protected void clickNewMob() {
        NPC mob = null;
        double shortestDistance = Double.MAX_VALUE;
        LocalPoint p = client.getLocalPlayer().getLocalLocation();
        for (NPC n : client.getNpcs()) {
            if (!mobTypes.contains(n.getId())) {
                continue;
            }
            if (n.getInteracting() != null) {
                continue;
            }
            if(n.getHealthRatio() == 0){
                continue;
            }
            double d = p.distanceTo(n.getLocalLocation());
            if (d < shortestDistance) {
                shortestDistance = d;
                mob = n;
            }
        }
        currentMob = mob;
        clickCurrentMob();
    }

    private void clickCurrentMob() {
        if (currentMob != null) {
            clicker.addActorClick(currentMob);
        }
    }

}
