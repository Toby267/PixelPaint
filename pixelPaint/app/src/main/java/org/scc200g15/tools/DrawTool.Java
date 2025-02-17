package org.scc200g15.tools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Layer;

/**
 * Basic tool to demo how basic draw functionality will work.
 * Now updated so that clicking or dragging draws a black pixel on the canvas.
 */
public class DrawTool implements Tool {

  /**
   * Sets the hover pixel based on the result from the inverse transform.
   */
  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {
    Point2D hoverPoint = c.getPixelPoint(e.getPoint());
    c.setHoverPixel(hoverPoint);
    c.repaint();
  }

  /**
   * Draws a pixel on the active layer when the mouse is dragged.
   */
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    Point2D point = c.getPixelPoint(e.getPoint());
    Layer activeLayer = c.getActiveImage().getActiveLayer();
    activeLayer.setPixel(point, Color.black);  // Draw a black pixel
    c.repaint();
  }

  /**
   * Draws a pixel on the active layer when the mouse is pressed.
   */
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    Point2D point = c.getPixelPoint(e.getPoint());
    Layer activeLayer = c.getActiveImage().getActiveLayer();
    activeLayer.setPixel(point, Color.black);  // Draw a black pixel
    c.repaint();
  }

  @Override
  public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {
    // Not implemented for drawing
  }

  @Override
  public void mouseClicked(PCanvas c, MouseEvent e) {
    // Not used in this implementation
  }

  @Override
  public void mouseExited(PCanvas c, MouseEvent e) {
    // Clear the hover pixel when the mouse exits the canvas.
    c.setHoverPixel(null);
    c.repaint();
  }

  @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {
    // Not used in this implementation
  }

  @Override
  public void mouseEntered(PCanvas c, MouseEvent e) {
    // Not used in this implementation
  }
}
