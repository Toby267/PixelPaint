package org.scc200g15.tools.drawShapes;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.Tool;

public class drawTriangle implements Tool {

    private Point2D startPoint = null;
    private Point2D endPoint = null;
    private boolean isDragging = false;
    private ArrayList<Point2D> vertices = new ArrayList<>();

    protected void draw(PCanvas c) {
        if (vertices.size() < 3) {
            return; // Ensure only 3 vertices are used to draw
        }

        // Get active color and layer
        Color color = GUI.getInstance().getSideBar().getActiveColor();
        Image image = c.getActiveImage();
        Layer activeLayer = image.getActiveLayer();

        // Get points to draw and fill the triangle
        ArrayList<Point2D> points = returnPixels(vertices);

        for (Point2D p : points) {
            if (c.isOutOfBounds(p)) {
                continue; // Skip if the point is out of bounds
            }

            // Set pixel on active layer
            activeLayer.setPixel((int) p.getX(), (int) p.getY(), color);
            c.recalculatePixel((int) p.getX(), (int) p.getY());
        }

        // Repaint canvas to reflect changes
        c.repaint();
    }

    @Override
    public void mousePressed(PCanvas c, MouseEvent e) {
        if (vertices.size() < 3) {
            // Add a new vertex on each mouse press, and check if 3 vertices have been set
            vertices.add(c.getPixelPoint(e.getPoint()));
            if (vertices.size() == 3) {
                draw(c);  // If there are 3 vertices, draw the final triangle
            }
        }
    }

    @Override
    public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {

    }

    @Override
    public void mouseClicked(PCanvas c, MouseEvent e) {

    }

    @Override
    public void mouseMoved(PCanvas c, MouseEvent e) {
        if (isDragging && vertices.size() < 3) {
            // Update the end point while dragging
            endPoint = c.getPixelPoint(e.getPoint());
            c.repaint();
        }
    }

    @Override
    public void mouseExited(PCanvas c, MouseEvent e) {

    }

    @Override
    public void mouseDragged(PCanvas c, MouseEvent e) {
        if (vertices.size() < 3) {
            endPoint = c.getPixelPoint(e.getPoint());  // Update the dragging point
            isDragging = true;
            c.repaint();  // Repaint to show the triangle preview
        }
    }

    @Override
    public void mouseReleased(PCanvas c, MouseEvent e) {
        if (vertices.size() == 3) {
            draw(c);  // Draw the triangle after all 3 vertices are defined
        }
        reset();  // Reset after drawing
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

    private ArrayList<Point2D> returnPixels(ArrayList<Point2D> vertices) {
        ArrayList<Point2D> points = new ArrayList<>();

        // Get the three vertices of the triangle
        Point2D p1 = vertices.get(0);
        Point2D p2 = vertices.get(1);
        Point2D p3 = vertices.get(2);

        // Find the bounding box of the triangle
        int minX = (int) Math.min(Math.min(p1.getX(), p2.getX()), p3.getX());
        int maxX = (int) Math.max(Math.max(p1.getX(), p2.getX()), p3.getX());
        int minY = (int) Math.min(Math.min(p1.getY(), p2.getY()), p3.getY());
        int maxY = (int) Math.max(Math.max(p1.getY(), p2.getY()), p3.getY());

        // Loop over each pixel in the bounding box and check if it is inside the triangle
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (isPointInTriangle(x, y, p1, p2, p3)) {
                    points.add(new Point2D.Double(x, y));
                }
            }
        }

        return points;
    }

    private boolean isPointInTriangle(int x, int y, Point2D p1, Point2D p2, Point2D p3) {
        // Same area checking method to determine if a point is inside the triangle
        double area = Math.abs(p1.getX() * (p2.getY() - p3.getY()) +
                p2.getX() * (p3.getY() - p1.getY()) +
                p3.getX() * (p1.getY() - p2.getY()));

        double area1 = Math.abs(x * (p2.getY() - p3.getY()) +
                p2.getX() * (p3.getY() - y) +
                p3.getX() * (y - p2.getY()));
        double area2 = Math.abs(p1.getX() * (y - p3.getY()) +
                x * (p3.getY() - p1.getY()) +
                p3.getX() * (p1.getY() - y));
        double area3 = Math.abs(p1.getX() * (p2.getY() - y) +
                p2.getX() * (y - p1.getY()) +
                x * (p1.getY() - p2.getY()));

        return area == (area1 + area2 + area3);
    }

    private void reset() {
        startPoint = null;
        endPoint = null;
        isDragging = false;
        vertices.clear(); // Clear the stored vertices for the next triangle
    }

    @Override
    public void deactivate(PCanvas c) {}
}
