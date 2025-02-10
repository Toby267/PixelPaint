package org.scc200g15.image;

import java.awt.Color;

public class Layer {
    Color[][] pixels;

    public Layer(Color c, int w, int h){
        pixels = new Color[w][h];


        for(int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){
                pixels[x][y] = c;
            }
        }
    }

    public Color getPixel(int x, int y){
        return pixels[x][y];
    }
}
