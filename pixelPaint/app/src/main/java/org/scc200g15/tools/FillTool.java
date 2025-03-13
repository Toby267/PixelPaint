package org.scc200g15.tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.canvas.PCanvas;

public class FillTool implements Tool{

    Color targetColor;
    Color newColor = Color.BLACK;

    Queue<Point> pointsToCheck;
    ArrayList<Point> checkedPoints;

    @Override
    public void mouseClicked(PCanvas c, MouseEvent e) {
        Point2D point = c.getPixelPoint(e.getPoint());
        Color[][] pixels = GUI.getInstance().getActiveImage().compressImage();

        targetColor = pixels[(int)point.getX()][(int)point.getY()];

        pointsToCheck = new LinkedList<>();
        checkedPoints = new ArrayList<>();

        pointsToCheck.add(new Point((int)point.getX(), (int)point.getY()));

        while (!pointsToCheck.isEmpty()){
            Point nextPoint = pointsToCheck.remove();

            if(checkedPoints.contains(nextPoint))
                continue;
            
            checkedPoints.add(nextPoint);

            //TODO: Check if in tolerance
            if(!targetColor.equals(pixels[nextPoint.x][nextPoint.y]))
                continue;
            
            GUI.getInstance().getActiveImage().getActiveLayer().setPixel(nextPoint.x, nextPoint.y, newColor);

            c.recalculatePixel(nextPoint.x, nextPoint.y);

            if(nextPoint.x - 1 >= 0)
                pointsToCheck.add(new Point(nextPoint.x - 1, nextPoint.y));
            if(nextPoint.y - 1 >= 0)
                pointsToCheck.add(new Point(nextPoint.x , nextPoint.y - 1));

            if(nextPoint.x + 1 < pixels.length)
                pointsToCheck.add(new Point(nextPoint.x + 1, nextPoint.y));
            if(nextPoint.y + 1 < pixels[0].length)
                pointsToCheck.add(new Point(nextPoint.x, nextPoint.y + 1));
        }
        c.repaint();
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
    
}
