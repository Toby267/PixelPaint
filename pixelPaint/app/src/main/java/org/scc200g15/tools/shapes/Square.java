package org.scc200g15.tools.shapes;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * used to represents a circle for drawing on the canvas
 */
public class Square implements Shape{
    /**
     * returns the pixels for a square of width width around center either filled or non filled
     */
    @Override
    public ArrayList<Point2D> returnPixels(Point2D center, int width, boolean fill) {
        if (fill)   return getPixelsFill(center, width);
        else        return getPixelsNonFill(center, width);
    }

    private ArrayList<Point2D> getPixelsFill(Point2D center, int width) {
        ArrayList<Point2D> points = new ArrayList<>();

        int radius = width/2;
        int x = (int) center.getX() - radius;
        int y = (int) center.getY() - radius;
        
        // loops through each point in the surrounding box, checking whether they are in the square
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                points.add(new Point(x+i, y+j));
            }
        }

        return points;
    }

    private ArrayList<Point2D> getPixelsNonFill(Point2D center, int width) {
        ArrayList<Point2D> points = new ArrayList<>();

        int radius = width/2;
        int x = (int) center.getX() - radius;
        int y = (int) center.getY() - radius;
        
        // loops through each point in the surrounding box, checking whether they are in the square
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (i != 0 && i != width-1 && j != 0 && j != width-1) continue;
                
                points.add(new Point(x+i, y+j));
            }
        }

        return points;
    }
}
