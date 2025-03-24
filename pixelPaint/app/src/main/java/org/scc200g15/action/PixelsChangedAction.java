package org.scc200g15.action;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import org.scc200g15.gui.GUI;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;

/**
 * PixelsChangedAction - Implementation of action for Pixel Changes
 */
public class PixelsChangedAction implements Action{

    Layer layer; // The Layer the pixels where changed on

    // Arrays to store the old and new data
    Point[] points;
    Color[] originalColors;
    Color[] newColors;

    /**
     * Constructor for PixelsChangedAction
     * @param layer the layer the change happened on
     * @param points array contain all of the points that where changed
     * @param originalColors array containing the original color of each point
     * @param newColors array containing the new color of each point
     */
    public PixelsChangedAction(Layer layer, Point[] points, Color[] originalColors, Color[] newColors) {
        this.layer = layer;

        if(points.length != originalColors.length || points.length != newColors.length){
            System.out.println("The Arrays Passed Are of different lengths");
            return;
        }

        this.points = points;
        this.originalColors = originalColors;
        this.newColors = newColors;
    }

    /**
     * Constructor for PixelsChangedAction
     * @param layer the layer the change happened on
     * @param points array contain all of the points that where changed
     * @param originalColors array containing the original color of each point
     * @param newColor the new color for all of the pixels that where changed
     */
    public PixelsChangedAction(Layer layer, Point[] points, Color[] originalColors, Color newColor) {
        this(layer, points, originalColors, new Color[points.length]);
        Arrays.fill(newColors, newColor);
    }

    /**
     * Constructor for PixelsChangedAction
     * @param layer the layer the change happened on
     * @param points list contain all of the points that where changed
     * @param originalColors list containing the original color of each point
     * @param newColor the new color for all of the pixels that where changed
     */
    public PixelsChangedAction(Layer layer, ArrayList<Point> points, ArrayList<Color> originalColors, Color newColor) {
        this.layer = layer;

        if(points.size() != originalColors.size()){
            System.out.println("The Arrays Passed Are of different lengths");
            return;
        }

        this.points = points.toArray(Point[]::new);
        this.originalColors = originalColors.toArray(Color[]::new);

        this.newColors = new Color[points.size()];
        Arrays.fill(this.newColors, newColor);
    }

    /**
     * undo - replaces all of the values with the original values
     */
    @Override
    public void undo(Image image) {
        // Set the values of the pixels
        layer.setPixels(points, originalColors);

        // Recalculate all of the rendered pixels
        for(Point p : points){
            GUI.getInstance().getCanvas().recalculatePixel(p.x, p.y);
        }

        // Repaint canvas
        GUI.getInstance().getCanvas().repaint();
    }

    /**
     * redo - replaces all of the values with the new values
     */
    @Override
    public void redo(Image image) {
        // Set the values of the pixels
        layer.setPixels(points, newColors);

        // Recalculate all of the rendered pixels
        for(Point p : points){
            GUI.getInstance().getCanvas().recalculatePixel(p.x, p.y);
        }

        // Repaint canvas
        GUI.getInstance().getCanvas().repaint();
    }
    
}
