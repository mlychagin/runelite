package net.runelite.client.plugins.zCyprus;

import lombok.SneakyThrows;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GraphicsObject;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.robot.Flexo;
import net.runelite.client.robot.FlexoMouse;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RealisticClicker extends Thread {
    private Client client;
    private Queue<Object> moveStack;
    private Flexo robot;
    private boolean running = true;

    public RealisticClicker(Client client) throws AWTException {
        this.client = client;
        this.moveStack = new LinkedBlockingQueue<>();
        this.robot = new Flexo();
    }

    public Flexo getFlexo(){
        return robot;
    }

    public boolean isEmpty(){
        return moveStack.isEmpty();
    }

    public void waitClicker() throws InterruptedException {
        while (!isEmpty()) {
            Thread.sleep(50);
        }
    }

    public void stopClicker(){
        running = false;
    }

    public void startClicker(){
        running = true;
    }


    public boolean addInventoryClick(WidgetItem widgetItem) {
        moveStack.add(FlexoMouse.getClickPoint(widgetItem.getCanvasBounds()));
        return true;
    }

    public boolean addActorClick(Actor a) {
        if(a == null){
            System.out.println("Invalid Actor");
            return false;
        }
        Shape s;
        try{
            s = a.getConvexHull();
            if(s == null){
                System.out.println("Actor out of Bounds");
                return false;
            }
        } catch (Exception e){
            return false;
        }
        Point p = FlexoMouse.getClickPoint(a.getConvexHull().getBounds());
        if(p == null){
            return false;
        }
        moveStack.add(p);
        return true;
    }

    public boolean addObjectClick(GameObject o) {
        if(o == null){
            System.out.println("Invalid Object");
            return false;
        }
        if(o.getConvexHull() == null){
            System.out.println("Object out of Bounds");
        }
        Point p = FlexoMouse.getClickPoint(o.getConvexHull().getBounds());
        if(p == null){
            return false;
        }
        moveStack.add(p);
        return true;
    }

    public boolean addActivateQuickPrayer() {
        moveStack.add(FlexoMouse.getClickPoint(client.getWidget(WidgetInfo.MINIMAP_PRAYER_ORB).getBounds()));
        return true;
    }

    public boolean addActivateRun() {
        moveStack.add(FlexoMouse.getClickPoint(client.getWidget(WidgetInfo.MINIMAP_TOGGLE_RUN_ORB).getBounds()));
        return true;
    }

    @SneakyThrows public void run() {
        while (true) {
            while(running && !moveStack.isEmpty()){
                Object o = moveStack.peek();
                if(o instanceof Point){
                    System.out.println("CLICK");
                    robot.mouseMove((Point) o);
                    robot.mousePressAndRelease(MouseEvent.BUTTON1);
                }
                moveStack.poll();
            }
            Thread.sleep(200);
        }
    }

}
