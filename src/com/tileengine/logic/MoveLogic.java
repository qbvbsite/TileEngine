package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.Direction;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;
import java.util.Random;

public class MoveLogic extends AbstractLogic
{
    private Random generator = new Random();
    private int[] directions = new int[]{Direction.DIRECTION_NORTH, Direction.DIRECTION_SOUTH, Direction.DIRECTION_WEST, Direction.DIRECTION_EAST};

    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {
        MoveableObject moveableObject = (MoveableObject) caller;

        gameController.moveObject(moveableObject, directions[generator.nextInt(4)], timeElapsed);

        return true;
    }
}
