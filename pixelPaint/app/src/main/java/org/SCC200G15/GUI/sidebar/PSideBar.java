package org.scc200g15.gui.sidebar;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class PSideBar extends JPanel {
    public PSideBar(JFrame window) {
        setBorder(new BevelBorder(BevelBorder.LOWERED));

        setPreferredSize(new Dimension(60, window.getHeight()));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel sideLabel = new JLabel("side");
        sideLabel.setHorizontalAlignment(SwingConstants.LEFT);

        add(sideLabel);
    }
}
