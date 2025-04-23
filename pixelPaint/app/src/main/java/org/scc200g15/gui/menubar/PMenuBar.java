package org.scc200g15.gui.menubar;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.imageio.ImageIO;
import org.scc200g15.gui.GUI;
import org.scc200g15.tools.Tool;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import org.scc200g15.image.Layer;
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
    JMenuItem openFileButton = new JMenuItem("Open PNG");
    openFileButton.addActionListener((ActionEvent e) -> {
      openImageFile();
    });

    
    fileMenu.add(openFileButton);




    JMenuItem saveAsBitmap = new JMenuItem("Save as BMP");

    saveAsBitmap.addActionListener((ActionEvent e) -> {
      org.scc200g15.image.Image currentImage = GUI.getInstance().getActiveImage();

      if (currentImage == null) {
        JOptionPane.showMessageDialog(null, "No active image to save!", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("No active image found.");
        return;
      }

      BufferedImage bufferedImage = currentImage.calculateImageBuffer();
      try {
        // Get desktop path
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        File outputFile = new File(desktopPath, "output.bmp");

        // Save image
        boolean success = ImageIO.write(bufferedImage, "png", outputFile);

        if (success) {
          JOptionPane.showMessageDialog(null, "Image saved to Desktop!", "Success", JOptionPane.INFORMATION_MESSAGE);
          System.out.println("Image successfully saved to: " + outputFile.getAbsolutePath());
        } else {
          JOptionPane.showMessageDialog(null, "Failed to save image!", "Error", JOptionPane.ERROR_MESSAGE);
          System.out.println("ImageIO.write() returned false, failed to save.");
        }
      } catch (IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
      String[] formats = ImageIO.getWriterFormatNames();
      System.out.println("Supported formats: " + String.join(", ", formats));
    });
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
    fileMenu.add(saveAsBitmap);
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
