package org.scc200g15;

import org.scc200g15.gui.GUI;
import org.scc200g15.image.Image;

public class PixelPaint {
  public static void main(String[] args) {
    GUI.getInstance().getCanvas().setActiveImage(new Image());
  }
}
