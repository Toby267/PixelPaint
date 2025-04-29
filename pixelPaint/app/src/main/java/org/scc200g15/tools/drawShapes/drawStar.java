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

public class drawStar implements Tool {

    private Point2D startPoint = null;  // Starting point for drag
    private Point2D endPoint = null;    // End point for drag
    private boolean isDragging = false;

    /**
     * Draws the 5-pointed star on the canvas using the active layer and colour.
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

        // Get points to draw and fill the 5-pointed star
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
            c.repaint();  // Repaint to show star preview
        }
    }

    @Override
    public void mouseReleased(PCanvas c, MouseEvent e) {
        if (startPoint != null && endPoint != null && isDragging) {
            draw(c, e);  // Draw final star on mouse release
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
     * Returns the pixels for a filled 5-pointed star.
     *
     * @param startPoint The starting point (center) of the star.
     * @param endPoint   The ending point (defines radius of the star).
     * @return ArrayList<Point2D> representing all the pixels within the star.
     */
    private ArrayList<Point2D> returnPixels(Point2D startPoint, Point2D endPoint) {
        ArrayList<Point2D> points = new ArrayList<>();

        // Calculate center and radius
        int centerX = (int) startPoint.getX();
        int centerY = (int) startPoint.getY();
        int outerRadius = (int) Math.sqrt(Math.pow(endPoint.getX() - startPoint.getX(), 2) +
                Math.pow(endPoint.getY() - startPoint.getY(), 2));

        int innerRadius = outerRadius / 2; // The inner radius (star's inner points)

        // Calculate the 10 points of the star (5 outer, 5 inner)
        ArrayList<Point2D> starPoints = calculateStarPoints(centerX, centerY, outerRadius, innerRadius);

        // Find the bounding box of the star
        int minX = (int) starPoints.stream().mapToDouble(p -> p.getX()).min().getAsDouble();
        int maxX = (int) starPoints.stream().mapToDouble(p -> p.getX()).max().getAsDouble();
        int minY = (int) starPoints.stream().mapToDouble(p -> p.getY()).min().getAsDouble();
        int maxY = (int) starPoints.stream().mapToDouble(p -> p.getY()).max().getAsDouble();

        // Loop over each pixel in the bounding box and check if it is inside the star
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (isPointInStar(x, y, starPoints)) {
                    points.add(new Point2D.Double(x, y));
                }
            }
        }

        return points;
    }

    /**
     * Calculates the points of a 5-pointed star.
     *
     * @param cx          The center X-coordinate.
     * @param cy          The center Y-coordinate.
     * @param outerRadius The radius of the outer points of the star.
     * @param innerRadius The radius of the inner points of the star.
     * @return ArrayList<Point2D> representing the 10 points of the star.
     */
    private ArrayList<Point2D> calculateStarPoints(int cx, int cy, int outerRadius, int innerRadius) {
        ArrayList<Point2D> points = new ArrayList<>();
        double angle = Math.PI / 5; // 36 degrees for outer points, 72 degrees between inner points

        for (int i = 0; i < 5; i++) {
            // Outer point
            double outerX = cx + outerRadius * Math.cos(i * 2 * angle);
            double outerY = cy - outerRadius * Math.sin(i * 2 * angle);
            points.add(new Point2D.Double(outerX, outerY));

            // Inner point
            double innerX = cx + innerRadius * Math.cos(i * 2 * angle + angle);
            double innerY = cy - innerRadius * Math.sin(i * 2 * angle + angle);
            points.add(new Point2D.Double(innerX, innerY));
        }

        return points;
    }

    /**
     * Checks if a point is inside the 5-pointed star using the ray-casting algorithm.
     *
     * @param x       The X-coordinate of the point.
     * @param y       The Y-coordinate of the point.
     * @param starPoints The list of points that define the star.
     * @return True if the point is inside the star, false otherwise.
     */
    private boolean isPointInStar(int x, int y, ArrayList<Point2D> starPoints) {
        // Ray-casting algorithm to check if the point is inside the polygon formed by starPoints
        int intersections = 0;
        for (int i = 0; i < starPoints.size(); i++) {
            Point2D p1 = starPoints.get(i);
            Point2D p2 = starPoints.get((i + 1) % starPoints.size());

            // Check if a horizontal ray from the point intersects the edge of the star
            if (y > Math.min(p1.getY(), p2.getY()) && y <= Math.max(p1.getY(), p2.getY()) && x <= Math.max(p1.getX(), p2.getX())) {
                double xinters = (y - p1.getY()) * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY()) + p1.getX();
                if (p1.getX() == p2.getX() || x <= xinters) {
                    intersections++;
                }
            }
        }

        // Point is inside the polygon if the number of intersections is odd
        return intersections % 2 != 0;
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
