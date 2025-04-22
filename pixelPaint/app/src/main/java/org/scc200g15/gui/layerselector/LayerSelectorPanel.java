package org.scc200g15.gui.layerselector;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import org.scc200g15.config.Config;
import org.scc200g15.gui.GUI;
import org.scc200g15.gui.icons.IconManager;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;

/**
 * The right hand side bar which will contain the layers
 */
public final class LayerSelectorPanel extends JPanel {

  private final JPanel contentPanel = new JPanel();

  private JPanel addLayerPanel;
  private Layer lastActiveLayer;

  JPanel separator;

  /**
   * SideBar which holds all the Layer Selectors
   */
  public LayerSelectorPanel(GUI window) {
    // Setting up parameters
    this.setBorder(new BevelBorder(BevelBorder.LOWERED));
    this.setPreferredSize(new Dimension(250, window.getHeight()));
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // JPanel which holds all the content
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    separator = new JPanel();
    //separator.setMaximumSize(new Dimension(contentPanel.getWidth(), 4));
    //separator.setPreferredSize(new Dimension(contentPanel.getWidth(), 4));
    separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
    separator.setBackground(Color.BLUE);  // or whatever color you want

    // Draw the layer selection menu
    drawMenuUI(window);
  }

  public void redrawMenuUI(){
    // Remove all of the old items from the JPanel
    removeAll();
    contentPanel.removeAll();

    // Draw the Layer Selection Menu
    drawMenuUI(GUI.getInstance());

    // Repaint Layer Selection Menu 
    revalidate();
    repaint();
  }

  /**
   * drawMenuUI - Draws the LayerSelectionGUI
   * @param window
   */
  public void drawMenuUI(GUI window) {
    // Get the active image
    Image image = window.getCanvas().getActiveImage();

    // Adding the title display
    JPanel titleDisplay = new JPanel();
    titleDisplay.add(new JLabel("Layer Menu"));
    contentPanel.add(titleDisplay);

    if (image != null) {
      // Add existing layers to the Layer Menu
      for (int i = 0; i < image.getLayerCount(); i++) {
        Layer currentLayer = image.getLayer(i);
        currentLayer.setMaximumSize(getMaxSize(currentLayer));
        contentPanel.add(currentLayer);

        // Add spacing between layers
        if (i < image.getLayerCount() - 1)
          contentPanel.add(Box.createRigidArea(new Dimension(0, 1)));
      }
        
    }

    // Add layer adder button
    addLayerPanel = new JPanel();

    addLayerPanel.add(createPlusLayerButton());
    contentPanel.add(addLayerPanel);

    // Disable the add layer button if there are 16 Layers
    if(image != null) addLayerPanel.setVisible(image.getLayerCount() <= (Config.MAX_LAYERS - 1));
    else addLayerPanel.setVisible(false);

    // Add the ability to scroll through the list of layers
    JScrollPane scroll = new JScrollPane(contentPanel);
    scroll.getVerticalScrollBar().setUnitIncrement(15);
    this.add(scroll);

    // Fix the spacing of the layer menu
    titleDisplay.setMaximumSize(getMaxSize(titleDisplay));
  }

