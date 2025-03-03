package org.scc200g15.gui.toolbar;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import org.scc200g15.gui.GUI;
import org.scc200g15.tools.Tool;

public class PToolBarButton extends JButton{
    Tool tool;

    public PToolBarButton(Tool t, ImageIcon icon) {
        super(icon);

        this.tool = t;

        this.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true)); 

        this.addActionListener((ActionEvent e) -> {

            if( GUI.getInstance().getToolManager().isActiveTool(t)){
                GUI.getInstance().getToolManager().setDefault();
            }else{
                GUI.getInstance().getToolManager().setActiveTool(t);
            }           
        });

    }

    public void updateActiveState(){
        setBackground(GUI.getInstance().getToolManager().isActiveTool(tool) ? Color.GRAY : Color.DARK_GRAY);
        repaint();
    } 
}

    
