package org.project.Logic.Game.player.human;

import org.project.Logic.Game.player.Player;

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


}
