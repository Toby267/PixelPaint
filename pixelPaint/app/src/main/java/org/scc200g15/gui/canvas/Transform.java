package org.scc200g15.gui.canvas;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Transform extends AffineTransform {
  /**
   * Apply a transformation scale around a point
   * 
   * @param SF     the scale factor to scale by
   * @param Center the center point to scale around
   */
  public void scaleAboutPoint(float SF, Point2D Center) {
    // Translate so the center point is at 0,0
    translate(Center.getX(), Center.getY());
    // Apply the scale
    scale(SF);
    // Translate the center point back
    translate(-Center.getX(), -Center.getY());
  }

  /**
   * Apply a scale in both the x and y axis
   * 
   * @param SF the scale factor to apply
   */
  public void scale(double SF) {
    scale(SF, SF);
  }
}
