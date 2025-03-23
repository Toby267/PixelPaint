package org.scc200g15.tools.drawableTools;

import java.awt.Color;

/**
 * Tool for erasing pixels on the canvas by setting them to transparent
 * Essentially a wrapper class for the DrawTool, only using what is needed from it
 */
public class EraserTool extends Drawable {
  @Override
  protected Color getColor() {
    return new Color(0, 0, 0, 0);
  }
}