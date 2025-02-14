package org.scc200g15.gui.layerselector;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * The right hand side bar which will contain the layers
 */
public class PLayerSelector extends JPanel {
  public PLayerSelector(JFrame window) {
    setBorder(new BevelBorder(BevelBorder.LOWERED));

    setPreferredSize(new Dimension(60, window.getHeight()));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JLabel sideLabel = new JLabel("layer");
    sideLabel.setHorizontalAlignment(SwingConstants.LEFT);

    add(sideLabel);
  }
}
