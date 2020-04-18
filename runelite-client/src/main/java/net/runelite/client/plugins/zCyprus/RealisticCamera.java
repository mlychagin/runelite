package net.runelite.client.plugins.zCyprus;

import lombok.SneakyThrows;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.robot.Flexo;

import java.awt.*;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RealisticCamera extends Thread {
    private Client client;
    private Queue<Actor> actorQueue;
    private Flexo robot;

    public RealisticCamera(Client client) throws AWTException {
        this.client = client;
        this.actorQueue = new LinkedBlockingQueue<>();
        this.robot = new Flexo();
    }

    public boolean panActor(Actor a) {
        LocalPoint l = a.getLocalLocation();
        if (l != null) {
            actorQueue.add(a);
            return true;
        }
        return false;
    }

    @SneakyThrows public void run() {
        while (true) {
            while(!actorQueue.isEmpty()){
                robot.panCameraActor(actorQueue.poll());
            }
            Thread.sleep(200);
        }
    }
}
