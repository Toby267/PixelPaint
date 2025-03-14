package org.scc200g15.tools.drawableTools;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.Tool;

/**
 * Tool for drawing pixels on the canvas
 */
public class DrawTool extends Drawable implements Tool {
  private Color colour = Color.RED;

  /**
   * sets the colour of the draw tool to the one specified by the rgb values
   * 
   * @param r the new red value of the colour
   * @param g the new red value of the colour
   * @param b the new red value of the colour
   */
  public void setColour(int r, int g, int b) {
    colour = new Color(r, g, b, colour.getAlpha());
  }

  /**
   * gets the colour of the draw tool
   */
  public Color getColour() {
    return colour;
  }

  /**
   * sets the oppacity of the draw tool to the one specified by the alpha value
   * 
   * @param alpha the new oppacity value
   */
  public void setOppacity(int alpha) {
    colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), alpha);
  }

  /**
   * gets the oppacity of the draw tool
   */
  public int getOppacity() {
    return colour.getAlpha();
  }

  /**
   * draws the current shape with the current colour on the canvas
   * 
   * @param c the canvas to draw on
   * @param e the mouse event that caused the interupt
   */
  @Override
  protected void draw(PCanvas c, MouseEvent e) {
    Point2D point = c.getPixelPoint(e.getPoint());
    Image image = c.getActiveImage();
    Layer activeLayer = image.getActiveLayer();
    int height = image.getHeight(), width = image.getHeight();

    ArrayList<Point2D> points = activeShape.returnPixels(point, size);
    
    for (Point2D p : points) {
      if (p.getX() < 0 || p.getX() >= width)  continue;
      if (p.getY() < 0 || p.getY() >= height) continue;

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