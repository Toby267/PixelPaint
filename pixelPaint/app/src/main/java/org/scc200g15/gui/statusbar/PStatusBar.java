package org.scc200g15.gui.statusbar;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.scc200g15.gui.GUI;

/**
 * The StatusBar at the bottom of the window that will show the user there current mouse pos and other useful info
 */
public class PStatusBar extends JPanel implements MouseMotionListener {
  JLabel zoomLabel;
  JLabel posLabel;

  JSpinner canvasX = new JSpinner(new SpinnerNumberModel(32, 0, 512, 1));
  JSpinner canvasY = new JSpinner(new SpinnerNumberModel(32, 0, 512, 1));

  public PStatusBar(GUI window) {
    setPreferredSize(new Dimension(window.getWidth(), 25));
    setLayout(new GridLayout(1,3));

    // Add Label to StatusBar
    zoomLabel = new JLabel("  Zoom: 100%", SwingConstants.LEFT);
    add(zoomLabel);

    setupCanvasResizer();

    posLabel = new JLabel("Mouse Position: (-1, -1)  ", SwingConstants.RIGHT);
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

  public void updateCanvasSizeValues(int w, int h)
  {
    canvasX.setValue(w);
    canvasY.setValue(h);
  }

  public void setupCanvasResizer() {
    JPanel canvasResizer = new JPanel();
    canvasResizer.setLayout(new BoxLayout(canvasResizer, BoxLayout.X_AXIS));

    JLabel statusLabel = new JLabel("Canvas Size: ");
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    canvasResizer.add(statusLabel);
    canvasResizer.add(Box.createHorizontalStrut(3));

    canvasResizer.add(new JLabel("X = "));
    canvasX.setMaximumSize(new Dimension(75, 30));
    canvasResizer.add(canvasX);

    canvasResizer.add(Box.createHorizontalStrut(8));

    canvasResizer.add(new JLabel("Y = "));
    canvasY.setMaximumSize(new Dimension(75, 30));
    canvasResizer.add(canvasY);

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
    add(canvasResizer);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
  }


}
