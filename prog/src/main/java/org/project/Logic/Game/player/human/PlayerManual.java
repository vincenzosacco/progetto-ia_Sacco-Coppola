package org.project.Logic.Game.player.human;

import org.project.Logic.Game.Player;
import org.project.Logic.Game.player.ai.actionSet;

import java.awt.*;
import java.util.Objects;

public class PlayerManual extends Player {
    public PlayerManual(Color color) {
        super(color);
    }

    @Override
    public PlayerManual copy() {
        return new PlayerManual(this);
    }

    public PlayerManual(PlayerManual player) {
        super(player);
    }


    @Override
    public int hashCode() {
        return Objects.hash(color, playerCode);
    }

    private Thread caller = null;

    @Override
    public actionSet call() throws Exception {
        //--WAIT FOR USER INPUT
        caller = Thread.currentThread();
        synchronized (caller) {
            caller.wait();
        }


        return null;
    }

    /**
     * Returns the thread that called {@code call()} method.
     *
     * @return
     */
    public Thread getCaller() {
        return caller;
    }
}
