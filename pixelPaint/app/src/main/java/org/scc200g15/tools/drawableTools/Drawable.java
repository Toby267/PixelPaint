package org.scc200g15.tools.drawableTools;

import java.awt.event.MouseEvent;
import java.util.HashMap;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.tools.shapes.Circle;
import org.scc200g15.tools.shapes.Shape;
import org.scc200g15.tools.shapes.Square;
import org.scc200g15.tools.shapes.Star;
import org.scc200g15.tools.shapes.Triangle;

public abstract class Drawable {
  // map of all shapes
  protected HashMap<String, Shape> shapes = new HashMap<>();
  protected Shape activeShape;
  
  protected int size = 1;

  /**
   * Constructor that sets up all the shapes, and the active shape
   */
  public Drawable() {
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
   * draws on the canvas
   * 
   * @param c the canvas to draw on
   * @param e the mouse event that caused the interupt
   */
  protected abstract void draw(PCanvas c, MouseEvent e);
}
