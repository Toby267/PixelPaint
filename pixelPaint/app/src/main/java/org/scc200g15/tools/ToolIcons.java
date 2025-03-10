package org.scc200g15.tools;

import java.net.URL;

import javax.swing.ImageIcon;


public final class ToolIcons {
    private static ImageIcon createImageIcon(int x, int y, String path) {
        URL p = ToolIcons.class.getResource(path);
        
        ImageIcon icon = new ImageIcon(p);
        //java.awt.Image image = icon.getImage().getScaledInstance(x, y, java.awt.Image.SCALE_SMOOTH);
        //return new ImageIcon(image, icon.getDescription());
        return icon;
    }

    // nZoom in icons created by berkahicon - Flaticon: https://www.flaticon.com/free-icons/zoom-i
    public static final ImageIcon PAN_ZOOM_ICON = createImageIcon(64,64, "/Icons/Pan_Zoom.png");
    // Pencil icons created by Those Icons - Flaticon: https://www.flaticon.com/free-icons/pencil
    public static final ImageIcon DRAW_ICON = createImageIcon(64,64, "/Icons/Draw.png");
}