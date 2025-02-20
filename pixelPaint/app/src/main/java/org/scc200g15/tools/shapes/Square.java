package org.scc200g15.tools.shapes;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * used to represents a circle for drawing on the canvas
 */
public class Square implements Shape{
    /**
     * returns the pixels for a square of width width around center
     */
    @Override
    public ArrayList<Point2D> returnPixels(Point2D center, int width)
    {
        ArrayList<Point2D> points = new ArrayList<Point2D>() {{
            add(center);
        }};

        int radius = width/2;
        int x = (int) center.getX() - radius;
        int y = (int) center.getY() - radius;
        
        //adds the points of each quadrant of the circle
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                points.add(new Point(x+i, y+j));
            }
        }

        return points;
    }
}
