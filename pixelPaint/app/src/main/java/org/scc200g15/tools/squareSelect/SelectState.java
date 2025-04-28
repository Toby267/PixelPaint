package org.scc200g15.tools.squareSelect;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.scc200g15.gui.canvas.PCanvas;

/**
 * interface that all square select states must adhere to
 */
public interface SelectState {
  public void mouseDragged(PCanvas c, MouseEvent e, SquareSelectTool context);

  public void mousePressed(PCanvas c, MouseEvent e, SquareSelectTool context);

  public void mouseReleased(PCanvas c, MouseEvent e, SquareSelectTool context);

  public void keyPressed(PCanvas c, KeyEvent e, SquareSelectTool context);
}
