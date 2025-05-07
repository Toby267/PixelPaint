package org.scc200g15.gui.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.scc200g15.gui.GUI;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.ToolManager;

/**
 * PCanvas - The canvas is the area on the screen where the main ActiveImage is
 * drawn it handles tracks mouse inputs and passes them to the tool manager
 */
public class PCanvas extends JPanel {

  Image activeImage = null;
  BufferedImage imageBuffer;

  Transform currentTransform;

  // Pan + Zoom
  float zoomLevel = 1;
  Point2D zoomCenter = new Point(0, 0);
  Point dif = new Point(0, 0);

  // Hover Pixel
  Point2D hoverPixel = new Point(-1, -1);
  int hoverWidth = 0, hoverHeight = 0;
  Color hoverColour = new Color(0, 0, 0, 0);

  // Hover Pixel
  Point2D moveArea = new Point(-1, -1);
  int moveWidth = 0, moveHeight = 0;
  Color moveColour = new Color(210, 210, 210, 100);

  /**
   * Default constructor, with no image active to start
   */
  public PCanvas() {
    this.setBackground(Color.BLACK);
  }

  /**
   * Overrides the rendering of the JPanel to allow the pixels of the image to be
   * rendered onto the screen, also responsible for handling the pan and zooming
   * of the image on the canvas
   */
  @Override
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    // Draw the black background across the entire canvas
    g2d.setColor(getBackground());
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

    // Dent draw anything but background if there is no image
    if (activeImage == null)
      return;

    // Calculate the X and Y SF to make the image fill the screen
    float XSF = (float) this.getWidth() / activeImage.getWidth();
    float YSF = (float) this.getHeight() / activeImage.getHeight();

    // Pick the smaller of the X and Y SF to make sure that the full image fits on
    // the screen
    float SF = Math.min(XSF, YSF);

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

    g2d.drawImage(imageBuffer, null, 0, 0);

    g2d.setColor(Color.LIGHT_GRAY);
    g2d.setStroke(new BasicStroke(0.1f));
    g2d.drawRect(0, 0, imageBuffer.getWidth(), imageBuffer.getHeight());

