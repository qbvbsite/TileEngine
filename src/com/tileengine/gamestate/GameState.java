package com.tileengine.gamestate;

import com.tileengine.GameController;
import com.tileengine.gamescreen.GameScreen;
import com.tileengine.InputController;
import com.tileengine.TileEngineCanvas;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Inferface for GameState
 *
 * @author James Keir
 */
public interface GameState
{
    /**
     * Gets all the current game screens
     *
     * @return Returns array of game screens
     */
    public ArrayList getGameScreens();

    /**
     * Adds a game screen to GameState
     *
     * @param gameScreen Game screen to add to the GameState
     */
    public void addGameScreen(GameScreen gameScreen);

    /**
     * Add a game screen at a specific index
     *
     * @param gameScreen Game screen to add to the GameState
     * @param index Index to add the game screen to
     */
    public void addGameScreen(GameScreen gameScreen, int index);

    /**
     * Remove a game screen from GameState
     *
     * @param gameScreen Game screen to remove
     */
    public void removeGameScreen(GameScreen gameScreen);

    /**
     * Set the current controller for the game state
     *
     * @param gameController Game controller to set as current
     */
    public void setCurrentGameController(GameController gameController);

    /**
     * Gets the current game controller for game state
     *
     * @return Current game controller
     */
    public GameController getCurrentGameController();

    /**
     * Changed game controller push old controller into stack
     *
     * @param gameController Game controller to put into stack
     */
    public void pushGameController(GameController gameController);

    /**
     * Pops old scene from stack
     *
     * @return old game controller
     */
    public GameController popGameController();

    /**
     * Used to setup kep mappings for game state
     */
    public void setupKeyMappings();

    /**
     * Used to remove kep mappings for game state
     */
    public void removeKeyMappings();

    /**
     * Used to change a game state
     * @param switchGameState GameState to switch to
     */
    public void pushGameState(GameState switchGameState);

    /**
     * This is for the game state to check if they should goto a new state
     * @return GameState to switch to ro null
     */

    public GameState popGameState();
    /**
     * Process input for GameState
     *
     * @param parent Tile Engine
     * @param inputController Input Controller
     * @param timeElapsed Time that elapsed since last call
     * @return GameState to move to (Mostly will be its self)
     */
    public GameState processInput(TileEngineCanvas parent, InputController inputController, long timeElapsed);

    /**
     * Preforms logic for the GameState
     *
     * @param parent Tile Engine
     * @param timeElasped Time that elapsed since last call
     * @return GameState to move to (Mostly will be its self)
     */
    public GameState preformLogic(TileEngineCanvas parent, long timeElasped);

    /**
     * Updates the screen of the GameState
     *
     * @param parent Tile Engine
     * @param graphicBuffer graphicBuffer Graphic buffer to update
     * @param timeElasped Time that elapsed since last call
     * @return GameState to move to (Mostly will be its self)
     */
    public GameState updateScreen(TileEngineCanvas parent, Graphics2D graphicBuffer, long timeElasped);
}
