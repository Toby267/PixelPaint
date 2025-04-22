package org.scc200g15.gui.toolbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.icons.IconManager;
import org.scc200g15.tools.squareSelect.SquareSelectTool;

public class SelectSubPanel extends JPanel {
    SquareSelectTool tool;

    public SelectSubPanel(int height, SquareSelectTool tool) {
        super(null);

        this.tool = tool;
        
        setMaximumSize(new Dimension(256, height));

        //buttons
        JButton deleteButton = new JButton(IconManager.DELETE_ICON);
        deleteButton.setSize(new Dimension(40, height - 4));
        deleteButton.setLocation(2,2);

        JButton escapeButton = new JButton(IconManager.ESCAPE_ICON);
        escapeButton.setSize(new Dimension(40, height - 4));
        escapeButton.setLocation(44,2);

        JButton copyButton = new JButton(IconManager.COPY_ICON);
        copyButton.setSize(new Dimension(40, height - 4));
        copyButton.setLocation(86,2);
        
        JButton pasteButton = new JButton(IconManager.PASTE_ICON);
        pasteButton.setSize(new Dimension(40, height - 4));
        pasteButton.setLocation(128,2);

        JButton rotateCWButton = new JButton(IconManager.ROT_CW_ICON);
        rotateCWButton.setSize(new Dimension(40, height - 4));
        rotateCWButton.setLocation(170,2);

        JButton rotateACWButton = new JButton(IconManager.ROT_ACW_ICON);
        rotateACWButton.setSize(new Dimension(40, height - 4));
        rotateACWButton.setLocation(212,2);

        //button action listeners
        deleteButton.addActionListener((ActionEvent e) -> {
            tool.delete(GUI.getInstance().getCanvas());
        });

        escapeButton.addActionListener((ActionEvent e) -> {
            tool.escape(GUI.getInstance().getCanvas());
        });

        copyButton.addActionListener((ActionEvent e) -> {
            tool.copy(GUI.getInstance().getCanvas());
        });

        pasteButton.addActionListener((ActionEvent e) -> {
            tool.paste(GUI.getInstance().getCanvas());
        });

        rotateCWButton.addActionListener((ActionEvent e) -> {
            tool.rotate(GUI.getInstance().getCanvas(), true);
        });

        rotateACWButton.addActionListener((ActionEvent e) -> {
            tool.rotate(GUI.getInstance().getCanvas(), false);
        });

        //adding the buttons
        add(deleteButton);
        add(escapeButton);
        add(copyButton);
        add(pasteButton);
        add(rotateCWButton);
        add(rotateACWButton);
    }
}
