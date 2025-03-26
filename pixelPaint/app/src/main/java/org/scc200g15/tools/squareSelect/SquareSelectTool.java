package org.scc200g15.tools.squareSelect;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.scc200g15.action.PixelsChangedAction;
import org.scc200g15.gui.GUI;
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

  ArrayList<Point> actionPoints = new ArrayList<>();
  ArrayList<Color> actionOldColours = new ArrayList<>();
  ArrayList<Color> actionNewColours = new ArrayList<>();

  private SelectState currentState = new Idle();
  private Side selectedSide;
  
  private Point2D startPoint, endPoint;
  private Point2D resizePoint;

  private Point2D moveStartPoint, moveEndPoint;
  private Color[][] cachedArea;

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
  protected void setEndPointX(double x) {
    endPoint = new Point2D.Double(x, endPoint.getY());
  }
  protected void setEndPointY(double y) {
    endPoint = new Point2D.Double(endPoint.getX(), y);
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

  protected void setMoveStartPoint(Point2D p) {
    moveStartPoint = p;
  }
  protected Point2D getMoveStartPoint() {
    return moveStartPoint;
  }

  protected void setMoveEndPoint(Point2D p) {
    moveEndPoint = p;
  }
  protected Point2D getMoveEndPoint() {
    return moveEndPoint;
  }

  // * ---------------------------------- [ PUBLIC ACTIONS ] ---------------------------------- * //

  public void delete(PCanvas c) {
    if (currentState instanceof Selecting) {
      deleteSelected(c);

      PixelsChangedAction drawAction = new PixelsChangedAction(c.getActiveImage().getActiveLayer(), actionPoints, actionOldColours, new Color(0, 0, 0, 0));
      GUI.getInstance().getActiveImage().addAction(drawAction);

      deselect(c);
    }
  }

  public void escape(PCanvas c) {
    if (currentState instanceof Selecting) {
      deselect(c);
    }
  }

  public void copy(PCanvas c) {
    if (currentState instanceof Selecting) {
      cacheSelectedArea(c);
    }
  }

  public void paste(PCanvas c) {
    if (currentState instanceof Selecting) {
      printCached(c);

      PixelsChangedAction drawAction = new PixelsChangedAction(c.getActiveImage().getActiveLayer(), actionPoints, actionOldColours, actionNewColours);
      GUI.getInstance().getActiveImage().addAction(drawAction);

      deselect(c);
    }
  }

  public void move(PCanvas c) {
    if (currentState instanceof Moving) {
      cacheAndDelete(c);
      printCached(c);

      PixelsChangedAction drawAction = new PixelsChangedAction(c.getActiveImage().getActiveLayer(), actionPoints, actionOldColours, actionNewColours);
      GUI.getInstance().getActiveImage().addAction(drawAction);

      deselect(c);
    }
  }

  @Override
  public void deactivate(PCanvas c) {
    if (currentState instanceof Selecting) {
      deselect(c);
    }
  }

  // * ---------------------------------- [ PROTECTED ACTIONS ] ---------------------------------- * //
  
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
   * deselects the selected area
   * 
   * @param c the canvas
   */
  protected void deselect(PCanvas c) {
    currentState = new Idle();

    setStartPoint(null);
    setEndPoint(null);

    setMoveStartPoint(null);
    setMoveEndPoint(null);

    actionPoints = new ArrayList<>();
    actionOldColours = new ArrayList<>();
    actionNewColours = new ArrayList<>();
    
    c.setHoverDimensions(0, 0);
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

    Point2D trueStart = calcTrueStart();
    int width = calcWidth(), height = calcHeight();

    for (int i = 0; i < width; i++){
      for (int j = 0; j < height; j++){
        int x = (int)trueStart.getX() + i;
        int y = (int)trueStart.getY() + j;

        if (c.isOutOfBounds(x, y)) continue;

        //adds the pixel to actionPoints, actionOldColours
        addPixelToActionLists(x, y, activeLayer.getPixel(x, y));
        
        activeLayer.setPixel(x, y, new Color(0, 0, 0, 0));
        c.recalculatePixel(x, y);
      }
    }

    c.repaint();
  }
  /**
   * caches the selected arrea within cachedArea
   * 
   * @param c the canvas
   */
  protected void cacheSelectedArea(PCanvas c) {
    cachedArea = new Color[calcWidth()][calcHeight()];

    Image image = c.getActiveImage();
    Layer activeLayer = image.getActiveLayer();

    Point2D trueStart = calcTrueStart();

    for (int i = 0; i < cachedArea.length; i++){
      for (int j = 0; j < cachedArea[0].length; j++){
        int x = (int)trueStart.getX() + i;
        int y = (int)trueStart.getY() + j;

        if (c.isOutOfBounds(x, y)) continue;
        
        cachedArea[i][j] = activeLayer.getPixel(x, y);
      }
    }
  }
  /**
   * caches the selected area while deleting the area on the canvas
   * 
   * @param c the canvas
   */
  protected void cacheAndDelete(PCanvas c) {
    cachedArea = new Color[calcWidth()][calcHeight()];

    Image image = c.getActiveImage();
    Layer activeLayer = image.getActiveLayer();

    Point2D trueStart = calcTrueStart();

    for (int i = 0; i < cachedArea.length; i++){
      for (int j = 0; j < cachedArea[0].length; j++){
        int x = (int)trueStart.getX() + i;
        int y = (int)trueStart.getY() + j;

        if (c.isOutOfBounds(x, y)) continue;

        //adds the pixel to actionPoints, actionOldColours, and actionNewColours
        addPixelToActionLists(x, y, activeLayer.getPixel(x, y), new Color(0, 0, 0, 0));
        
        cachedArea[i][j] = activeLayer.getPixel(x, y);
        activeLayer.setPixel(x, y, new Color(0, 0, 0, 0));
        c.recalculatePixel(x, y);
      }
    }

    c.repaint();
  }
  /**
   * prints the cached area to the move area wihtout deleting or deselecting the original area
   * 
   * @param c the canvas
   */
  protected void printCached(PCanvas c) {
    if (cachedArea == null)
      return;

    Image image = c.getActiveImage();
    Layer activeLayer = image.getActiveLayer();

    Point2D transform = calcMoveTransform();
    Point2D trueStart = calcTrueStart();

    for (int i = 0; i < cachedArea.length; i++){
      for (int j = 0; j < cachedArea[0].length; j++){
        //calculate the start and end coordinates
        int x = (int)trueStart.getX() + (int)transform.getX() + i;
        int y = (int)trueStart.getY() + (int)transform.getY() + j;

        //if the start pixel or end is out of bounds
        if (c.isOutOfBounds(x, y)) continue;
        
        //adds the pixel to actionPoints, actionOldColours, and actionNewColours
        addPixelToActionLists(x, y, activeLayer.getPixel(x, y), cachedArea[i][j]);

        //cache the colour and delete the pixel
        activeLayer.setPixel(x, y, cachedArea[i][j]);
        c.recalculatePixel(x, y);
      }
    }

    c.repaint();
  }

  // * ---------------------------------- [ HELPER METHODS ] ---------------------------------- * //

  //adds the change to actionPoints, actionOldColours and actionNewColours, overides the value in actionNewColours if it already exists
  private void addPixelToActionLists(int x, int y, Color oldColor, Color newColor) {
    int index = actionPoints.indexOf(new Point(x, y));

    if (index == -1) {
      actionPoints.add(new Point(x, y));
      actionOldColours.add(oldColor);
      actionNewColours.add(newColor);
    }
    else {
      actionNewColours.set(index, newColor);
    }
  }
  //adds the change to actionPoints, actionOldColours, overides the value in actionOldColours if it already exist
  private void addPixelToActionLists(int x, int y, Color oldColor) {
    int index = actionPoints.indexOf(new Point(x, y));

    if (index == -1) {
      actionPoints.add(new Point(x, y));
      actionOldColours.add(oldColor);
    }
    else {
      actionOldColours.set(index, oldColor);
    }
  }
  
  //calculates the top left pixel of the selected area
  protected Point2D calcTrueStart() {
    return new Point2D.Double(
      (int)Double.min(startPoint.getX(), endPoint.getX()),
      (int)Double.min(startPoint.getY(), endPoint.getY())
    );
  }
  //calculates the width of the selected area
  protected int calcWidth() {
    int width = (int)endPoint.getX() - (int)startPoint.getX();
    if (width < 0) width = -width;
    return width + 1;
  }
  //calculates the height of the selected area
  protected int calcHeight() {
    int height = (int)endPoint.getY() - (int)startPoint.getY();
    if (height < 0) height = -height;
    return height + 1;
  }
  //returns whether a given pixel is within the selected area
  protected boolean contains(Point2D p) {
    Point2D start = calcTrueStart();
    int width = calcWidth(), height = calcHeight();
    
    boolean x = p.getX() >= start.getX() && p.getX() <= start.getX() + width;
    boolean y = p.getY() >= start.getY() && p.getY() <= start.getY() + height;

    return (x && y);
  }
  //returns whether a given pixel is on the border with the selected area
  protected Side getBorder(Point2D p) {
    Point2D start = calcTrueStart();
    int width = calcWidth(), height = calcHeight();

    if (!contains(p))
      return null;
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
  //returns the startPoint or endPoint (or null) of the selected area, that corresponds to the given side
  protected Point2D getBorderPoint(Side s) {
    if (s == Side.RIGHT)  return startPoint.getX() > endPoint.getX() ? startPoint : endPoint;
    if (s == Side.LEFT)   return startPoint.getX() < endPoint.getX() ? startPoint : endPoint;
    if (s == Side.BOTTOM) return startPoint.getY() > endPoint.getY() ? startPoint : endPoint;
    if (s == Side.TOP)    return startPoint.getY() < endPoint.getY() ? startPoint : endPoint;
    
    return null;
  }
  //calculates the transformation needed for the move operation
  protected Point2D calcMoveTransform() {
    if (moveEndPoint == null) return new Point2D.Double(0f, 0f);
    
    return new Point2D.Double(
      (int)moveEndPoint.getX() - (int)moveStartPoint.getX(),
      (int)moveEndPoint.getY() - (int)moveStartPoint.getY()
    );
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
