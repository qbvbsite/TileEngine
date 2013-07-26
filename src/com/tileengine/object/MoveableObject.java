package com.tileengine.object;

import com.tileengine.pathing.Path;
import java.awt.Image;

/**
 * Moveable object class
 *
 * @author James Keir
 */
public class MoveableObject extends GameObject implements Sprite
{

    private Path path = null;

    /*
     * If object is moving or not
     */
    private boolean isMoving;

    /*
     * Direction object is facing
     */
    private int facingDirection = Direction.DIRECTION_SOUTH;

    /*
     * Objects movement speed
     */
    private double movementSpeed = 1;

    /*
     * Current X buffer of object
     */
    private double movementBufferX = 0.0;

    /*
     * current Y buffer of object
     */
    private double movementBufferY = 0.0;

    /*
     * If sprite is animated or not
     */
    private boolean isAnimated = false;

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
    private int spriteType;

    /*
     * Sprite Time Counter
     */
    private int spriteTileElapsed = 0;

     /*
     * How many times to loop animate per second
     */
    private int loopsPerSecond = 1;

     /*
     * Current sprite set
     */
    private int currentSpriteSet = Direction.DIRECTION_SOUTH;

    /**
     * Created a moveable object that is not a sprite
     *
     * @param id ID of sprite
     * @param image image of object
     * @param locationX Location x of object
     * @param locationY Location Y of object
     * @param width Width of object
     * @param height Height of object
     * @param isPenetrable If the object is penetrable or not
     * @param movementSpeed Movement speed of object
     */
    public MoveableObject(String id, Image image, int locationX, int locationY, int width, int height, boolean isPenetrable, double movementSpeed)
    {
        super(id, image, locationX, locationY, width, height, isPenetrable);

        //Set Movement Speed
        setMovementSpeed(movementSpeed);
    }

    /**
     * Created a moveable object that is a sprite
     *
     * @param id ID of sprite
     * @param spriteImages Sprite Images
     * @param locationX Location x of object
     * @param locationY Location Y of object
     * @param width Width of object
     * @param height Height of object
     * @param isPenetrable If the object is penetrable or not
     * @param movementSpeed Movement speed of object
     * @param loopsPerSecond How many times to run the animation per second
     * @param spriteType The type of sprite
     */
    public MoveableObject(String id, Image[][] spriteImages, int locationX, int locationY, int width, int height, boolean isPenetrable, double movementSpeed, int loopsPerSecond, int spriteType)
    {
        super(id, null, locationX, locationY, width, height, isPenetrable);

        //Set Movement Speed
        setMovementSpeed(movementSpeed);

        //Set Sprite Type
        setSpriteType(spriteType);

        //Set Loops per second
        setLoopsPerSecond(loopsPerSecond);

        //Set Sprite Images
        setSprites(spriteImages);

        //Set Starting Image
        setImage(spriteImages[0][0]);
    }

    /**
     * Set if the object is moving or not
     *
     * @param isMoving Set if the object is moving or not
     */
    public void setMoving(boolean isMoving)
    {
        this.isMoving = isMoving;
    }
    
    /**
     * See if the object is moving or not
     *
     * @return Returns if the object is moving or not
     */
    public boolean isMoving()
    {
        return isMoving;
    }

    /**
     * Sets the direction the object is faceing
     *
     * @param facingDirection The direction the object is facing
     */
    public void setFacingDirection(int facingDirection)
    {
        this.facingDirection = facingDirection;
    }

    /**
     * Gets the direction the object is facing
     *
     * @return Returns the direction the object is facing
     */
    public int getFacingDirection()
    {
        return facingDirection;
    }

    public Path getPath()
    {
        return path;
    }

    public void setPath(Path path)
    {
        this.path = path;
    }

    /**
     * Set the movement speed of object
     *
     * @param movementSpeed The movement speed of object
     */
    public void setMovementSpeed(double movementSpeed)
    {
        this.movementSpeed = movementSpeed;
    }

    /**
     * Get the movement speed of object
     *
     * @return Returns the movement speed of object
     */
    public double getMovementSpeed()
    {
        return movementSpeed;
    }

