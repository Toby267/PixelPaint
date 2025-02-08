package org.scc200g15.tools;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

import org.scc200g15.gui.canvas.PCanvas;

public class ToolManager implements MouseMotionListener, MouseListener, MouseWheelListener{

    private Map<String, Tool> tools;

    private Tool defaultTool;
    private Tool activeTool = null;
    private PCanvas canvas;

    public ToolManager(PCanvas canvas, Tool defaultTool){
        this.defaultTool = defaultTool;
        tools = new HashMap<>();

        this.canvas = canvas;
        canvas.registerToolManager(this);
    }

    public void registerTool(String ID, Tool t){}
    public Tool getTool(String ID){
        if(tools.containsKey(ID)){
            return tools.get(ID);
        }else{
            throw new Error("No tool exists with ID: " + ID);
        }
    }
    public void setActiveTool(Tool activeTool) {
        this.activeTool = activeTool;
    }
    public void setActiveTool(String ID) {
        this.activeTool = getTool(ID);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(activeTool != null) activeTool.mouseWheelMoved(canvas, e);
        else defaultTool.mouseWheelMoved(canvas, e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(activeTool != null) activeTool.mouseClicked(canvas, e);
        else defaultTool.mouseClicked(canvas, e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(activeTool != null) activeTool.mousePressed(canvas, e);
        else defaultTool.mousePressed(canvas, e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(activeTool != null) activeTool.mouseReleased(canvas, e);
        else defaultTool.mouseReleased(canvas, e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(activeTool != null) activeTool.mouseEntered(canvas, e);
        else defaultTool.mouseEntered(canvas, e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(activeTool != null) activeTool.mouseExited(canvas, e);
        else defaultTool.mouseExited(canvas, e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(activeTool != null) activeTool.mouseDragged(canvas, e);
        else defaultTool.mouseDragged(canvas, e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(activeTool != null) activeTool.mouseMoved(canvas, e);
        else defaultTool.mouseMoved(canvas, e);
    }
    
}
