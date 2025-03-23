package org.scc200g15.tools.drawableTools;

import java.awt.Color;

import org.scc200g15.gui.GUI;

/**
 * Tool for drawing pixels on the canvas
 */
public class DrawTool extends Drawable {
  @Override
  protected Color getColor() {
    return GUI.getInstance().getSideBar().getActiveColor();
  }
}