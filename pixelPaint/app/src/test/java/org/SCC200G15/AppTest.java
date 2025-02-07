package org.SCC200G15;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test public void appHasAGreeting() {
        PixelPaint classUnderTest = new PixelPaint();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
