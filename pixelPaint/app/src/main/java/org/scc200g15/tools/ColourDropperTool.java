package org.scc200g15.tools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.canvas.PCanvas;

public class ColourDropperTool implements Tool {
    
    public ColourDropperTool() {
        
    }

    @Override
    public void mouseReleased(PCanvas c, java.awt.event.MouseEvent e) {
        System.out.println("REACHED");
        Point2D point = c.getPixelPoint(e.getPoint());
        
        int x = (int) Math.round(point.getX());
        int y = (int) Math.round(point.getY());
        System.out.println("(" + x + ", " + y + ")");



        // From: https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        int color = GUI.getInstance().getCanvas().getBufferedImage().getRGB(x, y);
        Color colour = new Color((color & 0xff0000) >> 16, (color & 0xff00) >> 8, color & 0xff);
        System.out.println("RED = ");
        System.out.println("BLUE = ");
        System.out.println("GREEN = ");
        GUI.getInstance().getSideBar().setColourWheel(colour); // SET ACTIVE COLOUR IN COLOR WHEEL
    }

    // Required interface methods that we don't need
    @Override
    public void mouseDragged(PCanvas c, java.awt.event.MouseEvent e) {}
    @Override
    public void mousePressed(PCanvas c, java.awt.event.MouseEvent e) {}
    @Override
    public void mouseWheelMoved(PCanvas c, java.awt.event.MouseWheelEvent e) {}
    @Override
    public void mouseClicked(PCanvas c, MouseEvent e) {}
    @Override
    public void mouseMoved(PCanvas c, java.awt.event.MouseEvent e) {}
    @Override
    public void mouseExited(PCanvas c, java.awt.event.MouseEvent e) {}
    @Override
    public void mouseEntered(PCanvas c, java.awt.event.MouseEvent e) {}
    @Override
    public void keyTyped(PCanvas c, java.awt.event.KeyEvent e) {}
    @Override
    public void keyPressed(PCanvas c, java.awt.event.KeyEvent e) {}
    @Override
    public void keyReleased(PCanvas c, java.awt.event.KeyEvent e) {}

}
