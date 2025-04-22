package org.scc200g15.tools.shapes;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Interface all shapes must conform to; passes through any tool that requries a shape
 * 
 * probably need to make this an abstract class and store the initial matrix in here then 
 */
public interface Shape {
    public ArrayList<Point2D> returnPixels(Point2D center, int size);
}
