package com.tileengine.object;

import java.awt.Image;

/**
 * A Map Tile
 *
 * @author James Keir
 */
public class Tile implements DrawableObject
{
    /*
     * Store ID of object
     */
    private String id;

    /*
     * Image for object
     */
    private Image image = null;

    /*
     * If you can walk through top
     */
    private boolean walkThroughTop = true;

    /*
     * If you can walk through bottom
     */
    private boolean walkThroughBottom = true;

    /*
     * If you can walk through left
     */
    private boolean walkThroughLeft = true;

    /*
     * If you can walk through right
     */
    private boolean walkThroughRight = true;

    /**
     * Creates a basic walkthroughable tile
     *
     * @param id ID of tile
     * @param image Image of tile
     */
    public Tile(String id, Image image)
    {
        //Set ID
        setID(id);

        //Set Image
        setImage(image);
    }
    
    /**
     * Create a tile with boundries
     *
     * @param id ID of tile
     * @param image Image of tile
     * @param walkThroughTop If you can walk through top
     * @param walkThroughBottom If you can walk through bottom
     * @param walkThroughLeft If you can walk through left
     * @param walkThroughRight If you can walk through right
     */
    public Tile(String id, Image image, boolean walkThroughTop, boolean walkThroughBottom, boolean walkThroughLeft, boolean walkThroughRight)
    {
        //Set ID
        setID(id);

        //Set Image
        setImage(image);

        //Set Bounderies
        setWalkThroughTop(walkThroughTop);
        setWalkThroughBottom(walkThroughBottom);
        setWalkThroughLeft(walkThroughLeft);
        setWalkThroughRight(walkThroughRight);
    }

    /**
     * Create a tile with boundries
     * 
     * @param id ID of tile
     * @param image Image of tile
     * @param walkThrough If you can walkthrough the tile are not
     */
    public Tile(String id, Image image, boolean walkThrough)
    {
        //Set ID
        setID(id);

        //Set Image
        setImage(image);

        //Set Bounderies
        setWalkThrough(walkThrough);
    }

    /**
     * Set ID of object
     *
     * @param id ID to set object to
     */
    public void setID(String id)
    {
        this.id = id;
    }

    /**
     * Get ID of object
     *
     * @return Return ID of object
     */
    public String getID()
    {
        return id;
    }

    /**
     * Set image of object
     *
     * @param image Image to set to object
     */
    public void setImage(Image image)
    {
        this.image = image;
    }

    /**
     * Get the set image for the object
     *
     * @return Return the image set for the object
     */
    public Image getImage()
    {
        return this.image;
    }

    /**
     * Sets up Bounderies
     *
     * @param walkThrough If you can walkthrough the tile are not
     */
    public void setWalkThrough(boolean walkThrough)
    {
        //Set Bounderies
        setWalkThroughTop(walkThrough);
        setWalkThroughBottom(walkThrough);
        setWalkThroughLeft(walkThrough);
        setWalkThroughRight(walkThrough);
    }

    /**
     * Set if you can walk through top
     *
     * @param walkThroughTop If you can walk through top or not
     */
    public void setWalkThroughTop(boolean walkThroughTop)
    {
        this.walkThroughTop = walkThroughTop;
    }

    /**
     * Get if you can walk through top
     *
     * @return If you can walk top through are not
     */
    public boolean getWalkThroughTop()
    {
        return walkThroughTop;
    }

    /**
     * Set if you can walk through bottom
     * 
     * @param walkThroughBottom If you can walk through bottom or not
     */
    public void setWalkThroughBottom(boolean walkThroughBottom)
    {
        this.walkThroughBottom = walkThroughBottom;
    }

    /**
     * Get if you can walk through bottom
     * 
     * @return If you can walk bottom through are not
     */
    public boolean getWalkThroughBottom()
    {
        return walkThroughBottom;
    }

    /**
     * Set if you can walk through left
     * 
     * @param walkThroughLeft If you can walk through left or not
     */
    public void setWalkThroughLeft(boolean walkThroughLeft)
    {
        this.walkThroughLeft = walkThroughLeft;
    }

    /**
     * Get if you can walk through left
     * 
     * @return If you can walk left through are not
     */
    public boolean getWalkThroughLeft()
    {
        return walkThroughLeft;
    }

    /**
     * Set if you can walk through right
     * 
     * @param walkThroughRight If you can walk through right or not
     */
    public void setWalkThroughRight(boolean walkThroughRight)
    {
        this.walkThroughRight = walkThroughRight;
    }

    /**
     * Get if you can walk through right
     * 
     * @return If you can walk right through are not
     */
    public boolean getWalkThroughRight()
    {
        return walkThroughRight;
    }
}
