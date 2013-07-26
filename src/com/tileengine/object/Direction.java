package com.tileengine.object;

/**
 * Used for all Driection in the engine
 *
 * @author James Keir
 */
public class Direction
{
    //4 Basic Direction
    public final static int DIRECTION_NORTH = 2;
    public final static int DIRECTION_EAST = 4;
    public final static int DIRECTION_SOUTH = 8;
    public final static int DIRECTION_WEST = 16;

    //4 Diaginal Directions
    public final static int DIRECTION_NORTHEAST = DIRECTION_NORTH + DIRECTION_EAST;
    public final static int DIRECTION_NORTHWEST = DIRECTION_NORTH + DIRECTION_WEST;
    public final static int DIRECTION_SOUTHEAST = DIRECTION_SOUTH + DIRECTION_EAST;
    public final static int DIRECTION_SOUTHWEST = DIRECTION_SOUTH + DIRECTION_WEST;

    //Prevent Instanction
    private Direction()
    {}
}
