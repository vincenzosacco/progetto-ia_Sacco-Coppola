package org.project.UI.View.panels.home;

import org.project.UI.Controller.home.HomeController;
import org.project.UI.View.panels.MyPanel;

import javax.swing.*;
import java.awt.*;

class center extends MyPanel {
    private final SpringLayout layout;

    private JButton play_AIvsAI, play_AIvsHuman, play_HumanvsHuman;


     center() {
        super();
        buttonListener = new HomeController();
        setupButtons();


    //--LAYOUT
        layout = new SpringLayout();
        setLayout(layout);

        setupConstraints();

    }

    private final HomeController buttonListener;
    private void setupButtons() {
    //--AI vs AI
        play_AIvsAI = new JButton("AI vs AI");
        play_AIvsAI.addActionListener(buttonListener);
        add(play_AIvsAI);

    //--AI vs Human
        play_AIvsHuman = new JButton("AI vs Umano");
        play_AIvsHuman.addActionListener(buttonListener);
        add(play_AIvsHuman);

    //--Human vs Human
        play_HumanvsHuman = new JButton("Umano vs Umano");
        play_HumanvsHuman.addActionListener(buttonListener);
        add(play_HumanvsHuman);
    }

    private void setupConstraints() {
    //--AI vs AI
        layout.putConstraint(SpringLayout.NORTH, play_AIvsAI, 50, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, play_AIvsAI, 0, SpringLayout.HORIZONTAL_CENTER, this);
    //--AI vs Human
        layout.putConstraint(SpringLayout.NORTH, play_AIvsHuman, 50, SpringLayout.SOUTH, play_AIvsAI);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, play_AIvsHuman, 0, SpringLayout.HORIZONTAL_CENTER, this);
    //--Human vs Human
        layout.putConstraint(SpringLayout.NORTH, play_HumanvsHuman, 50, SpringLayout.SOUTH, play_AIvsHuman);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, play_HumanvsHuman, 0, SpringLayout.HORIZONTAL_CENTER, this);

    }
}
