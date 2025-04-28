package org.scc200g15.tools.drawableTools;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import org.scc200g15.action.PixelsChangedAction;
import org.scc200g15.gui.GUI;
import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.Tool;
import org.scc200g15.tools.shapes.Circle;
import org.scc200g15.tools.shapes.Shape;
import org.scc200g15.tools.shapes.Square;
import org.scc200g15.tools.shapes.Star;
import org.scc200g15.tools.shapes.Triangle;

public abstract class Drawable implements Tool {

  ArrayList<Point> actionPoints;
  ArrayList<Color> actionOldColors;

  // map of all shapes
  protected HashMap<String, Shape> shapes = new HashMap<>();
  protected Shape activeShape;
  
  protected int size = 1;
  protected boolean fill = true;

  /**
   * Constructor that sets up all the shapes, and the active shape
   */
  public Drawable() {
    shapes.put("Circle", new Circle());
    shapes.put("Square", new Square());
    shapes.put("Triangle", new Triangle());
    shapes.put("Star", new Star());

    activeShape = shapes.get("Circle");

    actionPoints = new ArrayList<>();
    actionOldColors = new ArrayList<>();
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
   * gets the shape of the draw tool
   */
  public Shape getShape() {
    return activeShape;
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
   * gets the size of the draw tool
   */
  public int getSize() {
    return size;
  }

  /**
   * sets the fill of the draw tool to the one specified by fill
   * 
   * @param fill the new fill value
   */
  public void setFill(boolean fill) {
    this.fill = fill;
  }

  /**
   * gets the fill of the draw tool
   */
  public boolean getFill() {
    return fill;
  }

  /**
   * toggles the fill value of the draw tool
   */
  public void toggleFill() {
    fill = !fill;
  }
  
  // draws when the mouse is dragged
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    draw(c, e);
  }

// draws when the mouse is clicked
@Override
public void mousePressed(PCanvas c, MouseEvent e) {
  actionPoints = new ArrayList<>();
  actionOldColors = new ArrayList<>();

  draw(c, e);
}

    @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {
    Color color = getColor();
    Image image = c.getActiveImage();
    Layer activeLayer = image.getActiveLayer();

    PixelsChangedAction drawAction = new PixelsChangedAction(activeLayer, actionPoints, actionOldColors, color);

    GUI.getInstance().getActiveImage().addAction(drawAction);
  }

  /**
   * draws on the canvas
   * 
   * @param c the canvas to draw on
   * @param e the mouse event that caused the interrupt
   */
  public void draw(PCanvas c, MouseEvent e){
    Point2D point = c.getPixelPoint(e.getPoint());
    Color color = getColor();

    Image image = c.getActiveImage();
    Layer activeLayer = image.getActiveLayer();

    ArrayList<Point2D> points = activeShape.returnPixels(point, size, fill);
    
    for (Point2D p : points) {
      if (c.isOutOfBounds(p)) continue;
      if(actionPoints.contains(new Point((int)p.getX(), (int)p.getY()))) continue;

      actionOldColors.add(activeLayer.getPixel((int)p.getX(), (int)p.getY()));
      actionPoints.add(new Point((int)p.getX(), (int)p.getY()));

      activeLayer.setPixel((int)p.getX(), (int)p.getY(), color);
      c.recalculatePixel((int)p.getX(), (int)p.getY());
    }

    c.repaint();
  }
  protected abstract Color getColor();

  @Override
  public void deactivate(PCanvas c) {}
  
  // Required interface methods that we don't need
  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {}
  @Override
  public void mouseClicked(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseExited(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseEntered(PCanvas c, MouseEvent e) {}
  @Override
  public void keyTyped(PCanvas c, KeyEvent e) {}
  @Override
  public void keyPressed(PCanvas c, KeyEvent e) {}
  @Override
  public void keyReleased(PCanvas c, KeyEvent e) {}
}
