package org.project.UI.View.panels.home;

import org.project.UI.View.panels.MyPanel;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends MyPanel {
    private final side left, right;
    private final centerPanel center;

    public HomePanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        left = new side("PLAYER 1");
        add(left );

        center = new centerPanel();
        add(center);

        right = new side("PLAYER 2");
        add(right);

    }




    @Override
    public void addNotify() {
        super.addNotify();
        Dimension size = new Dimension((int) (getParent().getWidth() * 0.2), getParent().getHeight());
        left.setPreferredSize(size);
        right.setPreferredSize(size);

        size = new Dimension((int) (getParent().getWidth() * 0.6), getParent().getHeight());
        center.setPreferredSize(size);

        validate();

    }


}
