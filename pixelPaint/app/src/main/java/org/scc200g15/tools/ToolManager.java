package org.scc200g15.tools;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.canvas.PCanvas;

/**
 * The toolManager is responsible for passing the mouse events to the current active tool or the default tool if no tool is active
 */
public class ToolManager implements MouseMotionListener, MouseListener, MouseWheelListener {

  // Map of all tools
  private Map<String, Tool> tools;

  private Tool defaultTool;
  private Tool activeTool = null;
  private PCanvas canvas;

  /**
   * Constructor that takes a canvas to work on and a default tool
   * 
   * @param canvas      the canvas for tools to work on
   * @param defaultTool the tool to be active if no tools are selected
   */
  public ToolManager(PCanvas canvas, Tool defaultTool) {
    this.defaultTool = defaultTool;
    tools = new HashMap<>();

    this.canvas = canvas;
    canvas.registerToolManager(this);
  }

  /**
   * Constructor that takes a canvas to work on
   * 
   * @param canvas the canvas for tools to work on
   */
  public ToolManager(PCanvas canvas) {
    tools = new HashMap<>();

    this.canvas = canvas;
    canvas.registerToolManager(this);
  }

  /**
   * Register a tool
   * 
   * @param ID the ID of the tool
   * @param t  the tool
   */
  public void registerTool(String ID, Tool t) {
    if (!tools.containsKey(ID)) {
      tools.put(ID, t);
    } else {
      throw new Error("A tool exists with ID: " + ID);
    }
  }

  /**
   * Get a tool based on the ID
   * 
   * @param ID the ID of the tool you want to get
   */
  public Tool getTool(String ID) {
    if (tools.containsKey(ID)) {
      return tools.get(ID);
    } else {
      throw new Error("No tool exists with ID: " + ID);
    }
  }

  /**
   * Set the active tool
   * 
   * @param activeTool the tool you want to be the active tool
   */
  public void setActiveTool(Tool activeTool) {
    this.activeTool = activeTool;
    GUI.getInstance().repaintToolBar();
  }

  /**
   * Set the active tool
   * 
   * @param ID the ID of the tool you want to be the active tool
   */
  public void setActiveTool(String ID) {
    this.activeTool = getTool(ID);
    GUI.getInstance().repaintToolBar();
  }

  /**
   * sets up the default tool
   */
  public void setDefaultTool(Tool defaultTool){
    this.defaultTool = defaultTool;
  }

  /**
   * sets active tool to the default tool
   */
  public void setDefault(){
    this.activeTool = defaultTool;
  }

  public boolean isActiveTool(Tool t){
    if(activeTool == null) return t.equals(defaultTool);
    else return  t.equals(activeTool);
  }

  // Pass through to the active or default tool
  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    if (activeTool != null)
      activeTool.mouseWheelMoved(canvas, e);
    else
      defaultTool.mouseWheelMoved(canvas, e);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (activeTool != null)
      activeTool.mouseClicked(canvas, e);
    else
      defaultTool.mouseClicked(canvas, e);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (activeTool != null)
      activeTool.mousePressed(canvas, e);
    else
      defaultTool.mousePressed(canvas, e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (activeTool != null)
      activeTool.mouseReleased(canvas, e);
    else
      defaultTool.mouseReleased(canvas, e);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    if (activeTool != null)
      activeTool.mouseEntered(canvas, e);
    else
      defaultTool.mouseEntered(canvas, e);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    if (activeTool != null)
      activeTool.mouseExited(canvas, e);
    else
      defaultTool.mouseExited(canvas, e);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (activeTool != null)
      activeTool.mouseDragged(canvas, e);
    else
      defaultTool.mouseDragged(canvas, e);
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    if (activeTool != null)
      activeTool.mouseMoved(canvas, e);
    else
      defaultTool.mouseMoved(canvas, e);
  }

}
