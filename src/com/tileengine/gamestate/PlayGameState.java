package com.tileengine.gamestate;

import com.tileengine.*;
import com.tileengine.gamescreen.DialogScreen;
import com.tileengine.logic.Logic;
import com.tileengine.object.Character;
import com.tileengine.object.Direction;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;
import com.tileengine.object.QuadTreeQuery;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PlayGameState extends AbstractGameState
{
    private final static int HERO_UP = 1;
    private final static int HERO_DOWN = 2;
    private final static int HERO_RIGHT = 3;
    private final static int HERO_LEFT = 4;
    private final static int HERO_TALK = 5;
    private final static int TOGGLE_PULL = 6;

    private boolean enterDialog = false;
    
    public PlayGameState(String mapFile)
    {
        GameController gameController = ResourceController.getInstance().loadMap(mapFile, this);
        setCurrentGameController(gameController);

        //Setup Key Mappings
        setupKeyMappings();

        //Load Sounds
        ResourceController.getInstance().loadAudio("sounds/zelda_get_heart.wav");

        //Start Background Music
        //AudioController.playLoop("sounds/legendofzelda_theme.mid");
    }

    public void setupKeyMappings()
    {
        //Input Controller
        InputController inputController = InputController.getInstance();

        //Setup Movement of Hero
        inputController.setMapping(InputController.TYPE_KEYBOARD, HERO_UP, KeyEvent.VK_UP);
        inputController.setMapping(InputController.TYPE_KEYBOARD, HERO_DOWN, KeyEvent.VK_DOWN);
        inputController.setMapping(InputController.TYPE_KEYBOARD, HERO_RIGHT, KeyEvent.VK_RIGHT);
        inputController.setMapping(InputController.TYPE_KEYBOARD, HERO_LEFT, KeyEvent.VK_LEFT);
        inputController.setMapping(InputController.TYPE_KEYBOARD, HERO_TALK, KeyEvent.VK_T);
        inputController.setMapping(InputController.TYPE_KEYBOARD, TOGGLE_PULL, KeyEvent.VK_P);
    }

    public void removeKeyMappings()
    {
        //Input Controller
        InputController inputController = InputController.getInstance();

        //Release Kep Mappings
        inputController.removeMapping(HERO_UP);
        inputController.removeMapping(HERO_DOWN);
        inputController.removeMapping(HERO_RIGHT);
        inputController.removeMapping(HERO_LEFT);
        inputController.removeMapping(HERO_TALK);
        inputController.removeMapping(TOGGLE_PULL);
    }

    public GameState processInput(TileEngineCanvas parent, InputController inputController, long timeElapsed)
    {
        //Return GameState
        GameState returnGameState = this;

        //Get Game Controller
        GameController gameController = getCurrentGameController();
        MoveableObject hero = gameController.getHeroObject();
        int currentX = hero.getX();
        int currentY = hero.getY();

        //Process Heros Movements
        processHeroMovements(gameController, inputController, (int) timeElapsed);

        if (inputController.getState(TOGGLE_PULL) && (hero.getX() != currentX || hero.getY() != currentY))
        {
            ArrayList pullObjects = null;
            
            //Based on Direction Get Object beside him (If Any)
            switch (hero.getFacingDirection())
            {
                case Direction.DIRECTION_NORTH:
                {
                    pullObjects = gameController.objectsCollidingWith(new QuadTreeQuery(currentX, currentY + 1, hero.getZ(), hero.getWidth(),hero.getHeight()));
                    break;
                }
                case Direction.DIRECTION_SOUTH:
                {
                    pullObjects = gameController.objectsCollidingWith(new QuadTreeQuery(currentX, currentY - 1, hero.getWidth(),hero.getHeight(), hero.getZ()));
                    break;
                }
                case Direction.DIRECTION_EAST:
                {
                    pullObjects = gameController.objectsCollidingWith(new QuadTreeQuery(currentX - 1, currentY, hero.getZ(), hero.getWidth(),hero.getHeight()));
                    break;
                }
                case Direction.DIRECTION_WEST:
                {
                    pullObjects = gameController.objectsCollidingWith(new QuadTreeQuery(currentX + 1, currentY, hero.getZ(), hero.getWidth(),hero.getHeight()));
                    break;
                }   
            }
            
            //Process Found Objects
            if(pullObjects != null)
            {
                //Remove Hero From Array
                if(pullObjects.contains(hero))
                {
                    pullObjects.remove(hero);
                }
                
                //Go Through Posible Pullable Objects
                if(pullObjects.size() > 0)
                {
                    for(int i=0; i<pullObjects.size(); i++)
                    {
                        GameObject pullObject = (GameObject) pullObjects.get(i);
                        
                        //Check for PullLogic
                        ArrayList pullLogicArray = pullObject.getGameLogicByClass("PullLogic");
                        if(pullLogicArray != null && pullLogicArray.size() > 0)
                        {
                            //Execute Pull
                            ((Logic) pullLogicArray.get(0)).executeLogic(gameController, returnGameState, pullObject, (GameObject) hero, (int) timeElapsed);
                        }
                    }
                }
            }
        }

        if(inputController.getState(HERO_TALK))
        {
            enterDialog = true;
        }
        else
        {
            if(enterDialog)
            {
                ArrayList dialogObjects = gameController.objectsCollidingWith(new QuadTreeQuery(hero.getX() - 1, hero.getY() - 1, hero.getWidth() + 1, hero.getHeight() + 1, hero.getZ()));

                //Remove Hero
                dialogObjects.remove(hero);
                
                for(int i=0; i<dialogObjects.size(); i++)
                {
                    GameObject dialogObject = (GameObject) dialogObjects.get(i);
                        
                    //Check for DialogLogic
                    ArrayList dialogObjectArray = dialogObject.getGameLogicByClass("DialogLogic");
                    if(dialogObjectArray != null && dialogObjectArray.size() > 0)
                    {
                        //Execute Pull
                        ((Logic) dialogObjectArray.get(0)).executeLogic(gameController, returnGameState, dialogObject, (GameObject) hero, (int) timeElapsed);
                    }
                }
                
                enterDialog = false;
            }
        }

        //Check to see if Any Logic Changed the State
        GameState changeGameState = popGameState();
        if(changeGameState != null)
        {
            returnGameState = changeGameState;
        }

        return returnGameState;
    }

    private void processHeroMovements(GameController gameController, InputController inputController, int timeElapsed)
    {
        int moveDirection = 0;
        boolean northMove = inputController.getState(HERO_UP);
        boolean southMove = inputController.getState(HERO_DOWN);
        boolean eastMove = inputController.getState(HERO_RIGHT);
        boolean westMove = inputController.getState(HERO_LEFT);

        if(northMove && eastMove)
        {
            moveDirection = Direction.DIRECTION_NORTHEAST;
        }
        else if(northMove && westMove)
        {
            moveDirection = Direction.DIRECTION_NORTHWEST;
        }
        else if(southMove && eastMove)
        {
            moveDirection = Direction.DIRECTION_SOUTHEAST;
        }
        else if(southMove && westMove)
        {
            moveDirection = Direction.DIRECTION_SOUTHWEST;
        }

        if(moveDirection == 0)
        {
            //North
            if(northMove)
            {
                moveDirection = Direction.DIRECTION_NORTH;
            }
            else if(southMove)
            {
                moveDirection = Direction.DIRECTION_SOUTH;
            }
            else if(eastMove)
            {
                moveDirection = Direction.DIRECTION_EAST;
            }
            else if(westMove)
            {
                moveDirection = Direction.DIRECTION_WEST;
            }
        }

        if(moveDirection != 0)
        {
            gameController.getHeroObject().setMoving(true);
            gameController.moveObject(gameController.getHeroObject(), moveDirection, (int) timeElapsed);
        }
        else
        {
            gameController.getHeroObject().setMoving(false);
        }
    }

    public GameState preformLogic(TileEngineCanvas parent, long timeElapsed)
    {
        //Get Game Controller
        GameController gameController = getCurrentGameController();

        gameController.executeObjectLogic((int) timeElapsed);

        return this;
    }

    public GameState updateScreen(TileEngineCanvas parent, Graphics2D graphicBuffer, long timeElapsed)
    {
        //Get Game Controller
        GameController gameController = getCurrentGameController();
        
        //Add Menu Image to Graphics Buffer
        graphicBuffer.clearRect(0, 0, parent.getWidth(), parent.getHeight());

        //Render Map Base
        gameController.renderTileObjects(parent, graphicBuffer, (int) timeElapsed);
        
        //Render All Visible Map Objects
        gameController.renderObjects(parent, graphicBuffer, (int) timeElapsed);

        //Draw FPS
        graphicBuffer.drawString("FPS: " + Math.round(parent.getFPS()), 10, 20);

        int numRenderedObjects = 0;
        ArrayList[] renderedObjects = gameController.objectsThatWillBeRendered();

        for(int z=0; z<renderedObjects.length; z++)
        {
            for(int i=0; i<renderedObjects[z].size(); i++)
            {
                GameObject object = (GameObject) renderedObjects[z].get(i);

                gameController.objectsCollidingWith(object);

                numRenderedObjects++;
            }
        }

        graphicBuffer.drawString("Objects Rendered: " + numRenderedObjects + "/" + gameController.getGameObjects().size(), 10, 35);
        graphicBuffer.drawString("Heros Location: " + gameController.getHeroObject().getX() + "," + gameController.getHeroObject().getY(), 10, 50);
       
        return this;
    }
}
