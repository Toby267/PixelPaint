package org.scc200g15.tools.squareSelect;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;

public class Selecting implements SelectState {
  /**
   * event for setting the new end point for the selected area, and showing the area on the canvas
   */
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e, SquareSelectTool context) {
    context.setEndPoint(c.getPixelPoint(e.getPoint()));

    context.paint(c);
  }
  
  /**
   * event for switching to other states from this state
   */
  @Override
  public void mousePressed(PCanvas c, MouseEvent e, SquareSelectTool context) {
    Point2D p = c.getPixelPoint(e.getPoint());

    if (context.getBorder(p) != null){
      context.setState(new Resizing());
      context.setSelectedSide(context.getBorder(p));
      context.setResizePoint(context.getBorderPoint(p));
    }
    else if (context.contains(p)) {
      context.setState(new Moving());
      //TODO: logic for switching to the moving state
    }
    else {
      context.deselect(c);
    }
  }
  
  /**
   * event for when a key is pressed, will either delete the selected area or deselect it
   */
  @Override
  public void keyPressed(PCanvas c, KeyEvent e, SquareSelectTool context) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_DELETE:
        context.deleteSelected(c);
        context.deselect(c);
      case KeyEvent.VK_ESCAPE:
        context.deselect(c);
      case KeyEvent.VK_ENTER:
        context.deselect(c);
    }
  }

  //unused action listeners
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e, SquareSelectTool context) {}
}
