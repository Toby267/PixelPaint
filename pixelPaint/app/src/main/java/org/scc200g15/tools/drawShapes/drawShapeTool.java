package org.scc200g15.tools.drawShapes;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.tools.Tool;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class drawShapeTool implements Tool {

    static String activeShape = "Circle";
    private drawCircle circleTool = new drawCircle();
    private drawSquare squareTool = new drawSquare();
    private drawTriangle triangleTool = new drawTriangle();
    private drawStar starTool = new drawStar();

    public static void setShape(String ID) {
        try {
            activeShape = ID;
            System.out.println("Active shape is: " + activeShape);  // Print the active shape to console
        } catch (Exception e) {
            throw new Error("No shape exists with ID: " + ID);
        }
    }

    @Override
    public void mousePressed(PCanvas c, MouseEvent e) {
        if (activeShape.equals("Circle")) {
            circleTool.mousePressed(c, e); // If active shape is circle, use drawCircle's method
        } else if (activeShape.equals("Square")) {
            squareTool.mousePressed(c, e); // If active shape is square, use drawSquare's method
        } else if (activeShape.equals("Triangle")) {
            triangleTool.mousePressed(c, e); // If active shape is triangle, use drawTriangle's method
        } else if (activeShape.equals("Star")) {
            starTool.mousePressed(c, e); // If active shape is star, use drawStar's method
        }
    }

    @Override
    public void mouseDragged(PCanvas c, MouseEvent e) {
        if (activeShape.equals("Circle")) {
            circleTool.mouseDragged(c, e); // Handle dragging for circle
        } else if (activeShape.equals("Square")) {
            squareTool.mouseDragged(c, e); // Handle dragging for square
        } else if (activeShape.equals("Triangle")) {
            triangleTool.mouseDragged(c, e); // Handle dragging for triangle
        } else if (activeShape.equals("Star")) {
            starTool.mouseDragged(c, e); // Handle dragging for star
        }
    }

    @Override
    public void mouseReleased(PCanvas c, MouseEvent e) {
        if (activeShape.equals("Circle")) {
            circleTool.mouseReleased(c, e); // Handle release for circle
        } else if (activeShape.equals("Square")) {
            squareTool.mouseReleased(c, e); // Handle release for square
        } else if (activeShape.equals("Triangle")) {
            triangleTool.mouseReleased(c, e); // Handle release for triangle
        } else if (activeShape.equals("Star")) {
            starTool.mouseReleased(c, e); // Handle release for star
        }
    }

    @Override
    public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {
        // Add any behavior needed on mouse wheel if applicable
    }

    @Override
    public void mouseClicked(PCanvas c, MouseEvent e) {
        // Handle mouse click event if needed
    }

    @Override
    public void mouseMoved(PCanvas c, MouseEvent e) {
        // Handle mouse move event if needed
    }

    @Override
    public void mouseExited(PCanvas c, MouseEvent e) {
        // Handle mouse exit event if needed
    }

    @Override
    public void mouseEntered(PCanvas c, MouseEvent e) {
        // Handle mouse enter event if needed
    }

    @Override
    public void keyTyped(PCanvas c, KeyEvent e) {
        // Handle key typed event if needed
    }

    @Override
    public void keyPressed(PCanvas c, KeyEvent e) {
        // Handle key pressed event if needed
    }

    @Override
    public void keyReleased(PCanvas c, KeyEvent e) {
        // Handle key released event if needed
    }

    @Override
    public void deactivate(PCanvas c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivate'");
    }
}
