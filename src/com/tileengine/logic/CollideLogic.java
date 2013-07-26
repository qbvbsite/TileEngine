/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tileengine.logic;

import com.tileengine.AudioController;
import com.tileengine.GameController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;

/**
 *
 * @author Jammer
 */
public class CollideLogic extends AbstractLogic
{
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {
        if(initiator instanceof MoveableObject)
        {
            AudioController.playOnce("sounds/zelda_get_heart.wav");
            
            return true;
        }

        return false;
    }
}
