package org.scc200g15.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.scc200g15.action.LayerRenameAction;
import org.scc200g15.gui.GUI;
import org.scc200g15.gui.icons.IconManager;

/** A layer of an image, stores a grid of pixels */
final public class Layer implements MouseListener, MouseMotionListener, Serializable {
    ArrayList<ArrayList <Color>> pixels = new ArrayList<>();

    private String layerName;

    // Actual Components of a LayerMenuItem
    transient private JPanel jPanel;
    transient private JButton displayButton;
    transient private JButton removeButton;
    transient private JLabel layerLabel;
    transient private JTextField renameLabelField;

    // State Variables
    transient private boolean isLayerVisible;
    transient private boolean isActive;
    transient private boolean isBeingRenamed;
    transient private boolean isSelected;

    transient int startPoint, originIndex;

    private void init() {
        // Actual Components of a LayerMenuItem
        jPanel = new JPanel();
        jPanel.setName("layer");
        displayButton = new JButton(IconManager.VISIBLE_EYE_OPEN_ICON);
        removeButton = new JButton(IconManager.VISIBLE_TRASH_ICON); 
        layerLabel = new JLabel();
        renameLabelField = new JTextField();

        // State Variables
        isLayerVisible = true;
        isActive = false;
        isBeingRenamed = false;
        isSelected = false;

        setupLayerMenuPanel();
    }

    /**
     * Basic constructor that creates a layer with a 2D array of pixels
     * 
     * @param layerName The name of the layer
     * @param pixels The 2D array of pixels to use
     */
    public Layer(String layerName, Color[][] pixels) {
        this.layerName = layerName;

        init(); // Init transient variables

        setPixels(pixels);
    }

    /**
     * Basic constructor that creates a layer with a given size that is one color
     * 
     * @param c         the color that the layer should be
     * @param w         the width of the layer
     * @param h         the height of the layer
     * @param layerName the name of the layer
     */
    public Layer(String layerName, Color c, int w, int h) {   
        this.layerName = layerName;
        
        init(); // Init transient variables

        for (int y = 0; y < h; y++) {
            ArrayList<Color> row = new ArrayList<>();
            for (int x = 0; x < w; x++)
                row.add(c);
            this.pixels.add(row);
        }
    }

