package org.scc200g15.gui.canvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JSpinner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.scc200g15.gui.GUI;
import org.scc200g15.gui.sidebar.PSideBar;

public class ColourPickerTest {
  PSideBar sideBar;

  @BeforeEach
  void setUp() {
    GUI gui = GUI.getInstance();
    sideBar = new PSideBar(gui);
  }

  @DisplayName("Color Accuracy")
  @Test
  public void testColorAccuracy() throws InvocationTargetException, InterruptedException {
    JSpinner rText, gText, bText;
    JButton button;

    assertNotNull(sideBar, "PSideBar is not initialised.");

    try {
      Field rField = sideBar.getClass().getDeclaredField("rText");
      Field gField = sideBar.getClass().getDeclaredField("gText");
      Field bField = sideBar.getClass().getDeclaredField("bText");
      Method buttonMethod = sideBar.getClass().getDeclaredMethod("button");

      rField.setAccessible(true);
      gField.setAccessible(true);
      bField.setAccessible(true);
      buttonMethod.setAccessible(true);
      
      rText = (JSpinner) rField.get(sideBar);
      gText = (JSpinner) gField.get(sideBar);
      bText = (JSpinner) bField.get(sideBar);
      button = (JButton) buttonMethod.invoke(sideBar);  
    } catch (Exception e) {
      throw new RuntimeException("One or more Fields: rText, gText, bText doesn't/don't exist in PSideBar.");
    }
    
    int red = 128;
    int green = 64;
    int blue = 255;

    rText.setValue(red);
    gText.setValue(green);
    bText.setValue(blue);

    assertEquals(red, rText.getValue(), "Red failed to be loaded in.");
    assertEquals(green, gText.getValue(), "Green failed to be loaded in.");
    assertEquals(blue, bText.getValue(), "Blue failed to be loaded in.");

    button.doClick();

    Color c = sideBar.getActiveColor();
    System.out.println("Color: (" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + ", " + c.getAlpha() + ")");

    assertEquals(red, c.getRed(), "Red component found.");
    assertEquals(green, c.getGreen(), "Green component found.");
    assertEquals(blue, c.getBlue(), "Blue component found.");

  }

}
