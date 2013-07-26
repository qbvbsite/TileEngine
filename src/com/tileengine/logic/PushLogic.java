package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.Direction;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;

public class PushLogic extends AbstractLogic
{
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {
        //If the Object Was Pushed or Not
        boolean pushedObject = false;
            
        if(caller instanceof GameObject && initiator instanceof MoveableObject)
        {
            //Set New Location of Init. Object
            int direction = ((MoveableObject) initiator).getFacingDirection();
            
            //Store Starting Location (Based on North/South it will change is East/West)
            int startingLocation = caller.getY();
            int newLocation = startingLocation;
            
            //Push Object
            switch(direction)
            {
                case Direction.DIRECTION_NORTH:
                case Direction.DIRECTION_SOUTH:
                {
                    //Store Starting Location
                    startingLocation = caller.getY();
                    
                    if(direction == Direction.DIRECTION_NORTH)
                    {
                        //Snap to Pusher
                        newLocation = gameController.snapToObject(Direction.DIRECTION_SOUTH, caller, initiator);
                    }
                    else
                    {
                        //Snap to Pusher
                        newLocation = gameController.snapToObject(Direction.DIRECTION_NORTH, caller, initiator);
                    }
                    
                    break;
                }

                case Direction.DIRECTION_EAST:
                case Direction.DIRECTION_WEST:
                {
                    //Store Starting Location
                    startingLocation = caller.getX();

                    if(direction == Direction.DIRECTION_EAST)
                    {
                        //Snap to Pusher
                        newLocation = gameController.snapToObject(Direction.DIRECTION_WEST, caller, initiator);
                    }
                    else
                    {
                        //Snap to Pusher
                        newLocation = gameController.snapToObject(Direction.DIRECTION_EAST, caller, initiator);
                    }
                    
                    break;
                }
            }

            //Validate to Map
            newLocation = gameController.mapMoveValid(caller, direction, newLocation);
                        
            //If Object was Pushed (Based on New Location and Starting Location)
            if(startingLocation != newLocation)
            {
                //Move Object
                if(direction == Direction.DIRECTION_NORTH || direction == Direction.DIRECTION_SOUTH)
                {
                    pushedObject = gameController.placeObject(caller, caller.getX(), newLocation, true);
                }
                else if(direction == Direction.DIRECTION_EAST || direction == Direction.DIRECTION_WEST)
                {
                    pushedObject = gameController.placeObject(caller, newLocation, caller.getY(), true);
                }
            }
        }
        
        return pushedObject;
    }
}
