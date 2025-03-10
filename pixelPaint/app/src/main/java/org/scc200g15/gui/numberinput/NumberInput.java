package org.scc200g15.gui.numberinput;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NumberInput extends JSpinner {

    private int oldValue;

    private int MAX;
    private int MIN;

    public NumberInput(int MIN, int MAX){
        super(new SpinnerNumberModel(MIN, MIN, MAX, 1)); // Set the initial value, min, max, and step

        this.MAX = MAX;
        this.MIN = MIN;

        oldValue = (Integer) getValue();

        // Submit when focus lost
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                submitValue();
            }
        });

        // Update valid state whenever a key is pressed
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateInput();
            }
        });

        // Add a DocumentListener to handle invalid input in the spinner text field
        JTextField textField = ((JSpinner.DefaultEditor) getEditor()).getTextField();
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateInput();
            }
        });
    }

    private void validateInput() {
        JTextField textField = ((JSpinner.DefaultEditor) getEditor()).getTextField();
        try {
            int enteredValue = Integer.parseInt(textField.getText());
            if (enteredValue < MIN || enteredValue > MAX) {
                textField.setForeground(Color.RED);
            } else {
                textField.setForeground(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            textField.setForeground(Color.RED);
        }
    }

    private void submitValue() {
        JTextField textField = ((JSpinner.DefaultEditor) getEditor()).getTextField();
        try {
            int enteredValue = Integer.parseInt(textField.getText());
            if (enteredValue < MIN || enteredValue > MAX) {
                setValue(oldValue);
                textField.setText(String.valueOf(oldValue));
            } else {
                oldValue = enteredValue;
            }
        } catch (NumberFormatException e) {
            setValue(oldValue);
            textField.setText(String.valueOf(oldValue));
        }
    }
}
