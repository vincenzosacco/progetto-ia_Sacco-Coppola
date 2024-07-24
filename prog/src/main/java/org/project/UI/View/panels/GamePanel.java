package org.project.UI.View.panels;

import org.project.Logic.Game.Board;
import org.project.Logic.Game.Player;
import org.project.Logic.Game.Unit;
import org.project.UI.Controller.InputButtonAction;
import org.project.UI.Controller.home.PlayButtonController;
import org.project.UI.Model.GameModel;
import org.project.UI.View.panels.home.HomePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.project.UI.Model.GameModel.*;
import static org.project.UI.Settings.*;


public class GamePanel extends MyPanel {
    private final int gameMode;
    private final JButton restartButton, backButton, nextButton;
    private final SpringLayout layout;

    /**
     * Constructor used to create a new GamePanel.
     *
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

        this.gameMode = gameMode;
        setBackground(Color.LIGHT_GRAY);

        //--BUTTONS
        layout = new SpringLayout();
        setLayout(layout);

        restartButton = new JButton("Restart");

        backButton = new JButton("Back");
        backButton.setEnabled(false);
        backButton.addActionListener(e -> {
            if (indexToDraw > 0) indexToDraw--;
            repaint();
        });

        nextButton = new JButton("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener(e -> {
            if (indexToDraw < graphics.size() - 1) indexToDraw++;
            repaint();
        });


        add(backButton);
        add(restartButton);
        add(nextButton);

        //--LAYOUT CONSTRAINTS
        // Back button
        layout.putConstraint(SpringLayout.WEST, backButton, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, backButton, 5, SpringLayout.NORTH, this);
        // Restart button
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, restartButton, 0, SpringLayout.VERTICAL_CENTER, backButton);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, restartButton, 0, SpringLayout.HORIZONTAL_CENTER, this);
        // Next button
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, nextButton, 0, SpringLayout.VERTICAL_CENTER, backButton);
        layout.putConstraint(SpringLayout.EAST, nextButton, -5, SpringLayout.EAST, this);


    }

    private boolean added = false;


    // Override the method because getParent() is not null only after the panel is added to a container.
    @Override
    public void addNotify() {
        super.addNotify();

        //--RESTART BUTTON
        // IF GamePanel is added to an HomePanel
        if (!added && getParent() instanceof HomePanel) {
            restartButton.addActionListener(new PlayButtonController((HomePanel) getParent()));
            added = true;
        }

    }

    private CountDownLatch refreshLatch = new CountDownLatch(1);

    public void setRefreshLatch(CountDownLatch refreshLatch) {
        this.refreshLatch = refreshLatch;
    }

    public void awaitRefresh() throws InterruptedException {
        refreshLatch.await();
    }

    // Reset the latch for the next refresh
    public void resetRefreshLatch() {
        refreshLatch = new CountDownLatch(1);
    }


    //--DRAW METHODS--------------------------------------------------------------------------------------------------------
    //TODO: implentare per input da mouse quando si gioca come Umano

    private final ArrayList<BufferedImage> graphics = new ArrayList<>();
    private int indexToDraw;


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        //--DRAW BOARD
        if (!GameModel.getInstance().getBoard().win()) {
            drawBoard((Graphics2D) g);
        } else {
            // FIRST DRAW AFTER WIN
            if (!backButton.isEnabled()) {
                backButton.setEnabled(true);
                nextButton.setEnabled(true);
                drawBoard((Graphics2D) g);
            }
            // CLICK ON BACK BUTTON
            else {
                g.drawImage(graphics.get(indexToDraw), (getWidth() - BOARD_WIDTH) / 2, (getHeight() - BOARD_HEIGHT) / 2, null);
            }

        }

        refreshLatch.countDown();
    }

    // Ogni chiamata disegna da capo ogni cella, non è efficiente ma per ora va bene
    private void drawBoard(Graphics2D g) {
        //--BACK BUTTON IMAGE
        BufferedImage gImage = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Board board = GameModel.getInstance().getBoard();
        Graphics2D toDraw = gImage.createGraphics();
        toDraw.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Anti-aliasing

        //--DRAW ON IMAGE
        int x, y;
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                // need to invert the coordinates to map the grid coordinates(i,j) to the screen coordinates(x,y).
                // For a good explanation you can ask ChatGPT :
                // "Can you explain why logical grid coordinates are represented as (row, column)
                // while graphical coordinates are represented as (x, y), and how to correctly map these logical coordinates
                // to graphical coordinates?"
                x = j * BOARD_CELL_SIZE; // the x coordinate of the top left corner of the cell
                y = i * BOARD_CELL_SIZE; // the y coordinate of the top left corner of the cell


                //--CELL
                drawCell(toDraw, x, y, i, j);

                //--FLOOR
                drawFloor(toDraw, x, y, i, j, board.heightAt(i, j));

            }
        }

        //--UNIT
        for (Player p : board.getPlayers()) {
            toDraw.setColor(p.getColor());
            for (Unit u : p.getUnits()) {
                x = (u.j() * BOARD_CELL_SIZE) + UNIT_OFFSET / 2; // the x coordinate of the top left corner of the unit; UNIT_OFFSET/2 is the offset to center the unit
                y = (u.i() * BOARD_CELL_SIZE) + UNIT_OFFSET / 2; // the y coordinate of the top left corner of the unit; UNIT_OFFSET/2 is the offset to center the unit
                toDraw.fillOval(x, y, BOARD_CELL_SIZE - UNIT_OFFSET, BOARD_CELL_SIZE - UNIT_OFFSET);
            }
        }

        //--DRAW IMAGE
        // Clear
        g.clearRect((getWidth() - BOARD_WIDTH) / 2, (getHeight() - BOARD_HEIGHT) / 2, BOARD_WIDTH, BOARD_HEIGHT);
        // Draw
        g.drawImage(gImage, (getWidth() - BOARD_WIDTH) / 2, (getHeight() - BOARD_HEIGHT) / 2, this);

        // Add the graphic to the list
        graphics.add(gImage);
        indexToDraw = graphics.size() - 1;

        toDraw.dispose(); // Release resources used by the Graphics object


    }

    private void drawCell(Graphics2D g, int x, int y, int i, int j) {
        g.setColor(FLOOR_0_COLOR);
        g.fill3DRect(x, y, BOARD_CELL_SIZE, BOARD_CELL_SIZE, false);

        //--INVISIBLE BUTTONS FOR MOUSE INPUT
        if (gameMode != AI_VS_AI) {
            add(new InputButton(x, y, i, j));
        }
    }


    private void drawFloor(Graphics2D g, int x, int y, int i, int j, int height) {
        if (height > 0) {
            switch (height) {
                case 1 -> {
                    drawFloorHeight(g, x, y, 0, FLOOR_1_COLOR);
                }

                case 2 -> {
                    drawFloorHeight(g, x, y, 0, FLOOR_1_COLOR);
                    drawFloorHeight(g, x, y, FLOOR_2_OFFSET, FLOOR_2_COLOR);
                }
                case 3 -> {
                    drawFloorHeight(g, x, y, 0, FLOOR_1_COLOR);
                    drawFloorHeight(g, x, y, FLOOR_2_OFFSET, FLOOR_2_COLOR);
                    drawFloorHeight(g, x, y, FLOOR_3_OFFSET, FLOOR_3_COLOR);
                }
                case 4 -> {
                    drawFloorHeight(g, x, y, 0, FLOOR_1_COLOR);
                    drawFloorHeight(g, x, y, FLOOR_2_OFFSET, FLOOR_2_COLOR);
                    drawFloorHeight(g, x, y, FLOOR_3_OFFSET, FLOOR_3_COLOR);

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
     *
     * @param g      the graphics object to draw on.
     * @param x      the x coordinate of the cell.
     * @param y      the y coordinate of the cell.
     * @param offset the offset of the floor.
     * @param color  the color of the floor.
     */
    private void drawFloorHeight(Graphics2D g, int x, int y, int offset, Color color) {
        if (offset > 0) {
            x = x + offset / 2;
            y = y + offset / 2;
        }
        g.setColor(color);
        if (offset != FLOOR_3_OFFSET)
            g.fill3DRect(x, y, BOARD_CELL_SIZE - offset, BOARD_CELL_SIZE - offset, true);
        else
            g.fillRoundRect(x, y, BOARD_CELL_SIZE - offset, BOARD_CELL_SIZE - offset, 10, 10);
    }



    public static class InputButton extends JButton {
        private final int i, j;

        InputButton(int x, int y, int i, int j) {
            super();
            setBounds(x, y, BOARD_CELL_SIZE, BOARD_CELL_SIZE);
            this.i = i;
            this.j = j;

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setAction(new InputButtonAction(this));

        }

        int getRow() {
            return i;
        }

        int getCol() {
            return j;
        }
    }


}
