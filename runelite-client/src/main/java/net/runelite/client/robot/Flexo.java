package net.runelite.client.robot;/*
 *
 *   Copyright (c) 2019, Zeruth <TheRealNull@gmail.com>
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, this
 *      list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *   ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

/*
Modified java.awt.Robot for use with openosrs. Hopefully we can make it stand far apart.
Uses
https://github.com/JoonasVali/NaturalMouseMotion
for mouse motion.
 */

import com.github.kbman99.naturalmouse.api.MouseMotionFactory;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.ClientUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.logging.Logger;

import static net.runelite.client.robot.Calculations.getCameraAngle;
import static net.runelite.client.robot.Calculations.getCameraMovementDirection;

public class Flexo extends Robot {
    public static boolean isActive;
    public static double scale;
    public static Client client;
    public static ClientUI clientUI;
    public static int fixedWidth = 765;
    public static int fixedHeight = 503;
    public static boolean isStretched;
    public static int minDelay = 45;
    public static MouseMotionFactory currentMouseMotionFactory;
    public boolean pausedIndefinitely = false;
    private Thread holdKeyThread;
    private Robot peer;

    public Flexo() throws AWTException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new AWTException("headless environment");
        }
        init(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
    }

    private void init(GraphicsDevice screen) {
        try {
            peer = new Robot();
        } catch (Exception e) {
            client.getLogger().error("Flexo not supported on this system configuration.");
        }
    }

    private transient Object anchor = new Object();

    private void pauseMS(int delayMS) {
        long initialMS = System.currentTimeMillis();
        while (System.currentTimeMillis() < initialMS + delayMS) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isActive = false;
    }

    @Override public synchronized void mouseMove(int x, int y) {
        try {
            //TODO: Must be better way to determine titlebar width
            currentMouseMotionFactory.build(client.getCanvas(), x, y).move();
            this.delay(getMinDelay());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void mouseMove(Point p) {
        Point p2 = p;
        mouseMove((int) p.getX(), (int) p.getY());
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override public synchronized void mousePress(int buttonID) {
        if (buttonID < 1 || buttonID > 5) {
            Logger.getAnonymousLogger().warning("Invalid mouse button ID. please use 1-5.");
            return;
        }
        dispatchMouseEvent(MouseEvent.MOUSE_PRESSED, buttonID, true);
    }

    public synchronized void mousePressAndRelease(int buttonID) {
        if (buttonID < 1 || buttonID > 5) {
            Logger.getAnonymousLogger().warning("Invalid mouse button ID. please use 1-5.");
            return;
        }
        dispatchMouseEvent(MouseEvent.MOUSE_PRESSED, buttonID, true);
        dispatchMouseEvent(MouseEvent.MOUSE_RELEASED, buttonID, false);
        dispatchMouseEvent(MouseEvent.MOUSE_CLICKED, buttonID, true);
    }

    @Override public synchronized void mouseRelease(int buttonID) {
        if (buttonID < 1 || buttonID > 5) {
            Logger.getAnonymousLogger().warning("Invalid mouse button ID. please use 1-5.");
            return;
        }
        dispatchMouseEvent(MouseEvent.MOUSE_RELEASED, buttonID, true);
    }

    private int getMinDelay() {
        Random random = new Random();
        int random1 = random.nextInt(minDelay);
        if (random1 < minDelay / 2)
            random1 = random.nextInt(minDelay / 2) + minDelay / 2 + random.nextInt(minDelay / 2);
        return random1;
    }

    private int getWheelDelay() {
        Random random = new Random();
        int random1 = random.nextInt(minDelay);
        if (random1 < minDelay / 2)
            random1 = random.nextInt(minDelay / 2) + minDelay / 2 + random.nextInt(minDelay / 2);
        return random1;
    }

    /**
     * Rotates the scroll wheel on wheel-equipped mice.
     *
     * @param wheelAmt number of "notches" to move the mouse wheel
     *                 Negative values indicate movement up/away from the user,
     *                 positive values indicate movement down/towards the user.
     * @since 1.4
     */
    @Override public synchronized void mouseWheel(int wheelAmt) {
        for (int i : new int[wheelAmt]) {
            peer.mouseWheel(wheelAmt);
            this.delay(getWheelDelay());
        }
    }

    /**
     * Presses a given key.  The key should be released using the
     * <code>keyRelease</code> method.
     * <p>
     * Key codes that have more than one physical key associated with them
     * (e.g. <code>KeyEvent.VK_SHIFT</code> could mean either the
     * left or right shift key) will map to the left key.
     *
     * @param keycode Key to press (e.g. <code>KeyEvent.VK_A</code>)
     * @throws IllegalArgumentException if <code>keycode</code> is not
     *                                  a valid key
     * @see #keyRelease(int)
     * @see java.awt.event.KeyEvent
     */
    @Override public synchronized void keyPress(int keycode) {
        dispatchKeyEvent(KeyEvent.KEY_PRESSED, keycode, true);
    }

    @Override public synchronized void keyRelease(int keycode) {
        dispatchKeyEvent(KeyEvent.KEY_RELEASED, keycode, true);
    }

    public synchronized void holdKey(int keycode, int timeMS) {
        new Thread(() -> {
            peer.keyPress(keycode);
            dispatchKeyEvent(KeyEvent.KEY_PRESSED, keycode, false);
            long startTime = System.currentTimeMillis();
            while ((startTime + timeMS) > System.currentTimeMillis()) {
            }
            peer.keyRelease(keycode);
            dispatchKeyEvent(KeyEvent.KEY_RELEASED, keycode, false);
            this.delay(getMinDelay());
        }).start();
    }

    public synchronized void holdKeyIndefinitely(int keycode) {
        holdKeyThread = new Thread(() -> {
            pausedIndefinitely = true;
            dispatchKeyEvent(KeyEvent.KEY_PRESSED, keycode, false);
            while (pausedIndefinitely) {
                try {
                    holdKeyThread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            dispatchKeyEvent(KeyEvent.KEY_RELEASED, keycode, false);
            this.delay(getMinDelay());
        });
        holdKeyThread.start();
    }

    public synchronized void keyPressAndRelease(int keycode) {
        dispatchKeyEvent(KeyEvent.KEY_PRESSED, keycode, true);
        dispatchKeyEvent(KeyEvent.KEY_RELEASED, keycode, true);
    }

    private void dispatchKeyEvent(int eventAction, int keyCode, boolean delay) {
        KeyEvent key = new KeyEvent(client.getCanvas(), eventAction, System.currentTimeMillis(), 0, keyCode,
                KeyEvent.CHAR_UNDEFINED, KeyEvent.KEY_LOCATION_STANDARD);
        client.getCanvas().dispatchEvent(key);
        if (delay) {
            this.delay(getMinDelay());
        }
    }

    private void dispatchMouseEvent(int eventAction, int buttonID, boolean delay) {
        MouseEvent press = new MouseEvent(client.getCanvas(), eventAction, System.currentTimeMillis(), 0,
                client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), 1, false, buttonID);
        client.getCanvas().dispatchEvent(press);
        if (delay) {
            this.delay(getMinDelay());
        }
    }

    public Color getPixelColor(int x, int y) {
        return peer.getPixelColor(x, y);
    }

    @Override public void delay(int ms) {
        pauseMS(ms);
    }

    public void delay(long ms) {
        pauseMS((int) ms);
    }

    /*
     * Cyprus Methods
     */
    public void panCameraActor(Actor a) {
        if (client.getLocalPlayer() == null) {
            return;
        }
        int keyPress = getCameraMovementDirection(client, a.getLocalLocation());
        int threshHold = 5;
        new Thread(() -> {
            peer.keyPress(keyPress);
            while (true) {
                LocalPoint actorLocation = a.getLocalLocation();
                double cameraAngle = getCameraAngle(client, actorLocation);
                if (Math.abs(180 - cameraAngle) < threshHold) {
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            peer.keyRelease(keyPress);
        }).start();
    }

}