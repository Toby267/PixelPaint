package org.scc200g15.gui.toolbar;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.scc200g15.gui.GUI;
import org.scc200g15.tools.Tool;

/**
 * The ToolBar at the top of the window that will allow the user to change the active tool
 * 
 * @param window The JFrame to use to size the Panel
 */
public class PToolBar extends JPanel {
  ArrayList<PToolBarButton> toolbarButtons;

  public static int height = 32;

  public PToolBar(GUI window) {
    setPreferredSize(new Dimension(window.getWidth(), height));
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    JButton a = new JButton("Undo");
    JButton b = new JButton("Redo");

    a.addActionListener((ActionEvent e) -> {
        GUI.getInstance().getActiveImage().undoAction();
    });

    b.addActionListener((ActionEvent e) -> {
      GUI.getInstance().getActiveImage().redoAction();
    });

    this.add(a);
    this.add(b);

    toolbarButtons = new ArrayList<>();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for(PToolBarButton btn: toolbarButtons){
      btn.updateActiveState();
    }
  }

  public void addTool(Tool t, ImageIcon icon){
    // Create Button
    PToolBarButton toolButton = new PToolBarButton(t, icon);

    // Add Button
    add(toolButton);
    toolbarButtons.add(toolButton);

    revalidate();
    repaint();
  }
  public void addTool(Tool t, ImageIcon icon, JPanel subPanel){
    // Create Button
    PToolBarButton toolButton = new PToolBarButton(t, icon, subPanel);

    // Add Button
    add(toolButton);
    add(subPanel);

    subPanel.setVisible(false);

    toolbarButtons.add(toolButton);

    revalidate();
    repaint();
  }
}
