package org.scc200g15.tools.squareSelect;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

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
    Point2D p = c.getPixelPoint(e.getPoint());
    if (c.isOutOfBounds(p)) return;
    
    context.setMoveEndPoint(p);
    Point2D transform = context.calcMoveTransform();
    Point2D trueStart = context.calcTrueStart();

    int x = (int)trueStart.getX() + (int)transform.getX();
    int y = (int)trueStart.getY() + (int)transform.getY();

    int width = context.calcWidth();
    int height = context.calcHeight();

    if((int)transform.getX() != 0 || (int)transform.getY() != 0) {
      c.setMovePixel(new Point(x, y));
      c.setMoveDimensions(width, height);
    }else{
      c.setMoveDimensions(0, 0);
    }

    c.repaint();
  }
  
  /**
   * moves the selected area, then deselects
   */
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e, SquareSelectTool context) {
    context.move(c);

    // Remove gray dragging template
    c.setMoveDimensions(0, 0);
    c.repaint();
  }

  //unused action listeners
  @Override
  public void keyPressed(PCanvas c, KeyEvent e, SquareSelectTool context) {}
  @Override
  public void mousePressed(PCanvas c, MouseEvent e, SquareSelectTool context) {}

  @Override
  public void mouseMoved(PCanvas c, MouseEvent e, SquareSelectTool context) {}
}
