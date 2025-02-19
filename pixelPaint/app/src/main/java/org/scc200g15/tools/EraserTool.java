package org.scc200g15.tools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Layer;

/**
 * Tool for erasing pixels on the canvas by setting them to transparent
 */
public class EraserTool implements Tool {

  private void erase(PCanvas c, MouseEvent e) {
    Point2D point = c.getPixelPoint(e.getPoint());     
    Layer activeLayer = c.getActiveImage().getActiveLayer();
    int x = (int) point.getX();
    int y = (int) point.getY();
    // Create a transparent color (alpha = 0)
    activeLayer.setPixel(x, y, new Color(0, 0, 0, 0));
    c.repaint();
  }
  
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    erase(c, e);
  }

  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    erase(c, e);
  }

  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {
    Point2D hoverPoint = c.getPixelPoint(e.getPoint());
    c.setHoverPixel(hoverPoint);
    c.setHoverColour(new Color(0, 0, 0, 0));
    c.repaint();
  }

  // Required interface methods that we don't need
  @Override
  public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {}
  @Override
  public void mouseClicked(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseExited(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseEntered(PCanvas c, MouseEvent e) {}
}