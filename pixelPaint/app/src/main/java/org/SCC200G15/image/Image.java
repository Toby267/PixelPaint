package org.scc200g15.image;

import java.awt.Color;
import java.util.ArrayList;

public class Image {
    public ArrayList<Layer> Layers;
    public Layer ActiveLayer;

    private Color[][] pixels;

    private int width = 16;
    private int height = 16;

    public Image() {
        Layers = new ArrayList<>();
        Layers.add(new Layer(Color.blue, width, height));
        ActiveLayer = Layers.get(0);

        toPixels();
        System.out.println(pixels[0][0]);
    }

    private void toPixels(){
        // Create empty grids
        pixels = new Color[this.getWidth()][this.getHeight()];

        recalculatePixels(0,0,this.width, this.height);
    }
    public void recalculatePixel(int x, int y){
        recalculatePixels(x, y, 1, 1);
    }
    public void recalculatePixels(int startX, int startY, int w, int h){
        // TODO: Handle ERROR
        if(startX + w > this.width){
            throw new Error("Index Out Of Bounds");
        }
        else if(startY + h > this.height){
            throw new Error("Index Out Of Bounds");
        } 
        

        // Start on back layer and move forward
        for (int l = 0; l < this.getLayerCount(); l++) {
            Layer layer = this.getLayer(l);
            for (int x = startX; x < startX + w; x++) {
                for (int y = startY; y < startY + h; y++) {
                    //TODO: Use Opacity to calculate laying onto of other pixels
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

    public Color[][] getPixels() {
        return pixels;
    }
    public int getLayerCount(){
        return Layers.size();
    }
    public Layer getLayer(int i){
        return Layers.get(i);
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
}
