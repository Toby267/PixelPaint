package org.scc200g15.gui.layerselector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class LayerMenuTools {

    /* --------------------------------------- [ICON CONSTANTS] --------------------------------------- */

    private final String EYE_OPEN_ICON_PATH = "/Icons/open_eye_icon.png";
    private final String EYE_SHUT_ICON_PATH = "/Icons/closed_eye_icon.png";
    private final String TRASH_ICON_PATH = "/Icons/trash_icon.png";

    // Colours for the layer menu background as seen in the UI design
    public final Color VISIBLE_BACKGROUND_COLOUR = new Color(227,227,227);
    public final Color HIDDEN_BACKGROUND_COLOUR = new Color(233,233,233);
    public final Color ACTIVE_BACKGROUND_COLOUR = new Color(64,64,64);
    
    // Colours for the icons/text as seen in the UI design
    public final Color VISIBLE_ICON_COLOUR = new Color(128,128,128);
    public final Color HIDDEN_ICON_COLOUR = new Color(191,191,191);
    public final Color ACTIVE_ICON_COLOUR = new Color(227,227,227);

    // The "eye open" icon is only used in visible/active states
    public final ImageIcon VISIBLE_EYE_OPEN_ICON = createImageIcon(30, 30, VISIBLE_ICON_COLOUR, EYE_OPEN_ICON_PATH);
    public final ImageIcon ACTIVE_EYE_OPEN_ICON = createImageIcon(30, 30, ACTIVE_ICON_COLOUR, EYE_OPEN_ICON_PATH);

    // The "eye shut" icon is only used in hidden/active states
    public final ImageIcon HIDDEN_EYE_SHUT_ICON = createImageIcon(30, 30, HIDDEN_ICON_COLOUR, EYE_SHUT_ICON_PATH);
    public final ImageIcon ACTIVE_EYE_SHUT_ICON = createImageIcon(30, 30, ACTIVE_ICON_COLOUR, EYE_SHUT_ICON_PATH);

    // The "trash" icon is used in all states
    public final ImageIcon VISIBLE_TRASH_ICON = createImageIcon(25, 25, VISIBLE_ICON_COLOUR, TRASH_ICON_PATH);
    public final ImageIcon HIDDEN_TRASH_ICON = createImageIcon(25, 25, HIDDEN_ICON_COLOUR, TRASH_ICON_PATH);
    public final ImageIcon ACTIVE_TRASH_ICON = createImageIcon(25, 25, ACTIVE_ICON_COLOUR, TRASH_ICON_PATH);

    // Borders
    public final Border DEFAULT_BORDER = BorderFactory.createLineBorder(new Color(197, 197, 197), 1, true);
    public final Border IS_SELECTED_BORDER = BorderFactory.createLineBorder(new Color(160, 175, 255), 3, false);
    
    /* --------------------------------------- [ICON GENERATION] --------------------------------------- */

    public ImageIcon createImageIcon(int x, int y, String path) {
        return new ImageIcon(
            new ImageIcon(getClass().getResource(path))
                .getImage()
                .getScaledInstance(x, y, java.awt.Image.SCALE_SMOOTH)
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
                // New colours but maintain the originally transparent pixels.
                Color newPixelColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), imageColor.getAlpha());
                image.setRGB(x, y, newPixelColor.getRGB());
            }
        }
        return new ImageIcon(image);
    }

    /* --------------------------------------- [SPACING] --------------------------------------- */

    // Get Largest Dimensions of a layer item
    public Dimension getMaxSize(JPanel panel) {
        return new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height);
    }

    /* --------------------------------------- [REFRESHING] --------------------------------------- */

    public void refreshUI(JComponent object) {
        object.revalidate();
        object.repaint();
    }

}