package org.scc200g15.image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.scc200g15.gui.GUI;

/**
 * The image class stores all of the relevant info about the image an image
 * including pixel and layer data
 */
public final class Image {
  public ArrayList<Layer> Layers;
  public Layer activeLayer;

  // The width and height of the image
  private int width = 1024;
  private int height = 1024;

  /**
   * Basic constructor that creates a 16x16 blue image
   */
  public Image() {
    Layers = new ArrayList<>(16);
    
    // Layers.add(new Layer("LAYER 1", Color.GRAY, width, height));
    // setActiveLayer(Layers.get(0), null);
    // GUI.getInstance().getLayerSelector().setLastActiveLayer(Layers.get(0));

    Layers.add(new Layer("LAYER 1", new Color(195, 127, 209, 128), width, height));
    setActiveLayer(Layers.get(0), null);
    GUI.getInstance().getLayerSelector().setLastActiveLayer(Layers.get(0));
    
    Layers.add(new Layer("LAYER 2", new Color(0, 83, 234, 128), width, height));

    Color[][] output = compressImage();
    displayColour(output[0][0]);

  }

  public int moveLayer(int index1, int index2) {
    if (index1 < 0 || index1 >= Layers.size() || index2 < 0 || index2 >= Layers.size()) {
      // TODO: Handle Error Invalid Layer ID
      return -1;
    }
    Layer tempLayer = Layers.get(index1);

    int increment = index1 < index2 ? 1 : -1;
    for (int i = index1; i != index2; i += increment) 
      Layers.set(i, Layers.get(i + increment));

    Layers.set(index2, tempLayer);
    return index2;

  }

  public int addLayer() {
    // Get the ID of the active layer
    int activeLayerID = Layers.indexOf(activeLayer);

    // Add a blank layer above the active layer
    Layers.add(activeLayerID + 1, new Layer("TEMPORARY", new Color(0, 0, 0, 0), width, height));

    // Set active layer to new layer if there is not one
    if (activeLayerID == -1)
      activeLayer = Layers.get(activeLayerID + 1);

    return activeLayerID + 1;
  }

  public int addLayer(Layer layer) {
    // Get the ID of the active layer
    int activeLayerID = Layers.indexOf(activeLayer);

    // Add a given layer above the active layer
    Layers.add(activeLayerID + 1, layer);

    // Set active layer to new layer if there is not one
    if (activeLayerID == -1)
      activeLayer = Layers.get(activeLayerID + 1);

    return activeLayerID + 1;
  }

  public int removeLayer(Layer layer) {
    return removeLayer(Layers.indexOf(layer));
  }

  public int removeLayer(int ID) {
    if (ID < 0 || ID >= Layers.size()) {
      // TODO: Handle Error Invalid Layer ID
      return -1;
    }

    Layer layer = Layers.get(ID);

    Layers.remove(layer);
    return ID;
  }
  
  /* Accessors */

  /**
   * Get the layer count of the image
   */
  public int getLayerCount() {
    return Layers.size();
  }

  public void setActiveLayer(Layer newActiveLayer, Layer oldActiveLayer) {
    activeLayer = newActiveLayer;
    newActiveLayer.activateLayer();
    if (oldActiveLayer != null) oldActiveLayer.deactivateLayer();
  }

  /**
   * Get the layer with the given index
   * 
   * @param i the index of the layer to get
   */
  public Layer getLayer(int i) {
    return Layers.get(i);
  }

  public int getLayerIndex(Layer layer) {
    return Layers.indexOf(layer);
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

  public ArrayList<Layer> getLayers() {
    return Layers;
  }

  public  Color[][] compressAllLayers(boolean skipInvisibleLayers){
    Color[][] finalImage = new Color[width][height];

    Color transparent = new Color(0,0,0,0);
    for (Color[] row: finalImage)
      Arrays.fill(row, transparent);

    for(int x = 0; x < width; x++) {
      for(int y = 0; y < height; y++) {
        ArrayList<Color> coloursToMix = new ArrayList<>();
        boolean isFirstLayer = true;

        for(Layer layer : Layers) {
          Color pixel = layer.getPixel(x, y);
          if(pixel.getAlpha() == 0) continue;
          if(!layer.getIsLayerVisible() && skipInvisibleLayers) continue;
          if(pixel.getAlpha() == 255) {
            if(isFirstLayer) finalImage[x][y] = pixel;
            else coloursToMix.add(pixel);
            break;
          }
          coloursToMix.add(pixel);
          isFirstLayer = false;
        }

        if(coloursToMix.size() != 0) {
          coloursToMix = new ArrayList<Color>(coloursToMix.reversed());
          Color outputColour = coloursToMix.get(0);
          for(int i = 1; i < coloursToMix.size(); i++) 
            outputColour = combineColours(outputColour, coloursToMix.get(i));
          finalImage[x][y] = adjustForAlpha(outputColour);
        }

      }
    }
    return finalImage;
  }
  public Color[][] compressImage() {
    return  compressAllLayers(false);
  }
  public Color[][] compressVisibleLayers() {
    return  compressAllLayers(true);
  }


  public Color combineColours(Color c1, Color c2) {
    double a1 = c1.getAlpha() / 255.0;
    double a2 = c2.getAlpha() / 255.0;
    double alpha = a1 + a2 * (1 - a1);

    int red =   (int) Math.round(((c1.getRed()   * a1) + (c2.getRed()   * a2 * (1 - a1))) / alpha);
    int green = (int) Math.round(((c1.getGreen() * a1) + (c2.getGreen() * a2 * (1 - a1))) / alpha);
    int blue =  (int) Math.round(((c1.getBlue()  * a1) + (c2.getBlue()  * a2 * (1 - a1))) / alpha);

    return new Color(red, green, blue, (int) Math.round(alpha * 255));
  }


  public Color adjustForAlpha(Color c) {
    double alpha = c.getAlpha() / 255.0;

    int red =   (int) Math.round(c.getRed()   * alpha + 255 * (1 - alpha));
    int green = (int) Math.round(c.getGreen() * alpha + 255 * (1 - alpha));
    int blue =  (int) Math.round(c.getBlue()  * alpha + 255 * (1 - alpha));

    return new Color(red, green, blue);
  }


  // TEMPORARY
  public void displayColour(Color c) {
    System.out.println("("+c.getRed()+", "+c.getGreen()+", "+c.getBlue()+", "+c.getAlpha()+") ");
  }



}