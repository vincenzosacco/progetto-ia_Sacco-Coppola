package org.project.UI.View.panels.home;

import org.project.Logic.FilesFromEncodings;
import org.project.UI.View.panels.MyPanel;

import javax.swing.*;
import java.awt.*;

class side extends JPanel {
    final JTabbedPane tabbedPane;
    final TabPanel ai, human;

    side(String title) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //--HEADER
        JLabel header = new JLabel(title);
        header.setBackground(Color.GRAY);
        add(header);

        //--TAB
        tabbedPane = new JTabbedPane();
        ai = new AiTabPanel(title + "_AI");
        human = new TabPanel(title);
        tabbedPane.addTab("AI", ai);
        tabbedPane.addTab("Umano", human);

        add(tabbedPane);
    }


    //--INNER CLASSES-----------------------------------------------------------------------------------------------
    static class TabPanel extends MyPanel {
        protected final SpringLayout layout;
        protected final JPanel name, color;
        private final JTextField nameField;
        private final JButton colorButton;

        private static final Color[] COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE};
        private static int NEXT_COLOR = 0;

        TabPanel(String title) {
            super("TabPanel");
            layout = new SpringLayout();
            setLayout(layout);

            //--NAME
            JLabel nameLabel = new JLabel("Nome");
            nameField = new JTextField(title.replace(" ", "").toLowerCase());

            name = new JPanel();
            name.add(nameLabel);
            name.add(nameField);

            add(name);

            //--COLOR
            JLabel colorLabel = new JLabel("Colore");
            colorButton = new JButton();
            colorButton.setBackground(COLORS[NEXT_COLOR++]);
            colorButton.addActionListener(e -> {
                colorButton.setBackground(COLORS[NEXT_COLOR++]);
                if (NEXT_COLOR == COLORS.length) NEXT_COLOR = 0;
            });

            color = new JPanel();
            color.add(colorLabel);
            color.add(colorButton);

            add(color);

            //--LAYOUT CONSTRAINTS
            // NAME
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, name, 0, SpringLayout.HORIZONTAL_CENTER, this);

            // COLOR
            layout.putConstraint(SpringLayout.NORTH, color, 10, SpringLayout.SOUTH, name);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, color, 0, SpringLayout.HORIZONTAL_CENTER, this);
        }

        Color choosedColor() {
            return colorButton.getBackground();
        }
    }

    static class AiTabPanel extends TabPanel {
        private final JComboBox<String> strategyBox;
        private final JLabel strategyLabel;

        AiTabPanel(String title) {
            super(title);

            strategyLabel = new JLabel("Strategia");
            strategyBox = new JComboBox<String>(FilesFromEncodings.getStrategies());

            JPanel strategy = new JPanel();
            strategy.add(strategyLabel);
            strategy.add(strategyBox);

            add(strategy);

            // LAYOUT CONSTRAINTS
            layout.putConstraint(SpringLayout.NORTH, strategy, 10, SpringLayout.SOUTH, color);
            layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, strategy, 0, SpringLayout.HORIZONTAL_CENTER, this);
        }

        String choosedStrategy() {
            return (String) strategyBox.getSelectedItem();
        }

    }
}
