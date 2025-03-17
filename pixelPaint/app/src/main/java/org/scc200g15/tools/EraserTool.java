package org.scc200g15.tools;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.tools.shapes.Shape;

/**
 * Tool for erasing pixels on the canvas by setting them to transparent
 * Essentially a wrapper class for the DrawTool, only using what is needed from it
 */
public class EraserTool implements Tool {

  private DrawTool drawTool;

  public EraserTool() {
    drawTool = new DrawTool();
    drawTool.setColour(0, 0, 0);
    drawTool.setOppacity(0);
    drawTool.setSize(9);
    drawTool.setShape("Circle");
  }
  
  /**
   * sets the shape to the shape specified by ID
   * 
   * @param ID The ID of the shape
   */
  public void setShape(String ID) {
    drawTool.setShape(ID);
  }

  /**
   * gets the shape of the eraser tool
   */
  public Shape getShape() {
    return drawTool.getShape();
  }

  /**
   * sets the size of the eraser tool to the one specified by size
   * 
   * @param size the new size value
   */
  public void setSize(int size) {
    drawTool.setSize(size);
  }

  /**
   * gets the size of the eraser tool
   */
  public int getSize() {
    return drawTool.getSize();
  }

  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    drawTool.mouseDragged(c, e);
  }

  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    drawTool.mousePressed(c, e);
  }

  // Required interface methods that we don't need
  @Override
  public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {}
  @Override
  public void mouseClicked(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseExited(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseEntered(PCanvas c, MouseEvent e) {}
}