package org.scc200g15.tools.shapes;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * used to represents a circle for drawing on the canvas
 */
public class Circle implements Shape {
    /**
     * returns the pixels for a circle of width width around center either filled or non filled
     */
    @Override
    public ArrayList<Point2D> returnPixels(Point2D center, int diameter, boolean fill) {
        return fill ? getPixelsFill(center, diameter) : getPixelsNonFill(center, diameter);
    }

    private ArrayList<Point2D> getPixelsFill(Point2D center, int diameter) {
        ArrayList<Point2D> points = new ArrayList<>();
        points.add(center);

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

    private ArrayList<Point2D> getPixelsNonFill(Point2D center, int diameter) {
        ArrayList<Point2D> points = new ArrayList<>();
        if (diameter <= 1) points.add(center);

        int radius = diameter/2;

        int x = (int)center.getX();
        int y = (int)center.getY();
        
        int startX = x - radius;
        int startY = y - radius;

        // loops through each point in the surrounding box, checking whether they are in the circle
        for (int i = 0; i < diameter; i++) {
            for (int j = 0; j < diameter; j++) {
                double dist = Math.pow((x - (startX+i)), 2) + Math.pow((y - (startY+j)), 2);
                if (Math.abs(dist - (Math.pow(radius, 2))) < radius) {
                    points.add(new Point(startX+i, startY+j));
                }
            }
        }

        return points;
    }
}