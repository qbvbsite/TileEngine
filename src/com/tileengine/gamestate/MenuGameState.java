package com.tileengine.gamestate;

import com.tileengine.InputController;
import com.tileengine.ResourceController;
import com.tileengine.TileEngineCanvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class MenuGameState extends AbstractGameState
{
    private Image menuLogo;
    private boolean executeGameState = false;

    private final static int LOAD_GAME = 1;

    public MenuGameState()
    {
        //Map Keys
        InputController inputController = InputController.getInstance();
        inputController.setMapping(InputController.TYPE_KEYBOARD, LOAD_GAME, KeyEvent.VK_ENTER);

        //Load Resources
        menuLogo = ResourceController.getInstance().loadImage("images/menu.png");
    }
    
    public GameState processInput(TileEngineCanvas parent, InputController inputController, long timeElapsed)
    {
        GameState returnState = this;

        if(inputController.getState(LOAD_GAME))
        {
            executeGameState = true;
        }
        else
        {
            if(executeGameState)
            {
                inputController.clearMappings();
                returnState = new PlayGameState("Configuration/TestMap.xml");
            }
        }

        return returnState;
    }

    public GameState preformLogic(TileEngineCanvas parent, long timeElapsed)
    {
        return this;
    }

    public GameState updateScreen(TileEngineCanvas parent, Graphics2D graphicBuffer, long timeElasped)
    {
        //Add Menu Image to Graphics Buffer
        graphicBuffer.clearRect(0, 0, parent.getWidth(), parent.getHeight());
        graphicBuffer.drawImage(menuLogo, 0, 0, parent);

        return this;
    }
}
