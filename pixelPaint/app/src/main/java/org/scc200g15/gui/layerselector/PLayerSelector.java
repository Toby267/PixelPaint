package org.scc200g15.gui.layerselector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

import org.scc200g15.image.Image;

/**
 * The right hand side bar which will contain the layers
 */
public class PLayerSelector extends JPanel {
  private JPanel contentPanel = new JPanel();
  private JPanel titleDisplay = new JPanel();
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
    
    // JPanel which holds all the content
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    // Adding the title display
    titleDisplay.add(new JLabel("Layer Menu"));
    contentPanel.add(titleDisplay);

    // Add first layers
    LayerMenuItem firstLayer = new LayerMenuItem("Layer #1", this);
    layers.add(firstLayer);
    contentPanel.add(firstLayer);
    
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
    firstLayer.setMaximumSize(getMaxSize(firstLayer));
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
          // Creates a new layer
          System.out.println("CURRENT NUMBER OF LAYERS: " + layers.size());
          if(layers.size() >= 16) {
            JOptionPane.showOptionDialog(
              null, "You have reached the maximum number of layers (16)", "Layer Maximum",
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.WARNING_MESSAGE,
              createImageIcon(40, 40, "/Icons/warning_icon.png"),
              null, null
            );
            return;
          }
          LayerMenuItem newLayer = new LayerMenuItem("Layer #" + (++totalCreatedLayerCount), Manager);
          Manager.layers.add(newLayer);
          contentPanel.add(newLayer, contentPanel.getComponentCount() - 1);
          newLayer.setMaximumSize(getMaxSize(newLayer));
          refreshUI();
        }
    });

    return addLayer;
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

  // Refresh the UI
  public void refreshUI() {
    revalidate();
    repaint();
  }

}
