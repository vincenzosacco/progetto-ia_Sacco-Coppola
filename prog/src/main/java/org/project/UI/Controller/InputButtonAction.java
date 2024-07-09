package org.project.UI.Controller;

import org.project.UI.View.panels.GamePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class InputButtonAction extends AbstractAction {
    private GamePanel.InputButton inputButton;

    public InputButtonAction(GamePanel.InputButton b) {
        super();
        inputButton = b;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
