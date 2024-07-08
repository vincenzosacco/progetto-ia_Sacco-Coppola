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
    int BOARD_WIDTH = BOARD_COLS * BOARD_CELL_SIZE;
    int BOARD_HEIGHT = BOARD_ROWS * BOARD_CELL_SIZE;

    //FLOORS
    int FLOOR_2_OFFSET = (int) (BOARD_CELL_SIZE - (BOARD_CELL_SIZE * 0.8));
    int FLOOR_3_OFFSET = (int) (BOARD_CELL_SIZE - (BOARD_CELL_SIZE * 0.6));

    //UNIT
    int UNIT_OFFSET = (int) (FLOOR_3_OFFSET * 0.8); // must be smaller than floor 3 size



}
