package org.scc200g15.tools.shapes;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Interface all shapes must conform to; passes through any tool that requries a shape
 * 
 * TODO: change the shapes so that they store a large matrix of its image (1080 by 1080) then apply matrix transforms for the position and size
 * probably need to make this an abstract class and store the initial matrix in here then 
 */
public interface Shape {
    public ArrayList<Point2D> returnPixels(Point2D center, int size);
}
