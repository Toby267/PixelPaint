package org.scc200g15.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.icons.IconManager;

/**
 * A layer of an image, stores a grid of pixels
 */
final public class Layer extends JPanel implements MouseListener, MouseMotionListener {
    ArrayList<ArrayList <Color>> pixels = new ArrayList<>();

    // Actual Components of a LayerMenuItem
    private final JButton displayButton = new JButton(IconManager.VISIBLE_EYE_OPEN_ICON);
    private final JButton removeButton = new JButton(IconManager.VISIBLE_TRASH_ICON); // ! TODO: REMOVE VISIBILITY WHEN LAST LAYER
    private final JLabel layerLabel = new JLabel();
    private final JTextField renameLabelField = new JTextField();

    // State Variables
    private boolean isLayerVisible = true;
    private boolean isActive = false;
    private boolean isBeingRenamed = false;
    private boolean isSelected = false;

    int startPoint, originIndex;

     /**
     * Basic constructor that creates a layer with a 2D array of pixels
     * 
     * @param layerName The name of the layer
     * @param pixels The 2D array of pixels to use
     */
    public Layer(String layerName, Color[][] pixels){
        setupLayerMenuPanel(layerName);
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
        setupLayerMenuPanel(layerName);
        for (int y = 0; y < h; y++) {
            ArrayList<Color> row = new ArrayList<>();
            for (int x = 0; x < w; x++)
                row.add(c);
            this.pixels.add(row);
        }

    }

