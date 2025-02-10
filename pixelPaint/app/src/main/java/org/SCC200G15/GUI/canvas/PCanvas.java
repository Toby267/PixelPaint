package org.scc200g15.gui.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import org.scc200g15.image.Image;
import org.scc200g15.tools.ToolManager;

public class PCanvas extends JPanel{
    Image activeImage = null;

    Transform currentTransform;

    float zoomLevel = 1;

    Point2D zoomCenter = new Point(-1, -1);
    Point2D hoverPixel = new Point(-1, -1);

    Point dif = new Point(0, 0);

    public PCanvas() {
        this.setBackground(Color.BLACK);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw the black background across the entire canvas
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Dent draw anything but background if there is no image
        if (activeImage == null) return;

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

        // Apply the transform
        g2d.transform(currentTransform);

        // Get the pixels of the image
        Color[][] pixels = activeImage.getPixels();

        // Render the pixels onto the screen
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                if((int)(hoverPixel.getX()) == x &&  (int)(hoverPixel.getY()) == y) g2d.setColor(Color.red);
                else g2d.setColor(pixels[x][y]);
            
                g2d.fillRect(x, y, 1, 1);
            }
        }
    }

    public Point2D getPixelPoint(Point p) {
        Point2D dsPoint = new Point2D.Float();
        try {
            currentTransform.inverseTransform(p, dsPoint);
        } catch (NoninvertibleTransformException e) {
            // Should never happen
            return new Point(0, 0);
        }

        return dsPoint;
    }

    public void setActiveImage(Image i) {
        activeImage = i;
    }

    public void registerToolManager(ToolManager toolManager){
        addMouseListener(toolManager);
        addMouseMotionListener(toolManager);
        addMouseWheelListener(toolManager);
    }

    public void zoomIn(Point p){
        zoomCenter = p;
        zoomLevel += 0.05;
    }

    public void zoomOut(Point p){
        zoomCenter = p;
        zoomLevel -= 0.05;
        if (zoomLevel <= 0.05)
            zoomLevel = 0.05f;
    }

    public void setDif(Point p){
        dif = p;
    }
    public Point getDif(){
        return dif;
    }

    public void setHoverPixel(Point2D hoverPixel){
        this.hoverPixel = hoverPixel;
    }
}
