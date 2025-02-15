package org.scc200g15.gui.layerselector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;


/**
 * The right hand side bar which will contain the layers
 */
public class PLayerSelector extends JPanel {
  private JPanel contentPanel;
  private JPanel titleDisplay;
  private JScrollPane scroll;

  private ArrayList<LayerMenuItem> layers = new ArrayList<LayerMenuItem>(16);

  private int totalCreatedLayerCount = 1;

  public ImageIcon createImageIcon(int x, int y, String path) {
    return new ImageIcon(
      new ImageIcon(getClass().getResource(path))
        .getImage()
        .getScaledInstance(x, y, java.awt.Image.SCALE_SMOOTH)
    );
  }


  /**
    SideBar which holds all the Layer Selectors
  */
  public PLayerSelector(JFrame window) {
    // Setting up parameters
    this.setBorder(new BevelBorder(BevelBorder.LOWERED));
    this.setPreferredSize(new Dimension(250, window.getHeight()));
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    contentPanel = new JPanel();

    // JPanel which holds all the content
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    layers.add(new LayerMenuItem("Layer #1", this));

    setupMenuUI();
  }


  public void setupMenuUI() {
    // Adding the title display
    titleDisplay = new JPanel();  
    titleDisplay.add(new JLabel("Layer Menu"));
    contentPanel.add(titleDisplay);

    // Add first layers
    for (LayerMenuItem panel : layers) contentPanel.add(panel);
    
    // Add layer adder button
    JPanel plusLayerHolderPanel = new JPanel();
    plusLayerHolderPanel.add(createPlusLayerButton(this));
    contentPanel.add(plusLayerHolderPanel);

    // Add the ability to scroll
    scroll = new JScrollPane(contentPanel);
    scroll.getVerticalScrollBar().setUnitIncrement(15);
    this.add(scroll);

    // Fix spacing
    this.titleDisplay.setMaximumSize(getMaxSize(this.titleDisplay));
    for (LayerMenuItem panel : layers) panel.setMaximumSize(getMaxSize(panel));
  }


  public JButton createPlusLayerButton(PLayerSelector Manager) {
    // Add new button to create new layer (symbol: +)
    JButton addLayer = new JButton(new ImageIcon(
      new ImageIcon(getClass().getResource("/Icons/plus_icon.png"))
        .getImage()
        .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)
    ));
    addLayer.setBorder(new LineBorder(new Color(0,0,0,0), 10, true));

    // Monitor addLayer button press
    addLayer.addActionListener(new ActionListener() { 
        @Override
        public void actionPerformed(ActionEvent e) {
          if(layers.size() >= 16) maxLayersErrorMessage();
          else addLayer(Manager);
        }
    });

    return addLayer;
  }

  // Add a new layerMenuItem to the layer menu
  public void addLayer(PLayerSelector Manager) {
    LayerMenuItem newLayer = new LayerMenuItem("Layer #" + (++totalCreatedLayerCount), Manager);
    layers.add(newLayer);
    contentPanel.add(newLayer, contentPanel.getComponentCount() - 1);
    newLayer.setMaximumSize(getMaxSize(newLayer));
    refreshUI();
  }

  // ! An alternative to this could be to remove the [+] sign which allows users to add more layers when 16 are reached
  // ! This might be cleaner...
  public void maxLayersErrorMessage() {
    JOptionPane.showOptionDialog(
      null, "You have reached the maximum number of layers (16)", "Layer Maximum",
      JOptionPane.DEFAULT_OPTION,
      JOptionPane.WARNING_MESSAGE,
      createImageIcon(40, 40, "/Icons/warning_icon.png"),
      null, null
    );
  }

  // Get Largest Dimensions of a layer item
  public Dimension getMaxSize(JPanel panel) {
    return new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height);
  }

  // Remove Layer item
  public void removeLayerMenuItem(LayerMenuItem layer) {
    layers.remove(layer);
    contentPanel.remove(layer);
    refreshUI();
  }

  // Swap and redraw layers
  public void swapLayers(int index1, int index2) {
    // Swap the content in layers ArrayList
    if(index2 > layers.size()) Collections.swap(layers, index1, layers.size() - 1);
    else if (index1 < 0) Collections.swap(layers, 0, index2);
    else Collections.swap(layers, index1, index2);

    // Flip the content in UI
    removeAll();
    contentPanel.removeAll();
    setupMenuUI();
    refreshUI();
  }

  // TODO
  public void setActiveLayer() {

  }

  public ArrayList<LayerMenuItem> getLayers() {
    return layers;
  }

  public void refreshUI() {
    revalidate();
    repaint();
  }

}
