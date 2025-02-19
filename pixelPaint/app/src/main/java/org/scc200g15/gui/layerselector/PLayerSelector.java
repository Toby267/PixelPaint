package org.scc200g15.gui.layerselector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import org.scc200g15.gui.GUI;
import org.scc200g15.image.Image;
import org.scc200g15.image.Layer;

/**
 * The right hand side bar which will contain the layers
 */
public class PLayerSelector extends JPanel {
  private final LayerMenuTools Tools = new LayerMenuTools();

  private JPanel contentPanel;
  private JPanel titleDisplay;
  private JScrollPane scroll;

  private int totalCreatedLayerCount = 1;
  private Layer lastActiveLayerIndex;

  /**
   * SideBar which holds all the Layer Selectors
   */
  public PLayerSelector(GUI window) {
    // Setting up parameters
    this.setBorder(new BevelBorder(BevelBorder.LOWERED));
    this.setPreferredSize(new Dimension(250, window.getHeight()));
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    contentPanel = new JPanel();

    // JPanel which holds all the content
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    drawMenuUI(window);
  }

  public void redrawMenuUI(GUI window){
    // Flip the content in UI
    removeAll();
    contentPanel.removeAll();
    drawMenuUI(GUI.getInstance());
    Tools.refreshUI(this);
  }

  public void drawMenuUI(GUI window) {
    Image image = window.getCanvas().getActiveImage();

    // Adding the title display
    titleDisplay = new JPanel();
    titleDisplay.add(new JLabel("Layer Menu"));
    contentPanel.add(titleDisplay);

    if (image != null) {
      // Add first layers
      for (int i = 0; i < image.getLayerCount(); i++)
        contentPanel.add(image.getLayer(i));
    }

    // Add layer adder button
    JPanel plusLayerHolderPanel = new JPanel();
    plusLayerHolderPanel.add(createPlusLayerButton(image));
    contentPanel.add(plusLayerHolderPanel);

    // Add the ability to scroll
    scroll = new JScrollPane(contentPanel);
    scroll.getVerticalScrollBar().setUnitIncrement(15);
    this.add(scroll);

    // Fix spacing
    this.titleDisplay.setMaximumSize(Tools.getMaxSize(this.titleDisplay));
    if (image != null) {
      for (int i = 0; i < image.getLayerCount(); i++)
        image.getLayer(i).setMaximumSize(Tools.getMaxSize(image.getLayer(i)));
    }
  }

  /*
   * --------------------------------------- [ADD LAYERS]
   * ---------------------------------------
   */

  public JButton createPlusLayerButton(Image image) {
    // Add new button to create new layer (symbol: +)
    JButton addLayer = new JButton(Tools.createImageIcon(20, 20, "/Icons/plus_icon.png"));
    addLayer.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));

    // Monitor addLayer button press
    addLayer.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Image image = GUI.getInstance().getActiveImage();
        if(image == null) return;
        // Set layer limit upper bound
        if (image.getLayerCount() >= 16)
          maxLayersErrorMessage();
        else
          createLayer();
      }
    });

    return addLayer;
  }

  public void createLayer(){
    Image image = GUI.getInstance().getActiveImage();
    if(image == null) 
    {
      //TODO: Handle Error
      return;
    }
    Layer newLayer = new Layer("TEMP", new Color(0, 0, 0, 0), image.getWidth(), image.getHeight());
  
    image.addLayer(newLayer);
    redrawMenuUI(GUI.getInstance());

    Tools.refreshUI(this);
  }

  // ! An alternative to this could be to remove the [+] sign which allows users
  // to add more layers when 16 are reached
  // ! This might be cleaner...
  public void maxLayersErrorMessage() {
    JOptionPane.showOptionDialog(
        null, "You have reached the maximum number of layers (16)", "Layer Maximum",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.WARNING_MESSAGE,
        Tools.createImageIcon(40, 40, "/Icons/warning_icon.png"),
        null, null);
  }

  /*
   * --------------------------------------- [REMOVE LAYERS]
   * ---------------------------------------
   */

  // Remove Layer item
  public void removeLayer(Layer layer) {
    Image image = GUI.getInstance().getCanvas().getActiveImage();
    image.removeLayer(layer);
    contentPanel.remove(layer);
    Tools.refreshUI(this);
  }

  /*
   * --------------------------------------- [REARRANGE LAYERS]
   * ---------------------------------------
   */

  // Insert and redraw layer
  // ! RENAME TO MOVE LAYER
  public void insertLayer(int index1, int index2) {
    Image image = GUI.getInstance().getCanvas().getActiveImage();

    int increment = index1 < index2 ? 1 : -1;
    for (int i = index1; i != index2; i += increment) {
      image.moveLayer(i, i + increment);
      // layers.set(i, layers.get(i + increment));
    }

    image.moveLayer(index2, index1);

    redrawMenuUI(GUI.getInstance());
  }

  /*
   * --------------------------------------- [SET ACTIVE LAYER]
   * ---------------------------------------
   */

  public void setActiveLayer(Layer activeLayer) {
    Image image = GUI.getInstance().getActiveImage();
    if (image == null)
      return;
    image.setActiveLayer(activeLayer);
  }
  public void setActiveLayer(int layerID) {
    Image image = GUI.getInstance().getActiveImage();
    if (image == null)
      return;

    image.setActiveLayer(image.getLayer(layerID));
  }

  /*
   * --------------------------------------- [ACCESSORS/MUTATORS]
   * ---------------------------------------
   */

  public ArrayList<Layer> getLayers() {
    return GUI.getInstance().getCanvas().getActiveImage().getLayers();
  }

}
