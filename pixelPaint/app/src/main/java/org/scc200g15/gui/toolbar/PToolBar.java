package org.scc200g15.gui.toolbar;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PToolBar extends JPanel{
    public PToolBar(JFrame window) {
        setPreferredSize(new Dimension(window.getWidth(), 16));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        JLabel toolLabel = new JLabel("status");
        toolLabel.setHorizontalAlignment(SwingConstants.LEFT);

        add(toolLabel);
    }
}
