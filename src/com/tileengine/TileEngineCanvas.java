package com.tileengine;

import com.tileengine.gamescreen.GameScreen;
import com.tileengine.gamestate.GameState;
import com.tileengine.gamestate.PlayGameState;
import com.tileengine.inputs.Keyboard;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class TileEngineCanvas extends Canvas
{
    /*
     * Input Controller
     */
    private static InputController inputController = null;

    /**
     * Graphic Device
     */
    private GraphicsDevice graphicDevice;

    /**
     * Window Width
     */
    private int windowWidth = 1024;

    /**
     * Window Height
     */
    private int windowHeight = 768;

    /**
     * Default FPS
     */
    private int targetFPS = 100;

    /**
     * Milliseconds per Frame
     */
    private float millisecondsPerFrame = 1000f / targetFPS;

    /**
     * Current FPS
     */
    private float currentFPS = 0.0f;

    public TileEngineCanvas(int windowWidth, int windowHeight, int targetFPS)
    {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.targetFPS = targetFPS;
        this.millisecondsPerFrame = 1000f / targetFPS;
    }

    /**
     * Gets windows width
     *
     * @return Returns the width of a window
     */
    public int getWindowWidth()
    {
        return windowWidth;
    }

    /**
     * Gets the windows height
     *
     * @return Returns the windows height
     */
    public int getWindowHeight()
    {
        return windowHeight;
    }

    /**
     * Gets the current FPS
     *
     * @return Returns the current Frames Per Second
     */
    public float getFPS()
    {
        return currentFPS;
    }

    /*
     * Main game loop
     */
    public void runGameLoop()
    {
        //Setup Screen
        initialize();

        //Setup BufferStrat
        BufferStrategy bufferStrategy = this.getBufferStrategy();

        //Start Game Loop
        GameState gameState = new PlayGameState("maps/TestMap.xml");
        GameState currentState = gameState;

        //current Frame
        int cycleSet = 0;

        //Set Starting Time for FPS
        long setStartTime = System.currentTimeMillis();

        //Last Logic Call Time
        long lastCallInputTime = System.currentTimeMillis();
        long nextCallInputTime;
        long lastCallLogicTime = System.currentTimeMillis();
        long nextCallLogicTime;
        long lastCallRenderTime = System.currentTimeMillis();
        long nextCallRenderTime;

        //Loop while we have a game state
        while (gameState != null)
        {
            //Increment Cycle
            cycleSet++;

            //Get SubGameState
            ArrayList gameScreens = gameState.getGameScreens();

            //Process Inputs
            nextCallInputTime = System.currentTimeMillis();
            boolean overrideInput = false;

            //Process GameScreen Inputs
            if(gameScreens != null)
            {
                for(int i=0; i<gameScreens.size(); i++)
                {
                    GameScreen subGameState = (GameScreen) gameScreens.get(i);
                    subGameState.processInput(this, inputController, nextCallInputTime - lastCallInputTime);

                    if(subGameState.overrideProcessInput())
                    {
                        overrideInput = true;
                    }
                }
            }

            //Process GameState Input if not overridden by a GameScreen
            if(gameScreens == null || !overrideInput)
            {
                gameState = gameState.processInput(this, inputController, nextCallInputTime - lastCallInputTime);
            }
            lastCallInputTime = nextCallInputTime;

            //Reset Inputs
            inputController.resetStates();

            //If Game State is Unchanged
            if(currentState == gameState)
            {
                //Preform Logic
                nextCallLogicTime = System.currentTimeMillis();
                boolean overrideLogic = false;

                //Process Game Screen Logic
                if(gameScreens != null)
                {
                    for(int i=0; i<gameScreens.size(); i++)
                    {
                        GameScreen subGameState = (GameScreen) gameScreens.get(i);
                        subGameState.preformLogic(this, nextCallLogicTime - lastCallLogicTime);

                        if(subGameState.overridePreformLogic())
                        {
                            overrideLogic = true;
                        }
                    }
                }

                //Process GameState Logic if not overridden a GameScreen
                if(gameScreens == null || !overrideLogic)
                {
                    gameState = gameState.preformLogic(this, nextCallLogicTime - lastCallLogicTime);
                }
                lastCallLogicTime = nextCallLogicTime;

                //If Game State is Unchanged
                if(currentState == gameState)
                {
                    //Get Graphics Object
                    Graphics grapics = bufferStrategy.getDrawGraphics();
                    Graphics2D graphics2d = (Graphics2D) grapics;

                    //Setup Rendering
                    graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                    //Update Screen Buffer
                    nextCallRenderTime = System.currentTimeMillis();
                    gameState = gameState.updateScreen(this, graphics2d, nextCallRenderTime - lastCallRenderTime);

                    //Update Game Screens
                    if(gameScreens != null)
                    {
                        for(int i=0; i<gameScreens.size(); i++)
                        {
                            GameScreen subGameState = (GameScreen) gameScreens.get(i);
                            subGameState.updateScreen(this, graphics2d, nextCallRenderTime - lastCallRenderTime);
                        }
                    }
                    lastCallRenderTime = nextCallRenderTime;

                    //Dispose Graphics Object
                    graphics2d.dispose();

                    //Show Buffer
                    bufferStrategy.show();

                    Toolkit.getDefaultToolkit().sync();
                }
            }

            //Keep Engine Running at a constant FPS (if set)
            float timeElapsed = (cycleSet * millisecondsPerFrame) - (System.currentTimeMillis() - setStartTime);

            if (timeElapsed > 0)
            {
                try
                {
                    Thread.sleep(Math.round(timeElapsed));
                }
                catch(Exception e){}
            }

            //Calculate FPS
            if(System.currentTimeMillis() != setStartTime)
            {
                currentFPS = cycleSet / ((float) (System.currentTimeMillis() - setStartTime) / 1000f);
            }

            //Restart Frame Set is Over 1 Second
            long currentTime = System.currentTimeMillis();
            if(currentTime - setStartTime >= 1000)
            {
                setStartTime = currentTime;
                cycleSet = 0;
            }

            //Set Current Game State
            currentState = gameState;
        }
    }

    /**
     * Setups the Tile Engine for game loop
     */
    private void initialize()
    {
        //Get Input Controller
        inputController = InputController.getInstance();

        //Ingore Repaint
        this.setIgnoreRepaint(true);

        //Setup Size of Frame
        this.setSize(getWindowWidth(), getWindowHeight());

        //Make Frame Visable;
        this.setVisible(true);

        //Adjust for Insets
        this.setSize(getWindowWidth(), getWindowHeight());

        //Setup Buffer Strategy (2 Buffers)
        this.createBufferStrategy(2);

        //Attach Keyboard Listener
        this.addKeyListener(Keyboard.getInstance());
    }
}
