package org.scc200g15.gui.layerselector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Image;

import java.awt.event.ActionEvent;

public class LayerMenuItem extends JPanel {
    private static String visibleIconPath = "/Icons/open_eye_icon.png";
    private static String invisibleIconPath = "/Icons/closed_eye_icon.png";
    private static String trashIconPath = "/Icons/trash_icon.png";
  
    public ImageIcon createImageIcon(int x, int y, String path) {
      return new ImageIcon(
        new ImageIcon(getClass().getResource(path))
          .getImage()
          .getScaledInstance(x, y, Image.SCALE_SMOOTH)
      );
    }
  
    private ImageIcon visibleIcon = createImageIcon(30, 30, visibleIconPath);
    private ImageIcon invisibleIcon = createImageIcon(30, 30, invisibleIconPath);
    private ImageIcon trashIcon = createImageIcon(25, 25, trashIconPath);

    private JButton displayButton = new JButton(this.visibleIcon);
    private JButton removeButton = new JButton(this.trashIcon);
    private JLabel layerLabel;
  
    public LayerMenuItem(String layerName) {
        this.layerLabel = new JLabel(layerName);

        this.displayButton.setBorder(new LineBorder(new Color(0,0,0,0), 10, true));
        this.removeButton.setBorder(new LineBorder(new Color(0,0,0,0), 10, true));    

        setLayout(new BorderLayout());
        add(this.displayButton, BorderLayout.WEST);
        add(this.layerLabel, BorderLayout.CENTER);
        add(this.removeButton, BorderLayout.EAST);

        this.displayButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { changeDisplayState(); }
        });

        this.removeButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { removeLayer(); }
        });

    }

    public void changeDisplayState() {
        System.out.println(this.layerLabel.getText() + ": VISION SWITCH");
        boolean isVisible = this.displayButton.getIcon().equals(this.visibleIcon);
        this.displayButton.setIcon(isVisible ? this.invisibleIcon : this.visibleIcon); 
    }


    public void removeLayer() {
        System.out.println(this.layerLabel.getText() + ": LAYER REMOVED");
    }

}