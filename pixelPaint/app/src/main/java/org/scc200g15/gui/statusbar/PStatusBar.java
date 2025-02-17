package org.scc200g15.gui.statusbar;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.scc200g15.gui.GUI;

/**
 * The StatusBar at the bottom of the window that will show the user there current mouse pos and other useful info
 */
public class PStatusBar extends JPanel {
  public PStatusBar() {
    setBorder(new BevelBorder(BevelBorder.LOWERED));

    setPreferredSize(new Dimension(GUI.getInstance().getWidth(), 16));
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    // Add Label to StatusBar
    JLabel statusLabel = new JLabel("StatusBar");
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    add(statusLabel);
  }
}
