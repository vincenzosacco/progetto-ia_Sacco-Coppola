package org.project.UI.View.panels.home;

import org.project.UI.View.panels.MyPanel;

import javax.swing.*;

public class side extends MyPanel {
    private final JLabel header;
    private final JTextField nameField;


    public side(String title) {
        super();
        SpringLayout layout = new SpringLayout();
        setLayout(layout);

    //--HEADER
        header = new JLabel(title);
        add(header);

    //--NAME
        JLabel nameLabel = new JLabel("Nome");
        add(nameLabel);
        nameField = new JTextField(title.replace(" ", "_").toLowerCase());
        add(nameField);

    //--LAYOUT CONSTRAINTS

        // HEADER
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, header, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, header, 10, SpringLayout.NORTH, this);

        // NAME
        layout.putConstraint(SpringLayout.EAST, nameLabel, -10, SpringLayout.WEST, nameField);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, nameLabel, 0, SpringLayout.VERTICAL_CENTER, nameField);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, nameField, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, nameField, 10, SpringLayout.SOUTH, header);

    }


}
