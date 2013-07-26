package com.tileengine.gamescreen;

import com.tileengine.InputController;
import com.tileengine.TileEngineCanvas;
import java.awt.Graphics2D;

/**
 * Interface for a GameScreen
 *
 * @author James Keir
 */
public interface GameScreen
{
    /**
     * Used to override the GameStates input
     *
     * @return true/false
     */
    public boolean overrideProcessInput();

    /**
     * Process GameScreen Input
     *
     * @param parent Tile Engine
     * @param inputController Input controller
     * @param timeElapsed Time that elapsed since last call
     */
    public void processInput(TileEngineCanvas parent, InputController inputController, long timeElapsed);

    /**
     * Used to override the GameStates logic
     * 
     * @return true/false
     */
    public boolean overridePreformLogic();

    /**
     * Process GameScreen Logic
     *
     * @param parent Tile Engine
     * @param timeElasped Time that elapsed since last call
     */
    public void preformLogic(TileEngineCanvas parent, long timeElasped);

    /**
     * Process GameScreen render
     *
     * @param parent Tile Engine
     * @param graphicBuffer Graphic buffer to update
     * @param timeElasped Time that elapsed since last call
     */
    public void updateScreen(TileEngineCanvas parent, Graphics2D graphicBuffer, long timeElasped);
}
