package org.scc200g15.gui.sidebar;

import java.awt.Color;

public class ColourPickerTools {

    // * --------------------- [Gradient Setup] --------------------- * //

    // Current accuracy rate: 95.01%
    public float[] gradientSteps = {0.0f, 0.64f, 0.77f, 1.0f};
    public float[] gradientStepsColour = {0.0f, 0.62f, 0.8f, 1.0f};
    public Color[] gradientColors(Color c) {
        return new Color[] {
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 255), 
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 200), 
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 100), 
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 0), 
        };
    }
    
    public float[] inverseGradientSteps = {0.0f, 0.8f, 0.81f, 0.85f, 0.9f, 0.95f, 1.0f};
    public Color[] inverseGradientColors(Color c) {
        return new Color[] {
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 0), 
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 0), 
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 3), 
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 5), 
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 10), 
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 70), 
            new Color(c.getRed(), c.getGreen(), c.getBlue(), 255),
        };
    }

    // * --------------------- [Trig / Unit Circle] --------------------- * //

    public double getθ(int a, int b) {
        return Math.atan2(a, b);
    }

    public double getθ(double hue) {
        return 2 * hue * Math.PI;
    }

    public float newX(int radius, double θ) { 
        return radius * (float) (Math.cos(θ)); // x = r * cos(θ)
    }
    public int newXint(int radius, double θ) {
        return (int) Math.round(newX(radius, θ));
    }

    public float newY(int radius, double θ) {
        return radius * (float) (Math.sin(θ)); // y = r * cos(θ)
    }
    public int newYint(int radius, double θ) {
        return (int) Math.round(newY(radius, θ));
    }

    // * --------------------- [General Helper Functions] --------------------- * //
    
    public double getDistance(double o, double a) {
        return Math.sqrt(Math.pow(o, 2) + Math.pow(a, 2)); // √(a^2 + b^2)
    }
    
    public double getHue(double θ) {
        return (Math.toDegrees(θ) % 360) / 360.0f;
    }
    
    // * --------------------- [Colour Tools] --------------------- * //
     
    public double colorDifference(Color target, Color attempt) {
        int r = target.getRed() - attempt.getRed();
        int g = target.getGreen() - attempt.getGreen();
        int b = target.getBlue() - attempt.getBlue();
        return Math.pow(r, 2) + Math.pow(g, 2) + Math.pow(b, 2);
    }
    
}


