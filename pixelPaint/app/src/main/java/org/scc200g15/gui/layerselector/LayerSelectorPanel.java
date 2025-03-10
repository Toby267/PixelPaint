package org.scc200g15.gui.layerselector;

import java.awt.Color;
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
  private final LayerMenuTools Tools = new LayerMenuTools();

  private final JPanel contentPanel = new JPanel();
    private JPanel addLayerPanel;

    private Layer lastActiveLayer;

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
        currentLayer.setMaximumSize(Tools.getMaxSize(currentLayer));
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
    titleDisplay.setMaximumSize(Tools.getMaxSize(titleDisplay));
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
        if (image == null) {
            return;
        }

        // Set layer limit upper bound
        if (image.getLayerCount() >= Config.MAX_LAYERS) {
            // Should not happen as the button should be hidden when 16 layers exist in the image
            return;
        }

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
    Layer newLayer = new Layer("TEMP", new Color(0, 0, 0, 0), image.getWidth(), image.getHeight());
  
    // Add the layer to the active image
    image.addLayer(newLayer);

    // Redraw the layer menu
    redrawMenuUI();

    addLayerPanel.setVisible(image.getLayerCount() <= 15);

    // TODO: add this to redrawMenuUI?
    Tools.refreshUI(this);
  }

  // * ----------------------- [REMOVE LAYERS] ----------------------- * //


  public void removeLayer(Layer layer) { 
    Image image = GUI.getInstance().getCanvas().getActiveImage();

    if(image == null){
      // TODO: Handle Error
      System.out.println("ERROR: removeLayerReferences image is null");
      return;
    }

    if(image.getLayerCount() == 1) {
      //TODO: Handle Error Message
      System.out.println("ERROR: last layer remaining");
      return;
    }

    image.removeLayer(layer);
    contentPanel.remove(layer);

    addLayerPanel.setVisible(image.getLayerCount() <= 15);

    Tools.refreshUI(this);
    GUI.getInstance().getCanvas().recalculateAllPixels();
    GUI.getInstance().getCanvas().repaint();
  }


  public void removeLayerWithWarning(Layer layer) {
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
   
  public void moveLayer(int index1, int index2) {
    Image image = GUI.getInstance().getCanvas().getActiveImage();

    image.moveLayer(index1, index2);

    redrawMenuUI();
    GUI.getInstance().getCanvas().recalculateAllPixels();
    GUI.getInstance().getCanvas().repaint();
  }

  // * ----------------------- [SET ACTIVE LAYER]  ----------------------- * //

  public void setActiveLayer(Layer activeLayer) {
    Image image = GUI.getInstance().getActiveImage();
    if (image == null)
      return;
    if (activeLayer == lastActiveLayer)
      return;
    image.setActiveLayer(activeLayer, lastActiveLayer);
    lastActiveLayer = activeLayer;
  }

  public void setActiveLayer(int layerID) {
    Image image = GUI.getInstance().getActiveImage();
    if (image == null)
      return;
    Layer activeLayer = image.getLayer(layerID);
    setActiveLayer(activeLayer);
  }

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

  public ArrayList<Layer> getLayers() {
    return GUI.getInstance().getCanvas().getActiveImage().getLayers();
  }

  public void setLastActiveLayer(Layer lastActiveLayer) {
      this.lastActiveLayer = lastActiveLayer;
  }

}
