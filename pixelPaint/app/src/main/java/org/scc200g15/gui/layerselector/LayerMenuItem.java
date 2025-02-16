package org.scc200g15.gui.layerselector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import java.awt.event.ActionEvent;

public class LayerMenuItem extends JPanel {
    private final LayerMenuTools Tools = new LayerMenuTools();
    
    // Actual Components of a LayerMenuItem
    private JButton displayButton = new JButton(Tools.VISIBLE_EYE_OPEN_ICON);
    private JButton removeButton = new JButton(Tools.VISIBLE_TRASH_ICON);
    private JLabel layerLabel = new JLabel();
    private JTextField renameLabelField = new JTextField();

    // State Variables
    private boolean isVisible = true;
    private boolean isActive = false;
    private boolean isBeingRenamed = false;
  
    public LayerMenuItem(String layerName, PLayerSelector Manager) {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(1, 1, 0, 1), // Spacing between each component
            BorderFactory.createLineBorder(new Color(197, 197, 197), 1, true)
        ));
        this.setBackground(Tools.VISIBLE_BACKGROUND_COLOUR);

        // Layer's text/name
        layerLabel.setText(layerName);
        layerLabel.setForeground(Tools.VISIBLE_ICON_COLOUR);

        // Remove borders from the icon buttons
        displayButton.setBorder(new LineBorder(new Color(0,0,0,0), 10, true));
        removeButton.setBorder(new LineBorder(new Color(0,0,0,0), 10, true));    

        // Add all components to the LayerMenuItem (JPanel)
        this.add(displayButton, BorderLayout.WEST);
        this.add(layerLabel, BorderLayout.CENTER);
        this.add(removeButton, BorderLayout.EAST);

        // Add actions to switch visibility (using the eye open/shut icons)
        displayButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { changeDisplayState(); }
        });

        // Add actions to remove a layer (using the trash icon)
        removeButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { removeLayer(Manager); }
        });


        // * ----- ACTION LISTENERS ----- * //

        // Add the ability to switch two layers
        this.addMouseListener(new MouseAdapter() {
            ArrayList<LayerMenuItem> layers = Manager.getLayers();
            int startPoint, originIndex;

            // TODO: Could add a feature where a blue bar is displayed where the layer would be moved if mouseReleased
            @Override
            public void mouseDragged(MouseEvent e) {}

            // ? Change border to blue when hovered
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint().y;
                originIndex = layers.indexOf((JPanel) e.getSource());
                Manager.setActiveLayer(layers.get(originIndex));
            }
        
            // Move the frame to the correct position
            @Override
            public void mouseReleased(MouseEvent e) {
                int frameHeight = layers.get(0).getHeight() - 1;
                int trueStartPoint = (originIndex * frameHeight) + startPoint;
                int trueEndPoint = trueStartPoint + (e.getPoint().y - startPoint); // endPoint = e.getPoint().y
                Manager.insertLayer(originIndex, (int) (trueEndPoint / frameHeight), layers.get(originIndex));

                // Check if double click to rename layer
                switchLabelToTextField(e);
            }
            
        });

        // Textbox action has finished (User pressed 'Enter' or moved to another layer)
        renameLabelField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renameLabelToTextField();    
            }
        });

    }

    /* --------------------------------------- [RENAME LAYERS] --------------------------------------- */

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

    /* --------------------------------------- [REMOVE LAYER] --------------------------------------- */

    
    // ? Could limit this pop-up on the condition of a layer not being empty (fully transparent) for usability.
    // ! COULD ADD A RESTRAINT IN CASE IT'S THE ONLY LAYER
    // Remove a layer with an aditional pop-up to limit human error
    public void removeLayer(PLayerSelector Manager) {
        int option = JOptionPane.showOptionDialog(
            null, "Are you sure you want to delete this layer?", "Layer Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            Tools.createImageIcon(40, 40, "/Icons/question_mark_icon.png"),
            null, null
        );
        if (option == JOptionPane.YES_OPTION) Manager.removeLayerMenuItem(this);
    }
    
        
    /* --------------------------------------- [ACTIVE/INACTIVE] --------------------------------------- */
    
    public void activateLayer() {
        setLayerStateUI("active");
        isActive = true;
        Tools.refreshUI(this);
    }
    
    public void disactivateLayer() {
        if(isBeingRenamed) renameLabelToTextField();
        if(isVisible) setLayerStateUI("visible");
        else setLayerStateUI("hidden");
        isActive = false;
        Tools.refreshUI(this);
    }
    
    /* --------------------------------------- [VISIBILITY STATE] --------------------------------------- */

    // Change the look of each layer in the menu to indicate it's current status
    public void changeDisplayState() {  
        isVisible = !isVisible;
        if(isActive) setLayerStateUI("active");
        else {
            if(!isVisible) setLayerStateUI("hidden");
            else setLayerStateUI("visible");
        }
        Tools.refreshUI(this);
    }    

    /* --------------------------------------- [CHANGE LAYER UI LOOK] --------------------------------------- */

    public void setLayerStateUI(String state) {
        switch (state.toLowerCase()) {
            case "active":
                this.setBackground(Tools.ACTIVE_BACKGROUND_COLOUR);
                displayButton.setIcon(isVisible ? Tools.ACTIVE_EYE_OPEN_ICON : Tools.ACTIVE_EYE_SHUT_ICON);
                layerLabel.setForeground(Tools.ACTIVE_ICON_COLOUR);
                removeButton.setIcon(Tools.ACTIVE_TRASH_ICON);
                break;
            case "visible":
                this.setBackground(Tools.VISIBLE_BACKGROUND_COLOUR);
                displayButton.setIcon(Tools.VISIBLE_EYE_OPEN_ICON);
                layerLabel.setForeground(Tools.VISIBLE_ICON_COLOUR);
                removeButton.setIcon(Tools.VISIBLE_TRASH_ICON);
                break;
            case "hidden":
                this.setBackground(Tools.HIDDEN_BACKGROUND_COLOUR);
                displayButton.setIcon(Tools.HIDDEN_EYE_SHUT_ICON);
                layerLabel.setForeground(Tools.HIDDEN_ICON_COLOUR);
                removeButton.setIcon(Tools.HIDDEN_TRASH_ICON);
                break;
            default:
                System.out.println("ERROR - Incorrect state.");
        }
    }

}