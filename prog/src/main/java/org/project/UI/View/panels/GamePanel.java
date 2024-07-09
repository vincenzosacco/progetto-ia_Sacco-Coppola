package org.project.UI.View.panels;
import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.Unit;
import org.project.UI.Controller.InputButtonAction;
import org.project.UI.Controller.home.PlayButtonController;
import org.project.UI.Model.GameModel;
import org.project.UI.View.panels.home.HomePanel;

import javax.swing.*;
import java.awt.*;

import static org.project.UI.Model.GameModel.*;
import static org.project.UI.Settings.*;


public class GamePanel extends MyPanel {
    private final int gameMode;
    private final JButton restartButton ;
    /**
     * Constructor used to create a new GamePanel.
     * @param gameMode static constant from {@code GameModel}.
     */
    public GamePanel(int gameMode) {
        super("GamePanel");

        String name = switch (gameMode) {
            case AI_VS_AI -> "AI vs AI";
            case AI_VS_HUMAN -> "AI vs Human";
            case HUMAN_VS_HUMAN -> "Human vs Human";
            default -> throw new IllegalArgumentException("Invalid game mode");
        };
        setName(name);

        restartButton = new JButton("Restart");
        this.gameMode = gameMode;
        setBackground(Color.LIGHT_GRAY);

    }

    private boolean added = false;
    @Override
    public void addNotify() {
        super.addNotify();

    //--RESTART BUTTON
        // IF GamePanel is added to an HomePanel
        if (!added && getParent() instanceof HomePanel){
            restartButton.addActionListener(new PlayButtonController((HomePanel) getParent()));
            add(restartButton);
            added = true;
        }

    }

    //--DRAW METHODS--------------------------------------------------------------------------------------------------------
    //TODO: implentare per input da mouse quando si gioca come Umano

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    //--DRAW BOARD
        drawBoard(g);
    }

    // Ogni chiamata disegna da capo ogni cella, non è efficiente ma per ora va bene
    private void drawBoard(Graphics g) {
        int startX = (getWidth() - BOARD_WIDTH) / 2;
        int startY = (getHeight() - BOARD_HEIGHT) / 2;
        Board board = GameModel.getInstance().getBoard();

        int x, y;
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                x = startX + j * BOARD_CELL_SIZE;
                y = startY + i * BOARD_CELL_SIZE;

            //--CELL
                drawCell(g, x, y, i, j);

            //--FLOOR
                drawFloor(g, x, y, i, j, board);

            }
        }

    //--UNIT
        for (Unit unit : board.getUnits()) {
            g.setColor(unit.player().getColor());
            x = (startX + unit.coord().y * BOARD_CELL_SIZE ) + UNIT_OFFSET/2;
            y = (startY + unit.coord().x * BOARD_CELL_SIZE) + UNIT_OFFSET/2;
            g.fillOval(x, y, BOARD_CELL_SIZE - UNIT_OFFSET , BOARD_CELL_SIZE - UNIT_OFFSET );
        }

    }
    private void drawCell(Graphics g, int x, int y, int i, int j) {
        g.setColor(Color.LIGHT_GRAY);
        g.fill3DRect(x, y, BOARD_CELL_SIZE, BOARD_CELL_SIZE, true);

    //--INVISIBLE BUTTONS FOR MOUSE INPUT
        if (gameMode != AI_VS_AI){
            add(new InputButton(x, y, i, j));
        }
    }


    private void drawFloor(Graphics g, int x, int y, int i, int j,  Board board) {
        int floor = board.heightAt(i, j);
        if (floor > 0) {
            int offset;
            switch (floor) {
                case 1 -> {
                    drawFloorHeight(g, x, y, 0, Color.GRAY);
                }

                case 2 -> {
                    drawFloorHeight(g, x, y, 0, Color.GRAY);
                    drawFloorHeight(g, x, y, FLOOR_2_OFFSET, Color.DARK_GRAY);
                }
                case 3 -> {
                    drawFloorHeight(g, x, y, 0, Color.GRAY);
                    drawFloorHeight(g, x, y, FLOOR_2_OFFSET, Color.DARK_GRAY);
                    drawFloorHeight(g, x, y, FLOOR_3_OFFSET, new Color(231, 214, 0));
                }
                case 4 -> {
                    drawFloorHeight(g, x, y, 0, Color.GRAY);
                    drawFloorHeight(g, x, y, FLOOR_2_OFFSET, Color.DARK_GRAY);
                    drawFloorHeight(g, x, y, FLOOR_3_OFFSET, new Color(231, 214, 0));

                    // Draw an X on the cell
                    g.setColor(Color.RED);
                    g.drawLine(x, y, x + BOARD_CELL_SIZE, y + BOARD_CELL_SIZE);
                    g.drawLine(x + BOARD_CELL_SIZE, y, x, y + BOARD_CELL_SIZE);


                    // Makes the cell opaque
                    g.setColor(new Color(0, 0, 0, 100));
                    g.fillRect(x, y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);
                }
                default -> throw new IllegalArgumentException("Invalid height");
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
    private void drawFloorHeight(Graphics g, int x, int y, int offset, Color color) {
        if (offset > 0){
            x = x + offset/2;
            y = y + offset/2;
        }
        g.setColor(color);
        if (offset != 3)
            g.fill3DRect(x, y, BOARD_CELL_SIZE-offset, BOARD_CELL_SIZE-offset, true);
        else
            g.fillRoundRect(x, y, BOARD_CELL_SIZE-offset, BOARD_CELL_SIZE-offset, 10, 10);
    }

    public static class InputButton extends JButton {
        private final int i,j;
        InputButton(int x, int y, int i, int j) {
            super();
            setBounds(x, y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);
            this.i = i;
            this.j = j;

//            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setAction(new InputButtonAction(this));

        }

        int getRow(){
             return i;
        }

        int getCol(){
             return j;
        }
    }


}
