package org.scc200g15.image;

import java.awt.Color;
import java.util.ArrayList;

/**
 * The image class stores all of the relevant info about the image an image
 * including pixel and layer data
 */
public class Image {
    // The Layers
    public ArrayList<Layer> Layers;
    public Layer ActiveLayer;

    // The width and height of the image
    private int width = 16;
    private int height = 16;

    /**
     * Basic constructor that creates a 16x16 blue image
     */
    public Image() {
        Layers = new ArrayList<>();
        Layers.add(new Layer(Color.BLACK, width, height));
        Layers.add(new Layer(new Color(255,255,255,255), width, height));
        ActiveLayer = Layers.get(0);

        //Layers.get(1).setIsActive(false);
    }

    /**
     * Get the layer count of the image
     */
    public int getLayerCount() {
        return Layers.size();
    }
    /**
     * Get the layer with the given index
     * @param i the index of the layer to get
     */
    public Layer getLayer(int i) {
        return Layers.get(i);
    }

    /**
     * Get the width of the image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height
     */
    public int getHeight() {
        return height;
    }
}
