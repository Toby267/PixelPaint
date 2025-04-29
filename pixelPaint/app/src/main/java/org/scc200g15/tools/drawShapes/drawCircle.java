package org.scc200g15.tools.drawShapes;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.Tool;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class drawCircle implements Tool {

    private Point2D startPoint = null;  // Starting point for drag
    private Point2D endPoint = null;    // End point for drag
    private boolean isDragging = false;

    /**
     * Draws the circle on the canvas using the active layer and colour.
     *
     * @param c The canvas to draw on.
     * @param e The mouse event that triggered the action.
     */
    protected void draw(PCanvas c, MouseEvent e) {
        if (startPoint == null || endPoint == null || !isDragging) {
            return; // No valid points to draw
        }

        // Get active color and layer
        Color colour = GUI.getInstance().getSideBar().getActiveColor();
        Image image = c.getActiveImage();
        Layer activeLayer = image.getActiveLayer();

        // Get points to draw and fill the circle
        ArrayList<Point2D> points = returnPixels(startPoint, endPoint);

        for (Point2D p : points) {
            if (c.isOutOfBounds(p)) {
                continue; // Skip if the point is out of bounds
            }

            // Set pixel on active layer
            activeLayer.setPixel((int) p.getX(), (int) p.getY(), colour);
            c.recalculatePixel((int) p.getX(), (int) p.getY());
        }

        // Repaint canvas to reflect changes
        c.repaint();
    }

    @Override
    public void mousePressed(PCanvas c, MouseEvent e) {
        startPoint = c.getPixelPoint(e.getPoint()); // Set start point on mouse press
        endPoint = startPoint;  // Initialize end point
    }

    @Override
    public void mouseDragged(PCanvas c, MouseEvent e) {
        if (startPoint != null) {
            endPoint = c.getPixelPoint(e.getPoint());  // Update end point while dragging
            isDragging = true;
            c.repaint();  // Repaint to show circle preview
        }
    }

    @Override
    public void mouseReleased(PCanvas c, MouseEvent e) {
        if (startPoint != null && endPoint != null && isDragging) {
            draw(c, e);  // Draw final circle on mouse release
        }
        reset();  // Reset state after drawing
    }

    @Override
    public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {
        // Implement mouse wheel behavior if needed
    }

    @Override
    public void mouseClicked(PCanvas c, MouseEvent e) {
        // Implement mouse click behavior if needed
    }

    @Override
    public void mouseMoved(PCanvas c, MouseEvent e) {
        // Implement mouse moved behavior if needed
    }

    @Override
    public void mouseExited(PCanvas c, MouseEvent e) {
        // Implement mouse exit behavior if needed
    }

    @Override
    public void mouseEntered(PCanvas c, MouseEvent e) {
        // Implement mouse enter behavior if needed
    }

    @Override
    public void keyTyped(PCanvas c, KeyEvent e) {
        // Implement key typed behavior if needed
    }

    @Override
    public void keyPressed(PCanvas c, KeyEvent e) {
        // Implement key pressed behavior if needed
    }

    @Override
    public void keyReleased(PCanvas c, KeyEvent e) {
        // Implement key released behavior if needed
    }

    /**
     * Returns the pixels for a filled circle using Bresenham's Circle Algorithm.
     *
     * @param startPoint The starting point of the drag (center).
     * @param endPoint   The ending point of the drag.
     * @return ArrayList<Point2D> representing all the pixels within the circle.
     */
    private ArrayList<Point2D> returnPixels(Point2D startPoint, Point2D endPoint) {
        ArrayList<Point2D> points = new ArrayList<>();

        // Calculate center and radius
        int centerX = (int) startPoint.getX();
        int centerY = (int) startPoint.getY();
        int radius = (int) Math.sqrt(Math.pow(endPoint.getX() - startPoint.getX(), 2) +
                Math.pow(endPoint.getY() - startPoint.getY(), 2));

        // Bresenham's Circle Algorithm
        int x = 0;
        int y = radius;
        int d = 3 - 2 * radius;

        // Add all points for filling the circle
        fillCircle(centerX, centerY, x, y, points);

        while (y >= x) {
            x++;

            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }

            fillCircle(centerX, centerY, x, y, points);
        }

        return points;
    }

    /**
     * Adds the points for filling a circle.
     *
     * @param cx Center X-coordinate.
     * @param cy Center Y-coordinate.
     * @param x  X-offset.
     * @param y  Y-offset.
     * @param points ArrayList to store the points.
     */
    private void fillCircle(int cx, int cy, int x, int y, ArrayList<Point2D> points) {
        // Fill the horizontal lines between the circle's boundary
        for (int i = cx - x; i <= cx + x; i++) {
            points.add(new Point2D.Double(i, cy + y)); // Lower part
            points.add(new Point2D.Double(i, cy - y)); // Upper part
        }
        for (int i = cx - y; i <= cx + y; i++) {
            points.add(new Point2D.Double(i, cy + x)); // Lower part
            points.add(new Point2D.Double(i, cy - x)); // Upper part
        }
    }

    /**
     * Resets the drag state.
     */
    private void reset() {
        startPoint = null;
        endPoint = null;
        isDragging = false;
    }

    @Override
    public void deactivate(PCanvas c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivate'");
    }
}
