package org.scc200g15.gui.statusbar;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class PStatusBar extends JPanel{
    public PStatusBar(JFrame window) {
        setBorder(new BevelBorder(BevelBorder.LOWERED));

        setPreferredSize(new Dimension(window.getWidth(), 16));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // Add Label to StatusBar
        JLabel statusLabel = new JLabel("StatusBar");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(statusLabel);
    }
}