    /**
     * Set the X Buffer for object
     *
     * @param movementBufferX X buffer value
     */
    public void setMovementBufferX(double movementBufferX)
    {
        this.movementBufferX = movementBufferX;
    }

    /**
     * Get the current X Buffer
     *
     * @return Current X Buffer value
     */
    public double getMovementBufferX()
    {
        return movementBufferX;
    }

    /**
     * Set the Y Buffer for object
     *
     * @param movementBufferY Y buffer value
     */
    public void setMovementBufferY(double movementBufferY)
    {
        this.movementBufferY = movementBufferY;
    }

    /**
     * Get the current Y Buffer
     *
     * @return Current Y Buffer value
     */
    public double getMovementBufferY()
    {
        return movementBufferY;
    }

    public boolean isAnimated()
    {
        return isMoving();
    }

    /**
     * Set the current active sprite set
     *
     * @param currentSpriteSet Sprite set to activate
     */
    public void setCurrentSpriteSet(int currentSpriteSet)
    {
        this.currentSpriteSet = currentSpriteSet;
    }

    /**
     * Gets current active sprite set
     *
     * @return Returns current active sprite set
     */
    public int getCurrentSpriteSet()
    {
        return currentSpriteSet;
    }

    /**
     * Sets sprite type
     *
     * @param spriteType Sprite type
     */
    private void setSpriteType(int spriteType)
    {
        this.spriteType = spriteType;
    }

    /**
     * Gets the type of the sprite
     *
     * @return Type of sprite
     */
    public int getSpriteType()
    {
        return spriteType;
    }

    /**
     * Set sprite images
     *
     * @param spriteImages Sprite images
     */
    private void setSprites(Image[][] spriteImages)
    {
        this.spriteImages = spriteImages;
    }

    /**
     * Gets sprite images
     *
     * @return Return the sprite Images
     */
    public Image[][] getSprites()
    {
        return spriteImages;
    }

    /**
     * Set number of time to run animate per second
     *
     * @param loopsPerSecond Number of time to run animate per second
     */
    public void setLoopsPerSecond(int loopsPerSecond)
    {
        this.loopsPerSecond = loopsPerSecond;
    }

    /**
     * Get the number of time to run animate per second
     *
     * @return Returns number of time to run animate per second
     */
    public int getLoopsPerSecond()
    {
        return loopsPerSecond;
    }

    public void runSprite(int timeElasped)
    {
        //Keep Running Total of Time * the Loops per Seconds
        spriteTileElapsed = (spriteTileElapsed + (timeElasped * loopsPerSecond)) % 1000;
        int spriteImage = (int) spriteTileElapsed / (1000/numberOfFrames);
        
        switch(currentSpriteSet)
        {
            case Direction.DIRECTION_SOUTH:
            case Direction.DIRECTION_SOUTHEAST:
            case Direction.DIRECTION_SOUTHWEST:
            {
                setImage(spriteImages[0][spriteImage]);
                break;
            }
            case Direction.DIRECTION_WEST:
            {
                setImage(spriteImages[1][spriteImage]);
                break;
            }
            case Direction.DIRECTION_EAST:
            {
                setImage(spriteImages[2][spriteImage]);
                break;
            }
            case Direction.DIRECTION_NORTH:
            case Direction.DIRECTION_NORTHWEST:
            case Direction.DIRECTION_NORTHEAST:
            {
                setImage(spriteImages[3][spriteImage]);
                break;
            }
        }

        if(spriteType == SPRITE_8D)
        {
            switch(currentSpriteSet)
            {
                case Direction.DIRECTION_NORTHEAST:
                {
                    setImage(spriteImages[7][spriteImage]);
                    break;
                }

                case Direction.DIRECTION_SOUTHEAST:
                {
                    setImage(spriteImages[6][spriteImage]);
                    break;
                }
                case Direction.DIRECTION_NORTHWEST:
                {
                    setImage(spriteImages[5][spriteImage]);
                    break;
                }
                case Direction.DIRECTION_SOUTHWEST:
                {
                    setImage(spriteImages[4][spriteImage]);
                    break;
                }
            }
        }
    }
}
