package org.scc200g15.gui.menubar;

import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
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
    JMenu viewMenu = new JMenu("view");
    JMenu helpMenu = new JMenu("help");

    viewMenu.add(new AbstractAction("Toggle Dark Mode") {

		  @Override
		  public void actionPerformed(ActionEvent e) {
			  GUI.getInstance().toggleDarkMode();
		  }

    });

    // Add Menus to MenuBar
    add(fileMenu);
    add(editMenu);
    add(helpMenu);
    add(viewMenu);
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
