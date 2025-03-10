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
    private final int RADIUS_INNER_H = 68;

    // Saturation + Brigthness components
    private final int RADIUS_SB = 49;

    private float hue = (float) 0;
    private float saturation = (float) 1;
    private float brightness = (float) 1;


    private int mouseX = -1;
    private int mouseY = -1;

    private int pastMouseX;
    private int pastMouseY;

    public ColourPicker() {

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("PRESSED (" + e.getX() + "," + e.getY() + ")");
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }
        });    
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Color c;

        int x = getWidth() / 2;
        int y = (int) Math.round(RADIUS_OUTER_H * 1.25);

        paintHPicker(g2, x, y);

        if (outerRingClicked(mouseX, mouseY, x, y)) {
            c = paintHHover(g2, mouseX, mouseY, x, y);
            pastMouseX = mouseX;
            pastMouseY = mouseY;
        } else {
            c = paintHHover(g2, pastMouseX, pastMouseY, x, y);
        }

        paintSBPicker(g2, x, y, c);


    }

    // ! TODO: STOP FROM BEING REPAINTED FOR OPTIMISATION
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

    public Color paintHHover(Graphics2D g2, int x, int y, int centerX, int centerY) {
        g2.setStroke(new BasicStroke(5));

        int ringWidth = 5 + (RADIUS_OUTER_H - RADIUS_INNER_H) * 2;
        int radius = (RADIUS_OUTER_H + RADIUS_INNER_H) / 2;

        double θ =  Math.atan2(y - centerY, x - centerX); 
        double hue = (Math.toDegrees(θ) % 360) / (float) 360;

        int destinationX = (int) Math.round(centerX + radius * Math.cos(θ));
        int destinationY = (int) Math.round(centerY + radius * Math.sin(θ));

        Color fillColor = Color.getHSBColor((float) hue, (float) 1, (float) 1);
        g2.setColor(fillColor);
        g2.fillOval(destinationX - ringWidth / 2, destinationY - ringWidth / 2, ringWidth, ringWidth);

        g2.setColor(Color.GRAY);
        g2.drawOval(destinationX - ringWidth / 2, destinationY - ringWidth / 2, ringWidth, ringWidth);

        return fillColor;
    }


    private boolean outerRingClicked(int x, int y, int centerX, int centerY) {
        if (mouseX == -1 && mouseY == -1) 
            return false;

        int o = x - centerX;
        int a = y - centerY;
        double h = Math.sqrt(Math.pow(o, 2) + Math.pow(a, 2));
            
        int clickTolerance = 13;

        if(h >= RADIUS_INNER_H - clickTolerance && h <= RADIUS_OUTER_H + clickTolerance)
            return true;

        return false;
    }
    

    public void paintSBPicker(Graphics2D g2, int x, int y, Color c) {
        // Inspired by: https://stackoverflow.com/questions/64876600/circular-saturation-brightness-gradient-for-color-wheel
        g2.setColor(c);
        g2.fillOval(x - RADIUS_SB, y - RADIUS_SB, RADIUS_SB * 2, RADIUS_SB * 2);
        
        int θ_black = 75;
        int θ_white = 200;

        addGradient(g2, x, y, Math.toRadians(θ_black), new Color(0, 0, 0), false);
        addGradient(g2, x, y, Math.toRadians(θ_white), new Color(255, 255, 255), false);
        
        addGradient(g2, x, y, Math.toRadians(θ_black), new Color(0, 0, 0), true);
        addGradient(g2, x, y, Math.toRadians(θ_white), new Color(255, 255, 255), true);
    }


    private RadialGradientPaint createInverseGradient(int x, int y, double θ, Color c) {
        return new RadialGradientPaint(
            x - RADIUS_SB * (float) (Math.cos(θ) * 2.5), 
            y - RADIUS_SB * (float) (Math.sin(θ) * 2.5), 
            (int) Math.round(RADIUS_SB * 3.45),
            new float[] {0.0f, 0.8f, 0.85f, 0.9f, 0.95f, 1.0f},
            new Color[] {
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 0), 
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 3), 
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 5), 
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 10), 
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 70), 
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 255), 
            }
        );
    }

    private RadialGradientPaint createGradient(int x, int y, double θ, Color c) {
        return new RadialGradientPaint(
            x + RADIUS_SB * (float) Math.cos(θ) * 2,
            y + RADIUS_SB * (float) Math.sin(θ) * 2, 
            (int) Math.round(RADIUS_SB * 2.6),
            new float[] {0.0f, 0.5f, 1.0f},
            new Color[] {
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 255), 
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 150), 
                new Color(c.getRed(), c.getGreen(), c.getBlue(), 0), 
            }
        );
    }

    private void addGradient(Graphics2D g2, int x, int y, double θ, Color c, boolean isInverted) {
        RadialGradientPaint gradient = isInverted ? createInverseGradient(x, y, θ, c) : createGradient(x, y, θ, c);
        g2.setPaint(gradient);
        g2.fillOval(x - RADIUS_SB, y - RADIUS_SB, RADIUS_SB * 2, RADIUS_SB * 2);
    }
    
    
}
