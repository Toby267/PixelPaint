package org.scc200g15.tools.squareSelect;

import java.awt.event.MouseEvent;

import org.scc200g15.gui.canvas.PCanvas;

public interface SelectState {
  public void mouseDragged(PCanvas c, MouseEvent e, SquareSelectTool context);

  public void mousePressed(PCanvas c, MouseEvent e, SquareSelectTool context);

  public void mouseReleased(PCanvas c, MouseEvent e, SquareSelectTool context);
}
