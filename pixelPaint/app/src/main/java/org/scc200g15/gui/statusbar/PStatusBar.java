package org.scc200g15.gui.statusbar;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.scc200g15.gui.GUI;

/**
 * The StatusBar at the bottom of the window that will show the user there current mouse pos and other useful info
 */
public class PStatusBar extends JPanel {
  JSpinner canvasX = new JSpinner(new SpinnerNumberModel(32, 0, 512, 1));
  JSpinner canvasY = new JSpinner(new SpinnerNumberModel(32, 0, 512, 1));

  public PStatusBar(GUI window) {
    setBorder(new BevelBorder(BevelBorder.LOWERED));

    setPreferredSize(new Dimension(window.getWidth(), 30));
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    // Add Label to StatusBar
    JLabel statusLabel = new JLabel("Canvas Size: ");
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    add(statusLabel);
    add(Box.createHorizontalStrut(3));

    setupCanvasResizer();

  }

  public void setupCanvasResizer() {
    add(new JLabel("X = "));
    canvasX.setMaximumSize(new Dimension(75, 30));
    add(canvasX);

    add(Box.createHorizontalStrut(8));

    add(new JLabel("Y = "));
    canvasY.setMaximumSize(new Dimension(75, 30));
    add(canvasY);

    canvasX.addChangeListener(new ChangeListener() {      
      @Override
      public void stateChanged(ChangeEvent e) {
        GUI.getInstance().getActiveImage().changeImageWidth((int) canvasX.getValue());
      }
    });

    canvasY.addChangeListener(new ChangeListener() {      
      @Override
      public void stateChanged(ChangeEvent e) {
        GUI.getInstance().getActiveImage().changeImageHeight((int) canvasY.getValue());
      }
    });
  }


}
