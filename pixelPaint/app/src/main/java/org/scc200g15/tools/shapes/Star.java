package org.scc200g15.tools.shapes;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * used to represents a star for drawing on the canvas
 */
public class Star implements Shape {
  final int POINTS = 5;
  final double INNER_RADIUS_RATIO = 0.5;
  
  /**
   * returns the pixels for a star of width width around center either filled or non filled
   */
  @Override
  public ArrayList<Point2D> returnPixels(Point2D center, int width, boolean fill) {
    return fill ? getPixelsFill(center, width) : getPixelsNonFill(center, width);
  }

  private ArrayList<Point2D> getPixelsFill(Point2D center, int width) {
    // Calculate points of the star
    ArrayList<Point2D> points = calculateStarPoints((int)center.getX(), (int)center.getY(), width/2);
    points.add(center);
    
    // Draw the star by filling the polygon
    fillPolygon(points);

    return points;
  }

  private ArrayList<Point2D> getPixelsNonFill(Point2D center, int width) {
    ArrayList<Point2D> points = new ArrayList<>();
    if (width <= 1){
      points.add(center);
      return points;
    }
    

    // Calculate the star's outline points
    ArrayList<Point2D> starPoints = calculateStarPoints((int) center.getX(), (int) center.getY(), width / 2);

    // Connect each point to the next to form the star's edges
    for (int i = 0; i < starPoints.size(); i++) {
        Point2D start = starPoints.get(i);
        Point2D end = starPoints.get((i + 1) % starPoints.size()); // Wrap around to the first point
        points.addAll(getLinePoints(start, end));
    }

    return points;
  }

  private ArrayList<Point2D> getLinePoints(Point2D start, Point2D end) {
    ArrayList<Point2D> linePoints = new ArrayList<>();

    int x0 = (int) start.getX();
    int y0 = (int) start.getY();
    int x1 = (int) end.getX();
    int y1 = (int) end.getY();

    int dx = Math.abs(x1 - x0);
    int dy = Math.abs(y1 - y0);

    int sx = x0 < x1 ? 1 : -1;
    int sy = y0 < y1 ? 1 : -1;

    int err = dx - dy;

    while (true) {
        linePoints.add(new Point(x0, y0));
        if (x0 == x1 && y0 == y1) break;
        int e2 = 2 * err;
        if (e2 > -dy) {
            err -= dy;
            x0 += sx;
        }
        if (e2 < dx) {
            err += dx;
            y0 += sy;
        }
    }

    return linePoints;
}  
  private ArrayList<Point2D> calculateStarPoints(int centerX, int centerY, int outerRadius) {
    ArrayList<Point2D> points = new ArrayList<>();
    int innerRadius = (int)(outerRadius * INNER_RADIUS_RATIO);
    
    for (int i = 0; i < POINTS * 2; i++) {
      // Alternate between outer and inner points
      int radius = (i % 2 == 0) ? outerRadius : innerRadius;
      
      // Calculate angle (starting from top, going clockwise)
      double angle = Math.PI / 2 + i * Math.PI / POINTS;
      
      int x = centerX + (int)(radius * Math.cos(angle));
      int y = centerY - (int)(radius * Math.sin(angle));
      
      points.add(new Point2D.Double(x, y));
    }
    
    return points;
  }
  
  private void fillPolygon(ArrayList<Point2D> points) {
    ArrayList<Point2D> innerPoints = new ArrayList<>();
    
    // Find bounding box
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    
    for (Point2D p : points) {
      minX = Math.min(minX, (int)p.getX());
      minY = Math.min(minY, (int)p.getY());
      maxX = Math.max(maxX, (int)p.getX());
      maxY = Math.max(maxY, (int)p.getY());
    }
    
    // Check each pixel in the bounding box
    for (int x = minX; x <= maxX; x++) {
      for (int y = minY; y <= maxY; y++) {
        if (isPointInPolygon(x, y, points)) {
            innerPoints.add(new Point(x, y));
        }
      }
    }

    points.addAll(innerPoints);
  }
  
  private boolean isPointInPolygon(int x, int y, ArrayList<Point2D> polygon) {
    boolean inside = false;
    int j = polygon.size() - 1;
    
    for (int i = 0; i < polygon.size(); i++) {
      Point2D pi = polygon.get(i);
      Point2D pj = polygon.get(j);
      
      if ((pi.getY() > y) != (pj.getY() > y) &&
          (x < (pj.getX() - pi.getX()) * (y - pi.getY()) / (pj.getY() - pi.getY()) + pi.getX())) {
        inside = !inside;
      }
      
      j = i;
    }
    
    return inside;
  }
}
