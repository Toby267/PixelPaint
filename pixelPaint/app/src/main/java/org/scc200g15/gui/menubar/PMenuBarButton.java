package org.scc200g15.gui.menubar;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import org.scc200g15.gui.GUI;
import org.scc200g15.tools.Tool;
import org.scc200g15.tools.ToolManager;

public class PMenuBarButton extends JMenuItem {
    Tool tool;

    public PMenuBarButton(Tool tool, String name) {
        super(new AbstractAction(name){
            @Override
            public void actionPerformed(ActionEvent e) {
                ToolManager.toolChangeAction(tool);
            }
            
        });

        this.tool = tool;
    }

    public void updateActiveState(){
        setBackground(GUI.getInstance().getToolManager().isActiveTool(tool) ? Color.blue : Color.gray);
        repaint();
    } 
}
