package org.scc200g15.tools;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;

public class SquareSelectTool implements Tool {
  Point2D startPoint;
  int width, height;
  boolean areaSelected = false;

  /**
   * deletes the selected pixels for the given canvas
   * 
   * @param c the canvas to delete the pixels in
   */
  private void deleteSelected(PCanvas c) {
    Image image = c.getActiveImage();
    Layer activeLayer = image.getActiveLayer();
    int maxHeight = image.getHeight(), maxWidth = image.getHeight();

    for (int i = 0; i < width; i++){
      for (int j = 0; j < height; j++){
        int x = (int)startPoint.getX() + i;
        int y = (int)startPoint.getY() + j;

        if (x < 0 || x >= maxWidth) continue;
        if (y < 0 || y >= maxHeight) continue;
        
        activeLayer.setPixel(x, y, new Color(0, 0, 0, 0));
        c.recalculatePixel(x, y);
      }
    }

    c.repaint();
  }

  /**
   * deselects the selected area
   * 
   * @param c the canvas that has the selected pixels
   */
  private void deselect(PCanvas c) {
    areaSelected = false;
    c.setHoverDimensions(0, 0);
    c.repaint();
  }
  
  /**
   * event for when the user presses a key
   * 
   * will either delete or unselect the selected area
   */
  @Override
  public void keyPressed(PCanvas c, KeyEvent e) {
    if (!areaSelected) return;

    switch (e.getKeyCode()) {
      case KeyEvent.VK_DELETE:
        deleteSelected(c);
        deselect(c);
      case KeyEvent.VK_ESCAPE:
        deselect(c);
    }
  }
  
  /**
   * event for when the user starts selecting an area
   * 
   * records the start pixel
   */
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    startPoint = c.getPixelPoint(e.getPoint());
    System.out.println("x: " + (int)startPoint.getX() + "y: " + (int)startPoint.getY());
  }
  
  /**
   * event for when the user finishes selcting an area
   * 
   * calculates the width, and height based on the start pixel and end pixel
   */
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {
    areaSelected = true;

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
  @Override
  public void keyTyped(PCanvas c, KeyEvent e) {}
  @Override
  public void keyReleased(PCanvas c, KeyEvent e) {}
}
