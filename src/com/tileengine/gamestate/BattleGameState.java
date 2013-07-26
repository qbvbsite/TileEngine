package com.tileengine.gamestate;

import com.tileengine.InputController;
import com.tileengine.ResourceController;
import com.tileengine.TileEngineCanvas;
import com.tileengine.battle.BattleTile;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import com.tileengine.object.Character;
import com.tileengine.object.GameObject;
import java.awt.Color;

public class BattleGameState extends AbstractGameState
{
    private final static int BUTTON_LEFT = 31;
    private final static int BUTTON_RIGHT = 32;
    private final static int BUTTON_UP = 33;
    private final static int BUTTON_DOWN = 34;
    private final static int BUTTON_SELECT = 35;

    private final static int HERO_TURN = 2;
    private final static int ENEMY_TURN = 4;
    private int currentTurn = HERO_TURN;
    
    private final static int START_STATE = 2;
    private final static int MOVE_STATE = 4;
    private final static int ACTION_STATE = 8;
    private final static int ATTACK_STATE = 16;
    private final static int LOSE_STATE = 32;
    private final static int WIN_STATE = 64;
    private int currentState = START_STATE;
    
    private GameState returnGameState = null;
    private Image battleLogo;

    private BattleTile[][] battleGrid;
    private int tileSize = 64;
    private int startGridX = -1;
    private int startGridY = -1;
    
    private Character[] enemys;
    private Character[] heros;

    private Character currentActionCharacter = null;
    private int currentActionIndex = -1;
    private int[] moveUndo;
    private int[] actionUndo;
    
    public BattleGameState(GameState returnGameState, Image battleLogo, Character[] enemys, Character[] heros, BattleTile[][] battleGrid)
    {
        ResourceController resourceController = ResourceController.getInstance();

        this.returnGameState = returnGameState;
        this.battleLogo = battleLogo;
        this.enemys = enemys;
        this.heros = heros;
        this.battleGrid = battleGrid;
        
        //Setup Key mappings
        setupKeyMappings();
    }

    public void setupKeyMappings()
    {
        //Map Keys
        InputController inputController = InputController.getInstance();
        inputController.setMapping(InputController.TYPE_KEYBOARD, BUTTON_LEFT, KeyEvent.VK_LEFT);
        inputController.setMapping(InputController.TYPE_KEYBOARD, BUTTON_RIGHT, KeyEvent.VK_RIGHT);
        inputController.setMapping(InputController.TYPE_KEYBOARD, BUTTON_UP, KeyEvent.VK_UP);
        inputController.setMapping(InputController.TYPE_KEYBOARD, BUTTON_DOWN, KeyEvent.VK_DOWN);
        inputController.setMapping(InputController.TYPE_KEYBOARD, BUTTON_SELECT, KeyEvent.VK_SPACE);
    }

    public void removeKeyMappings()
    {
        //Remove Mappings
        InputController inputController = InputController.getInstance();
        inputController.removeMapping(BUTTON_LEFT);
        inputController.removeMapping(BUTTON_RIGHT);
        inputController.removeMapping(BUTTON_UP);
        inputController.removeMapping(BUTTON_DOWN);
        inputController.removeMapping(BUTTON_SELECT);
    }

    public GameState processInput(TileEngineCanvas parent, InputController inputController, long timeElapsed)
    {
        GameState returnState = this;

        if(inputController.getState(BUTTON_SELECT))
        {
            //Remove Mappings for Battle State
            returnState.removeKeyMappings();

            //Go Back to World State
            returnState = returnGameState;

            //Remap Work State Keys
            returnGameState.setupKeyMappings();
        }
        else
        {
            
        }

        return returnState;
    }

    public GameState preformLogic(TileEngineCanvas parent, long timeElapsed)
    {
        GameState returnState = this;

        return returnState;
    }

