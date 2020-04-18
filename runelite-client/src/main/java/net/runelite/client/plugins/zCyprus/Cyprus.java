/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.zCyprus;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.StatChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.robot.Flexo;
import net.runelite.client.scripts.Script;
import net.runelite.client.scripts.combat.MobScript;
import net.runelite.client.scripts.gathering.FishingScript;
import net.runelite.client.scripts.gathering.MinningScript;
import net.runelite.client.scripts.gathering.WoodcuttingScript;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.robot.Calculations.getDistance;
import static net.runelite.client.scripts.gathering.MinningScript.COPPER_MODE;
import static net.runelite.client.scripts.gathering.WoodcuttingScript.OAK_MODE;

@PluginDescriptor(name = "Cyprus", description = "F2P PvP Bot") public class Cyprus extends Plugin {
    private static final Duration WAIT = Duration.ofSeconds(5);

    /*
     * Game Info
     */
    private Integer lastMaxHealth;
    @Inject private Client client;
    @Inject private ClientUI clientUI;
    @Inject private OverlayManager overlayManager;

    /*
     * Movement
     */
    private RealisticClicker clicker;
    private RealisticCamera camera;

    /*
     * Scripts
     */
    private ArrayList<Script> scripts = new ArrayList<>();

    @Override protected void startUp() throws Exception {
        this.clicker = new RealisticClicker(client);
        this.camera = new RealisticCamera(client);
        Flexo.client = client;
        Flexo.clientUI = clientUI;
        clicker.start();
        camera.start();
    }

    @Subscribe public void onNpcSpawned(NpcSpawned event) {
        for(Script s : scripts){
            s.onNpcSpawned(event);
        }
    }

    @Subscribe public void onNpcDespawned(NpcDespawned npcDespawned) {
        for(Script s : scripts){
            s.onNpcDespawned(npcDespawned);
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged statChanged){
        for(Script s : scripts){
            s.onStatChanged(statChanged);
        }
    }


    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {
        for(Script s : scripts){
            s.onInteractingChanged(event);
        }
    }


    @Subscribe public void onChatMessage(ChatMessage event) throws InterruptedException {
        String message = event.getMessage();
        System.out.println(event.getMessage());
        if (message.startsWith("P ")) {
            String desiredPlayerName = message.substring(2).toLowerCase();
            List<Player> playerList = client.getPlayers();
            for (Player p : playerList) {
                String name = p.getName();
                if (name == null) {
                    continue;
                }
                if (p.getName().toLowerCase().equals(desiredPlayerName)) {
                    clicker.addActorClick(p);
                }
            }
        }
        if (message.startsWith("N ")) {
            String desiredPlayerName = message.substring(2).toLowerCase();
            List<NPC> npcList = client.getNpcs();
            for (NPC n : npcList) {
                String name = n.getName();
                if (name == null) {
                    continue;
                }
                if (n.getName().toLowerCase().equals(desiredPlayerName)) {
                    camera.panActor(n);
                }
            }
        }
        if (message.equals("P")) {
            clicker.addActivateQuickPrayer();
        }
        if (message.equals("R")) {
            clicker.addActivateRun();
        }
        if (message.equals("F")) {
            FishingScript script = new FishingScript(client, overlayManager, camera, clicker);
            script.start();
            scripts.add(script);
        }
        if (message.equals("W")) {
            WoodcuttingScript script = new WoodcuttingScript(client, overlayManager, camera, clicker, OAK_MODE);
            script.start();
            scripts.add(script);
        }
        if (message.equals("M")) {
            MinningScript script = new MinningScript(client, overlayManager, camera, clicker, COPPER_MODE);
            script.start();
            scripts.add(script);
        }
        if (message.equals("C")) {
            MobScript script = new MobScript(client, overlayManager, camera, clicker);
            script.addMobType(NpcID.COW);
            script.addMobType(NpcID.COW_2791);
            script.addMobType(NpcID.COW_2793);
            script.addMobType(NpcID.COW_2795);
            script.addMobType(NpcID.COW_5842);
            script.addMobType(NpcID.COW_6401);
            script.start();
            scripts.add(script);
        }
        for(Script s : scripts){
            s.onChatMessage(event);
        }
    }

}