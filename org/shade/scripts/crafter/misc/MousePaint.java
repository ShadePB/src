package org.shade.scripts.crafter.misc;

import org.powerbot.game.api.methods.input.Mouse;

import java.awt.*;
import java.util.LinkedList;

public class MousePaint {

    public int waveSize = 0;

    @SuppressWarnings( { "serial" })
    public class mousePathPoint extends Point {

        private long finishTime;
        @SuppressWarnings("unused")
        private double lastingTime;

        public mousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }
    }

    public double getRot(int ticks) {
        return (System.currentTimeMillis() % (360 * ticks)) / ticks;
    }

    public LinkedList<mousePathPoint> mousePath = new LinkedList<mousePathPoint>();

    public void drawTrail(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
            mousePath.remove();
        }
        Point clientCursor = Mouse.getLocation();
        mousePathPoint mpp = new mousePathPoint(clientCursor.x,
                clientCursor.y, 250);
        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) {
            mousePath.add(mpp);
        }
        mousePathPoint lastPoint = null;
        for (mousePathPoint a : mousePath) {
            if (lastPoint != null) {
                long mpt = System.currentTimeMillis()
                        - Mouse.getPressTime();
                if (Mouse.getPressTime() == -1 || mpt >= 250) {
                    g2.setColor(Color.WHITE);
                }
                if (mpt < 250) {
                    g2.setColor(Color.RED);
                }
                g2.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = a;
        }
    }

    public void drawMouse(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);
        g2.drawOval(Mouse.getLocation().x - 8, Mouse.getLocation().y - 8,
                15, 15);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(0, 0, 0, 114));
        g2.fillOval(Mouse.getLocation().x - 8, Mouse.getLocation().y - 8,
                15, 15);
        Point MouseLocation = Mouse.getLocation();
        long mpt = System.currentTimeMillis() - Mouse.getPressTime();
        g2.rotate(Math.toRadians(getRot(5)), Mouse.getLocation().x, Mouse
                .getLocation().y);
        if (Mouse.getPressTime() == -1 || mpt >= 250) {
            g2.setColor(Color.WHITE);
            g2.drawLine(MouseLocation.x - 4, MouseLocation.y,
                    MouseLocation.x + 4, MouseLocation.y);
            g2.drawLine(MouseLocation.x, MouseLocation.y - 4,
                    MouseLocation.x, MouseLocation.y + 4);
        }
        if (mpt < 250) {
            g2.setColor(Color.RED);
            g2.drawLine(MouseLocation.x - 5, MouseLocation.y,
                    MouseLocation.x + 5, MouseLocation.y);
            g2.drawLine(MouseLocation.x, MouseLocation.y - 5,
                    MouseLocation.x, MouseLocation.y + 5);
        }
    }

    public void drawWave(Graphics g, Color color) {
        Graphics2D g2 = (Graphics2D) g;
        Point MouseLoc = new Point(Mouse.getPressX(), Mouse.getPressY());
        long mpt = System.currentTimeMillis() - Mouse.getPressTime();
        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
        g2.setColor(color);
        if (mpt < 600) {
            waveSize = (int) (mpt / 7.5);
        } else {
            waveSize = 0;
        }
        g2.setStroke(new BasicStroke(1));
        g2.drawOval(MouseLoc.x - (waveSize / 2), MouseLoc.y
                - (waveSize / 2), waveSize, waveSize);
        g2.drawOval(MouseLoc.x - ((waveSize / 2) / 2), MouseLoc.y
                - ((waveSize / 2) / 2), waveSize / 2, waveSize / 2);
    }

    public void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
        drawWave(g2, Color.BLACK);
        drawTrail(g2);
        drawMouse(g2);
    }

}
