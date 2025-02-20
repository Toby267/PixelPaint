package org.scc200g15.tools.shapes;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Interface all shapes must conform to; passes through any tool that requries a shape
 */
public interface Shape {
    public ArrayList<Point2D> returnPixels(Point2D center, int size);
}
