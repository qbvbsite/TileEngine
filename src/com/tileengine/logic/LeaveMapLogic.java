package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;

public class LeaveMapLogic extends AbstractLogic
{
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {
        Object[] parameters = getParameters();

        if(parameters != null && parameters.length == 3)
        {
            //Setup Parameters
            String positionType = (String) parameters[0];
            int locationX = (Integer) parameters[1];
            int locationY = (Integer) parameters[2];

            //Get Previous Controller
            GameController previousGameController = gameState.popGameController();

            //Set Heros Position
            MoveableObject heroObject = gameController.getHeroObject();

            //Remove Hero From Current Controller
            gameController.removeGameObject(heroObject);

            //Move Hero
            if(positionType.equals("Relative"))
            {
                heroObject.setLocation(heroObject.getX() + locationX,heroObject.getY() + locationY);
            }
            else
            {
                heroObject.setLocation(locationX, locationY);
            }
 
            //Add Hero To Current Controller
            previousGameController.setHeroObject(heroObject);
            previousGameController.addGameObject(heroObject);

            //Return to Previous State
            gameState.setCurrentGameController(previousGameController);
            
            return true;
        }

        return false;
    }
}