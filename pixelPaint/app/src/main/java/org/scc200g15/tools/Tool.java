package org.scc200g15.tools;

import java.awt.event.MouseEvent;

import org.scc200g15.gui.canvas.PCanvas;

/**
 * Interface all tools must conform to Passes through the current canvas and the mouse action which happened
 */
public interface Tool {

  public void mouseDragged(PCanvas c, java.awt.event.MouseEvent e);

  public void mousePressed(PCanvas c, java.awt.event.MouseEvent e);

  public void mouseWheelMoved(PCanvas c, java.awt.event.MouseWheelEvent e);

  public void mouseClicked(PCanvas c, MouseEvent e);

  public void mouseMoved(PCanvas c, java.awt.event.MouseEvent e);

  public void mouseExited(PCanvas c, java.awt.event.MouseEvent e);

  public void mouseReleased(PCanvas c, java.awt.event.MouseEvent e);

  public void mouseEntered(PCanvas c, java.awt.event.MouseEvent e);

  public void keyTyped(PCanvas c, java.awt.event.KeyEvent e);

  public void keyPressed(PCanvas c, java.awt.event.KeyEvent e);

  public void keyReleased(PCanvas c, java.awt.event.KeyEvent e);

}
