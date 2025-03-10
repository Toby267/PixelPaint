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
  protected enum State{
    NOTHING,
    SELECTING,
    MOVING,
    RESIZING
  }

  State state = State.NOTHING;
  
  Point2D startPoint, endPoint;

  Point2D dragStartPoint;
  Point2D dragEndPoint;

  /**
   * methods for calculating the true start, width and height based of the start and end points of the drag
   */
  private Point2D calcTrueStart() {
    return new Point2D.Double(
      Double.min(startPoint.getX(), endPoint.getX()),
      Double.min(startPoint.getY(), endPoint.getY())
    );
  }
  private int calcWidth() {
    int width = (int)endPoint.getX() - (int)startPoint.getX();
    if (width < 0) width = -width;
    return width;
  }
  private int calcHeight() {
    int height = (int)endPoint.getY() - (int)startPoint.getY();
    if (height < 0) height = -height;
    return height;
  }

  /**
   * deletes the selected pixels for the given canvas
   * 
   * @param c the canvas to delete the pixels in
   */
  private void deleteSelected(PCanvas c) {
    Image image = c.getActiveImage();
    Layer activeLayer = image.getActiveLayer();
    int maxHeight = image.getHeight(), maxWidth = image.getHeight();


    for (int i = 0; i < calcWidth(); i++){
      for (int j = 0; j < calcHeight(); j++){
        int x = (int)calcTrueStart().getX() + i;
        int y = (int)calcTrueStart().getY() + j;

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
    state = State.NOTHING;
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
    if (state == State.NOTHING) return;

    switch (e.getKeyCode()) {
      case KeyEvent.VK_DELETE:
        deleteSelected(c);
        deselect(c);
      case KeyEvent.VK_ESCAPE:
        deselect(c);
      case KeyEvent.VK_ENTER:
        deselect(c);
    }
  }

  /**
   * event for moving the selected area, or resizing it depending on where it was dragged from
   * 
   * if dragged from outline, resize, if dragged from center, move
   */
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    if (state == State.SELECTING) {
      endPoint = c.getPixelPoint(e.getPoint());

      c.setHoverPixel(calcTrueStart());
      c.setHoverDimensions(calcWidth(), calcHeight());
      
      c.repaint();
    }
    else if (state == State.MOVING) {

    }
    else if (state == State.RESIZING) {

    }
  }
  
  /**
   * event for determining what drag action to perform
   * 
   * records the start pixel
   */
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    if (state == State.NOTHING) {
      state = State.SELECTING;
      startPoint = c.getPixelPoint(e.getPoint());
    }
    else if (state == State.SELECTING){
      state = State.MOVING;
    }
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
  public void keyReleased(PCanvas c, KeyEvent e) {}
}
