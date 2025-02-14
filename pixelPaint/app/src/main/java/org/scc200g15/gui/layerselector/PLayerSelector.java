package org.scc200g15.gui.layerselector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import java.awt.Image;

import java.awt.event.ActionEvent;

/**
 * The right hand side bar which will contain the layers
 */
public class PLayerSelector extends JPanel {
  private String visibleIconPath = "/Icons/open_eye_icon.png";
  private String invisibleIconPath = "/Icons/closed_eye_icon.png";
  private String trashIconPath = "/Icons/trash_icon.png";

  private ImageIcon visibleIcon = new ImageIcon(
    new ImageIcon(getClass().getResource(visibleIconPath))
      .getImage()
      .getScaledInstance(30, 30, Image.SCALE_SMOOTH)
  );

  private ImageIcon invisibleIcon = new ImageIcon(
    new ImageIcon(getClass().getResource(invisibleIconPath))
      .getImage()
      .getScaledInstance(30, 30, Image.SCALE_SMOOTH)
  );

  private ImageIcon trashIcon = new ImageIcon(
    new ImageIcon(getClass().getResource(trashIconPath))
      .getImage()
      .getScaledInstance(25, 25, Image.SCALE_SMOOTH)
  );

  private JLabel sideLabel = new JLabel("Layer Menu");

  private JPanel singleLayerDisplay = new JPanel();
  private JButton displayButton = new JButton(this.visibleIcon);
  private JLabel layerLabel = new JLabel("LABEL");
  private JButton removeButton = new JButton(this.trashIcon);


  /**
    Placeholder. 
  */
  public PLayerSelector(JFrame window) {
    setBorder(new BevelBorder(BevelBorder.LOWERED));
    setPreferredSize(new Dimension(250, window.getHeight()));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    sideLabel.setHorizontalAlignment(SwingConstants.LEFT);
    add(sideLabel);

    singleLayerDisplay.add(displayButton);
    singleLayerDisplay.add(layerLabel);
    singleLayerDisplay.add(removeButton);
    add(singleLayerDisplay);

    displayButton.addActionListener(new ActionListener() { 
        @Override
        public void actionPerformed(ActionEvent e) { 
          display_switch(); 
        } 
    });

  }

  public void display_switch() {
    // test_display_button.setIcon(invisibleIcon);
    System.out.println("DISPLAY VISION HAS BEEN SWITCHED.");
    if (displayButton.getIcon().equals(visibleIcon)) {
      // Is currently visible
      displayButton.setIcon(invisibleIcon);
    } else {
      // Is currently invisible
      displayButton.setIcon(visibleIcon);
    }

  }

}
