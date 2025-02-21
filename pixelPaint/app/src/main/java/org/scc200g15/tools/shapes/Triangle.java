package org.scc200g15.tools.shapes;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * used to represents a triangle for drawing on the canvas
 */
public class Triangle implements Shape {
    /**
     * returns the pixels for a square of width width around center
     */
    @Override
    public ArrayList<Point2D> returnPixels(Point2D center, int width) {
        ArrayList<Point2D> points = new ArrayList<>();

        float radius = width/2;

        float startX = (float)center.getX() - radius;
        float startY = (float)center.getY() - radius;

        float m = (float)Math.sqrt(3);
        float c = startY + width;
        
        // equation 1: the one slanting upwards
        // M: -sqrt(3)
        // P: (startX, straighlineIntercept)
        // y = -sqrt(3) (x - startX) + straighlineIntercept
        
        // equation 2: the one slanting downwards
        // M: sqrt(3)
        // P: (startX + width, straighlineIntercept)
        // y = sqrt(3) (x - startX - width) + straighlineIntercept

        // equation 3: the straight line
        // y = straighlineIntercept


        // loops through each point in the surrounding box, checking whether they are in the triangle
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                float x = startX + i;
                float y = startY + j;
                
                float equation1 = -m * (x - startX) + c;
                float equation2 = m * (x - startX - width) + c;

                if (y < equation1)
                    continue;
                if (y < equation2)
                    continue;
                if (y > c)
                    continue;
                
                points.add(new Point((int)x, (int)y));
            }
        }
        
        return points;
    }
}
