package org.scc200g15.gui.canvas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ZoomTest {
  PCanvas canvas;

  @BeforeEach
  void setUp() {
    canvas = new PCanvas();
  }

  @DisplayName("Normal ZoomIn")
  @Test
  public void testNormalZoomIn() {
    // Zoom the canvas
    canvas.zoomIn(new Point(1, 1));

    // Check the zoom level is correct
    assertEquals(1.05f, canvas.zoomLevel, "Zoom should zoomLevel should step by 0.05");

    // Check the zoom center is correct
    assertEquals(new Point(1, 1), canvas.zoomCenter, "Zoom center should be around the given point");
  }

  @DisplayName("Normal ZoomOut")
  @Test
  public void testNormalZoomOut() {
    // Zoom the canvas
    canvas.zoomOut(new Point(1, 1));

    // Check the zoom level is correct
    assertEquals(0.95f, canvas.zoomLevel, "Zoom should zoomLevel should step by 0.05");

    // Check the zoom center is correct
    assertEquals(new Point(1, 1), canvas.zoomCenter, "Zoom center should be around the given point");
  }

  @DisplayName("Bound ZoomOut")
  @Test
  public void boundNormalZoomOut() {
    canvas.zoomLevel = 0.05f;
    canvas.zoomCenter = new Point(0, 0);

    // Zoom the canvas
    canvas.zoomOut(new Point(1, 1));

    // Check the zoom level is correct
    assertEquals(0.05f, canvas.zoomLevel, "The zoom level should stop at 0.05 and not be allowed to go lower");

    // Check the zoom center is correct
    assertEquals(new Point(0, 0), canvas.zoomCenter, "Zoom center should not move");
  }
}