package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.ResourceController;
import com.tileengine.battle.BattleTile;
import com.tileengine.gamestate.BattleGameState;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.GameObject;
import com.tileengine.object.Character;

public class EnterBattleLogic extends AbstractLogic
{
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {
        if(initiator.equals(gameController.getHeroObject()))
        {
            //Remove Monster From Map
            gameController.removeGameObject(caller);
            
            ((Character) caller).setBattleSpeed(5000);
            ((Character) caller).setBaseHitPoints(200);
            ((Character) caller).setCurrentHitPoints(142);
            
            ((Character) initiator).setBaseHitPoints(154);
            ((Character) initiator).setCurrentHitPoints(39);
            ((Character) initiator).setBaseManaPool(450);
            ((Character) initiator).setCurrentManaPool(291);
            
            //Setup Battle Field
            BattleTile[][] battleField = new BattleTile[][]{
                new BattleTile[]{
                    new BattleTile(BattleTile.ALLOWED_ENEMY_ONLY), new BattleTile(BattleTile.ALLOWED_ENEMY_ONLY), new BattleTile(BattleTile.ALLOWED_ENEMY_ONLY),
                    new BattleTile(BattleTile.ALLOWED_NONE),
                    new BattleTile(BattleTile.ALLOWED_PLAYER_ONLY), new BattleTile(BattleTile.ALLOWED_PLAYER_ONLY), new BattleTile(BattleTile.ALLOWED_PLAYER_ONLY)
                }, 
                new BattleTile[]{
                    new BattleTile(BattleTile.ALLOWED_ENEMY_ONLY), new BattleTile(BattleTile.ALLOWED_BOTH, caller), new BattleTile(BattleTile.ALLOWED_ENEMY_ONLY),
                    new BattleTile(BattleTile.ALLOWED_NONE),
                    new BattleTile(BattleTile.ALLOWED_PLAYER_ONLY), new BattleTile(BattleTile.ALLOWED_BOTH, initiator), new BattleTile(BattleTile.ALLOWED_PLAYER_ONLY)
                },
                new BattleTile[]{
                    new BattleTile(BattleTile.ALLOWED_ENEMY_ONLY), new BattleTile(BattleTile.ALLOWED_ENEMY_ONLY), new BattleTile(BattleTile.ALLOWED_ENEMY_ONLY),
                    new BattleTile(BattleTile.ALLOWED_NONE),
                    new BattleTile(BattleTile.ALLOWED_PLAYER_ONLY), new BattleTile(BattleTile.ALLOWED_PLAYER_ONLY), new BattleTile(BattleTile.ALLOWED_PLAYER_ONLY)
                }
            };
            
            //Create Battle GameState
            GameState battleGameState = new BattleGameState(gameState, ResourceController.getInstance().loadImage("images/battleBackground.png"),
                                                                new Character[]{(Character) caller}, new Character[]{(Character) initiator},
                                                                battleField);

            //Flag GameState Switch
            gameState.pushGameState(battleGameState);

            return true;
        }

        return false;
    }
}
