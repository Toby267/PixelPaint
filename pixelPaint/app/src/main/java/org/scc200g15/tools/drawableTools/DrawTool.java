package org.scc200g15.tools.drawableTools;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.Tool;

/**
 * Tool for drawing pixels on the canvas
 */
public class DrawTool extends Drawable implements Tool {
  /**
   * draws the current shape with the current colour on the canvas
   * 
   * @param c the canvas to draw on
   * @param e the mouse event that caused the interupt
   */
  @Override
  protected void draw(PCanvas c, MouseEvent e) {
    Point2D point = c.getPixelPoint(e.getPoint());
    Color colour = GUI.getInstance().getSideBar().getActiveColor();

    Image image = c.getActiveImage();
    Layer activeLayer = image.getActiveLayer();

    ArrayList<Point2D> points = activeShape.returnPixels(point, size);
    
    for (Point2D p : points) {
      if (c.isOutOfBounds(p)) continue;

      activeLayer.setPixel((int)p.getX(), (int)p.getY(), colour);
      c.recalculatePixel((int)p.getX(), (int)p.getY());
    }

    c.repaint();
  }
  
  // draws when the mouse is dragged
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    draw(c, e);
  }

  // draws when tthe mouse is clicked
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    draw(c, e);
  }

  
  // Required interface methods that we don't need
  @Override
  public void mouseMoved(PCanvas c, MouseEvent e) {}
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