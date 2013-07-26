package com.tileengine.object;

import java.awt.Image;

/**
 * A Sprite Object thats not moveable
 *
 * @author James Keir
 */
public class SpriteObject extends GameObject implements Sprite
{
    /*
     * Number of sprite frames
     */
    private int numberOfFrames = 3;

    /*
     * Sprite Images
     */
    private Image[][] spriteImages;

    /*
     * Sprite Type
     */
    private int spriteType = Sprite.SPRITE_ANIMATION;

    /*
     * Sprite Time Counter
     */
    private int spriteTimeElasped = 0;

    /*
     * How many times to loop animate per second
     */
    private int loopsPerSecond = 1;

    /*
     * Current sprite set
     */
    private int currentSpriteSet = 0;

    /*
     * If sprite is animated or not
     */
    private boolean isAnimated = true;

    /**
     * Creates a sprite object
     *
     * @param id ID of sprite
     * @param spriteImages Sprite Images
     * @param locationX Location x of object
     * @param locationY Location Y of object
     * @param width Width of object
     * @param height Height of object
     * @param isPenetrable If the object is penetrable or not
     * @param loopsPerSecond How many times to loop the animate per second
     */
    public SpriteObject(String id, Image[][] spriteImages, int locationX, int locationY, int width, int height, boolean isPenetrable, int loopsPerSecond)
    {
        super(id, null, locationX, locationY, width, height, isPenetrable);

        //Set loops per second
        setLoopsPerSecond(loopsPerSecond);

        //Set Sprite Images
        setSprites(spriteImages);

        //Set Starting Image
        setImage(spriteImages[0][0]);
    }

    /**
     * Sets if the sprite is animated or not
     *
     * @param isAnimated true/false if sprite is animated or not
     */
    public void setIsAnimated(boolean isAnimated)
    {
        this.isAnimated = isAnimated;
    }


    public boolean isAnimated()
    {
        return isAnimated;
    }

    /**
     * Gets the current sprite set that is active
     *
     * @return Sprite set that is active
     */
    public int getCurrentSpriteSet()
    {
        return currentSpriteSet;
    }

    /**
     * Set current active sprite set
     *
     * @param currentSpriteSet Sprite set you wish to activate
     */
    public void setCurrentSpriteSet(int currentSpriteSet)
    {
        this.currentSpriteSet = currentSpriteSet;
    }

    /**
     * Gets type of sprite
     *
     * @return Type of sprite
     */
    public int getSpriteType()
    {
        return spriteType;
    }

    /**
     * Sets prite images
     *
     * @param spriteImages Sprite images
     */
    private void setSprites(Image[][] spriteImages)
    {
        this.spriteImages = spriteImages;
    }

    /**
     * Get sprite images
     *
     * @return Returns sprite images
     */
    public Image[][] getSprites()
    {
        return spriteImages;
    }

    /**
     * Sets the number of times to run animation loop per second
     *
     * @param loopsPerSecond Number of time to run animation per second
     */
    public void setLoopsPerSecond(int loopsPerSecond)
    {
        this.loopsPerSecond = loopsPerSecond;
    }

    /**
     * Gets the number of time the animation is running per second
     *
     * @return Returns the number of time the animation is running per second
     */
    public int getLoopsPerSecond()
    {
        return loopsPerSecond;
    }

    public void runSprite(int timeElasped)
    {
        //Keep Running Total of Time * the Loops per Seconds
        spriteTimeElasped = (spriteTimeElasped + (timeElasped * loopsPerSecond)) % 1000;

        setImage(spriteImages[currentSpriteSet][(int) spriteTimeElasped / (1000/numberOfFrames)]);
    }
}
