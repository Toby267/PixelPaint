package org.scc200g15.tools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Layer;

/**
 * Tool for drawing pixels on the canvas
 */
public class DrawTool implements Tool {

  Color colour = Color.WHITE;

  public void setColour(int r, int g, int b) {
    colour = new Color(r, g, b, colour.getAlpha());
  }

  public void setOppacity(int alpha) {
    colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), alpha);
  }

  private void draw(PCanvas c, MouseEvent e) {
    Point2D point = c.getPixelPoint(e.getPoint());     
    Layer activeLayer = c.getActiveImage().getActiveLayer();
    int x = (int) point.getX();
    int y = (int) point.getY();
    activeLayer.setPixel(x, y, colour);
    c.repaint();
  }
  
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    draw(c, e);
  }

  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    draw(c, e);
  }

  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {
    Point2D hoverPoint = c.getPixelPoint(e.getPoint());
    c.setHoverPixel(hoverPoint);
    c.setHoverColour(colour);
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