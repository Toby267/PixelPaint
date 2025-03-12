package org.scc200g15.tools.squareSelect;

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
        context.getState().mousePressed(c, e, context);
      }
      else if (context.contains(p)) {
        context.setState(new Moving());
        context.getState().mousePressed(c, e, context);
      }
      else {
        context.deselect(c);
      }
    }

    @Override
    public void mouseReleased(PCanvas c, MouseEvent e, SquareSelectTool context) {
      context.setState(new Selecting());
    }
}
