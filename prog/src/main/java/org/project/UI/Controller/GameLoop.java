package org.project.UI.Controller;

import org.project.Logic.Game.Board;
import org.project.Logic.Game.Player;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.UI.Model.GameModel;
import org.project.UI.View.ProjectView;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class GameLoop {
    private static ScheduledThreadPoolExecutor executor;
    private static boolean isAlive = false;
    static Player currentPlayer;

    public static void startGameLoop() {
        Board board = GameModel.getInstance().getBoard();

        isAlive = true;
        try {
            //--GAMELOOP
            while (!board.win() && isAlive) {
                for (Player p : board.getPlayers()) {

                    long startTime = System.currentTimeMillis();
                    actionSet action = p.call(); //TODO: IMPLEMENTARE UN TIMEOUT PER LE AZIONI
                    long endTime = System.currentTimeMillis();
                    long actionTime = endTime - startTime;


                    long delay = Math.max(700 - actionTime, 200); // Minimum delay is 100ms
                    try {
                        Thread.sleep(delay); // Sleep for a while to make sure the player can see the result of his action
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Manage the interruption
                    }


                    // refresh model
                    GameModel.getInstance().playTurn(action);

                    // refresh view
                    ProjectView.getInstance().refreshGamePanel(Thread.currentThread());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            isAlive = false;
            if (caller != null) {
                synchronized (caller) {
                    caller.notify();
                }
            }

            ProjectView.getInstance().winGame();
        }
    }


    private static Thread caller = null;

    /**
     * By calling this method the game will be interrupted at the end of the current turn.
     *
     * @param caller the thread that called the method.
     * @Warning: This method makes the caller thread wait until the game loop is stopped.
     */
    public static void stopGameLoop(Thread caller) throws InterruptedException {
        isAlive = false;
        GameLoop.caller = caller;
        synchronized (GameLoop.caller) {
            GameLoop.caller.wait();
        }
    }

    /**
     * Check if the game loop is running.
     *
     * @return
     */
    public static boolean isRunning() {
        return isAlive;
    }


}
