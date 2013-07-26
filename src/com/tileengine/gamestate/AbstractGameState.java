package com.tileengine.gamestate;

import com.tileengine.GameController;
import com.tileengine.gamescreen.GameScreen;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Basic GameState to handle GameScreens and GameController Switching
 *
 * @author Jammer
 */
public abstract class AbstractGameState implements GameState
{
    private ArrayList gameScreens = null;
    private HashMap gameControllers = null;
    private GameState switchGameState = null;
    private String currentController = null;
    
    public ArrayList getGameScreens()
    {
        return gameScreens;
    }

    public void addGameScreen(GameScreen gameScreen)
    {
        if(gameScreens == null)
        {
            gameScreens = new ArrayList();
        }

        gameScreens.add(gameScreen);
    }

    public void addGameScreen(GameScreen gameScreen, int index)
    {
        if(gameScreens == null)
        {
            gameScreens = new ArrayList();
        }

        gameScreens.add(index, gameScreen);
    }

    public void removeGameScreen(GameScreen gameScreen)
    {
        gameScreens.remove(gameScreen);
    }

    public String getCurrentGameControllerKey()
    {
        return currentController;
    }
    
    public GameController getCurrentGameController()
    {
        return (GameController) gameControllers.get(currentController);
    }

    public void addGameController(String controllerKey, GameController gameController)
    {
        addGameController(controllerKey, gameController, false);
    }
    
    public void addGameController(String controllerKey, GameController gameController, boolean activateController)
    {
        if(gameControllers == null)
        {
            gameControllers = new HashMap();
        }

        gameControllers.put(controllerKey, gameController);
        
        if(activateController)
        {
            activateGameController(controllerKey);
        }
    }

    public GameController getGameController(String controllerKey)
    {
        GameController gameController = (GameController) gameControllers.get(controllerKey);
        
        return gameController;
    }

    public void removeGameController(String controllerKey)
    {
        gameControllers.remove(controllerKey);
    }
    
    public GameController activateGameController(String controllerKey)
    {
        GameController gameController = null;
        
        if(gameControllers.containsKey(controllerKey))
        {
            gameController = getGameController(controllerKey);
            
            if(gameController != null)
            {
                currentController = controllerKey;
            }
        }
        
        return gameController;
    }
    
    public void setupKeyMappings()
    {
    }

    public void removeKeyMappings()
    {
    }

    public void pushGameState(GameState switchGameState)
    {
        this.switchGameState = switchGameState;
    }

    public GameState popGameState()
    {
        GameState returnGameState = switchGameState;
        switchGameState = null;
        
        return returnGameState;
    }
}