    private void setupLayerMenuPanel(String layerName){
        this.setLayout(new BorderLayout());
        this.setBorder(IconManager.DEFAULT_BORDER);
        this.setBackground(IconManager.VISIBLE_BACKGROUND_COLOUR);

        displayButton.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));
        removeButton.setBorder(new LineBorder(new Color(0, 0, 0, 0), 10, true));
        
        displayButton.setOpaque(false);
        removeButton.setOpaque(false);
        
        displayButton.setContentAreaFilled(false);
        removeButton.setContentAreaFilled(false);
        
        layerLabel.setText(layerName);
        layerLabel.setForeground(IconManager.VISIBLE_ICON_COLOUR);
        
        // Add all components to the LayerMenuItem (JPanel)
        this.add(displayButton, BorderLayout.WEST);
        this.add(layerLabel, BorderLayout.CENTER);
        this.add(removeButton, BorderLayout.EAST);

        setActionListeners();
    }

    public void setActionListeners() {
        // Add actions to switch visibility (using the eye open/shut icons)
        displayButton.addActionListener((ActionEvent e) -> {
            changeDisplayState();
        });

        // Add actions to remove a layer (using the trash icon)
        removeButton.addActionListener((ActionEvent e) -> {
            GUI.getInstance().getLayerSelector().removeLayerWithWarning(this);
        });

        addMouseListener(this);
        addMouseMotionListener(this);

        // Textbox action has finished (User pressed 'Enter' or moved to another layer)
        renameLabelField.addActionListener((ActionEvent e) -> {
            renameLabelToTextField();
        });

    }

    // * ----------------------- [RENAME LAYERS] ----------------------- * //

    // Apply the new name from the text field to the layer's label
    public void renameLabelToTextField() {
        layerLabel.setText(renameLabelField.getText());
        this.remove(renameLabelField);
        this.add(layerLabel);
        isBeingRenamed = false;

        revalidate();
        repaint();
    }

    // Switch to text box so user can text field
    public void switchLabelToTextField(MouseEvent e) {
        if (e.getClickCount() == 2) {
            this.remove(layerLabel);
            renameLabelField.setText(layerLabel.getText());
            this.add(renameLabelField);
            renameLabelField.requestFocus();
            renameLabelField.selectAll();
            isBeingRenamed = true;

            revalidate();
            repaint();
        }
    }

    public void activateLayer() {
        setLayerStateUI("active");
        isActive = true;

        revalidate();
        repaint();
    }

    public void deactivateLayer() {
        if (isBeingRenamed) renameLabelToTextField();
        if (isLayerVisible) setLayerStateUI("visible");
        else setLayerStateUI("hidden");
        isActive = false;
        
        revalidate();
        repaint();
    }

    // * ----------------------- [VISIBILITY STATE] ----------------------- * //

    // Change the look of each layer in the menu to indicate it's current status
    // TODO: Look at refactoring
    public void changeDisplayState() {
        isLayerVisible = !isLayerVisible;

        String state = isActive ? "active" : (isLayerVisible ? "visible" : "hidden");        
        setLayerStateUI(state);

        revalidate();
        repaint();

        GUI.getInstance().getCanvas().repaint();
        GUI.getInstance().getCanvas().recalculateAllPixels();
    }


    // * ----------------------- [CONTEXT MENU] ----------------------- * //
    public JPopupMenu layerContextMenu() {
        Image image = GUI.getInstance().getActiveImage();
        
        JPopupMenu menu = new JPopupMenu();

        if(isSelected) {
            JMenuItem mergeOption = new JMenuItem("Merge");
            mergeOption.addActionListener((ActionEvent e) -> {
                image.mergeSelectedLayers();
            });
            menu.add(mergeOption);
        }

        return menu;
    }

    // * ----------------------- [CHANGE LAYER UI LOOK] ----------------------- * //

    public void setLayerStateUI(String state) {
        switch (state.toLowerCase()) {
            case "active" -> {
                this.setBackground(IconManager.ACTIVE_BACKGROUND_COLOUR);
                displayButton.setIcon(isLayerVisible ? IconManager.ACTIVE_EYE_OPEN_ICON : IconManager.ACTIVE_EYE_SHUT_ICON);
                layerLabel.setForeground(IconManager.ACTIVE_ICON_COLOUR);
                removeButton.setIcon(IconManager.ACTIVE_TRASH_ICON);
            }
            case "visible" -> {
                this.setBackground(IconManager.VISIBLE_BACKGROUND_COLOUR);
                displayButton.setIcon(IconManager.VISIBLE_EYE_OPEN_ICON);
                layerLabel.setForeground(IconManager.VISIBLE_ICON_COLOUR);
                removeButton.setIcon(IconManager.VISIBLE_TRASH_ICON);
            }
            case "hidden" -> {
                this.setBackground(IconManager.HIDDEN_BACKGROUND_COLOUR);
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
     * 
     * @param x xPos of the pixel
     * @param y yPos of the pixel
     */
    public Color getPixel(int x, int y) {
        return pixels.get(y).get(x);
    }

    public void setPixel(int x, int y, Color c) {
        pixels.get(y).set(x, c);
    }

    public Color[][] getPixels() {
        Color[][] array = new Color[pixels.size()][pixels.get(0).size()];
        for (int i = 0; i < pixels.size(); i++)
            array[i] = (Color []) pixels.get(i).toArray();
        return array;
    }

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

    public void setPixels(Point[] points, Color[] colors) {
        if(points.length != colors.length){
            return;
        }

        for(int i = 0; i < points.length; i++ ){
            setPixel(points[i].x, points[i].y, colors[i]);
        }
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Boolean getIsLayerVisible(){ 
        return isLayerVisible; 
    }

    public void switchSelectedLayerState() {
        this.isSelected = !isSelected;
        if (isSelected) this.setBorder(IconManager.IS_SELECTED_BORDER);
        else this.setBorder(IconManager.DEFAULT_BORDER);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void changeSize(int newWidth, int newHeight) {
        changeWidth(newWidth);
        changeHeight(newHeight);
        GUI.getInstance().getCanvas().repaint();
    }

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

    private int calcNewLayerIndex(Image image, MouseEvent e){
        int frameHeight = image.getLayer(0).getHeight() - 1;
        int trueStartPoint = (originIndex * frameHeight) + startPoint;
        int trueEndPoint = trueStartPoint + (e.getPoint().y - startPoint);

        int destinationIndex = (int) (trueEndPoint / frameHeight);

        if(destinationIndex < 0) destinationIndex = 0;
        if(destinationIndex >= image.getLayerCount()) destinationIndex = image.getLayerCount() - 1;

        return destinationIndex;
    }

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

    // Mouse listeners for layer reordering
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Image image = GUI.getInstance().getActiveImage();
        if (image == null)
            return;
                
        startPoint = e.getPoint().y;
        originIndex = image.getLayerIndex((Layer) e.getSource());

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

    public int getLayerWidth(){
        return pixels.get(0).size();
    }
    public int getLayerHeight(){
        return pixels.size();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
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
    public void mouseMoved(MouseEvent e) {
        
    }


}
