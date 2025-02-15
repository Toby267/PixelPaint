package org.scc200g15.gui.layerselector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import java.awt.Image;

import java.awt.event.ActionEvent;

/**
 * The right hand side bar which will contain the layers
 */
public class PLayerSelector extends JPanel {
  private String visibleIconPath = "/Icons/open_eye_icon.png";
  private String invisibleIconPath = "/Icons/closed_eye_icon.png";
  private String trashIconPath = "/Icons/trash_icon.png";

  public ImageIcon createImageIcon(int x, int y, String path) {
    return new ImageIcon(
      new ImageIcon(getClass().getResource(path))
        .getImage()
        .getScaledInstance(x, y, Image.SCALE_SMOOTH)
    );
  }

  private ImageIcon visibleIcon = createImageIcon(30, 30, visibleIconPath);
  private ImageIcon invisibleIcon = createImageIcon(30, 30, invisibleIconPath);
  private ImageIcon trashIcon = createImageIcon(25, 25, trashIconPath);

  private JPanel titleDisplay = new JPanel();
  private JLabel sideLabel = new JLabel("Layer Menu");

  private JPanel singleLayerDisplay = new JPanel();
  private JButton displayButton = new JButton(this.visibleIcon);
  private JLabel layerLabel = new JLabel("Layer #1");
  private JButton removeButton = new JButton(this.trashIcon);

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

    // Make transparent borders for both icons
    this.displayButton.setBorder(new LineBorder(new Color(0,0,0,0), 10, true));
    this.removeButton.setBorder(new LineBorder(new Color(0,0,0,0), 10, true));    

    // Add all the elements to one layer menu element/display.
    this.singleLayerDisplay.setLayout(new BorderLayout());
    this.singleLayerDisplay.add(this.displayButton, BorderLayout.WEST);
    this.singleLayerDisplay.add(this.layerLabel, BorderLayout.CENTER);
    this.singleLayerDisplay.add(this.removeButton, BorderLayout.EAST);
    add(this.singleLayerDisplay);

    // Fix spacing
    this.titleDisplay.setMaximumSize(minimizeSpacing(this.titleDisplay));
    this.singleLayerDisplay.setMaximumSize(minimizeSpacing(this.singleLayerDisplay));

    /* ----------------------------- [Event Listeners] ----------------------------- */

    // Monitor the display button being pressed
    this.displayButton.addActionListener(new ActionListener() { 
        @Override
        public void actionPerformed(ActionEvent e) { 
          System.out.println("VISION SWITCH");
          boolean isVisible = displayButton.getIcon().equals(visibleIcon);
          displayButton.setIcon(isVisible ? invisibleIcon : visibleIcon); 
        } 
    });

    // Monitor the delete button being pressed
    this.removeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          System.out.println("LAYER REMOVED");

        }

    });

  }

  public static Dimension minimizeSpacing(JPanel panel) {
    return new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height);
  }


}
