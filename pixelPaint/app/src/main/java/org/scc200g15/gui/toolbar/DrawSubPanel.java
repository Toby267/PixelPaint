package org.scc200g15.gui.toolbar;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.scc200g15.gui.icons.IconManager;
import org.scc200g15.gui.numberinput.NumberInput;
import org.scc200g15.tools.DrawTool;

public class DrawSubPanel extends JPanel{

    DrawTool drawTool;

    public DrawSubPanel(int height, DrawTool tool) {
        super(null);

        drawTool = tool;
        
        setMaximumSize(new Dimension(128, height));

        ImageIcon icons[] = {IconManager.CIRCLE_ICON, IconManager.SQUARE_ICON, IconManager.TRIANGLE_ICON, IconManager.STAR_ICON};

        JComboBox<ImageIcon> shapeSelector = new JComboBox<>(icons);
        shapeSelector.setLocation(2,2);
        shapeSelector.setSize(new Dimension(60, height - 4));

        NumberInput sizeInput = new NumberInput(1,100);
        sizeInput.setLocation(64,2);
        sizeInput.setSize(new Dimension(60, height - 4));

        add(sizeInput);
        add(shapeSelector);
    }
}
