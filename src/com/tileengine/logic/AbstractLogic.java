package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.GameObject;

/**
 * Abstact logic object for Parameter handling
 *
 * @author James Keir
 */
public abstract class AbstractLogic implements Logic
{
    /*
     * Parameters for Logic
     */
    private Object[] parameters = null;

    public void setParameters(Object[] parameters)
    {
        this.parameters = parameters;
    }

    public Object[] getParameters()
    {
        return this.parameters;
    }

    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, Object[] parameters, int timeElapsed)
    {
        setParameters(parameters);
        
        //Execute Logic with no initiator
        return executeLogic(gameController, gameState, caller, null, timeElapsed);
    }

    public abstract boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed);
}
