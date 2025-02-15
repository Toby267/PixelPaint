package org.scc200g15.gui.layerselector;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * The right hand side bar which will contain the layers
 */
public class PLayerSelector extends JPanel {
  private JPanel titleDisplay = new JPanel();
  private JLabel sideLabel = new JLabel("Layer Menu");

  private ArrayList<JPanel> layers = new ArrayList<JPanel>(16);

  /**
    Placeholder. 
  */
  public PLayerSelector(JFrame window) {
    // Setting up parameters
    setBorder(new BevelBorder(BevelBorder.LOWERED));
    setPreferredSize(new Dimension(250, window.getHeight()));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Adding the title display
    this.titleDisplay.add(this.sideLabel);
    add(this.titleDisplay);


    System.out.println("How many layers: "+layers.size());
    layers.add(new LayerMenuItem("Layer #1"));
    layers.add(new LayerMenuItem("Layer #2"));
    layers.add(new LayerMenuItem("Layer #3"));
    layers.add(new LayerMenuItem("Layer #4"));
    layers.add(new LayerMenuItem("Layer #5"));
    layers.add(new LayerMenuItem("Layer #6"));

    // Add every layer
    for(JPanel layer : layers) add(layer);

    // Fix spacing
    this.titleDisplay.setMaximumSize(minimizeSpacing(this.titleDisplay));
    for(JPanel layer : layers) layer.setMaximumSize(minimizeSpacing(layer));

  }

  public Dimension minimizeSpacing(JPanel panel) {
    return new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height);
  }

}
