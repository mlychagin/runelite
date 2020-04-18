package net.runelite.client.scripts;

import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.StatChanged;

public abstract class Script extends Thread {

    public void onNpcSpawned(NpcSpawned event) {
    }

    public void onNpcDespawned(NpcDespawned npcDespawned) {
    }

    public void onStatChanged(StatChanged statChanged) {
    }

    public void onChatMessage(ChatMessage event) {
    }

    public void onAnimationChanged(AnimationChanged e) {
    }

    public void onInteractingChanged(InteractingChanged event) {
    }

}
