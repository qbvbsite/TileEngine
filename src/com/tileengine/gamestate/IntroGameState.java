package com.tileengine.gamestate;

import com.tileengine.InputController;
import com.tileengine.ResourceController;
import com.tileengine.TileEngineCanvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class IntroGameState extends AbstractGameState
{
    private long startTime;

    private final static int LOAD_MENU = 1;
    private Image introLogo;
    private boolean gotoMenu = false;

    public IntroGameState()
    {
        //Map Keys
        InputController inputController = InputController.getInstance();
        inputController.setMapping(InputController.TYPE_KEYBOARD, LOAD_MENU, KeyEvent.VK_ENTER);

        //Load Resources
        introLogo = ResourceController.getInstance().loadImage("images/splash.png");

        //Store Start Time
        startTime = System.currentTimeMillis();
    }

    public GameState processInput(TileEngineCanvas parent, InputController inputController, long timeElapsed)
    {
        GameState returnState = this;

        if(inputController.getState(LOAD_MENU))
        {
            gotoMenu = true;
        }
        else
        {
            if(gotoMenu)
            {
                inputController.clearMappings();
                returnState = new MenuGameState();
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
        GameState returnState = this;

        //Add Intro Image to Graphics Buffer
        graphicBuffer.drawImage(introLogo, 0, 0, parent);

        //Automaticely goto Menu
        if(System.currentTimeMillis() - startTime > 2000)
        {
            InputController inputController = InputController.getInstance();
            inputController.clearMappings();
            
            returnState = new MenuGameState();
        }

        return returnState;
    }
}
