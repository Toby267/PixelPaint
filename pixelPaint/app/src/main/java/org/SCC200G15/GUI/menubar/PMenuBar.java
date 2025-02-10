package org.scc200g15.gui.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class PMenuBar extends JMenuBar{
    public PMenuBar() {
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
