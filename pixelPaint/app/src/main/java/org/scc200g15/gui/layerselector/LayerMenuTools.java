package org.scc200g15.gui.layerselector;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class LayerMenuTools {

    /* --------------------------------------- [SPACING] --------------------------------------- */
    // Get Largest Dimensions of a layer item
    public Dimension getMaxSize(JPanel panel) {
        return new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height);
    }

    /* --------------------------------------- [REFRESHING] --------------------------------------- */

    public void refreshUI(JComponent object) {
        object.revalidate();
        object.repaint();
    }

}