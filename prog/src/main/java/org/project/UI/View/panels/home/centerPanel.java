package org.project.UI.View.panels.home;

import org.project.UI.Controller.home.PlayButtonController;
import org.project.UI.View.panels.MyPanel;

import javax.swing.*;

public class centerPanel extends MyPanel {
    private final SpringLayout layout;
    private final JButton play;


    centerPanel() {
        super();

        layout = new SpringLayout();
        setLayout(layout);


    //--BUTTONS
        play = new JButton("GIOCA");

        add(play);

    //--LAYOUT CONSTRAINTS
        setupConstraints();

    }

    @Override
    public void addNotify() {
        super.addNotify();
        HomePanel parent;
        try {
            parent = (HomePanel) getParent();
        }
        catch (ClassCastException e) {
            throw new RuntimeException("centerPanel must be added to a HomePanel");
        }

        play.addActionListener(new PlayButtonController(parent));
    }

    private void setupConstraints() {
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, play, 0, SpringLayout.VERTICAL_CENTER, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, play, 0, SpringLayout.HORIZONTAL_CENTER, this);

    }
}
