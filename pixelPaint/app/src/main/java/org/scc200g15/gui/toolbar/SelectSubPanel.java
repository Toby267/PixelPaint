package org.scc200g15.gui.toolbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.scc200g15.gui.GUI;
import org.scc200g15.gui.icons.IconManager;
import org.scc200g15.tools.squareSelect.SquareSelectTool;

public class SelectSubPanel extends JPanel {
    SquareSelectTool tool;

    public SelectSubPanel(int height, SquareSelectTool tool) {
        super(null);

        this.tool = tool;
        
        setMaximumSize(new Dimension(277, height));

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

        JPanel rotatePanel = new JPanel();
        rotatePanel.setLayout(new BoxLayout(rotatePanel, BoxLayout.X_AXIS));
        rotatePanel.setSize(new Dimension(105, height - 4));
        rotatePanel.setLocation(170,2);
        rotatePanel.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));

        JButton rotateCWButton = new JButton(IconManager.ROT_CW_ICON);
        rotatePanel.add(rotateCWButton);       

        JButton rotateACWButton = new JButton(IconManager.ROT_ACW_ICON);
        rotatePanel.add(rotateACWButton);

        Integer[] rotateDropdownOptions = {90, 180, 270};
        JComboBox<Integer> rotateComboBox = new JComboBox<>(rotateDropdownOptions);
        rotateComboBox.setMaximumSize(new Dimension(60, height-5));
        rotateComboBox.setPreferredSize(new Dimension(60, height-5));
        rotatePanel.add(rotateComboBox);
        
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
            tool.rotate(GUI.getInstance().getCanvas(), true, (Integer)rotateComboBox.getSelectedItem());
        });

        rotateACWButton.addActionListener((ActionEvent e) -> {
            tool.rotate(GUI.getInstance().getCanvas(), false, (Integer)rotateComboBox.getSelectedItem());
        });

        //adding the buttons
        add(deleteButton);
        add(escapeButton);
        add(copyButton);
        add(pasteButton);
        add(rotatePanel);
    }
}
