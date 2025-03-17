package org.scc200g15.action;

import java.awt.Color;
import java.awt.Point;

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
            //TODO: Handle Error
            System.out.println("The Arrays Passed Are of different lengths");
        }

        this.points = points;
        this.originalColors = originalColors;
        this.newColors = newColors;
    }

    @Override
    public void undo(Image image) {
        layer.setPixels(points, newColors);
    }

    @Override
    public void redo(Image image) {
        layer.setPixels(points, originalColors);
    }
    
}
