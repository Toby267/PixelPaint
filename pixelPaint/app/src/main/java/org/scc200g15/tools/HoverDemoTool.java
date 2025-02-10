package org.scc200g15.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.scc200g15.gui.canvas.PCanvas;

public class HoverDemoTool implements Tool{

    Point offset = new Point(0, 0);

    @Override
    public void mouseMoved(PCanvas c, MouseEvent e) {
        c.setHoverPixel(c.getPixelPoint(e.getPoint()));
        c.repaint();
    }

    @Override
    public void mouseDragged(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mousePressed(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {
    }

    @Override
    public void mouseClicked(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseExited(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseReleased(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseEntered(PCanvas c, MouseEvent e) {
    }
    
}
