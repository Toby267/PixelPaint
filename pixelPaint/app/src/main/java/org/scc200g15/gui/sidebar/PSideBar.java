package org.scc200g15.gui.sidebar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.scc200g15.gui.GUI;

/**
 * The SideBar that will contain the color selector
 */
public class PSideBar extends JPanel {
  ColourPicker colourPicker = new ColourPicker();

  JLabel opacityValue = new JLabel("100%");
  int opacityPercent = 100;

  public PSideBar(GUI window) {
    setBorder(new BevelBorder(BevelBorder.LOWERED));

    setPreferredSize(new Dimension(200, window.getHeight()));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    sliderStyling();

    // Title
    JPanel title = new JPanel();
    panelSetup(title);
    title.add(titleLabel());
    add(title);

    // Colour Picker
    this.colourPicker.setAlignmentY(TOP_ALIGNMENT);
    add(this.colourPicker);
    add(Box.createVerticalStrut(8)); 
    
    // Opacity Slider
    JPanel opacitySlider = new JPanel();
    panelSetup(opacitySlider);
    opacitySlider.add(opacitySlider());
    opacitySlider.add(Box.createHorizontalStrut(2));
    opacitySlider.add(opacityValueLabel());
    opacitySlider.add(Box.createHorizontalStrut(5));
    add(opacitySlider);
    add(Box.createVerticalStrut(8));

    // Button
    JPanel button = new JPanel();
    panelSetup(button);
    button.add(button());
    add(button);

    // Bottom Spacing
    add(Box.createVerticalStrut(370));
  }

  public void panelSetup(JPanel panel) {
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
  }

  public JLabel titleLabel() {
    JLabel label = new JLabel("Colour Selector");
    label.setPreferredSize(new Dimension(200, 20));
    label.setHorizontalAlignment(JLabel.CENTER);
    return label;
  }

  public JLabel opacityValueLabel() {
    opacityValue.setPreferredSize(new Dimension(35, 20));
    opacityValue.setHorizontalAlignment(SwingConstants.CENTER);
    return opacityValue;
  }

  public JSlider opacitySlider() {
    JSlider slider = new JSlider(0, 100, 100);
    slider.setPreferredSize(new Dimension(120, 20));

    slider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        opacityPercent = ((JSlider) e.getSource()).getValue();
        opacityValue.setText(opacityPercent + "%");
      }
    });

    return slider;
  }

  public JButton button() {
    JButton button = new JButton("PRESS");

    button.addActionListener((ActionEvent e) -> {
      Color c = new Color(248, 122, 250); // ! Manually add the changes, moves it into input text
      this.colourPicker.setColor(c);

      Color out = getActiveColor();
      System.out.println("Active: (" + out.getRed() + ", " + out.getGreen() + ", " + out.getBlue() + ", " + out.getAlpha() + ")");
    });

    return button;
  }


  public void sliderStyling() {
    UIManager.put("Slider.thumbSize", new Dimension(12, 12));
    UIManager.put("Slider.trackWidth", 4);
  }


  public Color getActiveColor() {
    Color c = this.colourPicker.getActiveColor();
    return new Color(
      c.getRed()     / (float) 255.0, 
      c.getGreen()   / (float) 255.0, 
      c.getBlue()    / (float) 255.0, 
      opacityPercent / (float) 100.0
    );
  }


}
