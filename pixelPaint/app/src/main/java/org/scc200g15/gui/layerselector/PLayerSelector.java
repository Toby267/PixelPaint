package org.scc200g15.gui.layerselector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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

  private final JPanel contentPanel = new JPanel();;
  private JPanel titleDisplay;
  private JPanel addLayerPanel;
  private JScrollPane scroll;

  private Layer lastActiveLayer;

  /**
   * SideBar which holds all the Layer Selectors
   */
  public PLayerSelector(GUI window) {
    // Setting up parameters
    this.setBorder(new BevelBorder(BevelBorder.LOWERED));
    this.setPreferredSize(new Dimension(250, window.getHeight()));
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
    addLayerPanel = new JPanel();

    addLayerPanel.add(createPlusLayerButton(image));
    contentPanel.add(addLayerPanel);
    if(image != null) addLayerPanel.setVisible(image.getLayerCount() <= 15);

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
   * --------------------------------------- [ADD LAYERS] ---------------------------------------
   */

  public JButton createPlusLayerButton(Image image) {
    // Add new button to create new layer (symbol: +)
    JButton addLayer = new JButton(Tools.createImageIcon(20, 20, "/Icons/plus_icon.png"));
    addLayer.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));

    // Monitor addLayer button press
    addLayer.addActionListener((ActionEvent e) -> {
        Image image1 = GUI.getInstance().getActiveImage();
        if (image1 == null) {
            return;
        }
        // Set layer limit upper bound
        if (image1.getLayerCount() >= 16) {
            // TODO: Handle Error
            return;
        }
        createLayer();

        GUI.getInstance().getCanvas().repaint();
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

    addLayerPanel.setVisible(image.getLayerCount() <= 15);

    Tools.refreshUI(this);
  }

  /*
   * --------------------------------------- [REMOVE LAYERS]
   * ---------------------------------------
   */

  // Remove Layer item
  public void removeLayer(Layer layer) {
    Image image = GUI.getInstance().getCanvas().getActiveImage();

    if(image == null){
      //TODO: Handle Error
      return;
    }

    int option = JOptionPane.showOptionDialog(
      null, "Are you sure you want to delete this layer?", "Layer Deletion",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      Tools.createImageIcon(40, 40, "/Icons/question_mark_icon.png"),
      null, null);
    
    if (option != JOptionPane.YES_OPTION) return;

    System.out.println(image.getLayerCount());
    if(image.getLayerCount() == 1){
      //TODO: Handle Error Message
      return;
    }

    image.removeLayer(layer);
    contentPanel.remove(layer);

    addLayerPanel.setVisible(image.getLayerCount() <= 15);

    Tools.refreshUI(this);
    GUI.getInstance().getCanvas().repaint();
  }

  /*
   * --------------------------------------- [REARRANGE LAYERS]
   * ---------------------------------------
   */
  public void moveLayer(int index1, int index2) {
    Image image = GUI.getInstance().getCanvas().getActiveImage();

    image.moveLayer(index1, index2);

    redrawMenuUI(GUI.getInstance());
    GUI.getInstance().getCanvas().repaint();
  }

  /*
   * --------------------------------------- [SET ACTIVE LAYER]
   * ---------------------------------------
   */

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

  /*
   * --------------------------------------- [ACCESSORS/MUTATORS]
   * ---------------------------------------
   */

  public ArrayList<Layer> getLayers() {
    return GUI.getInstance().getCanvas().getActiveImage().getLayers();
  }

  public void setLastActiveLayer(Layer lastActiveLayer) {
      this.lastActiveLayer = lastActiveLayer;
  }

}
