package com.tileengine.object;

/**
 * Interface for all Placeable objects
 * 
 * @author James Keir
 */
public interface Placeable
{
    /**
     * Gets x location of Object
     * 
     * @return Return x location of object
     */
    public int getX();

    /**
     * Gets y location of Object
     * 
     * @return Return y location of object
     */
    public int getY();

    /**
     * Gets z location of Object
     * 
     * @return Return z location of object
     */
    public int getZ();

    /**
     * Gets width of Object
     * 
     * @return Return width of object
     */
    public int getWidth();

    /**
     * Gets height of Object
     * 
     * @return Return width of object
     */
    public int getHeight();

    /**
     * Used to see if this object collides with another one
     * 
     * @param collidableObject Object to test collision with
     * @return Return is the objects collide or not
     */
    public boolean collidesWith(Placeable collidableObject);

    /**
     * If the object is Penetrable or not
     * 
     * @return Returns in the object is Penetrable ot not
     */
    public boolean isPenetrable();
}
