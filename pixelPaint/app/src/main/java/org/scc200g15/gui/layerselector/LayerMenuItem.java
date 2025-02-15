package org.scc200g15.gui.layerselector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Image;

import java.awt.event.ActionEvent;

public class LayerMenuItem extends JPanel {
    private static String eyeOpenIconPath = "/Icons/open_eye_icon.png";
    private static String eyeShutIconPath = "/Icons/closed_eye_icon.png";
    private static String trashIconPath = "/Icons/trash_icon.png";

    // Colours for the layer menu background as seen in the UI design
    private final Color VISIBLE_BACKGROUND_COLOUR = new Color(227,227,227);
    private final Color HIDDEN_BACKGROUND_COLOUR = new Color(233,233,233);
    private final Color ACTIVE_BACKGROUND_COLOUR = new Color(64,64,64);
    
    // Colours for the icons/text as seen in the UI design
    private final Color VISIBLE_ICON_COLOUR = new Color(128,128,128);
    private final Color HIDDEN_ICON_COLOUR = new Color(191,191,191);
    private final Color ACTIVE_ICON_COLOUR = new Color(227,227,227);

    public ImageIcon createImageIcon(int x, int y, String path) {
      return new ImageIcon(
        new ImageIcon(getClass().getResource(path))
          .getImage()
          .getScaledInstance(x, y, Image.SCALE_SMOOTH)
      );
    }

    public ImageIcon createImageIcon(int x, int y, Color color, String path) {
        return changeColour(createImageIcon(x, y, path), color);
    }

    public BufferedImage convert_ImageIcon_To_BufferedImage(ImageIcon icon) {
        // Code from: Werner Kvalem Vesterås (https://stackoverflow.com/questions/15053214/converting-an-imageicon-to-a-bufferedimage)
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return image;
    }

    public ImageIcon changeColour(ImageIcon icon, Color newColor) {
        BufferedImage image = convert_ImageIcon_To_BufferedImage(icon);

        // Code from: foowtf (https://stackoverflow.com/questions/8029903/how-to-change-the-color-of-an-icon-based-on-user-action)
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color imageColor = new Color(image.getRGB(x, y), true);
                // New Colours but maintain the originally transparent pixels.
                Color newPixelColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), imageColor.getAlpha());
                image.setRGB(x, y, newPixelColor.getRGB());
            }
        }

        return new ImageIcon(image);
    }

    // The "eye open" icon is only used in visible/active states
    private final ImageIcon VISIBLE_EYE_OPEN_ICON = createImageIcon(30, 30, VISIBLE_ICON_COLOUR, eyeOpenIconPath);
    private final ImageIcon ACTIVE_VISIBLE_ICON = createImageIcon(30, 30, ACTIVE_ICON_COLOUR, eyeOpenIconPath);

    // The "eye shut" icon is only used in hidden/active states
    private final ImageIcon HIDDEN_EYE_SHUT_ICON = createImageIcon(30, 30, HIDDEN_ICON_COLOUR, eyeShutIconPath);
    private final ImageIcon ACTIVE_INVISIBLE_ICON = createImageIcon(30, 30, ACTIVE_ICON_COLOUR, eyeShutIconPath);

    // The "trash" icon is used in all states
    private final ImageIcon VISIBLE_TRASH_ICON = createImageIcon(25, 25, VISIBLE_ICON_COLOUR, trashIconPath);
    private final ImageIcon HIDDEN_TRASH_ICON = createImageIcon(25, 25, HIDDEN_ICON_COLOUR, trashIconPath);
    private final ImageIcon ACTIVE_TRASH_ICON = createImageIcon(25, 25, ACTIVE_ICON_COLOUR, trashIconPath);
    
    // Actual Components of a LayerMenuItem
    private JButton displayButton = new JButton(VISIBLE_EYE_OPEN_ICON);
    private JButton removeButton = new JButton(VISIBLE_TRASH_ICON);
    private JLabel layerLabel = new JLabel();

    private boolean isVisible = true;
  
    public LayerMenuItem(String layerName, PLayerSelector Manager) {
        // Setup LayerMenuItem configurations (layout, border and background)
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(1, 1, 0, 1), // Spacing between each component
            BorderFactory.createLineBorder(new Color(197, 197, 197), 1, true)
        ));
        this.setBackground(VISIBLE_BACKGROUND_COLOUR);

        // Layer's text/name
        layerLabel.setText(layerName);
        layerLabel.setForeground(VISIBLE_ICON_COLOUR);

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

        // TODO: CREATE ACTIVE LAYER
        // Add the ability to switch two layers
        this.addMouseListener(new MouseAdapter() {
            ArrayList<LayerMenuItem> layers = Manager.getLayers();
            int startPoint;
            int originIndex;
            int destinationIndex;

            // TODO: Could add a feature where a blue bar is displayed where the layer would be moved if mouseReleased
            @Override
            public void mouseDragged(MouseEvent e) {}

            // Change border to blue when hovered
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint().y;
                originIndex = layers.indexOf((JPanel) e.getSource());
            }
        
            // Swap both frames
            @Override
            public void mouseReleased(MouseEvent e) {
                int frameHeight = layers.get(0).getHeight() - 1;

                int trueStartPoint = (originIndex * frameHeight) + startPoint;
                int trueEndPoint = trueStartPoint + (e.getPoint().y - startPoint); // endPoint = e.getPoint().y
                destinationIndex = (int) (trueEndPoint / frameHeight);

                Manager.swapLayers(originIndex, destinationIndex);
            }
            
        });

    }

    // TODO: ADD FUNCTIONALITY FOR ACTIVE COLOURS
    // Change the look of each layer in the menu to indicate it's current status
    public void changeDisplayState() {  
        // Note: Not an if-else since the "Active" state hasn't yet been implemented
        if(isVisible) {
            this.setBackground(HIDDEN_BACKGROUND_COLOUR);
            displayButton.setIcon(HIDDEN_EYE_SHUT_ICON);
            layerLabel.setForeground(HIDDEN_ICON_COLOUR);
            removeButton.setIcon(HIDDEN_TRASH_ICON);
        } 
        if(!isVisible) {
            this.setBackground(VISIBLE_BACKGROUND_COLOUR);
            displayButton.setIcon(VISIBLE_EYE_OPEN_ICON);
            layerLabel.setForeground(VISIBLE_ICON_COLOUR);
            removeButton.setIcon(VISIBLE_TRASH_ICON);
        }
        isVisible = !isVisible;
        revalidate();
        repaint();
    }    

    // ? Could limit this pop-up on the condition of a layer not being empty (fully transparent) for usability.
    // Remove a layer with an aditional pop-up to limit human error
    public void removeLayer(PLayerSelector Manager) {
        int option = JOptionPane.showOptionDialog(
            null, "Are you sure you want to delete this layer?", "Layer Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            createImageIcon(40, 40, "/Icons/question_mark_icon.png"),
            null, null
        );
        if (option == JOptionPane.YES_OPTION) Manager.removeLayerMenuItem(this);
    }

}