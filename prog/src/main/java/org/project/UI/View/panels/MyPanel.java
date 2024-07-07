package org.project.UI.View.panels;

import javax.swing.*;

import static org.project.UI.Settings.BACKGROUND_COLOR;

public abstract class MyPanel extends JPanel {
    public MyPanel() {
        super();
        setBackground(BACKGROUND_COLOR);
    }

}
