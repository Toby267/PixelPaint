package org.scc200g15.gui.toolbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.icons.IconManager;
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

    toolbarButtons = new ArrayList<>();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for(PToolBarButton btn: toolbarButtons){
      btn.updateActiveState();
    }
  }

  public void addTool(Tool t, ImageIcon icon) {
    // Create Button
    PToolBarButton toolButton = new PToolBarButton(t, icon);
    toolButton.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));

    // Add Button
    add(toolButton);
    toolbarButtons.add(toolButton);


    revalidate();
    repaint();
  }

  public void addTool(Tool t, ImageIcon icon, JPanel subPanel) {
    // Create Button
    PToolBarButton toolButton = new PToolBarButton(t, icon, subPanel);
    toolButton.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));

    // Add Button
    add(toolButton);
    add(subPanel);

    subPanel.setVisible(false);

    toolbarButtons.add(toolButton);

    revalidate();
    repaint();
  }

  public void addUndoRedo() {
    JButton undoBTN = new JButton(IconManager.UNDO_ICON);
    undoBTN.setBorder(new LineBorder(new Color(0, 0, 0, 0), 8, true)); 
    JButton redoBTN = new JButton(IconManager.REDO_ICON);
    redoBTN.setBorder(new LineBorder(new Color(0, 0, 0, 0), 8, true));

    undoBTN.addActionListener((ActionEvent e) -> {
      GUI.getInstance().getActiveImage().undoAction();
    });

    redoBTN.addActionListener((ActionEvent e) -> {
      GUI.getInstance().getActiveImage().redoAction();
    });

    this.add(undoBTN);
    this.add(redoBTN);
  }
}
