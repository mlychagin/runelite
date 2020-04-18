package net.runelite.client.robot;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.WorldLocation;

import java.awt.event.KeyEvent;

public class Calculations {

    public static byte getCameraMovementDirection(Client c, LocalPoint o) {
        LocalPoint p = c.getLocalPlayer().getLocalLocation();
        int objectXAdjusted = o.getX() - p.getX();
        int objectYAdjusted = o.getY() - p.getY();
        int cameraXAdjusted = c.getCameraX() - p.getX();
        int cameraYAdjusted = c.getCameraY() - p.getY();
        int determinant = objectXAdjusted * cameraYAdjusted - cameraXAdjusted * objectYAdjusted;
        if (determinant > 0) {
            return KeyEvent.VK_RIGHT;
        } else {
            return KeyEvent.VK_LEFT;
        }
    }

    public static double getCameraAngle(Client c, LocalPoint o) {
        LocalPoint p = c.getLocalPlayer().getLocalLocation();
        int cameraX = c.getCameraX();
        int cameraY = c.getCameraY();

        double numerator =
                Math.pow(getDistance(p, o), 2)
                        + Math.pow(getDistance(p.getX(), p.getY(), cameraX, cameraY), 2)
                        - Math.pow(getDistance(cameraX, cameraY, o.getX(), o.getY()), 2);
        double denominator = 2 * getDistance(p, o) * getDistance(p.getX(), p.getY(), cameraX, cameraY);

        return Math.toDegrees(Math.acos(numerator / denominator));
    }

    public static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static double getDistance(LocalPoint p1, LocalPoint p2) {
        return getDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
    public static double getDistance(Point p1, Point p2) {
        return getDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public static double getDistance(WorldPoint p1, WorldPoint p2) {
        return getDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }


}
