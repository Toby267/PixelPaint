package org.scc200g15.tools.squareSelect;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.scc200g15.gui.canvas.PCanvas;

/**
 * moving state for when the selected area is being moved
 */
public class Moving implements SelectState{
  /**
   * updates the move end point with the new point from the drag
   */
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e, SquareSelectTool context) {
    context.setMoveEndPoint(c.getPixelPoint(e.getPoint()));
  }
  
  /**
   * moves the selected area, then deselects
   */
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e, SquareSelectTool context) {
    context.cacheAndDelete(c);
    context.printCached(c);
    context.deselect(c);

    context.setMoveStartPoint(null);
    context.setMoveEndPoint(null);
  }

  //unused action listeners
  @Override
  public void keyPressed(PCanvas c, KeyEvent e, SquareSelectTool context) {}
  @Override
  public void mousePressed(PCanvas c, MouseEvent e, SquareSelectTool context) {}
}
