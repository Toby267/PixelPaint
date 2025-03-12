package org.scc200g15.tools.squareSelect;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.Tool;

/**
 * class for the square select tool, essentially a state machine for selecting, resizing, moving, etc
 * uses the hover pixel logic in PCanvas for highlighting the selected area
 */
public class SquareSelectTool implements Tool {
  protected enum Side {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM
  }

  private SelectState currentState = new Idle();
  private Side selectedSide = null;
  
  private Point2D startPoint, endPoint;
  private Point2D resizePoint = null;

  // * ---------------------------------- [ GETTERS/SETTERS ] ---------------------------------- * //

  protected void setState(SelectState state) {
      currentState = state;
  }
  protected SelectState getState() {
    return currentState;
  }

  protected Point2D getStartPoint() {
    return startPoint;
  }
  protected void setStartPoint(Point2D p) {
    startPoint = p;
  }

  protected Point2D getEndPoint() {
    return endPoint;
  }
  protected void setEndPoint(Point2D p) {
    endPoint = p;
  }

  protected Side getSelectedSide() {
    return selectedSide;
  }
  protected void setSelectedSide(Side s) {
    selectedSide = s;
  }

  protected Point2D getResizePoint() {
    return resizePoint;
  }
  protected void setResizePoint(Point2D p) {
    resizePoint = p;
  }

  // * ---------------------------------- [ ACTIONS ] ---------------------------------- * //

  /**
   * highlights the selected area on the given canvas
   * 
   * @param c the canvas
   */
  protected void paint(PCanvas c) {
    c.setHoverColour(new Color(110, 193, 240, 100));
    c.setHoverPixel(calcTrueStart());
    c.setHoverDimensions(calcWidth(), calcHeight());
    
    c.repaint();
  }

  /**
   * deletes the selected pixels for the given canvas
   * 
   * @param c the canvas
   */
  protected void deleteSelected(PCanvas c) {
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
   * @param c the canvas
   */
  protected void deselect(PCanvas c) {
    currentState = new Idle();

    c.setHoverDimensions(0, 0);
    c.repaint();
  }

  // * ---------------------------------- [ USED ACTION LISTENERS ] ---------------------------------- * //
  
  /**
   * used events will pass the event to the current state if not null
   */
  @Override
  public void keyPressed(PCanvas c, KeyEvent e) {
    if (currentState != null)
      currentState.keyPressed(c, e, this);
  }
  @Override
  public void mouseDragged(PCanvas c, MouseEvent e) {
    if (currentState != null)
      currentState.mouseDragged(c, e, this);
  }
  @Override
  public void mouseReleased(PCanvas c, MouseEvent e) {
    if (currentState != null)
      currentState.mouseReleased(c, e, this);
  }
  @Override
  public void mousePressed(PCanvas c, MouseEvent e) {
    if (currentState != null) {
      currentState.mousePressed(c, e, this);
    }
  }

  // * ---------------------------------- [ HELPER METHODS ] ---------------------------------- * //

  protected Point2D calcTrueStart() {
    return new Point2D.Double(
      Double.min(startPoint.getX(), endPoint.getX()),
      Double.min(startPoint.getY(), endPoint.getY())
    );
  }
  protected int calcWidth() {
    int width = (int)endPoint.getX() - (int)startPoint.getX();
    if (width < 0) width = -width;
    return width + 1;
  }
  protected int calcHeight() {
    int height = (int)endPoint.getY() - (int)startPoint.getY();
    if (height < 0) height = -height;
    return height + 1;
  }
  protected boolean contains(Point2D p) {
    Point2D start = calcTrueStart();
    int width = calcWidth(), height = calcHeight();
    
    boolean x = p.getX() >= start.getX() && p.getX() <= start.getX() + width - 1;
    boolean y = p.getY() >= start.getY() && p.getY() <= start.getY() + height - 1;

    return (x && y);
  }
  protected Side getBorder(Point2D p) {
    Point2D start = calcTrueStart();
    int width = calcWidth(), height = calcHeight();

    if ((int)p.getX() == (int)start.getX())
      return Side.LEFT;
    if ((int)p.getX() == (int)start.getX() + width - 1)
      return Side.RIGHT;
    if ((int)p.getY() == (int)start.getY())
      return Side.TOP;
    if ((int)p.getY() == (int)start.getY() + height - 1)
      return Side.BOTTOM;

    return null;
  }
  protected Point2D getBorderPoint(Point2D p) {
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
