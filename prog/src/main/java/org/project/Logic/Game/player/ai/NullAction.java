package org.project.Logic.Game.player.ai;

import org.project.Logic.Game.Player;

import java.awt.*;

/**
 * This class represent a null action.<p>
 * Return an instance of this class in {@code callEmbAsp} when the player cannot move or build.
 */
public class NullAction extends actionSet {
    public NullAction(Player player, int unitCode) {
        super(player, unitCode, new Point(), new Point());
    }

    @Override
    public Point move(){
        return null;
    }

    @Override
    public Point build(){
        return null;
    }


}
