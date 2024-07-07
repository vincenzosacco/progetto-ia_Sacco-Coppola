package org.project.UI.View.panels;
import org.project.Logic.Game.Board;
import org.project.UI.Model.GameModel;

import java.awt.*;

import static org.project.UI.Settings.*;


public class GamePanel extends MyPanel {
    public static final int AI_VS_AI = 0;
    public static final int AI_VS_HUMAN = 1;
    public static final int HUMAN_VS_HUMAN = 2;

    /**
     * Constructor for the GamePanel class.
     * @param gameMode the game mode to be played. It can be AI_VS_AI, AI_VS_HUMAN or HUMAN_VS_HUMAN.
     */
    public GamePanel(int gameMode) {
        super();
        init(gameMode);


        setBackground(Color.LIGHT_GRAY);

    }

    private void init(int gameMode) {
        String name;
        switch (gameMode) {
            case AI_VS_AI:
                name = "AI vs AI";

                break;
            case AI_VS_HUMAN:
                name = "AI vs Human";
                break;
            case HUMAN_VS_HUMAN:
                name = "Human vs Human";
                break;
            default: throw new IllegalArgumentException("Invalid game mode");
        }

        setName(name);
    }

//--VIEW METHODS--------------------------------------------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    //--DRAW MATRIX
        drawBoard(g);
    }

    // Ogni chiamata disegna da capo ogni cella, non è efficiente ma per ora va bene
    private void drawBoard(Graphics g) {
        int startX = (getWidth() - BOARD_WIDTH) / 2;
        int startY = (getHeight() - BOARD_HEIGHT) / 2;
        Board board = GameModel.getInstance().getBoard();

        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
        //--DRAW CELL
                int x = startX + j * BOARD_CELL_SIZE;
                int y = startY + i * BOARD_CELL_SIZE;
//                g.drawRect(x, y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);
                g.setColor(Color.GRAY);
                g.fill3DRect(x, y, BOARD_CELL_SIZE, BOARD_CELL_SIZE, true);

        //--FLOOR
                int floor = board.heightAt(i, j);
                if (floor>0){
                    int offset;
                    switch (floor) {
                        case 1 -> {
                            drawFloor(g, x, y, 0, Color.DARK_GRAY);
                        }

                        case 2 -> {
                            drawFloor(g, x, y, 0, Color.DARK_GRAY);
                            drawFloor(g, x, y, FLOOR_2_OFFSET, Color.GRAY);
                        }
                        case 3 -> {
                            drawFloor(g, x, y, 0, Color.DARK_GRAY);
                            drawFloor(g, x, y, FLOOR_2_OFFSET, Color.GRAY);
                            drawFloor(g, x, y, FLOOR_3_OFFSET, new Color(231, 214, 0));
                        }
                        case 4 ->{
                            drawFloor(g, x, y, 0, Color.DARK_GRAY);
                            drawFloor(g, x, y, FLOOR_2_OFFSET, Color.GRAY);
                            drawFloor(g, x, y, FLOOR_3_OFFSET, new Color(231, 214, 0));

                            // Draw an X on the cell
                            g.setColor(Color.RED);
                            g.drawLine(x, y, x+BOARD_CELL_SIZE, y+BOARD_CELL_SIZE);
                            g.drawLine(x+BOARD_CELL_SIZE, y, x, y+BOARD_CELL_SIZE);


                            // Makes the cell opaque
                            g.setColor(new Color(0, 0, 0, 100));
                            g.fillRect(x, y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);
                        }
                        default -> throw new IllegalArgumentException("Invalid height");
                    }
                }

        //--UNIT
                if (board.unitAt(i, j) != null) {
                    g.setColor(Color.BLUE);

                    x = x + 5;
                    y = y + 5;

                    g.fillOval(x , y , UNIT_SIZE, UNIT_SIZE);
                }
            }
        }

    }

    /**
     * Draw a floor on the board.
     * @param g the graphics object to draw on.
     * @param x the x coordinate of the cell.
     * @param y the y coordinate of the cell.
     * @param offset the offset of the floor.
     * @param color the color of the floor.
     */
    private void drawFloor(Graphics g, int x, int y, int offset, Color color) {
        if (offset > 0){
            x = x + offset/2;
            y = y + offset/2;
        }
        g.setColor(color);
        g.fill3DRect(x, y, BOARD_CELL_SIZE-offset, BOARD_CELL_SIZE-offset, true);
    }



}
