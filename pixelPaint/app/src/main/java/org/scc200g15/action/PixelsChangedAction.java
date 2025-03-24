package org.scc200g15.action;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import org.scc200g15.gui.GUI;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;

public class PixelsChangedAction implements Action{

    Layer layer;

    Point[] points;
    Color[] originalColors;
    Color[] newColors;

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

    public PixelsChangedAction(Layer layer, Point[] points, Color[] originalColors, Color newColor) {
        this.layer = layer;

        if(points.length != originalColors.length){
            System.out.println("The Arrays Passed Are of different lengths");
            return;
        }

        this.points = points;
        this.originalColors = originalColors;
        this.newColors = new Color[points.length];
        Arrays.fill(this.newColors, newColor);
    }

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

    @Override
    public void undo(Image image) {
        layer.setPixels(points, originalColors);
        for(Point p : points){
            GUI.getInstance().getCanvas().recalculatePixel(p.x, p.y);
        }
        GUI.getInstance().getCanvas().repaint();
    }

    @Override
    public void redo(Image image) {
        layer.setPixels(points, newColors);

        for(Point p : points){
            GUI.getInstance().getCanvas().recalculatePixel(p.x, p.y);
        }
        GUI.getInstance().getCanvas().repaint();
    }
    
}
