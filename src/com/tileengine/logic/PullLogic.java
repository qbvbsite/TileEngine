package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.Direction;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;
import com.tileengine.object.QuadTreeQuery;
import java.util.ArrayList;

public class PullLogic extends AbstractLogic
{
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {
        if(initiator instanceof MoveableObject)
        {
            MoveableObject hero = (MoveableObject) initiator;

            //Pull the Object
            switch(hero.getFacingDirection())
            {
                case Direction.DIRECTION_NORTH:
                {
                    gameController.placeObject(caller,  caller.getX(), initiator.getY() + initiator.getHeight(), true);
                    break;
                }
                case Direction.DIRECTION_SOUTH:
                {
                    gameController.placeObject(caller,  caller.getX(), initiator.getY() - initiator.getHeight(), true);
                    break;
                }
                case Direction.DIRECTION_WEST:
                {
                    gameController.placeObject(caller,  hero.getX() + initiator.getWidth(), caller.getY(), true);

                    break;
                }
                case Direction.DIRECTION_EAST:
                {
                    gameController.placeObject(caller,  hero.getX() - initiator.getWidth(), caller.getY(), true);
                    break;
                }
            }

            //Object Pulled
            return true;
        }

        //No Object Pulled
        return false;
    }
}
