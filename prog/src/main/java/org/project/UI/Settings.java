package org.project.UI;

import java.awt.*;

public interface Settings {
    Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension GAME_SIZE_DEFAULT = new Dimension((int) (SCREEN_SIZE.width / 1.5), (int) (SCREEN_SIZE.height / 1.5));
    Color BACKGROUND_COLOR = new Color(0xD8ECF5);

//--BOARD SETTINGS--------------------------------------------------------------------------------------------------------
    int BOARD_ROWS = 5;
    int BOARD_COLS = 5;
    int BOARD_CELL_SIZE = 50;
    int BOARD_WIDTH = BOARD_ROWS * BOARD_CELL_SIZE;
    int BOARD_HEIGHT = BOARD_COLS * BOARD_CELL_SIZE;

    //FLOORS
    Color FLOOR_0_COLOR = new Color(220, 220, 220);
    Color FLOOR_1_COLOR = Color.GRAY;
    Color FLOOR_2_COLOR = Color.DARK_GRAY;
    Color FLOOR_3_COLOR = new Color(231, 214, 0);

    int FLOOR_2_OFFSET = (int) (BOARD_CELL_SIZE * 0.2);
    int FLOOR_3_OFFSET = (int) (BOARD_CELL_SIZE * 0.4);

    //UNIT
    int UNIT_OFFSET = (int) (FLOOR_3_OFFSET * 1.5); // must be smaller than floor 3 size



}
