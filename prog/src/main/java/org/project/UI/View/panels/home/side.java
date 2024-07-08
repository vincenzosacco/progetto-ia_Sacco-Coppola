package org.project.UI.View.panels.home;

import org.project.Logic.FilesFromEncodings;
import org.project.UI.View.panels.MyPanel;
import javax.swing.*;
import java.awt.*;

class side extends JPanel {
    final JTabbedPane tabbedPane;

     side(String title) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    //--HEADER
        JLabel header = new JLabel(title);
        header.setBackground(Color.GRAY);
        add(header);

    //--TAB
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("AI", new TabPanel(title + "_AI"));
        tabbedPane.addTab("Umano", new TabPanel(title));

        add(tabbedPane);
    }


//--INNER CLASSES-----------------------------------------------------------------------------------------------
    private static class TabPanel extends MyPanel {

        private final JTextField nameField;

        TabPanel(String title) {
            super();
            SpringLayout layout = new SpringLayout();
            setLayout(layout);



        //--NAME
            JLabel nameLabel = new JLabel("Nome");
            add(nameLabel);
            nameField = new JTextField(title.replace(" ", "").toLowerCase());
            add(nameField);

        //--LAYOUT CONSTRAINTS

            // NAME
            layout.putConstraint(SpringLayout.EAST, nameLabel, -10, SpringLayout.WEST, nameField);
            layout.putConstraint(SpringLayout.VERTICAL_CENTER, nameLabel, 0, SpringLayout.VERTICAL_CENTER, nameField);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, nameField, 0, SpringLayout.HORIZONTAL_CENTER, this);
        }
    }

    private static class AiTabPanel extends TabPanel{
        private final JComboBox<String> strategy;
        AiTabPanel(String title) {
            super(title);
            strategy = new JComboBox<>();
        }

    }
}
