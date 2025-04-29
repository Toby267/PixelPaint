package org.scc200g15.gui.canvas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.awt.Point;
import java.awt.geom.AffineTransform;

public class TransformTest {
  Transform t;
  AffineTransform at;
  double[] atMatrix;
  double[] tMatrix;

  @BeforeEach
  void setUp() {
    t = new Transform();
    at = new AffineTransform();
    atMatrix = new double[6];
    tMatrix = new double[6];
  }

  @DisplayName("Scale x,y")
  @Test
  void scaleTest() {
    at.scale(10, 10);
    t.scale(10);

    at.getMatrix(atMatrix);
    t.getMatrix(tMatrix);

    assertArrayEquals(atMatrix, tMatrix, "Scale(sf) should be preform the same scale as scale(sf,sf)");
  }

  @DisplayName("Scale About Point")
  @Test
  void scaleAboutPointTest() {
    at.translate(1, 1);
    at.scale(10, 10);
    at.translate(-1, -1);

    t.scaleAboutPoint(10, new Point(1, 1));

    at.getMatrix(atMatrix);
    t.getMatrix(tMatrix);

    assertArrayEquals(atMatrix, tMatrix, "Scale about point should do the same as applying the transforms individually");
  }
}
