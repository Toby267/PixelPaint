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
     */
    @Override
    public ArrayList<Point2D> returnPixels(Point2D center, int diameter)
    {
        ArrayList<Point2D> points = new ArrayList<Point2D>() {{
            add(center);
        }};

        int radius = (diameter-1)/2;
        int x = (int)center.getX();
        int y = (int)center.getY();

        // adds the points for the center row & column
        // otherwise they will be repeated when the rest of the points are computed
        for (int i = 1; i <= radius; i++) {
            points.add(new Point(x+i, y));
            points.add(new Point(x-i, y));
            points.add(new Point(x, y+i));
            points.add(new Point(x, y-i));
        }
        
        //adds the points of each quadrant of the circle
        for (int j = 1; j <= radius; j++) {
            for (int i = 1; i <= radius-j; i++) {
                points.add(new Point(x+i, y+j));
                points.add(new Point(x+i, y-j));
                points.add(new Point(x-i, y+j));
                points.add(new Point(x-i, y-j));
            }
        }

        return points;
    }
}