package org.project.Logic.embAsp;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;



public interface Group {
    /**
    * Method used to set all the necessary settings for running embAsp<p>
    * @param player
    * @return the actionSet to be executed, if the unit cannot perform any action, return a NullAction.
    * @throws Exception
    */
    actionSet callEmbAsp(PlayerAi player) throws Exception ;


}
