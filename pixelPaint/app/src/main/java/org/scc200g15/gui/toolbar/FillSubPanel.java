package org.scc200g15.gui.toolbar;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JSlider;

public class FillSubPanel extends JPanel{
    JSlider toleranceSlider;

    public FillSubPanel(int height) {
        super(null);
        
        setMaximumSize(new Dimension(128, height));
        
        toleranceSlider = new JSlider();
        toleranceSlider.setValue(0);
        
        toleranceSlider.setLocation(new Point(2,2));
        toleranceSlider.setSize(new Dimension(124 ,height - 4));

        toleranceSlider.setToolTipText("Tolerance");

        this.add(toleranceSlider);

    }

    public int getTolerance(){
        return toleranceSlider.getValue();
    }
}
