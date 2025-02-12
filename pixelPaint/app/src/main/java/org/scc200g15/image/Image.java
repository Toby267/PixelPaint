package org.scc200g15.image;

import java.awt.Color;
import java.util.ArrayList;

/**
 * The image class stores all of the relevant info about the image an image including pixel and layer data
 */
public class Image {
  // The Layers
  public ArrayList<Layer> Layers;
  public Layer activeLayer;

  // The width and height of the image
  private int width = 16;
  private int height = 16;

  /**
   * Basic constructor that creates a 16x16 blue image
   */
  public Image() {
    Layers = new ArrayList<>();
    Layers.add(new Layer(Color.BLACK, width, height));
    activeLayer = Layers.get(0);

    // addLayer(new Layer(Color.BLUE, width, height));
    // addLayer();
    // moveLayerUp(0);
    // moveLayer(0, 1);
  }

  /**
   * Get the layer count of the image
   */
  public int getLayerCount() {
    return Layers.size();
  }

  /**
   * Get the layer with the given index
   * 
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

  public int moveLayerUp(int ID){
    if(ID < 0 || ID >= Layers.size()){
      //TODO: Handle Error Invalid Layer ID
      return -1;
    }

    if(ID + 1 < Layers.size()){
      Layer tmp = Layers.get(ID + 1);
      Layers.set(ID + 1, Layers.get(ID));
      Layers.set(ID, tmp);
      return ID + 1;
    }
    return ID;
  }
  public int moveLayerDown(int ID){
    if(ID < 0 || ID >= Layers.size()){
      //TODO: Handle Error Invalid Layer ID
      return -1;
    }

    if(ID - 1 >= 0){
      Layer tmp = Layers.get(ID - 1);
      Layers.set(ID - 1, Layers.get(ID));
      Layers.set(ID, tmp);
      return ID - 1;
    }
    return ID;
  }
  public int moveLayer(int ID, int NewID){
    if(ID < 0 || ID >= Layers.size() || NewID < 0 || NewID >= Layers.size() ){
      //TODO: Handle Error Invalid Layer ID
      return -1;
    }

    Layer l = Layers.get(ID);
    Layers.remove(ID);
    Layers.add(NewID, l);
    return NewID;
  }
  public int addLayer(){
    // Get the ID of the active layer
    int activeLayerID = Layers.indexOf(activeLayer);

    // Add a blank layer above the active layer
    Layers.add(activeLayerID + 1, new Layer(new Color(0,0,0,0), width, height));

    // Set active layer to new layer if there is not one
    if(activeLayerID == -1)
      activeLayer = Layers.get(activeLayerID + 1);

    return activeLayerID + 1;
  }
  public int addLayer(Layer layer){
    // Get the ID of the active layer
    int activeLayerID = Layers.indexOf(activeLayer);

    // Add a given layer above the active layer
    Layers.add(activeLayerID + 1, layer);

    // Set active layer to new layer if there is not one
    if(activeLayerID == -1)
      activeLayer = Layers.get(activeLayerID + 1);

    return activeLayerID + 1;
  }
}
