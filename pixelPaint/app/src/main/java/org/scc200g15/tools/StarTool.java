package org.scc200g15.tools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Layer;

/**
 * Tool for drawing star shapes on the canvas
 */
public class StarTool implements Tool {

  private Color colour = Color.WHITE;
  private Point2D startPoint = null;
  private int points = 5; // Default number of star points
  private double innerRadiusRatio = 0.5; // Ratio of inner radius to outer radius
  
  public void setColour(int r, int g, int b) {
    colour = new Color(r, g, b, colour.getAlpha());
  }

  public void setOpacity(int alpha) {
    colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), alpha);
  }
  
  public void setPoints(int points) {
    if (points >= 3) {
      this.points = points;
    }
  }
  
  public void setInnerRadiusRatio(double ratio) {
    if (ratio > 0 && ratio < 1) {
      this.innerRadiusRatio = ratio;
    }
  }

  private void drawStar(PCanvas c, int centerX, int centerY, int radius) {
    Layer activeLayer = c.getActiveImage().getActiveLayer();
    
    // Calculate points of the star
    ArrayList<Point2D> starPoints = calculateStarPoints(centerX, centerY, radius);
    
    // Draw the star by filling the polygon
    fillPolygon(activeLayer, starPoints);
    
    // Redraw the affected area
    c.recalculateArea(centerX - radius, centerY - radius, radius * 2, radius * 2);
    c.repaint();
  }
  
  private ArrayList<Point2D> calculateStarPoints(int centerX, int centerY, int outerRadius) {
    ArrayList<Point2D> points = new ArrayList<>();
    int innerRadius = (int)(outerRadius * innerRadiusRatio);
    
    for (int i = 0; i < this.points * 2; i++) {
      // Alternate between outer and inner points
      int radius = (i % 2 == 0) ? outerRadius : innerRadius;
      
      // Calculate angle (starting from top, going clockwise)
      double angle = Math.PI / 2 + i * Math.PI / this.points;
      
      int x = centerX + (int)(radius * Math.cos(angle));
      int y = centerY - (int)(radius * Math.sin(angle));
      
      points.add(new Point2D.Double(x, y));
    }
    
    return points;
  }
  
  private void fillPolygon(Layer layer, ArrayList<Point2D> points) {
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
          layer.setPixel(x, y, colour);
        }
      }
    }
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
  
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    startPoint = c.getPixelPoint(e.getPoint());
  }

  @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {
    if (startPoint != null) {
      Point2D endPoint = c.getPixelPoint(e.getPoint());
      
      int centerX = (int)startPoint.getX();
      int centerY = (int)startPoint.getY();
      
      // Calculate radius based on distance from start to end point
      int dx = (int)(endPoint.getX() - startPoint.getX());
      int dy = (int)(endPoint.getY() - startPoint.getY());
      int radius = (int)Math.sqrt(dx*dx + dy*dy);
      
      drawStar(c, centerX, centerY, radius);
      startPoint = null;
    }
  }

  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {
    Point2D hoverPoint = c.getPixelPoint(e.getPoint());
    c.setHoverPixel(hoverPoint);
    c.setHoverColour(colour);
    c.repaint();
  }

  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    // Update hover point while dragging
    Point2D currentPoint = c.getPixelPoint(e.getPoint());
    c.setHoverPixel(currentPoint);
    c.repaint();
  }

  // Required interface methods that we don't need
  @Override
  public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {}
  @Override
  public void mouseClicked(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseExited(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseEntered(PCanvas c, MouseEvent e) {}
} 