package org.scc200g15.gui.sidebar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;


public class ColourPicker extends JComponent {
    // Hue component
    private final int RADIUS_OUTER_H = 80;
    private final int RADIUS_INNER_H = 70;

    // Saturation + Brigthness components
    private final int RADIUS_SB = 50;

    private float hue = (float) 0;
    private float saturation = (float) 1;
    private float brightness = (float) 1;

    public ColourPicker() {

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

        });    
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int x = getWidth() / 2;
        int y = (int) Math.round(RADIUS_OUTER_H * 1.25);
        paintHPicker(g2, x, y);
        paintSBPicker(g2, x, y);
    }

    public void paintHPicker(Graphics2D g2, int x, int y) {
        // Draw a line for each colour over a 360° donut.
        for (int i = 0; i < 360; i++) {
            double θ = Math.toRadians(i);
            float hue = i / (float) 360;
            g2.setStroke(new BasicStroke(10));
            g2.setColor(Color.getHSBColor(hue, (float) 1, (float) 1));
            g2.drawLine(
                (int) Math.round(x + RADIUS_OUTER_H * Math.cos(θ)), // x1 = r1 * cos(θ)
                (int) Math.round(y + RADIUS_OUTER_H * Math.sin(θ)), // y1 = r1 * sin(θ)
                (int) Math.round(x + RADIUS_INNER_H * Math.cos(θ)), // x2 = r2 * cos(θ)
                (int) Math.round(y + RADIUS_INNER_H * Math.sin(θ))  // y2 = r2 * sin(θ)
            );
        }
    }

    public void paintSBPicker(Graphics2D g2, int x, int y) {
        // Inspired by: https://stackoverflow.com/questions/64876600/circular-saturation-brightness-gradient-for-color-wheel
        g2.setColor(Color.RED);
        g2.fillOval(x - RADIUS_SB, y - RADIUS_SB, RADIUS_SB * 2, RADIUS_SB * 2);
        
        RadialGradientPaint gradient_black = new RadialGradientPaint(
            x + RADIUS_SB * (float) Math.cos(Math.toRadians(60)),
            y + RADIUS_SB * (float) Math.sin(Math.toRadians(60)), 
            (int) Math.round(RADIUS_SB * 1.75),
            new float[] {0, 1},
            new Color[] {Color.BLACK, new Color(0, 0, 0, 0)}
        );
        g2.setPaint(gradient_black);
        g2.fillOval(x - RADIUS_SB, y - RADIUS_SB, RADIUS_SB * 2, RADIUS_SB * 2);

        RadialGradientPaint gradient_white = new RadialGradientPaint(
            x + RADIUS_SB * (float) Math.cos(Math.toRadians(120)),
            y + RADIUS_SB * (float) Math.sin(Math.toRadians(120)), 
            (int) Math.round(RADIUS_SB * 1.75),
            new float[] {0, 1},
            new Color[] {Color.WHITE, new Color(0, 0, 0, 0)}
        );
        g2.setPaint(gradient_white);
        g2.fillOval(x - RADIUS_SB, y - RADIUS_SB, RADIUS_SB * 2, RADIUS_SB * 2);

    }
    
    
}
