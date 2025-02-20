package org.scc200g15.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.layerselector.LayerMenuTools;

/**
 * A layer of an image, stores a grid of pixels
 */
final public class Layer extends JPanel {
    Color[][] pixels;

    private final LayerMenuTools Tools = new LayerMenuTools();

    // Actual Components of a LayerMenuItem
    private final JButton displayButton = new JButton(Tools.VISIBLE_EYE_OPEN_ICON);
    private final JButton removeButton = new JButton(Tools.VISIBLE_TRASH_ICON);
    private final JLabel layerLabel = new JLabel();
    private final JTextField renameLabelField = new JTextField();

    // State Variables
    private boolean isLayerVisible = true;
    private boolean isActive = false;
    private boolean isBeingRenamed = false;

    /**
     * Basic constructor that creates a layer with a given size that is one color
     * 
     * @param c         the color that the layer should be
     * @param w         the width of the layer
     * @param h         the height of the layer
     * @param layerName the name of the layer
     */
    public Layer(String layerName, Color c, int w, int h) {

        // Formatting
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(1, 1, 0, 1), // Spacing between each component
            BorderFactory.createLineBorder(new Color(197, 197, 197), 1, true)));
        this.setBackground(Tools.VISIBLE_BACKGROUND_COLOUR);
        displayButton.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));
        removeButton.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));
        layerLabel.setText(layerName);
        layerLabel.setForeground(Tools.VISIBLE_ICON_COLOUR);

        // Add all components to the LayerMenuItem (JPanel)
        this.add(displayButton, BorderLayout.WEST);
        this.add(layerLabel, BorderLayout.CENTER);
        this.add(removeButton, BorderLayout.EAST);

        setActionListeners();

        // * ----- ACTUAL LAYER PIXELS ----- * //
        pixels = new Color[w][h];

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                pixels[x][y] = c;
            }
        }

    }

    public void setActionListeners() {
        // Add actions to switch visibility (using the eye open/shut icons)
        displayButton.addActionListener((ActionEvent e) -> {
            changeDisplayState();
        });

        // Add actions to remove a layer (using the trash icon)
        removeButton.addActionListener((ActionEvent e) -> {
            GUI.getInstance().getLayerSelector().removeLayer(this);
        });

        // // Add the ability to switch two layers
        this.addMouseListener(new MouseAdapter() {
            int startPoint, originIndex;

            // TODO: Could add a feature where a blue bar is displayed where the layer
            // would be moved if mouseReleased
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            // ? Change border to blue when hovered
            @Override
            public void mousePressed(MouseEvent e) {
                Image image = GUI.getInstance().getActiveImage();
                if (image == null)
                    return;
                startPoint = e.getPoint().y;
                originIndex = image.getLayerIndex((Layer) e.getSource());
                GUI.getInstance().getLayerSelector().setActiveLayer(originIndex);
            }

            // Move the frame to the correct position
            @Override
            public void mouseReleased(MouseEvent e) {
                Image image = GUI.getInstance().getActiveImage();
                if (image == null)
                    return;

                int frameHeight = image.getLayer(0).getHeight() - 1;
                int trueStartPoint = (originIndex * frameHeight) + startPoint;
                int trueEndPoint = trueStartPoint + (e.getPoint().y - startPoint);
                // endPoint = e.getPoint().y
                int destinationIndex = (int) (trueEndPoint / frameHeight);
                
                if (originIndex != destinationIndex) 
                    GUI.getInstance().getLayerSelector().moveLayer(originIndex, destinationIndex);
                
                // Check if double click to rename layer
                switchLabelToTextField(e);
            }

        });

        // Textbox action has finished (User pressed 'Enter' or moved to another
        // layer)
        renameLabelField.addActionListener((ActionEvent e) -> {
            renameLabelToTextField();
        });

    }

    /*
     * --------------------------------------- [RENAME LAYERS]
     * ---------------------------------------
     */

    // Apply the new name from the text field to the layer's label
    public void renameLabelToTextField() {
        layerLabel.setText(renameLabelField.getText());
        this.remove(renameLabelField);
        this.add(layerLabel);
        Tools.refreshUI(this);
        isBeingRenamed = false;
    }

    // Switch to text box so user can text field
    public void switchLabelToTextField(MouseEvent e) {
        if (e.getClickCount() == 2) {
            this.remove(layerLabel);
            renameLabelField.setText(layerLabel.getText());
            this.add(renameLabelField);
            Tools.refreshUI(this);
            renameLabelField.requestFocus();
            renameLabelField.selectAll();
            isBeingRenamed = true;
        }
    }

    public void activateLayer() {
        setLayerStateUI("active");
        isActive = true;
        Tools.refreshUI(this);
    }

    public void deactivateLayer() {
        if (isBeingRenamed) renameLabelToTextField();
        if (isLayerVisible) setLayerStateUI("visible");
        else setLayerStateUI("hidden");
        isActive = false;
        Tools.refreshUI(this);
    }

    /*
     * --------------------------------------- [VISIBILITY STATE]
     * ---------------------------------------
     */

    // Change the look of each layer in the menu to indicate it's current status
    public void changeDisplayState() {
        isLayerVisible = !isLayerVisible;
        if (isActive)
            setLayerStateUI("active");
        else {
            if (!isLayerVisible)
                setLayerStateUI("hidden");
            else
                setLayerStateUI("visible");
        }
        Tools.refreshUI(this);
        GUI.getInstance().getCanvas().repaint();
    }

    /*
     * --------------------------------------- [CHANGE LAYER UI LOOK]
     * ---------------------------------------
     */

    public void setLayerStateUI(String state) {
        switch (state.toLowerCase()) {
            case "active" -> {
                this.setBackground(Tools.ACTIVE_BACKGROUND_COLOUR);
                displayButton.setIcon(isLayerVisible ? Tools.ACTIVE_EYE_OPEN_ICON : Tools.ACTIVE_EYE_SHUT_ICON);
                layerLabel.setForeground(Tools.ACTIVE_ICON_COLOUR);
                removeButton.setIcon(Tools.ACTIVE_TRASH_ICON);
            }
            case "visible" -> {
                this.setBackground(Tools.VISIBLE_BACKGROUND_COLOUR);
                displayButton.setIcon(Tools.VISIBLE_EYE_OPEN_ICON);
                layerLabel.setForeground(Tools.VISIBLE_ICON_COLOUR);
                removeButton.setIcon(Tools.VISIBLE_TRASH_ICON);
            }
            case "hidden" -> {
                this.setBackground(Tools.HIDDEN_BACKGROUND_COLOUR);
                displayButton.setIcon(Tools.HIDDEN_EYE_SHUT_ICON);
                layerLabel.setForeground(Tools.HIDDEN_ICON_COLOUR);
                removeButton.setIcon(Tools.HIDDEN_TRASH_ICON);
            }
            default -> System.out.println("ERROR - Incorrect state.");
        }
    }

  /**
   * Get the color of a given pixel
   * 
   * @param x xPos of the pixel
   * @param y yPos of the pixel
   */
  public Color getPixel(int x, int y) {
    return pixels[x][y];
  }



  public Color setPixel(int x, int y, Color c) {
    return pixels[x][y] = c;
  }


    /*
    */
    public Color[][] getPixels() {
        return pixels;
    }

    public Boolean getIsActive() {
        return isActive;
    }
    public Boolean getIsLayerVisible(){return  isLayerVisible; }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
