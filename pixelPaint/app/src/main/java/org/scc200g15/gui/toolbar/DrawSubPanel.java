package org.scc200g15.gui.toolbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.scc200g15.gui.icons.IconManager;
import org.scc200g15.gui.numberinput.NumberInput;
import org.scc200g15.tools.DrawTool;
import org.scc200g15.tools.shapes.Circle;
import org.scc200g15.tools.shapes.Square;
import org.scc200g15.tools.shapes.Star;
import org.scc200g15.tools.shapes.Triangle;

public class DrawSubPanel extends JPanel{

    DrawTool drawTool;

    public DrawSubPanel(int height, DrawTool tool) {
        super(null);

        drawTool = tool;
        
        setMaximumSize(new Dimension(128, height));

        Map<ImageIcon, String> shapes = new HashMap<>();
        shapes.put(IconManager.CIRCLE_ICON, "Circle");
        shapes.put(IconManager.SQUARE_ICON, "Square");
        shapes.put( IconManager.TRIANGLE_ICON, "Triangle");
        shapes.put(IconManager.STAR_ICON, "Star");

        Set<ImageIcon> keys = shapes.keySet();
        ImageIcon[] icons = keys.toArray(new ImageIcon[keys.size()]);

        JComboBox<ImageIcon> shapeSelector = new JComboBox<>(icons);
        shapeSelector.setLocation(2,2);
        shapeSelector.setSize(new Dimension(60, height - 4));

        shapeSelector.getSelectedIndex();

        NumberInput sizeInput = new NumberInput(1,100);
        sizeInput.setLocation(64,2);
        sizeInput.setSize(new Dimension(60, height - 4));

        // Pass Shape input to draw tool
        shapeSelector.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                JComboBox<ImageIcon> selector = (JComboBox<ImageIcon>) e.getSource();
                String shapeID = shapes.get(selector.getSelectedItem());
                tool.setShape(shapeID);
			}
            
        });

        // Pass Size input to draw tool
        sizeInput.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				NumberInput input = (NumberInput)e.getSource();
                int val = (int)input.getValue();
                tool.setSize(val);
			}
        });

        add(sizeInput);
        add(shapeSelector);
    }
}
