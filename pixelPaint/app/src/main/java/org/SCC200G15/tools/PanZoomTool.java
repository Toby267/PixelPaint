package org.scc200g15.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.scc200g15.gui.canvas.PCanvas;

public class PanZoomTool implements Tool{

    Point offset = new Point(0, 0);

    @Override
    public void mouseDragged(PCanvas c, MouseEvent e) {
        c.setDif(new Point(e.getX() - offset.x, e.getY() - offset.y));
        c.repaint();
    }

    @Override
    public void mousePressed(PCanvas c, MouseEvent e) {
        Point dif = c.getDif();
        offset = new Point(e.getX() - dif.x, e.getY() - dif.y);
    }

    @Override
    public void mouseWheelMoved(PCanvas c, MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) {
                c.zoomIn(e.getPoint());
            } else {
                c.zoomOut(e.getPoint());
            }
            c.repaint();
        }
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
    public void mouseReleased(PCanvas c, MouseEvent e) {
    }

    @Override
    public void mouseEntered(PCanvas c, MouseEvent e) {
    }
    
}
