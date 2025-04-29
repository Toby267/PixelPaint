package org.scc200g15.gui.toolbar;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.icons.IconManager;
import org.scc200g15.tools.Tool;
import org.scc200g15.tools.ToolManager;

public class PToolBarButton extends JButton{
    Tool tool;
    JPanel subJPanel = null;

    ImageIcon activeIcon;
    ImageIcon inactiveIcon;

    public PToolBarButton(Tool t, ImageIcon icon) {
        super(icon);
        this.tool = t;

        this.setBorder(new LineBorder(new Color(0, 0, 0, 0), 8, true)); 

        activeIcon = IconManager.changeColour(icon, IconManager.ACTIVE_ICON_COLOUR);
        inactiveIcon = IconManager.changeColour(icon, IconManager.INACTIVE_ICON_COLOUR);

        this.addActionListener((ActionEvent e) -> {
            ToolManager.toolChangeAction(t);
        });

    }
    public PToolBarButton(Tool t, ImageIcon icon, JPanel subJPanel) {
        this(t, icon);

        this.subJPanel = subJPanel;
        this.tool = t;
    }

    public void updateActiveState(){
        setOpaque(false);
        if(GUI.getInstance().getToolManager().isActiveTool(tool)){
            setIcon(activeIcon);
            setBackground(Color.DARK_GRAY);
            setOpaque(true);

            if(subJPanel != null) subJPanel.setVisible(true);
        }else{
            setIcon(inactiveIcon);
            setBackground(IconManager.VISIBLE_BACKGROUND_COLOUR);
            //setBackground(new Color(0,0,0,0));

            if(subJPanel != null) subJPanel.setVisible(false);
        }
        
        repaint();
    } 
    
    public Tool getTool() {
        return this.tool;
    }    
}

    
