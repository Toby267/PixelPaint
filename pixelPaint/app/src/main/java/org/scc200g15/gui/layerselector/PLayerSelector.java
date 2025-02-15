package org.scc200g15.gui.layerselector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

/**
 * The right hand side bar which will contain the layers
 */
public class PLayerSelector extends JPanel {
  private JPanel titleDisplay = new JPanel();
  private JLabel sideLabel = new JLabel("Layer Menu");

  private ArrayList<LayerMenuItem> layers = new ArrayList<LayerMenuItem>(16);

  private int totalCreatedLayerCount = 1;

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

    // Add first layers
    LayerMenuItem firstLayer = new LayerMenuItem("Layer #1", this);
    layers.add(firstLayer);
    add(firstLayer);
    
    // Add layer adder button
    JPanel plusLayerHolderPanel = new JPanel();
    plusLayerHolderPanel.add(createPlusLayerButton(this));
    add(plusLayerHolderPanel);

    // Fix spacing
    this.titleDisplay.setMaximumSize(minimizeSpacing(this.titleDisplay));
    firstLayer.setMaximumSize(minimizeSpacing(firstLayer));

  }

  public JButton createPlusLayerButton(PLayerSelector Manager) {
    JButton addLayer = new JButton(new ImageIcon(
      new ImageIcon(getClass().getResource("/Icons/plus_icon.png"))
        .getImage()
        .getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH)
    ));
    addLayer.setBorder(new LineBorder(new Color(0,0,0,0), 10, true));

    addLayer.addActionListener(new ActionListener() { 
        @Override
        public void actionPerformed(ActionEvent e) { 
          LayerMenuItem newLayer = new LayerMenuItem("Layer #" + (++totalCreatedLayerCount), Manager);
          layers.add(newLayer);
          add(newLayer, getComponentCount() - 1);
          newLayer.setMaximumSize(minimizeSpacing(newLayer));
          revalidate();
          repaint();
        }
    });

    return addLayer;
  }


  public Dimension minimizeSpacing(JPanel panel) {
    return new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height);
  }

  public void removeLayerMenuItem(LayerMenuItem layer) {
    layers.remove(layer);
    remove(layer);
    revalidate();
    repaint();
  }

}
