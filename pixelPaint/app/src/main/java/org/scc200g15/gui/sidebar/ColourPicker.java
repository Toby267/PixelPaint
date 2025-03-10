package org.scc200g15.gui.sidebar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class ColourPicker extends JComponent {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int radius = 70;
        int width = 15;
        int x = getWidth() / 2;
        int y = (int) Math.round(radius * 1.25);
        paintOuterPicker(g2, x, y, radius, width);

    }

    public void paintOuterPicker(Graphics2D g2, int x, int y, int r1, int width) {
        int r2 = r1 - width;
        // Draw a line for each colour over a 360° donut.
        for (int i = 0; i < 360; i++) {
            double θ = Math.toRadians(i);
            g2.setStroke(new BasicStroke(10));
            g2.setColor(Color.getHSBColor(i / (float) 360, (float) 1, (float) 1));
            g2.drawLine(
                (int) Math.round(x + r1 * Math.cos(θ)), // x1 = r1 * cos(θ)
                (int) Math.round(y + r1 * Math.sin(θ)), // y1 = r1 * sin(θ)
                (int) Math.round(x + r2 * Math.cos(θ)), // x2 = r2 * cos(θ)
                (int) Math.round(y + r2 * Math.sin(θ))  // y2 = r2 * sin(θ)
            );
        }
    }



    
    
}
