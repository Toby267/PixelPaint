package org.scc200g15.gui.menubar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.scc200g15.gui.GUI;
import org.scc200g15.image.Layer;
import org.scc200g15.tools.Tool;
/**
 * The MenuBar that will appear at the very top of the menu
 */
public class PMenuBar extends JMenuBar {
  JMenu editMenu;
  ArrayList<PMenuBarButton> editMenuButtons;
  private Layer getSelectedLayer() {
    // Return the currently selected Layer, make sure it's implemented
    // For example, you could get it from a Layer selector or another part of your program
    return GUI.getInstance().getActiveImage().getActiveLayer();  // or any method to get the active layer
  }
  public PMenuBar(GUI window) {
    editMenuButtons = new ArrayList<>();

    // Create Menus
    JMenu fileMenu = new JMenu("file");
    editMenu = new JMenu("edit");
    JMenu viewMenu = new JMenu("view");
    JMenu helpMenu = new JMenu("help");
    JMenuItem openFileButton = new JMenuItem("Import Image");
    openFileButton.addActionListener((ActionEvent e) -> {
      openImageFile();
    });

    
    fileMenu.add(openFileButton);

    JMenuItem save = new JMenuItem("Export Image");

    save.addActionListener((ActionEvent e) -> {
      GUI.getInstance().getCanvas().saveImage();
    });

    JMenuItem saveLayer = new JMenuItem("Export Active Layer");

    saveLayer.addActionListener((ActionEvent e) -> {
      GUI.getInstance().getCanvas().saveActiveLayer();
    });

    JMenuItem saveWithLayer = new JMenuItem("Save With Layers");

    saveWithLayer.addActionListener((ActionEvent e) -> {
      GUI.getInstance().getCanvas().saveWithLayers();
    });

    JMenuItem openWithLayer = new JMenuItem("Open With Layers");

    openWithLayer.addActionListener((ActionEvent e) -> {
      GUI.getInstance().getCanvas().openWithLayers();
    });


    fileMenu.add(save);
    fileMenu.add(saveLayer);
    fileMenu.add(saveWithLayer);
    fileMenu.add(openWithLayer);

    viewMenu.add(new AbstractAction("Toggle Dark Mode") {

		  @Override
		  public void actionPerformed(ActionEvent e) {
			  GUI.getInstance().toggleDarkMode();
		  }

    });

    // Add Menus to MenuBar
    add(fileMenu);
    add(editMenu);
    add(helpMenu);
    add(viewMenu);
  }


  private void openImageFile() {
    // JFileChooser to select PNG file
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Open Image");
    fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

    int result = fileChooser.showOpenDialog(null);

    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      try {
        // Load the image
        BufferedImage loadedImage = ImageIO.read(selectedFile);
        if (loadedImage == null) {
          JOptionPane.showMessageDialog(null, "Failed to load the image!", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // Convert to Color[][] (pixels)
        int width = loadedImage.getWidth();
        int height = loadedImage.getHeight();
        Color[][] pixelData = new Color[width][height];

        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {
            pixelData[x][y] = new Color(loadedImage.getRGB(x, y));
          }
        }

        // Update the selected layer
        Layer selectedLayer = getSelectedLayer();  // Get selected layer (from Image or LayerSelector)
        if (selectedLayer != null) {
          selectedLayer.setPixels(pixelData);
          GUI.getInstance().getCanvas().recalculateAllPixels();
          GUI.getInstance().getCanvas().repaint();  // Repaint the canvas to reflect changes
        } else {
          JOptionPane.showMessageDialog(null, "No active layer selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
      } catch (IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error loading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  





  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for(PMenuBarButton btn: editMenuButtons){
      btn.updateActiveState();
    }
  }

  public void addTool(Tool t, String name){
    // Create Button
    PMenuBarButton toolButton = new PMenuBarButton(t, name);

    // Add Button
    editMenu.add(toolButton);
    editMenuButtons.add(toolButton);

    revalidate();
    repaint();
  }
}
