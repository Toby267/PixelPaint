package org.scc200g15.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

import org.scc200g15.action.Action;
import org.scc200g15.gui.GUI;

/**
 * The image class stores all of the relevant info about the image an image
 * including pixel and layer data
 */
public final class Image {
  public ArrayList<Layer> Layers;

  public Layer activeLayer;
  public ArrayList<Layer> selectedLayers;

  ArrayDeque<Action> actionHistory = new ArrayDeque<>(20);
  ArrayDeque<Action> undoHistory = new ArrayDeque<>(20);

  // The width and height of the image
  private int width = 32;
  private int height = 32;

  /**
   * Basic constructor that creates a 16x16 blue image
   */
  public Image() {
    Layers = new ArrayList<>(16);

    // Selected layers to be merged
    selectedLayers = new ArrayList<>(16);

    Layers.add(new Layer("LAYER 1", new Color(195, 127, 209, 128), width, height));
    setActiveLayer(Layers.getFirst(), null);
    GUI.getInstance().getLayerSelector().setLastActiveLayer(Layers.getFirst());
    
    Layers.add(new Layer("LAYER 2", new Color(0, 83, 234, 128), width, height));
  }

  public Image(BufferedImage bufferedImage) {
    Layers = new ArrayList<>(16);

    Color[][] pixels = new Color[bufferedImage.getWidth()][bufferedImage.getHeight()];

    for(int i = 0; i < bufferedImage.getWidth(); i++){
      for(int j = 0; j < bufferedImage.getHeight(); j++){
        pixels[i][j] = new Color(bufferedImage.getRGB(i, j));
      }
    }

    this.width = bufferedImage.getWidth();
    this.height = bufferedImage.getHeight();

    Layers.add(new Layer("LAYER 1", pixels));
    setActiveLayer(Layers.getFirst(), null);
    GUI.getInstance().getLayerSelector().setLastActiveLayer(Layers.getFirst());
}

  public int moveLayer(int index1, int index2) {
    if (index1 < 0 || index1 >= Layers.size() || index2 < 0 || index2 >= Layers.size()) {
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

  private Color[][] compressAllLayers(ArrayList<Layer> layersToCompress, boolean skipInvisibleLayers, boolean adjustAlpha, int startX, int startY, int w, int h){
    Color[][] finalImage = new Color[w][h];

    Color transparent = new Color(0,0,0,0);
    for (Color[] row: finalImage)
      Arrays.fill(row, transparent);

    for(int x = startX; x < startX + w; x++) {
      for(int y = startY; y < startY + h; y++) {
        ArrayList<Color> colorsToMix = new ArrayList<>();
        boolean isFirstLayer = true;

        for(Layer layer : layersToCompress) {
          Color pixel = layer.getPixel(x, y);
          if(pixel.getAlpha() == 0) continue;
          if(!layer.getIsLayerVisible() && skipInvisibleLayers) continue;
          if(pixel.getAlpha() == 255) {
            if(isFirstLayer) finalImage[x - startX][y - startY] = pixel;
            else colorsToMix.add(pixel);
            break;
          }
          colorsToMix.add(pixel);
          isFirstLayer = false;
        }

        if(!colorsToMix.isEmpty()) {
          colorsToMix = new ArrayList<>(colorsToMix.reversed());
          Color outputColour = colorsToMix.get(0);
          for(int i = 1; i < colorsToMix.size(); i++) 
            outputColour = combineColors(outputColour, colorsToMix.get(i));

          if(adjustAlpha) finalImage[x - startX][y - startY] = adjustForAlpha(outputColour);
          else finalImage[x - startX][y - startY] = outputColour;
        }

      }
    }
    return finalImage;
  }
  public Color[][] compressImage() {
    return compressAllLayers(Layers, false, true, 0, 0, width, height);
  }
  public Color[][] compressVisibleLayers() {
    return compressAllLayers(Layers, true, true, 0, 0, width, height);
  }
  public Color[][] compressSelectedLayers(ArrayList<Layer> layers) {
    return compressAllLayers(layers, false, false, 0, 0, width, height);
  }
  public Color[][] compressVisiblePixels(int x, int y, int w, int h) {
    return compressAllLayers(Layers, true, true, x, y, w, h);
  }
  public Color compressVisiblePixel(int x, int y) {
    return compressAllLayers(Layers, true, true, x, y, 1, 1)[0][0];
  }

  public BufferedImage calculateImageBuffer() {
    BufferedImage imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    updateImageBuffer(imageBuffer, 0, 0, width, height);

    return imageBuffer;
  }
  public BufferedImage updateImageBuffer(BufferedImage imageBuffer, int startX, int startY, int w, int h) {
    Color[][] pixelData = compressVisiblePixels(startX, startY, w, h);

    for(int x = startX; x < startX + w; x++){
      for(int y = startY; y < startY + h; y++){
        imageBuffer.setRGB(x,y, pixelData[x - startX][y - startY].getRGB());
      }
    }

    return imageBuffer;
  }

  public Color combineColors(Color c1, Color c2) {
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

  public Layer getActiveLayer() {
    return activeLayer;
  }

  public void addSelectedLayer(Layer layer) {
    selectedLayers.add(layer);
  }
  
  public void removeSelectedLayer(Layer layer) {
    selectedLayers.remove(layer);
  }

  public void disableSelectedLayers() {
    for(Layer layer : selectedLayers) layer.switchSelectedLayerState();
    selectedLayers = new ArrayList<>(16); // Effectively removes all elements
  }

  public void mergeSelectedLayers() {
    // Reorder the layers so they're compressed in the correct order
    ArrayList<Layer> orderedLayers = new ArrayList<>();
    for(Layer layer : Layers)
      for(Layer selectedLayer : selectedLayers)
        if (selectedLayer == layer) orderedLayers.add(layer);

    Color[][] output = compressSelectedLayers(orderedLayers);

    Layer compressedLayer = orderedLayers.get(0);
    compressedLayer.setPixels(output);
    compressedLayer.activateLayer();
    compressedLayer.switchSelectedLayerState();
    
    for(int i = 1; i < orderedLayers.size(); i++)
      GUI.getInstance().getLayerSelector().removeLayerWithoutWarning(orderedLayers.get(i));
    
    selectedLayers = new ArrayList<>(16); // Effectively removes all elements
  }

  public void addAction(Action action){
    if(actionHistory.size() >= 20){
      actionHistory.removeFirst();
    }
    actionHistory.add(action);
  }
  public void undoAction(){
    if(actionHistory.isEmpty()) return;

    Action a = actionHistory.removeLast();
    a.undo(this);

    if(undoHistory.size() >= 20){
      undoHistory.removeLast();
    }
    undoHistory.addFirst(a);
  }

  public void redoAction(){
    if(undoHistory.isEmpty()) return;

    Action a = undoHistory.removeFirst();
    a.redo(this);

    addAction(a);
  }
}