package org.scc200g15.tools.squareSelect;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;

/**
 * idle state for when the select tool is not doing anything
 */
public class Idle implements SelectState {
  /**
   * starts selecting an area
   */
  private void select(PCanvas c, MouseEvent e, SquareSelectTool context) {
    Point2D p = c.getPixelPoint(e.getPoint());
    if (c.isOutOfBounds(p)) return;
    
    context.setState(new Selecting());
    
    context.setStartPoint(p);
    context.setEndPoint(p);
    
    context.paint(c);
  }
  
  //event for starting to select an area
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e, SquareSelectTool context) {
    select(c, e, context);
  }
  
  //event for starting to select an area
  @Override
  public void mousePressed(PCanvas c, MouseEvent e, SquareSelectTool context) {
    select(c, e, context);
  }
  
  //unused action listeners
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e, SquareSelectTool context) {}
  @Override
  public void keyPressed(PCanvas c, KeyEvent e, SquareSelectTool context) {}
}
