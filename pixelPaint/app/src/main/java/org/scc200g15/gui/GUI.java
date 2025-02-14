package org.scc200g15.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.gui.layerselector.PLayerSelector;
import org.scc200g15.gui.menubar.PMenuBar;
import org.scc200g15.gui.sidebar.PSideBar;
import org.scc200g15.gui.statusbar.PStatusBar;
import org.scc200g15.gui.toolbar.PToolBar;
import org.scc200g15.tools.HoverDemoTool;
import org.scc200g15.tools.PanZoomTool;
import org.scc200g15.tools.ToolManager;

/**
 * The JFrame that is the brain of the application Controls the creation of the canvas and the other parts of the UI
 */
public class GUI extends JFrame {

  PCanvas canvas;
  ToolManager toolManager;

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
    
    // Add the LayerSelector to the JFrame
    PLayerSelector layerSelector = new PLayerSelector(this);
    add(layerSelector, BorderLayout.EAST);

    // Canvas
    canvas = new PCanvas();
    add(canvas);

    // ToolManager
    PanZoomTool defaultTool = new PanZoomTool();
    toolManager = new ToolManager(canvas, defaultTool);

    // Register Tools
    HoverDemoTool hoverDemo = new HoverDemoTool();
    toolManager.registerTool("hover", hoverDemo);

    toolManager.setActiveTool("hover");

    // General
    setSize(500, 500);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  public PCanvas getCanvas() {
    return canvas;
  }
}
