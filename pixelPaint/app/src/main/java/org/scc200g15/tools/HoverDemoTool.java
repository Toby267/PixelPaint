package org.scc200g15.tools;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;

/**
 * Basic tool to demo how you can get a pixel pos from the mouse pos
 */
public class HoverDemoTool implements Tool {

  /**
   * Sets the hover pixel based on the result from the inverse transform
   */
  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {
    Point2D hoverPoint = c.getPixelPoint(e.getPoint());

    c.setHoverPixel(hoverPoint);
    c.repaint();
  }

  // Not Used
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
  }

  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
  }

  @Override
  public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {
  }

  @Override
  public void mouseClicked(PCanvas c, MouseEvent e) {
  }

  @Override
  public void mouseExited(PCanvas c, MouseEvent e) {
  }

  @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {
  }

  @Override
  public void mouseEntered(PCanvas c, MouseEvent e) {
  }

}
