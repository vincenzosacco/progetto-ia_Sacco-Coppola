package org.project.UI.View.panels.home;

import org.project.UI.View.ProjectView;
import org.project.UI.View.panels.MyPanel;

import javax.swing.*;
import java.awt.*;

import static org.project.UI.Settings.BACKGROUND_COLOR;

public class HomePanel extends MyPanel {
    private final side left, right;
    private final center center;

    public HomePanel() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        left = new side("PLAYER 1");
        add(left );

        center = new center();
        add(center);

        right = new side("PLAYER 2");
        add(right);

    }




    @Override
    public void addNotify() {
        super.addNotify();
        Dimension size = new Dimension((int) (getParent().getWidth() * 0.4), getParent().getHeight());
        center.setPreferredSize(size);
        validate();

    }


}
