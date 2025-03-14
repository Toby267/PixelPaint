package org.scc200g15.tools;

import java.awt.event.KeyEvent;
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
    if(c.getActiveImage() == null) return;

    Point2D hoverPoint = c.getPixelPoint(e.getPoint());

    c.setHoverPixel(hoverPoint);
    c.repaint();
  }

  // Not Used
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {}
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {}
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
  @Override
  public void keyTyped(PCanvas c, KeyEvent e) {}
  @Override
  public void keyPressed(PCanvas c, KeyEvent e) {}
  @Override
  public void keyReleased(PCanvas c, KeyEvent e) {}
}
