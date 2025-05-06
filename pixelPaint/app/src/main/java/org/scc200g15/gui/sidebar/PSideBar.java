package org.scc200g15.gui.sidebar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.scc200g15.gui.icons.IconManager;

import org.scc200g15.gui.GUI;

/**
 * The SideBar that will contain the color selector
 */
public class PSideBar extends JPanel {
  ColourPicker colourPicker = new ColourPicker();

  JLabel opacityValue = new JLabel("100%");
  int opacityPercent = 100;

  JSpinner rText = createTextField();
  JSpinner gText = createTextField();
  JSpinner bText = createTextField();

  private final JButton dropperButton = new JButton(IconManager.DROPPER_ICON);

  public PSideBar(GUI window) {
    setBorder(new BevelBorder(BevelBorder.LOWERED));

    setPreferredSize(new Dimension(200, window.getHeight()));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    sliderStyling();

    createDisplay();
  }

  private void createDisplay() {
    JPanel titlePanel = new JPanel();
    panelSetup(titlePanel);

    // Title
    JPanel title = new JPanel();
    panelSetup(title);
    title.add(titleLabel());
    titlePanel.add(title);

    // Colour Picker button    
    dropperButton.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));
    dropperButton.setOpaque(false);
    dropperButton.setContentAreaFilled(false);
    dropperButton.addActionListener((ActionEvent e) -> {
      GUI.getInstance().getToolManager().setActiveTool("dropper");
    });
    titlePanel.add(dropperButton);

    add(titlePanel);

    Dimension size = new Dimension(500, 200);

    // Colour Picker
    this.colourPicker.setAlignmentY(TOP_ALIGNMENT);
    add(this.colourPicker);
    colourPicker.setMinimumSize(size);
    colourPicker.setPreferredSize(size);
    colourPicker.setMaximumSize(size);

    add(Box.createVerticalStrut(8));
    
    // Opacity Label
    JPanel opacityLabel = new JPanel();
    panelSetup(opacityLabel);
    opacityLabel.add(Box.createHorizontalStrut(10));
    opacityLabel.add(new JLabel("Opacity"));
    add(opacityLabel);
    add(Box.createVerticalStrut(3));

    // Opacity Slider
    JPanel opacitySlider = new JPanel();
    panelSetup(opacitySlider);
    opacitySlider.add(opacitySlider());
    opacitySlider.add(Box.createHorizontalStrut(2));
    opacitySlider.add(opacityValueLabel());
    opacitySlider.add(Box.createHorizontalStrut(5));
    add(opacitySlider);
    add(Box.createVerticalStrut(8));

    // RGB Input
    JPanel inputRGB = new JPanel();
    panelSetup(inputRGB);
    inputRGB.add(Box.createHorizontalStrut(3));
    inputRGB.add(rText);
    inputRGB.add(Box.createHorizontalStrut(5));
    inputRGB.add(gText);
    inputRGB.add(Box.createHorizontalStrut(5));
    inputRGB.add(bText);
    inputRGB.add(Box.createHorizontalStrut(3));
    add(inputRGB);
    add(Box.createVerticalStrut(8));

    // Button
    JPanel button = new JPanel();
    panelSetup(button);
    button.add(button());
    add(button);

    // Bottom Spacing
    add(Box.createVerticalGlue());
  }

  private void panelSetup(JPanel panel) {
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
  }

  private JLabel titleLabel() {
    JLabel label = new JLabel("   Colour Selector");
    label.setPreferredSize(new Dimension(200, 30));
    label.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
    label.setHorizontalAlignment(JLabel.LEFT);
    return label;
  }

  private JLabel opacityValueLabel() {
    opacityValue.setPreferredSize(new Dimension(35, 20));
    opacityValue.setHorizontalAlignment(SwingConstants.CENTER);
    return opacityValue;
  }

  private JSlider opacitySlider() {
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

  private JButton button() {
    JButton button = new JButton("Get Colour");

    button.addActionListener((ActionEvent e) -> {
      this.colourPicker.setColor(getNormalisedColor(
        (int) rText.getValue(), 
        (int) gText.getValue(), 
        (int) bText.getValue(), 
        opacityPercent
      ));
    });

    return button;
  }

  private JSpinner createTextField() {
    JSpinner field = new JSpinner();
    field.setPreferredSize(new Dimension(50, 20));
    return field;
  }

  private void sliderStyling() {
    UIManager.put("Slider.thumbSize", new Dimension(12, 12));
    UIManager.put("Slider.trackWidth", 4);
  }


  private Color getNormalisedColor(int red, int green, int blue, int opacity) {
    return new Color(
      red     / (float) 255.0, 
      green   / (float) 255.0, 
      blue    / (float) 255.0, 
      opacity / (float) 100.0
    );
  }

  public Color getActiveColor() {
    Color c = this.colourPicker.getActiveColor();
    return getNormalisedColor(c.getRed(), c.getGreen(), c.getBlue(), opacityPercent);
  }


  public void setColourWheel(Color c) {
    this.colourPicker.setColor(c);
    this.repaint();
  }


}