  /**
   * createPlusLayerButton - Creates the button that allows a user to add a layer to the image
   * @return returns the new JButton
   */
  public JButton createPlusLayerButton() {
    // Add new button to create new layer (symbol: +)
    JButton addLayer = new JButton(IconManager.createImageIcon(20, 20, "/Icons/plus_icon.png"));
    addLayer.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));

    // Monitor addLayer button press
    addLayer.addActionListener((ActionEvent e) -> {
        Image image = GUI.getInstance().getActiveImage();

        // If no active image do nothing
        if (image == null) 
            return;

        // Set layer limit upper bound
        if (image.getLayerCount() >= Config.MAX_LAYERS) return; // Should not happen as the button should be hidden when 16 layers exist in the image

        // Call the create layer function
        createLayer();

        // Redraw the canvas
        GUI.getInstance().getCanvas().recalculateAllPixels();
        GUI.getInstance().getCanvas().repaint();        
    });

    return addLayer;
  }

  /**
   * createLayer - Creates a new transparent layer and adds it to the current image
   */
  public void createLayer(){
    Image image = GUI.getInstance().getActiveImage();
    if(image == null) 
    {
      addLayerPanel.setVisible(false);
      return;
    }
    // Create a new layer with a TEMP na,e
    Layer newLayer = new Layer("New Layer", new Color(0, 0, 0, 0), image.getWidth(), image.getHeight());
  
    // Add the layer to the active image
    image.addLayer(newLayer);

    // Redraw the layer menu
    redrawMenuUI();

    addLayerPanel.setVisible(image.getLayerCount() <= 15);    
  }

  // * ----------------------- [REMOVE LAYERS] ----------------------- * //

  /**
   * Handel removing a layer from the image
   * @param layer the layer to remove
   */
  public void removeLayer(Layer layer) { 
    Image image = GUI.getInstance().getCanvas().getActiveImage();

    // Make sure the image is not null
    if(image == null){
      System.out.println("ERROR: removeLayerReferences image is null");
      return;
    }

    // Make sure there is always 1 layer
    if(image.getLayerCount() == 1) {
      JOptionPane.showMessageDialog(GUI.getInstance(), "You are not able to remove the last final layer form an image!");
      return;
    }

    // Remove the layer
    image.removeLayer(layer);
    contentPanel.remove(layer);

    // If needed readd the create layer button
    addLayerPanel.setVisible(image.getLayerCount() <= 15);

    // Repaint and recalculate
    revalidate();
    repaint();
    
    GUI.getInstance().getCanvas().recalculateAllPixels();
    GUI.getInstance().getCanvas().repaint();
  }

  /**
   * Handel removing a layer from the image but show a warning message first
   * @param layer the layer to remove
   */
  public void removeLayerWithWarning(Layer layer) {
    Image image = GUI.getInstance().getCanvas().getActiveImage();

    if(image.getLayerCount() == 1) {
      JOptionPane.showMessageDialog(GUI.getInstance(), "You are not able to remove the last final layer form an image!");
      return;
    }

    int option = JOptionPane.showOptionDialog(
      null, "Are you sure you want to delete this layer?", "Layer Deletion",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      IconManager.createImageIcon(40, 40, "/Icons/question_mark_icon.png"),
      null, null);
    
    if (option != JOptionPane.YES_OPTION) return;

    removeLayer(layer);
  }

  public void removeLayerWithoutWarning(Layer layer) {
    removeLayer(layer);
  }

  // * ----------------------- [REARRANGE LAYERS] ----------------------- * //
   
  /**
   * Move a layer from pos 1 to pos 2
   * @param index1 the start pos
   * @param index2 the end pos
   */
  public void moveLayer(int index1, int index2) {
    Image image = GUI.getInstance().getCanvas().getActiveImage();

    image.moveLayer(index1, index2);

    // Redraw the menu
    redrawMenuUI();

    // Recalculate pixels and redraw
    GUI.getInstance().getCanvas().recalculateAllPixels();
    GUI.getInstance().getCanvas().repaint();
  }

  // * ----------------------- [SET ACTIVE LAYER]  ----------------------- * //

  /**
   * Set a layer to be the active layer
   * @param activeLayer
   */
  public void setActiveLayer(Layer activeLayer) {
    // Get the active image
    Image image = GUI.getInstance().getActiveImage();

    if (image == null)
      return;

    // Check if the layer is already active  
    if (activeLayer == lastActiveLayer)
      return;

    // Tell the layer it is the active layer
    image.setActiveLayer(activeLayer, lastActiveLayer);
    
    // Update the record of last active layer
    lastActiveLayer = activeLayer;
  }

  /**
   * Set a layer to be the active layer
   * @param layerID the layer id of the layer to become active
   */
  public void setActiveLayer(int layerID) {
    // Get the active image
    Image image = GUI.getInstance().getActiveImage();

    if (image == null)
      return;
    
    // Get the layer from the ID
    Layer activeLayer = image.getLayer(layerID);
    setActiveLayer(activeLayer);
  }

  /**
   * Toggle whether a given layer is selected or not
   * @param layerID
   */
  public void switchSelectedLayerState(int layerID) {
    Image image = GUI.getInstance().getActiveImage();
    if (image == null)
      return;

    Layer layer = image.getLayer(layerID);

    // add/remove layer from arraylist containing selected layers
    if (layer.isSelected()) image.removeSelectedLayer(layer);
    else image.addSelectedLayer(layer);

    layer.switchSelectedLayerState();
  }

  // * ----------------------- [ACCESSORS/MUTATORS]  ----------------------- * //

  /**
   * Get the list of all layers
   */
  public ArrayList<Layer> getLayers() {
    return GUI.getInstance().getCanvas().getActiveImage().getLayers();
  }

  public void setLastActiveLayer(Layer lastActiveLayer) {
      this.lastActiveLayer = lastActiveLayer;
  }

  /**
   * Get Largest Dimensions of a layer item
   * @param panel
   * @return
   */
  Dimension getMaxSize(JPanel panel) {
    return new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height);
  } 

  public void setSeparatorPOS(int pos){
    Image image = GUI.getInstance().getActiveImage();

    contentPanel.remove(separator);
    System.out.println(pos);
    if(pos < 0) {
      contentPanel.repaint();
      contentPanel.revalidate(); // refresh layout
      return;
    }

    Component[] components = contentPanel.getComponents();
  
    int layerCount = 0;
    int insertIndex =  (image.getLayerCount() <= (Config.MAX_LAYERS - 1)) ? components.length - 1 : components.length; // default to end if not found

    for (int i = 0; i < components.length; i++) {
      if (components[i] instanceof Layer) {
          if (layerCount == pos) {
              insertIndex = i; // insert after this Layer
              break;
          }
          layerCount++;
      }
    }

    contentPanel.add(separator, insertIndex); // insert at the calculated index
    contentPanel.repaint();
    contentPanel.revalidate(); // refresh layout
  }
}

