package org.scc200g15.gui.sidebar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
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

    // Colour wheel center points
    private int x;
    private int y;
    
    // Current selected colour
    private Color currentColor;

    // Mouse Position
    private int mouseX = -1;
    private int mouseY = -1;
    
    // Saved Hue mouse position
    private int pastX_H;
    private int pastY_H;

    // Saved Saturation + Brigthness mouse position
    private int pastX_BS;
    private int pastY_BS;

    // Stores Saturation + Brigthness to search by pixel
    private BufferedImage IMAGE_SB;

    private RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
    );

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


    public void setColor(Color c) {
        setH(c);
        paintImmediately(0, 0, getWidth(), getHeight());
        setSB(c);  
        paintImmediately(0, 0, getWidth(), getHeight());
    }

    public void setH(Color c) {
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();

        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));

        double hue = calculateHue(red, green, blue, max, min);
        int radius = (RADIUS_OUTER_H + RADIUS_INNER_H) / 2;

        this.mouseX = this.x + (int) Math.round(radius * Math.cos(Tools.getθ(hue))); 
        this.mouseY = this.y + (int) Math.round(radius * Math.sin(Tools.getθ(hue)));

        repaint();
    }

    public void setSB(Color c) {
        Color tempC = null;
        double minDifference = colorDifference(c, getPixelColor(0, 0));
        for(int x_i = 0; x_i < this.IMAGE_SB.getWidth(); x_i++) {
            for (int y_i = 0; y_i < this.IMAGE_SB.getHeight(); y_i++) {
                double difference = colorDifference(c, getPixelColor(x_i, y_i));
                if(difference < minDifference) {
                    tempC = getPixelColor(x_i, y_i);
                    minDifference = difference;
                    this.mouseX = x_i;
                    this.mouseY = y_i;
                }
            }
        }
        System.out.println("Calculated: (" + tempC.getRed() + ", " + tempC.getGreen() + ", " + tempC.getBlue() + ")");
        repaint();
    }



    public double colorDifference(Color target, Color attempt) {
        int r = target.getRed() - attempt.getRed();
        int g = target.getGreen() - attempt.getGreen();
        int b = target.getBlue() - attempt.getBlue();
        return Math.pow(r, 2) + Math.pow(g, 2) + Math.pow(b, 2);
    }

    public double calculateHue(int red, int green, int blue, int max, int min) {
        // Taken from: https://cs.stackexchange.com/questions/64549/convert-hsv-to-rgb-colors
        double hue = 60;
        if (max != min) {
            if (max == red) hue *= (green - blue) / (double) (max - min);
            else if (max == green) hue *= 2 + (blue - red) / (double) (max - min);
            else hue *= 4 + (red - green) / (double) (max - min);
            if (hue < 0) hue += 360;
        }
        hue /= 360.0;
        return hue % 1.0;
    }

    // * ---------------------------------- [ PAINT ] ---------------------------------- * //

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setRenderingHints(rh);

        // Colour wheel center points
        x = getWidth() / 2;
        y = (int) Math.round(RADIUS_OUTER_H * 1.25);

        // Draw the hue color ring
        paintPickerH(g2, x, y);

        // Draw the hue hover tool
        if(clickedH(this.mouseX, this.mouseY)) {
            this.currentColor = paintHoverH(g2, this.mouseX, this.mouseY);
            this.pastX_H = this.mouseX;
            this.pastY_H = this.mouseY;
        } else 
            this.currentColor = paintHoverH(g2, this.pastX_H, this.pastY_H);

        // Draw the saturation + brightness color ring
        this.IMAGE_SB = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d_temp = this.IMAGE_SB.createGraphics();
        g2d_temp.setRenderingHints(rh);
        g2d_temp.setColor(getBackground());

        paintPickerSB(g2d_temp, x, y, this.currentColor);

        g2d_temp.dispose();
        g2.drawImage(this.IMAGE_SB, 0, 0, null);

        // Draw the saturation + brightness hover tool
        if(clickedBS()) {
            this.currentColor = paintHoverSB(g2, this.mouseX, this.mouseY);
            this.pastX_BS = this.mouseX;
            this.pastY_BS = this.mouseY;
        } else 
            this.currentColor = paintHoverSB(g2, this.pastX_BS, this.pastY_BS);

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

    public Color paintHoverH(Graphics2D g2, int mouseX, int mouseY) {
        g2.setStroke(new BasicStroke(4));

        // Calculate radius of the circle and width of the ring
        int ringWidth = 5 + (RADIUS_OUTER_H - RADIUS_INNER_H) * 2;
        int radius = (RADIUS_OUTER_H + RADIUS_INNER_H) / 2;

        // Find angle (θ) to use in calcuating hue (normalise 0-1)
        double θ = Tools.getθ(mouseY - y, mouseX - x); 
        double hue = Tools.getHue(θ);

        // Find correct coordinates using trigonometry / unit circle
        int destinationX = y + Tools.newXint(radius, θ); // x = r * cos(θ) 
        int destinationY = y + Tools.newYint(radius, θ); // y = r * sin(θ) 

        // Create hover bubble
        Color fillColor = Color.getHSBColor((float) hue, 1.0f, 1.0f);
        g2.setColor(fillColor);
        g2.fillOval(destinationX - ringWidth / 2, destinationY - ringWidth / 2, ringWidth, ringWidth);
        g2.setColor(Color.WHITE);
        g2.drawOval(destinationX - ringWidth / 2, destinationY - ringWidth / 2, ringWidth, ringWidth);

        return fillColor;
    }


    private boolean clickedH(int mouseX, int mouseY) {
        double distance = Tools.getDistance(mouseX - x, mouseY - y);
        int tolerance = 9;
        if (this.mouseX == -1 && this.mouseY == -1) 
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

        // ! TODO: MAYBE LIMIT THE FILL COLOR OR MAKE IT A GRADIENT AS WELL
        // if doesn't work add grey gradient

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
            x = getWidth() / 2;
            y = (int) Math.round(RADIUS_OUTER_H * 1.25);    
        }
            
        int radius = (int) Math.round(RADIUS_SB / 5f);
        Color fillColor = getPixelColor(x, y);
        g2.setColor(fillColor);
        g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        g2.setColor(Color.WHITE);
        g2.drawOval(x - radius, y - radius, radius * 2, radius * 2);

        return fillColor;
    }

    private boolean clickedBS() {
        double distance = Tools.getDistance(mouseX - x, mouseY - y);
        int tolerance = -1;
        if (this.mouseX == -1 && this.mouseY == -1) 
            return false;
        return distance <= RADIUS_SB + tolerance;
    }


    private RadialGradientPaint createInverseGradient(int x, int y, double θ, Color c) {
        return new RadialGradientPaint(
            x - Tools.newX(RADIUS_SB, θ) * 2.45f,
            y - Tools.newY(RADIUS_SB, θ) * 2.45f,
            RADIUS_SB * 3.4f,
            Tools.inverseGradientSteps,
            Tools.inverseGradientColors(c)
        );
    }

    private RadialGradientPaint createGradient(int x, int y, double θ, Color c) {
        return new RadialGradientPaint(
            x + Tools.newX(RADIUS_SB, θ) * 2.f,
            y + Tools.newY(RADIUS_SB, θ) * 2.f,
            RADIUS_SB * 2.53f,
            Tools.gradientSteps,
            Tools.gradientColors(c)
        );
    }

    private void addGradient(Graphics2D g2, int x, int y, double θ, Color c, boolean isInverted) {
        RadialGradientPaint gradient = isInverted ? createInverseGradient(x, y, θ, c) : createGradient(x, y, θ, c);
        g2.setPaint(gradient);
        g2.fillOval(x - RADIUS_SB, y - RADIUS_SB, RADIUS_SB * 2, RADIUS_SB * 2);
    }

    // From: https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
    private Color getPixelColor(int x, int y) {
        int color = this.IMAGE_SB.getRGB(x, y);
        return new Color((color & 0xff0000) >> 16, (color & 0xff00) >> 8, color & 0xff);
    }

    // * ------------------------- [ ACCESSOR / MUTATORS ] ------------------------- * //

    public Color getActiveColor() {
        return this.currentColor;
    }

}