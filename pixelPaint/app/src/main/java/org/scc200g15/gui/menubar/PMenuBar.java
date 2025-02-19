package org.scc200g15.gui.menubar;

import java.awt.Window;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.scc200g15.gui.GUI;

/**
 * The MenuBar that will appear at the very top of the menu
 */
public class PMenuBar extends JMenuBar {
  public PMenuBar(GUI window) {
    // Create Menus
    JMenu fileMenu = new JMenu("file");
    JMenu editMenu = new JMenu("edit");
    JMenu helpMenu = new JMenu("help");

    // Create Menu Items
    JMenuItem size = new JMenuItem("size");
    editMenu.add(size);

    // Add Menus to MenuBar
    add(fileMenu);
    add(editMenu);
    add(helpMenu);
  }
}
