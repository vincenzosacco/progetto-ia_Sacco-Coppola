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
                    currentPlayer = p;

                    long startTime= System.currentTimeMillis();
                    actionSet action = p.call(); //TODO: IMPLEMENTARE UN TIMEOUT PER LE AZIONI
                    long endTime= System.currentTimeMillis();
                    long actionTime = endTime - startTime;

                    // refresh model
                    GameModel.getInstance().playTurn(action);

                    if (actionTime < 700) Thread.sleep(700 - actionTime); // wait for the player to see the move

                    // refresh view
                    ProjectView.getInstance().refreshGamePanel();

                    if (board.win())
                        break;
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
