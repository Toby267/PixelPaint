package org.scc200g15.gui.statusbar;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.scc200g15.gui.GUI;

/**
 * The StatusBar at the bottom of the window that will show the user there current mouse pos and other useful info
 */
public class PStatusBar extends JPanel implements MouseMotionListener {
  JLabel zoomLabel;
  JLabel posLabel;

  public PStatusBar(GUI window) {

    setPreferredSize(new Dimension(window.getWidth(), 16));
    setLayout(new GridLayout(1,2));

    // Add Label to StatusBar
    zoomLabel = new JLabel("Zoom: 100%", SwingConstants.CENTER);
    add(zoomLabel);

    posLabel = new JLabel("Mouse Position: (-1, -1)", SwingConstants.CENTER);
    add(posLabel);
  }

  public void updateZoom(float zoom){
    int scaledZoom = (int)(zoom * 1000);
    float scaledP = scaledZoom / 10;

    zoomLabel.setText(Float.toString(scaledP) + "%");
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    Point2D point = GUI.getInstance().getCanvas().getPixelPoint(e.getPoint());

    StringBuilder sb = new StringBuilder("Mouse Position: (");
    sb.append((int) point.getX());
    sb.append(",");
    sb.append((int) point.getY());
    sb.append(")");
    posLabel.setText(sb.toString());

  }

  @Override
  public void mouseDragged(MouseEvent e) {
  }
}
