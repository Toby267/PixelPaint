package org.scc200g15.gui.sidebar;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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

    JLabel sideLabel = new JLabel("side");
    sideLabel.setHorizontalAlignment(SwingConstants.LEFT);

    add(sideLabel);

    add(this.colourPicker);
  }

  public Color getActiveColor() {
    return this.colourPicker.getActiveColor();
  }

}
