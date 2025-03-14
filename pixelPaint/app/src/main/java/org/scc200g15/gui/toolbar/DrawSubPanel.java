package org.scc200g15.gui.toolbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import org.scc200g15.gui.icons.IconManager;
import org.scc200g15.gui.numberinput.NumberInput;
import org.scc200g15.tools.DrawTool;

public class DrawSubPanel extends JPanel{

    DrawTool drawTool;

    public DrawSubPanel(int height, DrawTool tool) {
        super(null);

        drawTool = tool;
        
        setMaximumSize(new Dimension(128, height));
        
        ImageIcon icons[] = {IconManager.CIRCLE_ICON,IconManager.SQUARE_ICON, IconManager.TRIANGLE_ICON, IconManager.STAR_ICON};
        String shapeIDs[] = {"Circle", "Square", "Triangle", "Star"};

        JComboBox<ImageIcon> shapeSelector = new JComboBox<>(icons);
        shapeSelector.setLocation(2,2);
        shapeSelector.setSize(new Dimension(60, height - 4));

        shapeSelector.getSelectedIndex();

        NumberInput sizeInput = new NumberInput(1,100, 2);
        sizeInput.setLocation(64,2);
        sizeInput.setSize(new Dimension(60, height - 4));

        // Pass Shape input to draw tool
        shapeSelector.addActionListener((ActionEvent e) -> {
            JComboBox<ImageIcon> selector = (JComboBox<ImageIcon>) e.getSource();
            
            int index = java.util.Arrays.asList(icons).indexOf(selector.getSelectedItem());
            tool.setShape(shapeIDs[index]);
        });

        // Pass Size input to draw tool
        sizeInput.addChangeListener((ChangeEvent e) -> {
            NumberInput input = (NumberInput)e.getSource();
            int val = (int)input.getValue();
            tool.setSize(val);
        });

        add(sizeInput);
        add(shapeSelector);
    }
}
