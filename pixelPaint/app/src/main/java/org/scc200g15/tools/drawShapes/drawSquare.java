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

public class drawSquare implements Tool {
    private Point2D startPoint = null;  // Starting point for drag
    private Point2D endPoint = null;    // End point for drag
    private boolean isDragging = false;

    /**
     * Draws the square on the canvas using the active layer and colour.
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

        // Get points to fill the square
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
            c.repaint();  // Repaint to show square preview
        }
    }

    @Override
    public void mouseReleased(PCanvas c, MouseEvent e) {
        if (startPoint != null && endPoint != null && isDragging) {
            draw(c, e);  // Draw final square on mouse release
        }
        reset();  // Reset state after drawing
    }

    @Override
    public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {

    }

    @Override
    public void mouseClicked(PCanvas c, MouseEvent e) {

    }

    @Override
    public void mouseMoved(PCanvas c, MouseEvent e) {

    }

    @Override
    public void mouseExited(PCanvas c, MouseEvent e) {

    }

    @Override
    public void mouseEntered(PCanvas c, MouseEvent e) {

    }

    @Override
    public void keyTyped(PCanvas c, KeyEvent e) {

    }

    @Override
    public void keyPressed(PCanvas c, KeyEvent e) {

    }

    @Override
    public void keyReleased(PCanvas c, KeyEvent e) {

    }

    /**
     * Returns the pixels for a square/rectangle based on start and end points.
     *
     * @param startPoint The starting point of the drag.
     * @param endPoint   The ending point of the drag.
     * @return ArrayList<Point2D> representing all the pixels within the square.
     */
    private ArrayList<Point2D> returnPixels(Point2D startPoint, Point2D endPoint) {
        ArrayList<Point2D> points = new ArrayList<>();

        // Get the starting (top-left) and ending (bottom-right) points
        int startX = (int) Math.min(startPoint.getX(), endPoint.getX());
        int startY = (int) Math.min(startPoint.getY(), endPoint.getY());
        int endX = (int) Math.max(startPoint.getX(), endPoint.getX());
        int endY = (int) Math.max(startPoint.getY(), endPoint.getY());

        // Loop through all points inside the rectangle and add them to the list
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                points.add(new Point2D.Double(i, j));  // Add each pixel to the list
            }
        }

        return points;
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

