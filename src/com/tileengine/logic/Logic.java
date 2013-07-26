package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.GameObject;

/**
 * Logic interface for game logic
 *
 * @author James Keir
 */
public interface Logic
{
    /*
     * General Logic
     */
    public static final int LOGIC_TYPE_GENERAL = 2;

    /*
     * Collide Logic
     */
    public static final int LOGIC_TYPE_COLLIDE = 4;

    /*
     * Custom Logic
     */
    public static final int LOGIC_TYPE_CUSTOM = 8;
    /**
     * Sets the parameters for the Logic Object
     *
     * @param parameters Parameters to set
     */
    public void setParameters(Object[] parameters);

    /**
     * Gets the parameters of the logic Object
     *
     * @return Return current parameters
     */
    public Object[] getParameters();

    /**
     * Executes logic for the object
     *
     * @param gameController Executing game controller
     * @param gameState Executing game controller
     * @param caller Object executing logic
     * @param initiator Object that initiated logic (colliding object for instance)
     * @param timeElapsed Time Elapsed since last call (Mainly for general logic)
     */
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed);
}