    // Draws the selected area if any
    g2d.setColor(hoverColour);
    g2d.fillRect((int) hoverPixel.getX(), (int) hoverPixel.getY(), hoverWidth, hoverHeight);
    g2d.setColor(moveColour);
    g2d.fillRect((int) moveArea.getX(), (int) moveArea.getY(), moveWidth, moveHeight);
  }

  /**
   * Handles applying the reese transform to get the pixel pos from the position
   * on screen
   * 
   * @param p The pixel on screen
   * @return returns the pixel pos for the given mouse position on screen
   */
  public Point2D getPixelPoint(Point p) {
    if (currentTransform == null)
      return new Point(0, 0);

    // Define a point to put the result in
    Point2D dsPoint = new Point2D.Float();
    try {
      // Apply the inverse of the zoom and pan transform
      currentTransform.inverseTransform(p, dsPoint);
    } catch (NoninvertibleTransformException e) {
      // This should not be possible this would only happen if the scale was ever set
      // to 0
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
    recalculateAllPixels();

    GUI.getInstance().getLayerSelector().redrawMenuUI();

    repaint();
  }

  /**
   * 
   * @return returns the current active image for the canvas
   */
  public Image getActiveImage() {
    return activeImage;
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
    addKeyListener(toolManager);
    this.setFocusable(true);
  }

  /**
   * Zoom in the canvas about a point
   * 
   * @param p The point to zoom the canvas about
   */
  public void zoomIn(Point p) {
    zoomCenter = p;
    zoomLevel += 0.05f;

    // Update the status bar zoom field
    GUI.getInstance().getStatusBar().updateZoom(zoomLevel);
  }

  /**
   * Zoom out the canvas about a point
   * 
   * @param p The point to zoom the canvas about
   */
  public void zoomOut(Point p) {
    zoomLevel -= 0.05f;
    if (zoomLevel <= 0.05)
      zoomLevel = 0.05f;
    else
      zoomCenter = p;

    // Update the status bar zoom field
    GUI.getInstance().getStatusBar().updateZoom(zoomLevel);
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
   * @param hoverPixel the pos of the current hover pixel
   */
  public void setHoverPixel(Point2D hoverPixel) {
    this.hoverPixel = hoverPixel;
  }

  /**
   * Sets the dimensions for the hover pixels
   * 
   * @param width  the width
   * @param height the height
   */
  public void setHoverDimensions(int width, int height) {
    this.hoverWidth = width;
    this.hoverHeight = height;
  }

  /**
   * Sets the color for the hover pixel
   * 
   * @param hoverColour the color for the hover pixel
   */
  public void setHoverColour(Color hoverColour) {
    this.hoverColour = hoverColour;
  }

  /**
   * @param moveArea the pos of the current move pixel
   */
  public void setMovePixel(Point2D moveArea) {
    this.moveArea = moveArea;
  }

  /**
   * Sets the dimensions for the move pixels
   * 
   * @param width  the width
   * @param height the height
   */
  public void setMoveDimensions(int width, int height) {
    this.moveWidth = width;
    this.moveHeight = height;
  }

  /**
   * Sets the color for the move pixel
   * 
   * @param moveColour the color for the move pixel
   */
  public void setMoveColour(Color moveColour) {
    this.moveColour = moveColour;
  }

  /**
   * Go through all layers for a pixel and work out what color the final result
   * should be
   * 
   * @param x the x pos
   * @param y the y pos
   */
  public void recalculatePixel(int x, int y) {
    imageBuffer = activeImage.updateImageBuffer(imageBuffer, x, y, 1, 1);
  }

  /**
   * Go through all layers for a rectangle of pixels and work out what there
   * colors the final result should be
   * 
   * @param x the x pos
   * @param y the y pos
   * @param w the width of the rectangle
   * @param h the height of the rectangle
   */
  public void recalculatePixels(int x, int y, int w, int h) {
    imageBuffer = activeImage.updateImageBuffer(imageBuffer, x, y, w, h);
  }

  /**
   * Go through all pixels and work out what there colors the final result should
   * be
   */
  public void recalculateAllPixels() {
    imageBuffer = activeImage.calculateImageBuffer();
  }

  public BufferedImage getBufferedImage() {
    return imageBuffer;
  }

  /**
   * @param point the point to check
   * @return returns true if point is out of canvas bounds
   */
  public boolean isOutOfBounds(Point2D point) {
    if (point.getX() < 0 || point.getX() >= activeImage.getWidth())
      return true;
    if (point.getY() < 0 || point.getY() >= activeImage.getHeight())
      return true;

    return false;
  }

  /**
   * @param x
   * @param y
   * @return returns true if point is out of canvas bounds
   */
  public boolean isOutOfBounds(int x, int y) {
    if (x < 0 || x >= activeImage.getWidth())
      return true;
    if (y < 0 || y >= activeImage.getHeight())
      return true;

    return false;
  }

  public void saveImage() {
    if (activeImage == null) {
      JOptionPane.showMessageDialog(this, "No image to save!", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Image");
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG and BMP Files", "png", "bmp"));

    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();
      try {
        if (fileToSave.getAbsolutePath().endsWith(".png"))
          ImageIO.write(imageBuffer, "png", new File(fileToSave.getAbsolutePath()));
        else if (fileToSave.getAbsolutePath().endsWith(".bmp")){
          System.out.println("BMP");
          BufferedImage newImage = new BufferedImage(imageBuffer.getWidth(), imageBuffer.getHeight(), BufferedImage.TYPE_INT_RGB);

          Graphics2D g = newImage.createGraphics();
          g.drawImage(imageBuffer, 0, 0, imageBuffer.getWidth(), imageBuffer.getHeight(), null);
          g.dispose();

          ImageIO.write(newImage, "bmp", new File(fileToSave.getAbsolutePath()));
        } else
          ImageIO.write(imageBuffer, "png", new File(fileToSave.getAbsolutePath() + ".png"));
        JOptionPane.showMessageDialog(this, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving image!", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void saveActiveLayer() {
    if (activeImage == null) {
      JOptionPane.showMessageDialog(this, "No image to save!", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Layer");
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG and BMP Files", "png", "bmp"));

    int userSelection = fileChooser.showSaveDialog(this);

    Layer l = GUI.getInstance().getActiveImage().getActiveLayer();

    BufferedImage layerImage = new BufferedImage(l.getLayerWidth(), l.getLayerHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = layerImage.createGraphics();

    for (int i = 0; i < l.getLayerWidth(); i++) {
      for (int j = 0; j < l.getLayerHeight(); j++) {
        g2d.setColor(l.getPixel(i, j));
        g2d.fillRect(i, j, 1, 1);
      }
    }

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();
      try {
        if (fileToSave.getAbsolutePath().endsWith(".png"))
          ImageIO.write(layerImage, "png", new File(fileToSave.getAbsolutePath()));
        else if (fileToSave.getAbsolutePath().endsWith(".bmp")){
          System.out.println("BMP");
          BufferedImage newImage = new BufferedImage(imageBuffer.getWidth(), imageBuffer.getHeight(), BufferedImage.TYPE_INT_RGB);

          Graphics2D g = newImage.createGraphics();
          g.drawImage(imageBuffer, 0, 0, imageBuffer.getWidth(), imageBuffer.getHeight(), null);
          g.dispose();

          ImageIO.write(newImage, "bmp", new File(fileToSave.getAbsolutePath()));
        }else
          ImageIO.write(layerImage, "png", new File(fileToSave.getAbsolutePath() + ".png"));
        JOptionPane.showMessageDialog(this, "Layer saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving layer!", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void openImage() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Open Image");
    fileChooser
        .setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG and BMP Files", "png", "bmp"));

    int userSelection = fileChooser.showOpenDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToOpen = fileChooser.getSelectedFile();
      try {
        BufferedImage openedImage = ImageIO.read(fileToOpen);
        if (openedImage != null) {
          setActiveImage(new Image(openedImage));
        }

      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error opening image!", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void saveWithLayers() {
    if (activeImage == null) {
      JOptionPane.showMessageDialog(this, "No image to save!", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Image");
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Pixel Paint Save Layers", "ppsl"));

    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();
      try {
        FileOutputStream fileOut;
        if (!fileToSave.getAbsolutePath().endsWith(".ppsl"))
          fileOut = new FileOutputStream(new File(fileToSave.getAbsolutePath() + ".ppsl"));
        else
          fileOut = new FileOutputStream(new File(fileToSave.getAbsolutePath()));

        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(activeImage);
        out.close();
        JOptionPane.showMessageDialog(this, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error saving image!", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void openWithLayers() {

    int res = JOptionPane.showConfirmDialog(
        this,
        "Do you want to proceed?",
        "Are you sure you want to load this image any changes currently made will be lost",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    if (res != JOptionPane.YES_OPTION)
      return;

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Open Image");
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Pixel Paint Save Layers", "ppsl"));

    int userSelection = fileChooser.showOpenDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToOpen = fileChooser.getSelectedFile();

      try {
        FileInputStream fileIn = new FileInputStream(fileToOpen);

        ObjectInputStream in = new ObjectInputStream(fileIn);

        Image image = (Image) in.readObject();
        in.close();

        setActiveImage(image);
      } catch (IOException | ClassNotFoundException e) {
        JOptionPane.showMessageDialog(this, "Error opening image!", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}