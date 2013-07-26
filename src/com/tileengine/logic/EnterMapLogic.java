package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.ResourceController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;

public class EnterMapLogic extends AbstractLogic
{
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {
        Object[] parameters = getParameters();
        MoveableObject heroObject = gameController.getHeroObject();

        if(parameters != null && parameters.length == 4 && initiator.equals(heroObject))
        {
            //Setup Parameters
            String mapFile = (String) parameters[0];
            String positionType = (String) parameters[1];
            int locationX = (Integer) parameters[2];
            int locationY = (Integer) parameters[3];

            //Load Map File
            GameController newGameController = ResourceController.getInstance().loadMap(mapFile, gameState);

            //Remove Hero From Current Controller
            gameController.removeGameObject(heroObject);
            gameController.setHeroObject(null);

            //Setup Hero in new Controller
            if(positionType.equals("Relative"))
            {
                heroObject.setLocation(heroObject.getX() + locationX,heroObject.getY() + locationY);
            }
            else
            {
                heroObject.setLocation(locationX, locationY);
            }

            newGameController.setHeroObject(heroObject);
            newGameController.addGameObject(heroObject);

            //Switch to new Controller
            gameState.pushGameController(gameState.getCurrentGameController());
            gameState.setCurrentGameController(newGameController);
            
            return true;
        }

        return false;
    }
}