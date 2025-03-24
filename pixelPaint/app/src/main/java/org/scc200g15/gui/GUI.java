package org.scc200g15.gui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.scc200g15.gui.canvas.PCanvas;
import org.scc200g15.gui.icons.IconManager;
import org.scc200g15.gui.layerselector.LayerSelectorPanel;
import org.scc200g15.gui.menubar.PMenuBar;
import org.scc200g15.gui.sidebar.PSideBar;
import org.scc200g15.gui.statusbar.PStatusBar;
import org.scc200g15.gui.toolbar.DrawSubPanel;
import org.scc200g15.gui.toolbar.FillSubPanel;
import org.scc200g15.gui.toolbar.PToolBar;
import org.scc200g15.gui.toolbar.SelectSubPanel;
import org.scc200g15.image.Image;
import org.scc200g15.tools.FillTool;
import org.scc200g15.tools.PanZoomTool;
import org.scc200g15.tools.StarTool;
import org.scc200g15.tools.Tool;
import org.scc200g15.tools.ToolManager;
import org.scc200g15.tools.drawableTools.DrawTool;
import org.scc200g15.tools.drawableTools.EraserTool;
import org.scc200g15.tools.squareSelect.SquareSelectTool;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;


/**
 * The JFrame that is the brain of the application Controls the creation of the
 * canvas and the other parts of the UI
 */
public class GUI extends JFrame {

  boolean isDarkMode = false;

  private static GUI instance;

  public static GUI getInstance() {
    if (instance == null) {
      instance = new GUI();
    }

    return instance;
  }

  PCanvas canvas;
  ToolManager toolManager;
  PToolBar toolBar;
  PMenuBar menuBar;
  PSideBar sideBar;
  LayerSelectorPanel layerSelector;
  PStatusBar statusBar;
  FillSubPanel fillSP;

  private void registerTool(Tool tool, ImageIcon icon, String toolID, String name){
    toolManager.registerTool(toolID, tool);
    toolBar.addTool(tool, icon);
    menuBar.addTool(tool, name);
  }
  private void registerTool(Tool tool, ImageIcon icon, String toolID, String name, JPanel subPanel){
    toolManager.registerTool(toolID, tool);
    toolBar.addTool(tool, icon, subPanel);
    menuBar.addTool(tool, name);
  }

  private void registerTools(){
    SquareSelectTool squareSelect = new SquareSelectTool();
    SelectSubPanel ssp = new SelectSubPanel(PToolBar.height, squareSelect);
    registerTool(squareSelect, IconManager.SQUARE_SELECT_ICON, "squareSelect", "Square Select Tool", ssp);
    
    DrawTool drawTool = new DrawTool();
    DrawSubPanel dsp = new DrawSubPanel(PToolBar.height, drawTool);
    registerTool(drawTool, IconManager.DRAW_ICON, "draw", "Draw Tool", dsp);

    EraserTool eraseTool = new EraserTool();
    DrawSubPanel esp = new DrawSubPanel(PToolBar.height, eraseTool);
    registerTool(eraseTool, IconManager.ERASE_ICON, "erase", "Erase Tool", esp);

    FillTool fillTool = new FillTool();
    fillSP = new FillSubPanel(PToolBar.height);
    registerTool(fillTool, IconManager.FILL_ICON, "fill", "Fill Tool", fillSP);

    StarTool starTool = new StarTool();
    registerTool(starTool, IconManager.STAR_ICON, "star", "Star Tool");
  }

  private GUI() {
    super("Pixel Paint");

    try {
    UIManager.setLookAndFeel( new FlatLightLaf() );
} catch( UnsupportedLookAndFeelException ex ) {
    System.err.println( "Failed to initialize LaF" );
}

    setLayout(new BorderLayout());

    // Add the MenuBar to the JFrame
    menuBar = new PMenuBar(this);
    setJMenuBar(menuBar);

    // Add Toolbar to the JFrame
    toolBar = new PToolBar(this);
    add(toolBar, BorderLayout.NORTH);

    // Add the StatusBar to the JFrame
    statusBar = new PStatusBar(this);
    add(statusBar, BorderLayout.SOUTH);

    // Add the SideBar to the JFrame
    sideBar = new PSideBar(this);
    add(sideBar, BorderLayout.WEST);

    // Canvas
    canvas = new PCanvas();
    add(canvas);

    canvas.addMouseMotionListener(statusBar);

    // Add the LayerSelector to the JFrame
    layerSelector = new LayerSelectorPanel(this);
    add(layerSelector, BorderLayout.EAST);

    // ToolManager
    PanZoomTool defaultTool = new PanZoomTool();
    toolBar.addTool(defaultTool, IconManager.PAN_ZOOM_ICON);
    menuBar.addTool(defaultTool, "Pan Zoom");
    toolManager = new ToolManager(canvas, defaultTool);

    registerTools();

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

  public ToolManager getToolManager() {
    return toolManager;
  }
  public PToolBar getToolBar(){
    return toolBar;
  }
  public void repaintToolBar(){
    toolBar.repaint();
  }

  public PSideBar getSideBar() {
    return sideBar;
  }

  public void toggleDarkMode(){
    System.out.println(isDarkMode);
    if(isDarkMode){
      try {
        UIManager.setLookAndFeel( new FlatLightLaf() );
      } catch( UnsupportedLookAndFeelException ex ) {
        System.err.println( "Failed to initialize LaF" );
      }
    }else{
      try {
        UIManager.setLookAndFeel( new FlatDarkLaf() );
      } catch( UnsupportedLookAndFeelException ex ) {
        System.err.println( "Failed to initialize LaF" );
      }
    }

    SwingUtilities.updateComponentTreeUI(this);

    isDarkMode = !isDarkMode;
  }
  public PStatusBar getStatusBar(){
    return statusBar;
  }

  public int getFillTolerance(){
    return fillSP.getTolerance();
  }
}
