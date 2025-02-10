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

    // Pre computed grid of pixels to optimize rendering
    private Color[][] pixels;

    // The width and height of the image
    private int width = 16;
    private int height = 16;

    /**
     * Basic constructor that creates a 16x16 blue image
     */
    public Image() {
        Layers = new ArrayList<>();
        Layers.add(new Layer(Color.blue, width, height));
        ActiveLayer = Layers.get(0);

        toPixels();
    }

    /**
     * Recompute the display values of every pixel in the image
     */
    private void toPixels() {
        // Create empty grids
        pixels = new Color[this.getWidth()][this.getHeight()];

        recalculatePixels(0, 0, this.width, this.height);
    }

    /**
     * Recalculate the given pixels display values
     * 
     * @param x X position of the pixel
     * @param y Y position of the pixel
     */
    public void recalculatePixel(int x, int y) {
        recalculatePixels(x, y, 1, 1);
    }

    /**
     * Recalculate the given areas display values
     * 
     * @param startX X position of the top left pixel
     * @param startY Y position of the top left pixel
     * @param w      The width of the area
     * @param h      The height of the area
     */
    public void recalculatePixels(int startX, int startY, int w, int h) {
        // TODO: Handle ERROR
        if (startX + w > this.width) {
            throw new Error("Index Out Of Bounds");
        } else if (startY + h > this.height) {
            throw new Error("Index Out Of Bounds");
        }

        // Start on back layer and move forward
        for (int l = 0; l < this.getLayerCount(); l++) {
            Layer layer = this.getLayer(l);
            for (int x = startX; x < startX + w; x++) {
                for (int y = startY; y < startY + h; y++) {
                    // TODO: Use Opacity to calculate laying onto of other pixels
                    Color c = layer.getPixel(x, y);
                    if (c != null && c.getAlpha() == 0) {
                        pixels[x][y] = new Color(0, 0, 0, 0);
                    } else {
                        pixels[x][y] = c;
                    }
                }
            }
        }
    }

    /**
     * Get the value of color value of all the display pixels
     */
    public Color[][] getPixels() {
        return pixels;
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
