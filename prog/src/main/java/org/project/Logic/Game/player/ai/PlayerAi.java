package org.project.Logic.Game.player.ai;


import org.project.Logic.FilesFromEncodings;
import org.project.Logic.Game.player.Player;
import org.project.Logic.embAsp.Group;

import java.awt.*;
import java.util.Objects;


public class PlayerAi extends Player {
    private final Group myGroup;





    public PlayerAi(Color color, String strategy) {
        super(color);
        myGroup = FilesFromEncodings.getGroupFromStrategy(strategy);
    }

    public PlayerAi(PlayerAi player){
        //MAKING A COPY OF THE PLAYER
        super(player);
        myGroup = player.myGroup;
    }


    @Override
    public PlayerAi copy() {
        return new PlayerAi(this);
    }


//--GAME METHODS--------------------------------------------------------------------------------------------------------
    //TODO:trovare un modo per non inizializzare ogni volta handler in Group.java
    //TODO: implementare per  più di una pedina per giocatore
    @Override
    public actionSet call() throws Exception {
        return myGroup.callEmbAsp(this);
    }


        //--JAVA OBJECT--------------------------------------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return Objects.hash(playerCode, color, myGroup.getClass().toString());
    }
}
