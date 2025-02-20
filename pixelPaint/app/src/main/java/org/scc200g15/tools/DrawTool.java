package org.scc200g15.tools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.shapes.Circle;
import org.scc200g15.tools.shapes.Shape;
import org.scc200g15.tools.shapes.Square;
import org.scc200g15.tools.shapes.Star;
import org.scc200g15.tools.shapes.Triangle;

/**
 * Tool for drawing pixels on the canvas
 */
public class DrawTool implements Tool {
  
  // map of all shapes
  private HashMap<String, Shape> shapes = new HashMap<>();

  private Shape activeShape;
  private Color colour = Color.WHITE;
  private int size = 5;

  /**
   * Constructor that sets up all the shapes, and the active shape
   */
  public DrawTool() {
    shapes.put("Circle", new Circle());
    shapes.put("Square", new Square());
    shapes.put("Triangle", new Triangle());
    shapes.put("Star", new Star());

    activeShape = shapes.get("Circle");
  }

  /**
   * sets the shape to the shape specified by ID
   * 
   * @param ID The ID of the shape
   */
  public void setShape(String ID) {
    try {
      activeShape = shapes.get(ID);
    }
    catch (Exception e) {
      throw new Error("No shape exists with ID: " + ID);
    }
  }

  /**
   * sets the size of the draw tool to the one specified by size
   * 
   * @param size the new size value
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * sets the colour of the draw tool to the one specified by the rgb values
   * 
   * @param r the new red value of the colour
   * @param g the new red value of the colour
   * @param b the new red value of the colour
   */
  public void setColour(int r, int g, int b) {
    colour = new Color(r, g, b, colour.getAlpha());
  }

  /**
   * sets the oppacity of the draw tool to the one specified by the alpha value
   * 
   * @param alpha the new oppacity value
   */
  public void setOppacity(int alpha) {
    colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), alpha);
  }

  /**
   * draws the current shape with the current colour on the canvas
   * 
   * @param c the canvas to draw on
   * @param e the mouse event that caused the interupt
   */
  private void draw(PCanvas c, MouseEvent e) {
    Point2D point = c.getPixelPoint(e.getPoint());     
    Layer activeLayer = c.getActiveImage().getActiveLayer();

    ArrayList<Point2D> points = activeShape.returnPixels(point, size);
    
    for (int i = 0; i < points.size(); i++) {
      int x = (int) points.get(i).getX();
      int y = (int) points.get(i).getY();

      activeLayer.setPixel(x, y, colour);
    }

    c.repaint();
  }
  
  // draws when the mouse is dragged
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    draw(c, e);
  }

  // draws when tthe mouse is clicked
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    draw(c, e);
  }

  // TODO: make it so it hovers all points in the shape
  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {
    Point2D hoverPoint = c.getPixelPoint(e.getPoint());
    c.setHoverPixel(hoverPoint);
    c.setHoverColour(colour);
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
  public void mouseReleased(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseEntered(PCanvas c, MouseEvent e) {}
}