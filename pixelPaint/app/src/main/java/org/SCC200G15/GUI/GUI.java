package org.scc200g15.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.scc200g15.gui.menubar.PMenuBar;
import org.scc200g15.gui.sidebar.PSideBar;
import org.scc200g15.gui.statusbar.PStatusBar;
import org.scc200g15.gui.toolbar.PToolBar;


public class GUI extends JFrame {

    public GUI() {
        super("Pixel Paint");

        setLayout(new BorderLayout());

        // Add the MenuBar to the JFrame
        PMenuBar menuBar = new PMenuBar();
        setJMenuBar(menuBar);

        // Add Toolbar to the JFrame
        PToolBar toolBar = new PToolBar(this);
        add(toolBar, BorderLayout.NORTH);

        // Add the StatusBar to the JFrame
        PStatusBar statusBar = new PStatusBar(this);
        add(statusBar, BorderLayout.SOUTH);

        // Add the SideBar to the JFrame
        PSideBar sideBar = new PSideBar(this);
        add(sideBar, BorderLayout.WEST);

        // Canvas

        // General 
        setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
