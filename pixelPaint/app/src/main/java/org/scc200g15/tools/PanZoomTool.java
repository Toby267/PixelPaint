package org.scc200g15.tools;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.scc200g15.gui.canvas.PCanvas;

/**
 * The default tool for the app that allows the user to pan and zoom
 */
public class PanZoomTool implements Tool {

  Point offset = new Point(0, 0);

  /**
   * Track the dragging of the mouse and apply the needed transform for the pan
   */
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    c.setDif(new Point(e.getX() - offset.x, e.getY() - offset.y));
    c.repaint();
  }

  /**
   * Update the offset to be the current offset when a new drag is started
   */
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    Point dif = c.getDif();
    offset = new Point(e.getX() - dif.x, e.getY() - dif.y);
  }

  /**
   * Track mouse wheel events and call relevant zoomIn or ZoomOut functions
   */
  @Override
  public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {
    if (e.isControlDown()) {
      if (e.getWheelRotation() < 0) {
        c.zoomIn(e.getPoint());
      } else {
        c.zoomOut(e.getPoint());
      }
      c.repaint();
    }
  }

  // Not used
  @Override
  public void deactivate(PCanvas c) {}
  @Override
  public void mouseClicked(PCanvas c, MouseEvent e) {}
  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {}
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
