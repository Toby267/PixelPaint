package org.scc200g15.tools.squareSelect;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.tools.squareSelect.SquareSelectTool.Side;

public class Resizing implements SelectState{
  /**
   * event for resizing the selected area when the mouse is dragged 
   */    
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e, SquareSelectTool context) {
      Side selectedSide = context.getSelectedSide();
      Point2D resizePoint = context.getResizePoint();
  
      if (selectedSide == Side.RIGHT || selectedSide == Side.LEFT)
      resizePoint.setLocation(c.getPixelPoint(e.getPoint()).getX(), resizePoint.getY());
      else if (selectedSide == Side.TOP || selectedSide == Side.BOTTOM)
      resizePoint.setLocation(resizePoint.getX(), c.getPixelPoint(e.getPoint()).getY());
  
      context.paint(c);
  }

  /**
   * event for switching back to selecting after resizing is complete
   */
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e, SquareSelectTool context) {
      context.setState(new Selecting());
  }
  
  //unused action listeners
  @Override
  public void keyPressed(PCanvas c, KeyEvent e, SquareSelectTool context) {}
  @Override
  public void mousePressed(PCanvas c, MouseEvent e, SquareSelectTool context) {}
}
