package com.tileengine.battle;

import com.tileengine.object.GameObject;

public class BattleTile 
{
    public static final int ALLOWED_NONE = 2;
    public static final int ALLOWED_PLAYER_ONLY = 4;
    public static final int ALLOWED_ENEMY_ONLY = 8;
    public static final int ALLOWED_BOTH = 16;
    
    private int playersAllowed = ALLOWED_BOTH;
    private GameObject occupant = null;
    
    public BattleTile()
    {}
    
    public BattleTile(int playersAllowed)
    {
        setPlayerAllowed(playersAllowed);
    }
    
    public BattleTile(GameObject occupant)
    {
        setOccupant(occupant);
    }
    
    public BattleTile(int playersAllowed, GameObject occupant)
    {
        setPlayerAllowed(playersAllowed);
        setOccupant(occupant);
    }
    
    public void setPlayerAllowed(int playersAllowed)
    {
        this.playersAllowed = playersAllowed;
    }
    
    public int getPlayerAllowed()
    {
        return playersAllowed;
    }
    
    public void setOccupant(GameObject occupant)
    {
        this.occupant = occupant;
    }
    
    public GameObject getOccupant()
    {
        return occupant;
    }
}
