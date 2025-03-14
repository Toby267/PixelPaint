package org.scc200g15.gui.canvas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scc200g15.tools.drawableTools.DrawTool;

public class DrawToolTest {
    PCanvas canvas = new PCanvas();
    DrawTool drawTool = new DrawTool();

    @BeforeEach
    void DrawToolTest() {
        canvas = new PCanvas();
        drawTool = new DrawTool();
    }

    @DisplayName("Colour draw test")
    @Test
    void ColourTest() {
        // drawTool.setColour(255, 0, 0);
        // drawTool.setOppacity(0);
        
        // MouseEvent e = new MouseEvent(canvas, 0, 0, 0, 0, 0, 0, false);
        // drawTool.mousePressed(canvas, e);

        //Colour col = drawTool.get
    }

    @DisplayName("Oppacity draw test")
    @Test
    void OppacityTest() {

    }

    @DisplayName("Square draw test")
    @Test
    void SquareTest() {

    }

    @DisplayName("Circle draw test")
    @Test
    void CircleTest() {
        
    }

    @DisplayName("Triangle draw test")
    @Test
    void TriangleTest() {
        
    }
}
