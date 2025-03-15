package org.scc200g15.gui.sidebar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.scc200g15.gui.GUI;

/**
 * The SideBar that will contain the color selector
 */
public class PSideBar extends JPanel {
  ColourPicker colourPicker = new ColourPicker();

  public PSideBar(GUI window) {
    setBorder(new BevelBorder(BevelBorder.LOWERED));

    setPreferredSize(new Dimension(200, window.getHeight()));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JLabel sideLabel = new JLabel("Colour Selector");
    sideLabel.setHorizontalAlignment(SwingConstants.CENTER);

    add(sideLabel);

    add(this.colourPicker);


    JSlider slider = new JSlider(0, 100, 100);



    add(slider);

    JButton button = new JButton("PRESS");
    button.addActionListener((ActionEvent e) -> {
      Color c = new Color(248, 122, 250); // ! Manually add the changes, moves it into input text
      this.colourPicker.setColor(c);

      Color out = this.colourPicker.getActiveColor();
      System.out.println("Active: (" + out.getRed() + ", " + out.getGreen() + ", " + out.getBlue() + ")");


          
    });


    add(button);
  }





  public Color getActiveColor() {
    return this.colourPicker.getActiveColor();
  }

}