    public GameState updateScreen(TileEngineCanvas parent, Graphics2D graphicBuffer, long timeElasped)
    {
        GameState returnState = this;

        //Calculate Grid Start if not done
        if(startGridX == -1 || startGridY == -1)
        {
            //Calculate Center X
            startGridX = (int) (parent.getWidth() / 2) - ((tileSize * battleGrid[0].length) / 2);
            
            //Calculate Center Y
            startGridY = (int) (parent.getHeight() / 2) - ((tileSize * battleGrid.length) / 2);
        }
        
        //Add Battle Image to Graphics Buffer
        graphicBuffer.clearRect(0, 0, parent.getWidth(), parent.getHeight());
        graphicBuffer.drawImage(battleLogo, 0, 0, parent);

        //Render Battle Field
        int offsetX = 0;
        int offsetY = 0;
        int padding = 1;
        
        for(int y=0; y<battleGrid.length; y++)
        {
            offsetX = 0;
            
            for(int x=0; x<battleGrid[y].length; x++)
            {
                //Check for tile
                if(battleGrid[y][x] != null)
                {
                    //Calculate Grid X/Y
                    int tileX = startGridX + x * tileSize + offsetX;
                    int tileY = startGridY + y * tileSize + offsetY;

                    //Draw Grid If Needed
                    if(battleGrid[y][x].getPlayerAllowed() != BattleTile.ALLOWED_NONE)
                    {
                        if(battleGrid[y][x].getPlayerAllowed() == BattleTile.ALLOWED_ENEMY_ONLY)
                        {
                            graphicBuffer.setColor(Color.RED);
                        }
                        else if(battleGrid[y][x].getPlayerAllowed() == BattleTile.ALLOWED_PLAYER_ONLY)
                        {
                            graphicBuffer.setColor(Color.BLUE);
                        }
                        else
                        {
                            graphicBuffer.setColor(Color.GREEN);
                        }

                        graphicBuffer.drawRect(tileX, tileY, tileSize, tileSize);
                    }

                    //PLace Occupant
                    GameObject tileOccupant = battleGrid[y][x].getOccupant();
                    if(tileOccupant != null)
                    {
                        //Calculate Occupant X/Y
                        int occupantX = (int) ((tileX + tileSize) - (tileSize/2)) - (tileOccupant.getWidth() / 2);
                        int occupantY = (int) ((tileY + tileSize) - (tileSize/2)) - (tileOccupant.getHeight() / 2);

                        //Draw Occupant
                        graphicBuffer.drawImage(tileOccupant.getImage(), occupantX, occupantY, parent);

                        //Render Health Bars If Character
                        if(tileOccupant instanceof Character)
                        {
                            renderHealthManaBars(graphicBuffer, (Character) tileOccupant, tileX, tileY, tileSize);
                        }
                    }
                }
                
                //Increase X Offset
                offsetX += padding;
            }
            
            //Increase Y Offset
            offsetY += padding;
        }
        
        return returnState;
    }

    private void renderHealthManaBars(Graphics2D graphicBuffer, Character characterObject, int baseX, int baseY, int tileSize)
    {
        //Health Bar
        graphicBuffer.setColor(Color.DARK_GRAY);
        graphicBuffer.draw3DRect(baseX + 1, baseY + 1, tileSize - 2, 5, true);
        graphicBuffer.setColor(Color.RED);
        graphicBuffer.fill3DRect(baseX + 1, baseY + 1, (int) (75f * (float) characterObject.getCurrentHitPoints() / (float) characterObject.getBaseHitPoints()), 5, true);
        graphicBuffer.setColor(Color.BLACK);
        
        //Mana Bar
        if(characterObject.getBaseManaPool() > 0)
        {
            graphicBuffer.setColor(Color.DARK_GRAY);
            graphicBuffer.draw3DRect(baseX + 1, baseY + 6, tileSize - 2, 5, true);
            graphicBuffer.setColor(Color.BLUE);
            graphicBuffer.fill3DRect(baseX + 1, baseY + 6, (int) (75f * (float) characterObject.getCurrentManaPool() / (float) characterObject.getBaseManaPool()), 5, true);
            graphicBuffer.setColor(Color.BLACK);
        }
    }
}