package org.scc200g15.tools;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;

public class SquareSelectTool implements Tool {
    Point2D startPoint;
    int width, height;

  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    startPoint = c.getPixelPoint(e.getPoint());
  }
  
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {
    Point2D point = c.getPixelPoint(e.getPoint());
    width = (int)point.getX() - (int)startPoint.getX();
    height = (int)point.getY() - (int)startPoint.getY();

    c.setHoverPixel(startPoint);
    c.setHoverDimensions(width, height);
    
    c.repaint();
  }
  
  // Required interface methods that we don't need
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {}
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
}
