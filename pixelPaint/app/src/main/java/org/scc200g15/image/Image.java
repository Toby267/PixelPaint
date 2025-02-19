package org.scc200g15.image;

import java.awt.Color;
import java.util.ArrayList;

import org.scc200g15.gui.GUI;

/**
 * The image class stores all of the relevant info about the image an image
 * including pixel and layer data
 */
public final class Image {
  public ArrayList<Layer> Layers;
  public Layer activeLayer;

  // The width and height of the image
  private int width = 16;
  private int height = 16;

  /**
   * Basic constructor that creates a 16x16 blue image
   */
  public Image() {
    Layers = new ArrayList<>(16);
    
    Layers.add(new Layer("LAYER 1", Color.GRAY, width, height));
    setActiveLayer(Layers.get(0), null);
    GUI.getInstance().getLayerSelector().setLastActiveLayer(Layers.get(0));
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

    // Cannot remove active layer
    if (activeLayer == layer) {
      // TODO: Handle Error Invalid Layer ID
      return -1;
    }

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

}