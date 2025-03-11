package org.scc200g15.tools;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;

/**
 * TODO: make the action listeners come form the tool menu,
 *      that way it can have its own paintComponent method instead of highjacking the hover pixel functionality from PCanvas,
 *      and you can then draw the highlighted area without the half pixel offset
 */
public class SquareSelectTool implements Tool {
  protected enum State {
    NOTHING,
    SELECTING,
    MOVING,
    RESIZING
  }
  protected enum Side {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM
  }

  private State state = State.NOTHING;
  private Side selectedSide = Side.LEFT;
  
  private Point2D startPoint, endPoint;
  private Point2D resizePoint = null;

  // * ---------------------------------- [ ACTIONS ] ---------------------------------- * //

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

  // * ---------------------------------- [ USED ACTION LISTENERS ] ---------------------------------- * //
  
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
      //TODO: this
    }
    else if (state == State.RESIZING) {
      if (selectedSide == Side.RIGHT || selectedSide == Side.LEFT)
        resizePoint.setLocation(c.getPixelPoint(e.getPoint()).getX(), resizePoint.getY());
      else if (selectedSide == Side.TOP || selectedSide == Side.BOTTOM)
        resizePoint.setLocation(resizePoint.getX(), c.getPixelPoint(e.getPoint()).getY());

      c.setHoverPixel(calcTrueStart());
      c.setHoverDimensions(calcWidth(), calcHeight());
      
      c.repaint();
    }
  }
  
  /**
   * event for determining what drag action to perform
   * 
   * records the start pixel
   */
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    Point2D p = c.getPixelPoint(e.getPoint());
    
    //state will either be nothing or selecting
    if (state == State.NOTHING) {
      state = State.SELECTING;
      startPoint = p;
      return;
    }

    //mouseReleased ensures that it is already selecting, this is just to make sure
    if (state != State.SELECTING) return;

    if (onBorder(p)){
      state = State.RESIZING;
      selectedSide = getBorder(p);
      resizePoint = getBorderPoint(p);
    }
    else if (contains(p)) {
      state = State.MOVING;
    }
    else {
      state = State.NOTHING;
      deselect(c);
    }
  }

  /**
   * event for determining the state after a click/drag has been performed
   */
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {
    //after moving or resizing, go back to the selecting state
    if (state == State.MOVING || state == State.RESIZING)
      state = State.SELECTING;
  }

  // * ---------------------------------- [ HELPER METHODS ] ---------------------------------- * //

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
  private boolean contains(Point2D p) {
    Point2D start = calcTrueStart();
    int width = calcWidth(), height = calcHeight();
    
    boolean x = p.getX() >= start.getX() && p.getX() <= start.getX() + width;
    boolean y = p.getY() >= start.getY() && p.getY() <= start.getY() + height;

    return (x && y);
  }
  private boolean onBorder(Point2D p) {
    Point2D start = calcTrueStart();
    int width = calcWidth(), height = calcHeight();
    
    boolean x = (int)p.getX() == (int)start.getX() || (int)p.getX() == (int)start.getX() + width;
    boolean y = (int)p.getY() == (int)start.getY() || (int)p.getY() == (int)start.getY() + height;

    return (x || y);
  }
  private Side getBorder(Point2D p) {
    Point2D start = calcTrueStart();
    int width = calcWidth(), height = calcHeight();

    if ((int)p.getX() == (int)start.getX())
      return Side.LEFT;
    if ((int)p.getX() == (int)start.getX() + width)
      return Side.RIGHT;
    if ((int)p.getY() == (int)start.getY())
      return Side.TOP;
    if ((int)p.getY() == (int)start.getY() + height)
      return Side.BOTTOM;

    return null;
  }
  private Point2D getBorderPoint(Point2D p) {
    if ((int)p.getX() == (int)startPoint.getX() || (int)p.getY() == (int)startPoint.getY())
      return startPoint;
    if ((int)p.getX() == (int)endPoint.getX() || (int)p.getY() == (int)endPoint.getY())
      return endPoint;

    return null;
  }

  
  // * ---------------------------------- [ UNUSED ACTION LISTENERS ] ---------------------------------- * //
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