    /**
     * Setup the UI for the layer menu panel.
     */
    private void setupLayerMenuPanel() {
        // Setup holder JPanel
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(IconManager.DEFAULT_BORDER);
        jPanel.setBackground(IconManager.VISIBLE_BACKGROUND_COLOUR);

        // Setup display and remove layer button styles
        displayButton.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));
        removeButton.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));
        displayButton.setOpaque(false);
        removeButton.setOpaque(false);
        displayButton.setContentAreaFilled(false);
        removeButton.setContentAreaFilled(false);
        
        // Setup layer name label
        layerLabel.setText(layerName);
        layerLabel.setForeground(IconManager.VISIBLE_ICON_COLOUR);
        
        // Add all components to the LayerMenuItem (JPanel)
        jPanel.add(displayButton, BorderLayout.WEST);
        jPanel.add(layerLabel, BorderLayout.CENTER);
        jPanel.add(removeButton, BorderLayout.EAST);

        displayButton.setEnabled(true);

        jPanel.repaint();
        jPanel.repaint();

        setActionListeners();
    }

    /**
     * Add the action listeners to the layer menu panel's buttons.
     */
    public void setActionListeners() {
        // Add actions to switch visibility (using the eye open/shut icons)
        displayButton.addActionListener((ActionEvent e) -> {
            changeDisplayState();
        });

        // Add actions to remove a layer (using the trash icon)
        removeButton.addActionListener((ActionEvent e) -> {
            GUI.getInstance().getLayerSelector().removeLayerWithWarning(this);
        });

        jPanel.addMouseListener(this);
        jPanel.addMouseMotionListener(this);

        // Textbox action has finished (User pressed 'Enter' or moved to another layer)
        renameLabelField.addActionListener((ActionEvent e) -> {
            renameLabelToTextField();
        });

    }

    // * ----------------------- [RENAME LAYERS] ----------------------- * //

    /**
     * Sets the name of the layer to be displayed.
     * @param name Name of the layer (for display)
     */
    public void setLayerName(String name){
        this.layerName = name;
        layerLabel.setText(name);
        renameLabelField.setText(name);

        jPanel.repaint();
        jPanel.repaint();
    }

    /** 
     * Apply the new name from the text field to the layer's label 
     */
    public void renameLabelToTextField() {
        if(!this.layerName.equals(renameLabelField.getText()))
            GUI.getInstance().getActiveImage().addAction(new LayerRenameAction(this, this.layerName, renameLabelField.getText()));

        setLayerName(renameLabelField.getText());
        jPanel.remove(renameLabelField);
        jPanel.add(layerLabel);
        isBeingRenamed = false;

        jPanel.repaint();
        jPanel.repaint();
    }

    /**
     * Switch to text box so user can input into the text field.
     * @param e MouseEvent to track double clicks.
     */
    public void switchLabelToTextField(MouseEvent e) {
        if (e.getClickCount() == 2) {
            jPanel.remove(layerLabel);
            renameLabelField.setText(this.layerName);
            jPanel.add(renameLabelField);
            renameLabelField.requestFocus();
            renameLabelField.selectAll();
            isBeingRenamed = true;

            jPanel.repaint();
            jPanel.repaint();
        }
    }

    /**
     * Set this layer as active (includes backend and UI).
     */
    public void activateLayer() {
        setLayerStateUI("active");
        isActive = true;

        jPanel.repaint();
        jPanel.repaint();
    }

    /**
     * Set this layer as inactive (includes backend and UI).
     */
    public void deactivateLayer() {
        if (isBeingRenamed) renameLabelToTextField();
        if (isLayerVisible) setLayerStateUI("visible");
        else setLayerStateUI("hidden");
        isActive = false;
        
        jPanel.repaint();
        jPanel.repaint();
    }

    // * ----------------------- [VISIBILITY STATE] ----------------------- * //

    /**
     * Change the look of each layer in the menu to indicate it's current status
     */
    public void changeDisplayState() {
        isLayerVisible = !isLayerVisible;
        setLayerStateUI(isActive ? "active" : (isLayerVisible ? "visible" : "hidden"));

        jPanel.repaint();
        jPanel.repaint();
        GUI.getInstance().getCanvas().repaint();
        GUI.getInstance().getCanvas().recalculateAllPixels();
    }

    // * ----------------------- [CONTEXT MENU] ----------------------- * //

    /**
     * Returns a menu which allows the user to merge layers.
     * @return The JPopupMenu with the option to "Merge"
     */
    public JPopupMenu layerContextMenu() {
        Image image = GUI.getInstance().getActiveImage();
        JPopupMenu menu = new JPopupMenu();

        // If layer is selected, give the option to merge multiple layers.
        if(isSelected) {
            JMenuItem mergeOption = new JMenuItem("Merge");
            mergeOption.addActionListener((ActionEvent e) -> { 
                image.mergeSelectedLayers(); 
            });
            menu.add(mergeOption);
        }

        return menu;
    }

    /**
     * Check if the context menu should be pressed and if selected layers should be reset.
     * @param e MouseEvent
     * @param isMousePressed
     */
    private void checkDisplayContextMenu(MouseEvent e, boolean isMousePressed) {
        if (isMousePressed && (System.getProperty("os.name").startsWith("Windows")))
            return;
        if (!isMousePressed && !System.getProperty("os.name").startsWith("Windows"))
            return;

        if(e.isPopupTrigger())
            layerContextMenu().show(e.getComponent(), e.getX(), e.getY());
        else
            GUI.getInstance().getActiveImage().disableSelectedLayers();
    }

    // * ----------------------- [CHANGE LAYER UI LOOK] ----------------------- * //

    /**
     * Switch the layer's UI based on it's current state.
     * @param state The state of the layer ("active", "visible" or "hidden")
     */
    public void setLayerStateUI(String state) {
        switch (state.toLowerCase()) {
            case "active" -> {
                jPanel.setBackground(IconManager.ACTIVE_BACKGROUND_COLOUR);
                displayButton.setIcon(isLayerVisible ? IconManager.ACTIVE_EYE_OPEN_ICON : IconManager.ACTIVE_EYE_SHUT_ICON);
                layerLabel.setForeground(IconManager.ACTIVE_ICON_COLOUR);
                removeButton.setIcon(IconManager.ACTIVE_TRASH_ICON);
            }
            case "visible" -> {
                jPanel.setBackground(IconManager.VISIBLE_BACKGROUND_COLOUR);
                displayButton.setIcon(IconManager.VISIBLE_EYE_OPEN_ICON);
                layerLabel.setForeground(IconManager.VISIBLE_ICON_COLOUR);
                removeButton.setIcon(IconManager.VISIBLE_TRASH_ICON);
            }
            case "hidden" -> {
                jPanel.setBackground(IconManager.HIDDEN_BACKGROUND_COLOUR);
                displayButton.setIcon(IconManager.HIDDEN_EYE_SHUT_ICON);
                layerLabel.setForeground(IconManager.HIDDEN_ICON_COLOUR);
                removeButton.setIcon(IconManager.HIDDEN_TRASH_ICON);
            }
            default -> System.out.println("ERROR - Incorrect state.");
        }
    }

    // * ----------------------- [ACCESSORS / MUTATORS] ----------------------- * //

    /**
     * Get the color of a given pixel
     * @param x xPos of the pixel
     * @param y yPos of the pixel
     */
    public Color getPixel(int x, int y) {
        return pixels.get(y).get(x);
    }

    /**
     * Set the color of a given pixel
     * @param x xPos of the pixel
     * @param y yPos of the pixel
     * @param c Color of the pixel
     */
    public void setPixel(int x, int y, Color c) {
        pixels.get(y).set(x, c);
    }

    /**
     * Get the pixels of the current layer.
     * @return Color[][]
     */
    public Color[][] getPixels() {
        Color[][] array = new Color[pixels.size()][pixels.get(0).size()];
        for (int i = 0; i < pixels.size(); i++)
            array[i] = (Color []) pixels.get(i).toArray();
        return array;
    }

    /**
     * Set the pixels of the current layer.
     * @param newPixels 2D array of Color pixels.
     */
    public void setPixels(Color[][] newPixels) {
        if(newPixels.length > this.pixels.size() || newPixels[0].length > this.pixels.get(0).size()){
            // Expand Canvas
            GUI.getInstance().getActiveImage().changeImageWidth(Math.max(newPixels.length, this.pixels.size()));
            GUI.getInstance().getActiveImage().changeImageHeight(Math.max(newPixels[0].length, this.pixels.get(0).size()));   
        }

        for (int j = 0; j < newPixels[0].length; j++) { 
            for (int i = 0; i < newPixels.length; i++) { 
                this.pixels.get(j).set(i, newPixels[i][j]);
            }
        }
    } 

    /**
     * Set a list of pixels to a list of colours. 
     * @param points Array of Points to modify
     * @param colors Array of Colors to change the points to
     */
    public void setPixels(Point[] points, Color[] colors) {
        if(points.length != colors.length)
            return;
        for(int i = 0; i < points.length; i++)
            setPixel(points[i].x, points[i].y, colors[i]);
    }


    /**
     * Returns if the current layer is the active layer.
     * @return If the layer is active.
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * Returns if the current layer is visible.
     * @return If the layer is visible.
     */
    public Boolean getIsLayerVisible(){ 
        return isLayerVisible; 
    }

    /**
     * Toggle the if this layer is currently selected.
     */
    public void switchSelectedLayerState() {
        this.isSelected = !isSelected;
        if (isSelected) jPanel.setBorder(IconManager.IS_SELECTED_BORDER);
        else jPanel.setBorder(IconManager.DEFAULT_BORDER);
    }

    /**
     * Returns if the current layer is selected.
     * @return Is the current layer selected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Change the dimensions of the current layer.
     * @param newWidth New layer's width
     * @param newHeight New layer's height
     */
    public void changeSize(int newWidth, int newHeight) {
        changeWidth(newWidth);
        changeHeight(newHeight);
        GUI.getInstance().getCanvas().repaint();
    }

    /**
     * Change the width of the current layer.
     * @param newWidth New layer's width
     */
    public void changeWidth(int newWidth) {
        int currentWidth = pixels.get(0).size();
        if(newWidth > currentWidth) {
            for(ArrayList<Color> rows : pixels)
                for(int i = currentWidth; i < newWidth; i++)
                    rows.add(new Color(0,0,0,0));
        } else {
            for(ArrayList<Color> row : pixels) {
                while (row.size() > newWidth)
                    row.remove(row.size() - 1);
            }
        }
        GUI.getInstance().getCanvas().repaint();
    }

    /**
     * Change the height of the current layer.
     * @param newHeight New layer's height
     */
    public void changeHeight(int newHeight) {
        int currentHight = pixels.size();
        if(newHeight > currentHight) {
            for(int i = currentHight; i < newHeight; i++) {
                ArrayList<Color> temp = new ArrayList<>();
                for(int j = 0; j < pixels.get(0).size(); j++)
                    temp.add(new Color(0,0,0,0));
                pixels.add(temp);
            }
        } else {
            while (pixels.size() > newHeight)
                pixels.remove(pixels.size() - 1);    
        }
        GUI.getInstance().getCanvas().repaint();
    }

    /**
     * Returns the holder JPanel for this current layer.
     * @return Holder JPanel
     */
    public JPanel getJPanel(){
        return jPanel;
    }

    /**
     * Returns the current layer's width.
     * @return Layer Width
     */
    public int getLayerWidth(){
        return pixels.get(0).size();
    }
    
    /**
     * Returns the current layer's height.
     * @return Layer Height
     */
    public int getLayerHeight(){
        return pixels.size();
    }

    
    // * ----------------------- [EVENT HANDLERS] ----------------------- * //


    @Override
    public void mousePressed(MouseEvent e) {
        Image image = GUI.getInstance().getActiveImage();
        if (image == null)
        return;
        
        startPoint = e.getPoint().y;
        originIndex = image.getLayerIndex((JPanel) e.getSource());
        
        if(e.isAltDown()) {
            GUI.getInstance().getLayerSelector().switchSelectedLayerState(originIndex);
            return;
        } 
        
        if(!e.isAltDown())
        checkDisplayContextMenu(e, true);
        
        GUI.getInstance().getLayerSelector().setActiveLayer(originIndex);
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        Image image = GUI.getInstance().getActiveImage();
        if (image == null)
        return;
        
        GUI.getInstance().getLayerSelector().setSeparatorPOS(-1);
        
        int destinationIndex = calcNewLayerIndex(image, e);
        
        if (originIndex != destinationIndex) 
        GUI.getInstance().getLayerSelector().moveLayer(originIndex, destinationIndex);
        
        if(!e.isAltDown())
        checkDisplayContextMenu(e, false);
        
        // Check if double click to rename layer
        switchLabelToTextField(e);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        Image image = GUI.getInstance().getActiveImage();
        if (image == null)
        return;
        
        int destinationIndex = calcNewLayerIndex(image, e);
        
        if (originIndex != destinationIndex){
            if (originIndex < destinationIndex) GUI.getInstance().getLayerSelector().setSeparatorPOS(destinationIndex + 1);
            else GUI.getInstance().getLayerSelector().setSeparatorPOS(destinationIndex);
        }else GUI.getInstance().getLayerSelector().setSeparatorPOS(-1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}


    // * ----------------------- [EVENT HANDLER HELPER FUNCTION] ----------------------- * //


    /**
     * Calculate the new position of a layer which is being moved.
     * @param image
     * @param e
     * @return New position of the layer
     */
    private int calcNewLayerIndex(Image image, MouseEvent e){
        int frameHeight = image.getLayer(0).getJPanel().getHeight() - 1;
        int trueStartPoint = (originIndex * frameHeight) + startPoint;
        int trueEndPoint = trueStartPoint + (e.getPoint().y - startPoint);

        int destinationIndex = (int) (trueEndPoint / frameHeight);

        if(destinationIndex < 0) destinationIndex = 0;
        if(destinationIndex >= image.getLayerCount()) destinationIndex = image.getLayerCount() - 1;

        return destinationIndex;
    }
    

    // * ----------------------- [SERIALIZATION FUNCTIONS] ----------------------- * //


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Deserialize non-transient fields
        init(); // Init transient variables
    }

}
