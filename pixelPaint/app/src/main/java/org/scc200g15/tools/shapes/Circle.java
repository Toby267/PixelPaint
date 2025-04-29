package org.scc200g15.tools.shapes;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * used to represents a circle for drawing on the canvas
 */
public class Circle implements Shape {
    /**
     * returns the pixels for a circle of size diameter around center
     * 
     * TODO: implement Bresenham’s Circle Algorithm to find the outline, then scan each line filling them in - just more efficient
     */
    @Override
    public ArrayList<Point2D> returnPixels(Point2D center, int diameter)
    {
        ArrayList<Point2D> points = new ArrayList<>();

        int radius = diameter/2;

        int x = (int)center.getX();
        int y = (int)center.getY();
        
        int startX = x - radius;
        int startY = y - radius;

        // loops through each point in the surrounding box, checking whether they are in the circle
        for (int i = 0; i < diameter; i++) {
            for (int j = 0; j < diameter; j++) {
                double dist = Math.pow((x - (startX+i)), 2) + Math.pow((y - (startY+j)), 2);
                if (dist <= (Math.pow(radius, 2))) {
                    points.add(new Point(startX+i, startY+j));
                }
            }
        }

        return points;
    }
}