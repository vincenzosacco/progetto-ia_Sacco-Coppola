package org.project.UI.View.panels;

import javax.swing.*;

import static org.project.UI.Settings.BACKGROUND_COLOR;

public abstract class MyPanel extends JPanel {
    private static int NAME_COUNTER = 0;

    public MyPanel(String name) {
        super();
        setName(name + NAME_COUNTER++);

        setBackground(BACKGROUND_COLOR);

    }

}
