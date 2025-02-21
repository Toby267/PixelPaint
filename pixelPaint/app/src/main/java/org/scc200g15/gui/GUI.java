package org.scc200g15.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.gui.layerselector.LayerSelectorPanel;
import org.scc200g15.gui.menubar.PMenuBar;
import org.scc200g15.gui.sidebar.PSideBar;
import org.scc200g15.gui.statusbar.PStatusBar;
import org.scc200g15.gui.toolbar.PToolBar;
import org.scc200g15.tools.DrawTool;
import org.scc200g15.image.Image;
import org.scc200g15.tools.HoverDemoTool;
import org.scc200g15.tools.PanZoomTool;
import org.scc200g15.tools.ToolManager;

import com.formdev.flatlaf.FlatLightLaf;


/**
 * The JFrame that is the brain of the application Controls the creation of the
 * canvas and the other parts of the UI
 */
public class GUI extends JFrame {

  private static GUI instance;

  public static GUI getInstance() {
    if (instance == null) {
      instance = new GUI();
    }

    return instance;
  }

  PCanvas canvas;
  ToolManager toolManager;
  LayerSelectorPanel layerSelector;

  private GUI() {
    super("Pixel Paint");

    try {
    UIManager.setLookAndFeel( new FlatLightLaf() );
} catch( Exception ex ) {
    System.err.println( "Failed to initialize LaF" );
}

    setLayout(new BorderLayout());

    // Add the MenuBar to the JFrame
    PMenuBar menuBar = new PMenuBar(this);
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
    canvas = new PCanvas();
    add(canvas);

    // Add the LayerSelector to the JFrame
    layerSelector = new LayerSelectorPanel(this);
    add(layerSelector, BorderLayout.EAST);

    // ToolManager
    PanZoomTool defaultTool = new PanZoomTool();
    toolManager = new ToolManager(canvas, defaultTool);

    // Register Tools
    HoverDemoTool hoverDemo = new HoverDemoTool();
    //toolManager.registerTool("hover", hoverDemo);

    //toolManager.setActiveTool("hover");

    DrawTool drawDemo = new DrawTool();
    toolManager.registerTool("draw", drawDemo);

    toolManager.setActiveTool("draw");


    // General
    setSize(1250, 750);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  public void setActiveImage(Image i){
    this.canvas.setActiveImage(i);
    layerSelector.redrawMenuUI();
  }
  public Image getActiveImage(){
    return canvas.getActiveImage();
  }

  public PCanvas getCanvas() {
    return canvas;
  }

  public LayerSelectorPanel getLayerSelector() {
    return layerSelector;
  }

}
