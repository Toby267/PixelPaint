package org.scc200g15.tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.scc200g15.action.PixelsChangedAction;
import org.scc200g15.gui.GUI;
import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Layer;

public class
FillTool implements Tool{

    @Override
    public void mouseClicked(PCanvas c, MouseEvent e) {
        Point2D point = c.getPixelPoint(e.getPoint());
        Color[][] pixels = GUI.getInstance().getActiveImage().compressVisibleLayers();

        Color newColor = GUI.getInstance().getSideBar().getActiveColor();

        Color targetColor = pixels[(int)point.getX()][(int)point.getY()];

        Queue<Point> pointsToCheck = new LinkedList<>();
        ArrayList<Point> checkedPoints = new ArrayList<>();

        ArrayList<Point> actionPoints = new ArrayList<>();
        ArrayList<Color> actionOldColors = new ArrayList<>();

        pointsToCheck.add(new Point((int)point.getX(), (int)point.getY()));

        Layer activeLayer = GUI.getInstance().getActiveImage().getActiveLayer();
        int tolerance = GUI.getInstance().getFillTolerance();

        int count = 0;

        while (!pointsToCheck.isEmpty()){
            Point nextPoint = pointsToCheck.remove();

            if(checkedPoints.contains(nextPoint))
                continue;
            
            checkedPoints.add(nextPoint);

            if(getTolerance(targetColor, pixels[nextPoint.x][nextPoint.y]) > tolerance)
                continue;

            count ++;

            actionOldColors.add(activeLayer.getPixel(nextPoint.x, nextPoint.y));
            actionPoints.add(new Point(nextPoint.x, nextPoint.y));

            activeLayer.setPixel(nextPoint.x, nextPoint.y, newColor);

            c.recalculatePixel(nextPoint.x, nextPoint.y);

            if(nextPoint.x - 1 >= 0)
                pointsToCheck.add(new Point(nextPoint.x - 1, nextPoint.y));
            if(nextPoint.y - 1 >= 0)
                pointsToCheck.add(new Point(nextPoint.x , nextPoint.y - 1));

            if(nextPoint.x + 1 < pixels.length)
                pointsToCheck.add(new Point(nextPoint.x + 1, nextPoint.y));
            if(nextPoint.y + 1 < pixels[0].length)
                pointsToCheck.add(new Point(nextPoint.x, nextPoint.y + 1));
        
            if(count > 10000) break;
        }

        PixelsChangedAction fillAction = new PixelsChangedAction(activeLayer, actionPoints, actionOldColors, newColor);
        GUI.getInstance().getActiveImage().addAction(fillAction);

        c.repaint();
    }

    public double getTolerance(Color c1, Color c2) {
        // Calculate Squared Diff
        double rDiff = c1.getRed() - c2.getRed();
        double gDiff = c1.getGreen() - c2.getGreen();
        double bDiff = c1.getBlue() - c2.getBlue();
        
        // Find the distance in 3D space
        double distance = Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
        

        // Convert into scale 0, 100;
        double maxDistance = Math.sqrt(255 * 255 * 3); // 441.67
        double tolerance = (distance / maxDistance) * 100;
        
        return Math.max(0, Math.min(100, tolerance)); // Ensure it's within [0, 100]
    }

    @Override
    public void deactivate(PCanvas c) {
    }

    @Override
    public void mousePressed(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {
    }

    @Override
    public void mouseDragged(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseMoved(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseExited(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseReleased(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseEntered(PCanvas c, MouseEvent e) {
    }

    @Override
    public void keyTyped(PCanvas c, KeyEvent e) {
    }

    @Override
    public void keyPressed(PCanvas c, KeyEvent e) {
    }

    @Override
    public void keyReleased(PCanvas c, KeyEvent e) {
    }
    
}
