package com.tileengine.gamescreen;

import com.tileengine.InputController;
import com.tileengine.TileEngineCanvas;

/**
 * Abstract Basic GameScreen
 *
 * @author James Keir
 */
public abstract class AbstractGameScreen implements GameScreen
{
    /**
     * Default to not override the input
     *
     * @return false
     */
    public boolean overrideProcessInput()
    {
        return false;
    }

    /**
     * Default to not having any input
     *
     * @param parent Tile Engine
     * @param inputController Input controller
     * @param timeElapsed Time that elapsed since last call
     */
    public void processInput(TileEngineCanvas parent, InputController inputController, long timeElapsed)
    {}

     /**
     * Default to not override the logic
     *
     * @return false
     */
    public boolean overridePreformLogic()
    {
        return false;
    }

    /**
     * Default to not having any logic
     *
     * @param parent Tile Engine
     * @param timeElasped Time that elapsed since last call
     */
    public void preformLogic(TileEngineCanvas parent, long timeElasped)
    {}
}
