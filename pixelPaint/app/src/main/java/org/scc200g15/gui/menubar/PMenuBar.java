package org.scc200g15.gui.menubar;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.scc200g15.gui.GUI;
import org.scc200g15.tools.Tool;

/**
 * The MenuBar that will appear at the very top of the menu
 */
public class PMenuBar extends JMenuBar {
  JMenu editMenu;
  ArrayList<PMenuBarButton> editMenuButtons;

  public PMenuBar(GUI window) {
    editMenuButtons = new ArrayList<>();

    // Create Menus
    JMenu fileMenu = new JMenu("file");
    editMenu = new JMenu("edit");
    JMenu helpMenu = new JMenu("help");

    // Add Menus to MenuBar
    add(fileMenu);
    add(editMenu);
    add(helpMenu);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for(PMenuBarButton btn: editMenuButtons){
      btn.updateActiveState();
    }
  }

  public void addTool(Tool t, String name){
    // Create Button
    PMenuBarButton toolButton = new PMenuBarButton(t, name);

    // Add Button
    editMenu.add(toolButton);
    editMenuButtons.add(toolButton);

    revalidate();
    repaint();
  }
}
