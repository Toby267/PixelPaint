package org.scc200g15.image;

import java.awt.Color;

/**
 * A layer of an image, stores a grid of pixels
 * TODO: allow a layer to be deactivated
 * TODO: stores a queue of all actions so they can be reverted
 */
public class Layer {
    Color[][] pixels;
    boolean isActive = true;

    /**
     * Basic constructor that creates a layer with a given size that is one color
     * @param c the color that the layer should be
     * @param w the width of the layer
     * @param h the height of the layer
     */
    public Layer(Color c, int w, int h){
        pixels = new Color[w][h];

        for(int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){
                pixels[x][y] = c;
            }
        }
    }

    /**
     * Get the color of a given pixel
     * @param x xPos of the pixel
     * @param y yPos of the pixel
     */
    public Color getPixel(int x, int y){
        return pixels[x][y];
    }
    /*
     */
    public Color[][] getPixels(){
        return pixels;
    }
    public Boolean getIsActive(){
        return isActive;
    }
    public void setIsActive(Boolean isActive){
       this.isActive = isActive;
    }
}
