package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.GameObject;

public class SuccessFailLogic extends AbstractLogic
{
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {
        return (Boolean) getParameters()[0];
    }
}
