package org.scc200g15.gui.icons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

public class IconManager {
    /* --------------------------------------- [ICON CONSTANTS] --------------------------------------- */
    private static final String EYE_OPEN_ICON_PATH = "/Icons/open_eye_icon.png";
    private static final String EYE_SHUT_ICON_PATH = "/Icons/closed_eye_icon.png";
    private static final String TRASH_ICON_PATH = "/Icons/trash_icon.png";

    private static final String DRAW_ICON_PATH = "/Icons/Draw.png";
    private static final String PAN_ZOOM_ICON_PATH = "/Icons/Pan_Zoom.png";
    private static final String ERASE_ICON_PATH = "/Icons/eraser.png";
    private static final String FILL_ICON_PATH = "/Icons/fill.png";

    public static final String SQUARE_ICON_PATH = "/Icons/square.png";
    public static final String CIRCLE_ICON_PATH = "/Icons/circle.png";
    public static final String TRIANGLE_ICON_PATH = "/Icons/triangle.png";
    public static final String STAR_ICON_PATH = "/Icons/star.png";

    // Colours for the layer menu background as seen in the UI design
    public static final Color VISIBLE_BACKGROUND_COLOUR = new Color(227,227,227);
    public static final Color INACTIVE_BACKGROUND_COLOUR = VISIBLE_BACKGROUND_COLOUR;
    public static final Color HIDDEN_BACKGROUND_COLOUR = new Color(233,233,233);
    public static final Color ACTIVE_BACKGROUND_COLOUR = new Color(64,64,64);

    // Colours for the icons/text as seen in the UI design
    public static final Color VISIBLE_ICON_COLOUR = new Color(128,128,128);
    public static final Color INACTIVE_ICON_COLOUR = VISIBLE_ICON_COLOUR;
    public static final Color HIDDEN_ICON_COLOUR = new Color(191,191,191);
    public static final Color ACTIVE_ICON_COLOUR = new Color(227,227,227);

    // The "eye open" icon is only used in visible/active states
    public static final ImageIcon VISIBLE_EYE_OPEN_ICON = createImageIcon(30, 30, VISIBLE_ICON_COLOUR, EYE_OPEN_ICON_PATH);
    public static final ImageIcon ACTIVE_EYE_OPEN_ICON = createImageIcon(30, 30, ACTIVE_ICON_COLOUR, EYE_OPEN_ICON_PATH);

    // The "eye shut" icon is only used in hidden/active states
    public static final ImageIcon HIDDEN_EYE_SHUT_ICON = createImageIcon(30, 30, HIDDEN_ICON_COLOUR, EYE_SHUT_ICON_PATH);
    public static final ImageIcon ACTIVE_EYE_SHUT_ICON = createImageIcon(30, 30, ACTIVE_ICON_COLOUR, EYE_SHUT_ICON_PATH);

    // The "trash" icon is used in all states
    public static final ImageIcon VISIBLE_TRASH_ICON = createImageIcon(25, 25, VISIBLE_ICON_COLOUR, TRASH_ICON_PATH);
    public static final ImageIcon HIDDEN_TRASH_ICON = createImageIcon(25, 25, HIDDEN_ICON_COLOUR, TRASH_ICON_PATH);
    public static final ImageIcon ACTIVE_TRASH_ICON = createImageIcon(25, 25, ACTIVE_ICON_COLOUR, TRASH_ICON_PATH);

    // Borders
    public static final Border DEFAULT_BORDER = BorderFactory.createLineBorder(new Color(197, 197, 197), 1, true);
    public static final Border IS_SELECTED_BORDER = BorderFactory.createLineBorder(new Color(160, 175, 255), 3, false);
    
    // Tool Bar Icons
    public static final ImageIcon DRAW_ICON = createImageIcon(32, 32, VISIBLE_ICON_COLOUR, DRAW_ICON_PATH);
    public static final ImageIcon PAN_ZOOM_ICON = createImageIcon(32, 32, PAN_ZOOM_ICON_PATH);
    public static final ImageIcon ERASE_ICON = createImageIcon(32, 32, VISIBLE_ICON_COLOUR, ERASE_ICON_PATH);
    public static final ImageIcon FILL_ICON = createImageIcon(32, 32, VISIBLE_ICON_COLOUR, FILL_ICON_PATH);

    // Shape Icons
    public static final ImageIcon SQUARE_ICON = createImageIcon(16, 16, VISIBLE_ICON_COLOUR, SQUARE_ICON_PATH);
    public static final ImageIcon CIRCLE_ICON = createImageIcon(16, 16, CIRCLE_ICON_PATH);
    public static final ImageIcon TRIANGLE_ICON = createImageIcon(16, 16, TRIANGLE_ICON_PATH);
    public static final ImageIcon STAR_ICON = createImageIcon(16, 16, STAR_ICON_PATH);

    /* --------------------------------------- [ICON GENERATION] --------------------------------------- */

    public static ImageIcon createImageIcon(int x, int y, String path) {
        return new ImageIcon(

            new ImageIcon(IconManager.class.getResource(path))
                .getImage()
                .getScaledInstance(x, y, java.awt.Image.SCALE_SMOOTH)
        );
    }

    public static ImageIcon createImageIcon(int x, int y, Color color, String path) {
        return changeColour(createImageIcon(x, y, path), color);
    }

    public static BufferedImage convert_ImageIcon_To_BufferedImage(ImageIcon icon) {
        // Code from: Werner Kvalem Vesterås (https://stackoverflow.com/questions/15053214/converting-an-imageicon-to-a-bufferedimage)
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return image;
    }

    public static ImageIcon changeColour(ImageIcon icon, Color newColor) {
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
}
