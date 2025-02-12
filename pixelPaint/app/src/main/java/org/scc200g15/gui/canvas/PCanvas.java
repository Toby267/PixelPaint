package org.scc200g15.gui.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.ToolManager;

/**
 * PCanvas - The canvas is the area on the screen where the main ActiveImage is drawn it handles tracks mouse inputs and passes them to the tool manager
 */
public class PCanvas extends JPanel {
  Image activeImage = null;

  Transform currentTransform;

  float zoomLevel = 1;

  Point2D zoomCenter = new Point(0, 0);
  Point2D hoverPixel = new Point(-1, -1);

  Point dif = new Point(0, 0);

  /**
   * Default constructor, with no image active to start
   */
  public PCanvas() {
    this.setBackground(Color.BLACK);
  }

  /**
   * Overrides the rendering of the JPanel to allow the pixels of the image to be rendered onto the screen, also responsible for handling the pan and zooming of the image on the canvas
   */
  @Override
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    Rectangle r = g.getClipBounds();

    // Draw the black background across the entire canvas
    g2d.setColor(getBackground());
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

    // Dent draw anything but background if there is no image
    if (activeImage == null)
      return;

    // Calculate the X and Y SF to make the image fill the screen
    float XSF = this.getWidth() / activeImage.getWidth();
    float YSF = this.getHeight() / activeImage.getHeight();

    // Pick the smaller of the X and Y SF to make sure that the full image fits on the screen
    float SF = XSF > YSF ? YSF : XSF;

    // Calculate the move needed to move the grid into the middle of the canvas
    int xMove = (this.getWidth() - (int) (SF * activeImage.getWidth())) / 2;
    int yMove = (this.getHeight() - (int) (SF * activeImage.getHeight())) / 2;

    // Create a new transform
    currentTransform = new Transform();

    // Move to the middle of the screen and then apply the offset for pan
    currentTransform.translate(xMove + dif.x, yMove + dif.y);

    // Zoom in with SF zoomLevel about the mouse position
    currentTransform.scaleAboutPoint(zoomLevel, zoomCenter);

    // Scale the pixels so they fill the screen on zoom level 1
    currentTransform.scale(SF);

    g2d.transform(currentTransform);

    Point2D s = getPixelPoint(new Point(r.x, r.y));
    Point2D e = getPixelPoint(new Point((int) r.getMaxX(), (int) r.getMaxY()));

    int sx = (int) s.getX() < 0 ? 0 : (int) s.getX();
    int sy = (int) s.getY() < 0 ? 0 : (int) s.getY();

    int ex = Math.min(activeImage.getWidth(), (int) Math.ceil(e.getX()));
    int ey = Math.min(activeImage.getHeight(), (int) Math.ceil(e.getY()));

    Point2D startPoint = new Point(sx, sy);
    Point2D endPoint = new Point(ex, ey);

    // Render the pixels onto the screen
    for (int l = 0; l < activeImage.getLayerCount(); l++) {
      Layer layer = activeImage.getLayer(l);
      if (!layer.getIsActive())
        continue;

      Color[][] pixels = layer.getPixels();

      for (int x = (int) startPoint.getX(); x < endPoint.getX(); x++) {
        for (int y = (int) startPoint.getY(); y < endPoint.getY(); y++) {
          if ((int) (hoverPixel.getX()) == x && (int) (hoverPixel.getY()) == y)
            g2d.setColor(Color.red);
          else
            g2d.setColor(pixels[x][y]);

          g2d.fillRect(x, y, 1, 1);
        }
      }
    }
  }

  /**
   * Handles applying the reese transform to get the pixel pos from the position on screen
   * 
   * @param p The pixel on screen
   * @return returns the pixel pos for the given mouse position on screen
   */
  public Point2D getPixelPoint(Point p) {
    // Define a point to put the result in
    Point2D dsPoint = new Point2D.Float();
    try {
      // Apply the inverse of the zoom and pan transform
      currentTransform.inverseTransform(p, dsPoint);
    } catch (NoninvertibleTransformException e) {
      // This should not be possible this would only happen if the scale was ever set to 0
      return new Point(0, 0);
    }

    return dsPoint;
  }

  /**
   * Set the active image to draw on the screen
   * 
   * @param i the new image to draw
   */
  public void setActiveImage(Image i) {
    activeImage = i;
    repaint();
  }

  /**
   * Register the tool managers listeners with to listen to canvas mouse events
   * 
   * @param toolManager
   */
  public void registerToolManager(ToolManager toolManager) {
    addMouseListener(toolManager);
    addMouseMotionListener(toolManager);
    addMouseWheelListener(toolManager);
  }

  /**
   * Zoom in the canvas about a point
   * 
   * @param p The point to zoom the canvas about
   */
  public void zoomIn(Point p) {
    zoomCenter = p;
    zoomLevel += 0.05;
  }

  /**
   * Zoom out the canvas about a point
   * 
   * @param p The point to zoom the canvas about
   */
  public void zoomOut(Point p) {
    zoomLevel -= 0.05;
    if (zoomLevel <= 0.05)
      zoomLevel = 0.05f;
    else
      zoomCenter = p;
  }

  /**
   * Set the dif that is applied by the transform due to the pan tool
   * 
   * @param p the new dif
   */
  public void setDif(Point p) {
    dif = p;
  }

  /**
   * Get the dif that is applied by the transform due to the pan tool
   */
  public Point getDif() {
    return dif;
  }

  /**
   * FORDEV: set the current hover pixel
   * 
   * @param hoverPixel the pos of the current hover pixel
   */
  public void setHoverPixel(Point2D hoverPixel) {
    this.hoverPixel = hoverPixel;
  }
}
