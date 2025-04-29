package org.scc200g15.gui.toolbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.scc200g15.gui.icons.IconManager;
import org.scc200g15.tools.drawShapes.drawShapeTool;

public class DrawSubPanel1 extends JPanel{

    drawShapeTool tool;

    public DrawSubPanel1(int height, drawShapeTool tool) {
        super(null);

        this.tool = tool;

        setMaximumSize(new Dimension(128, height));

        ImageIcon icons[] = {IconManager.CIRCLE_ICON,IconManager.SQUARE_ICON, IconManager.TRIANGLE_ICON, IconManager.STAR_ICON};
        String shapeIDs[] = {"Circle", "Square", "Triangle", "Star"};

        JComboBox<ImageIcon> shapeSelector = new JComboBox<>(icons);
        shapeSelector.setLocation(2,2);
        shapeSelector.setSize(new Dimension(60, height - 4));

        shapeSelector.getSelectedIndex();



        // Pass Shape input to draw tool
        shapeSelector.addActionListener((ActionEvent e) -> {
            JComboBox<ImageIcon> selector = (JComboBox<ImageIcon>) e.getSource();

            int index = java.util.Arrays.asList(icons).indexOf(selector.getSelectedItem());
            drawShapeTool.setShape(shapeIDs[index]);
        });




        add(shapeSelector);
    }
}
