package org.scc200g15.gui.sidebar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class ColourPicker extends JComponent {
    // Hue component
    private final int RADIUS_OUTER_H = 80;
    private final int RADIUS_INNER_H = 68;

    // Saturation + Brigthness components
    private final int RADIUS_SB = 50;

    // Mouse Position
    private int mouseX = -1;
    private int mouseY = -1;
    
    // Saved Hue Mouse Position
    private int pastX_H;
    private int pastY_H;

    // Saved Saturation + Brigthness Mouse Position
    private int pastX_BS;
    private int pastY_BS;

    // Stores Saturation + Brigthness to search by pixel
    private BufferedImage IMAGE_SB;

    private final ColourPickerTools Tools = new ColourPickerTools();

    public ColourPicker() {

        // Mouse Listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }
        });

        // Mouse Listener
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

    // * ---------------------------------- [ PAINT ] ---------------------------------- * //

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Color c;

        // Colour wheel center points
        int x = getWidth() / 2;
        int y = (int) Math.round(RADIUS_OUTER_H * 1.25);

        // Draw the hue color ring
        paintPickerH(g2, x, y);

        // Draw the hue hover tool
        if(clickedH(mouseX, mouseY, x, y)) {
            c = paintHoverH(g2, mouseX, mouseY, x, y);
            pastX_H = mouseX;
            pastY_H = mouseY;
        } else 
            c = paintHoverH(g2, pastX_H, pastY_H, x, y);

        // Setup the bufferImage to use getPixel
        IMAGE_SB = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d_temp = IMAGE_SB.createGraphics();
        g2d_temp.setColor(getBackground());

        // Draw the saturation + brightness color ring
        paintPickerSB(g2d_temp, x, y, c);
        g2d_temp.dispose();
        g2.drawImage(IMAGE_SB, 0, 0, null);

        // Draw the saturation + brightness hover tool
        if(clickedBS(mouseX, mouseY, x, y)) {
            c = paintHoverSB(g2, mouseX, mouseY);
            pastX_BS = mouseX;
            pastY_BS = mouseY;
        } else 
            c = paintHoverSB(g2, pastX_BS, pastY_BS);

    }

    // * ---------------------------------- [ HUE ] ---------------------------------- * //

    // ! TODO: STOP FROM BEING REPAINTED FOR OPTIMISATION
    public void paintPickerH(Graphics2D g2, int x, int y) {
        // Draw a line for each colour over a 360° donut.
        for (int i = 0; i < 360; i++) {
            double θ = Math.toRadians(i);
            double hue = Tools.getHue(θ);
            g2.setStroke(new BasicStroke(10));
            g2.setColor(Color.getHSBColor((float) hue, 1.0f, 1.0f));
            g2.drawLine(
                x + Tools.newXint(RADIUS_OUTER_H, θ),
                y + Tools.newYint(RADIUS_OUTER_H, θ),
                x + Tools.newXint(RADIUS_INNER_H, θ),
                y + Tools.newYint(RADIUS_INNER_H, θ)
            );
        }
    }

    public Color paintHoverH(Graphics2D g2, int x, int y, int centerX, int centerY) {
        g2.setStroke(new BasicStroke(4));

        // Calculate radius of the circle and width of the ring
        int ringWidth = 5 + (RADIUS_OUTER_H - RADIUS_INNER_H) * 2;
        int radius = (RADIUS_OUTER_H + RADIUS_INNER_H) / 2;

        // Find angle (θ) to use in calcuating hue (normalise 0-1)
        double θ = Tools.getθ(y - centerY, x - centerX); 
        double hue = Tools.getHue(θ);

        // Find correct coordinates using trigonometry / unit circle
        int destinationX = centerX + Tools.newXint(radius, θ); // x = r * cos(θ) 
        int destinationY = centerX + Tools.newYint(radius, θ); // y = r * sin(θ) 

        // Create hover bubble
        Color fillColor = Color.getHSBColor((float) hue, 1.0f, 1.0f);
        g2.setColor(fillColor);
        g2.fillOval(destinationX - ringWidth / 2, destinationY - ringWidth / 2, ringWidth, ringWidth);
        g2.setColor(Color.GRAY);
        g2.drawOval(destinationX - ringWidth / 2, destinationY - ringWidth / 2, ringWidth, ringWidth);

        return fillColor;
    }


    private boolean clickedH(int x, int y, int centerX, int centerY) {
        double distance = Tools.getDistance(x - centerX, y - centerY);
        int tolerance = 13;
        if (mouseX == -1 && mouseY == -1) 
            return false;
        return distance >= RADIUS_INNER_H - tolerance && distance <= RADIUS_OUTER_H + tolerance;
    }

    // * ------------------------- [ SATURATION / BRIGHTNESS ] ------------------------- * //
    
    // Inspired by: https://stackoverflow.com/questions/64876600/circular-saturation-brightness-gradient-for-color-wheel
    public void paintPickerSB(Graphics2D g2, int x, int y, Color c) {
        g2.setColor(c);
        g2.fillOval(x - RADIUS_SB, y - RADIUS_SB, RADIUS_SB * 2, RADIUS_SB * 2);
        
        int θ_black = 75;
        int θ_white = 200;

        // Add gradients from outside, which fades as distance increases
        addGradient(g2, x, y, Math.toRadians(θ_black), new Color(0, 0, 0), false);
        addGradient(g2, x, y, Math.toRadians(θ_white), new Color(255, 255, 255), false);
        
        // Add gradients from outside opposite side, which intensifies as distance increases
        addGradient(g2, x, y, Math.toRadians(θ_black), new Color(0, 0, 0), true);
        addGradient(g2, x, y, Math.toRadians(θ_white), new Color(255, 255, 255), true);
    }

    public Color paintHoverSB(Graphics2D g2, int x, int y) {
        g2.setStroke(new BasicStroke(3));

        if (x <= 0 && y <= 0) {
            System.out.println("HAS TRIGGERED");
            x = getWidth() / 2;
            y = (int) Math.round(RADIUS_OUTER_H * 1.25);    
        }
            
        System.out.println("x = "+x);
        System.out.println("y = "+y);

        int radius = (int) Math.round(RADIUS_SB / 5f);
        int color = IMAGE_SB.getRGB(x, y);

        // Create hover bubble
        // From: https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        Color fillColor = new Color((color & 0xff0000) >> 16, (color & 0xff00) >> 8, color & 0xff);
        g2.setColor(fillColor);
        g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        g2.setColor(Color.GRAY);
        g2.drawOval(x - radius, y - radius, radius * 2, radius * 2);

        return fillColor;
    }

    private boolean clickedBS(int x, int y, int centerX, int centerY) {
        double distance = Tools.getDistance(x - centerX, y - centerY);
        int tolerance = 5;
        if (mouseX == -1 && mouseY == -1) 
            return false;
        return distance <= RADIUS_SB - tolerance;
    }



    private RadialGradientPaint createInverseGradient(int x, int y, double θ, Color c) {
        return new RadialGradientPaint(
            x - Tools.newX(RADIUS_SB, θ) * 2.5f,
            y - Tools.newY(RADIUS_SB, θ) * 2.5f,
            RADIUS_SB * 3.5f,
            Tools.inverseGradientSteps,
            Tools.inverseGradientColors(c)
        );
    }

    private RadialGradientPaint createGradient(int x, int y, double θ, Color c) {
        return new RadialGradientPaint(
            x + Tools.newX(RADIUS_SB, θ) * 2.f,
            y + Tools.newY(RADIUS_SB, θ) * 2.f,
            RADIUS_SB * 2.6f,
            Tools.gradientSteps,
            Tools.gradientColors(c)
        );
    }

    private void addGradient(Graphics2D g2, int x, int y, double θ, Color c, boolean isInverted) {
        RadialGradientPaint gradient = isInverted ? createInverseGradient(x, y, θ, c) : createGradient(x, y, θ, c);
        g2.setPaint(gradient);
        g2.fillOval(x - RADIUS_SB, y - RADIUS_SB, RADIUS_SB * 2, RADIUS_SB * 2);
    }
    
}