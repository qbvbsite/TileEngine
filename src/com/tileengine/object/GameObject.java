
package com.tileengine.object;

import com.tileengine.GameController;
import com.tileengine.gamestate.GameState;
import com.tileengine.logic.Logic;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Placeable GAme Object
 *
 * @author James Keir
 */
public class GameObject implements Placeable, DrawableObject
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
     * X Location of Object
     */
    private int x = 0;

    /*
     * Y Location of Object
     */
    private int y = 0;

    /*
     * Z Location of Object
     */
    private int z = 0;

    /*
     * Width of Object
     */
    private int width;

    /*
     * Height of Object
     */
    private int height;

    /*
     * Is object penetrable
     */
    private boolean isPenetrable = true;

    /*
     * Objects logic
     */
    private Map gameLogic;
    
    /*
     * Custom Logic By Class
     */
    private Map gameLogicByClass;
    
    /**
     * Creates a game object
     *
     * @param id ID of object
     * @param image Image for object
     * @param x X Location of object
     * @param y Y Location of object
     * @param width Width of object
     * @param height Height of object
     * @param isPenetrable If the object is penetrable or not
     */
    public GameObject(String id, Image image, int x, int y, int width, int height, boolean isPenetrable)
    {
        //Set ID
        setID(id);

        //Set Image
        setImage(image);
        
        //Sets objects size
        setSize(width, height);

        //Sets object Location
        setLocation(x, y);

        //Set Is Penterable
        setIsPenetrable(isPenetrable);
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
     * Sets the X location of object
     *
     * @param x The x Location you want to set the object to
     */
    public void setX(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    /**
     * Sets the Y location of object
     * 
     * @param y The y Location you want to set the object to
     */
    public void setY(int y)
    {
        this.y = y;
    }

    public int getY()
    {
        return y;
    }

    /**
     * Sets the Z location of object
     * 
     * @param z The z Location you want to set the object to
     */
    public void setZ(int z)
    {
        this.z = z;
    }

    public int getZ()
    {
        return this.z;
    }

    /**
     * Sets location of object
     *
     * @param x Location x for object
     * @param y Location y for object
     */
    public void setLocation(int x, int y)
    {
        setX(x);
        setY(y);
    }

    /**
     * Set width of object
     *
     * @param width Width to set object to
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getWidth()
    {
        return width;
    }

    /**
     * Set height of object
     * 
     * @param height Height to set object to
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getHeight()
    {
        return height;
    }

    /**
     * Sets size of object
     *
     * @param width Width to set object to
     * @param height Height to set object to
     */
    public void setSize(int width, int height)
    {
        setWidth(width);
        setHeight(height);
    }

    public boolean collidesWith(Placeable collidableObject)
    {
        //Check for X Over Lap
        if(this.getX() > (collidableObject.getX() + collidableObject.getWidth() - 1) ||
                (this.getX() + this.width - 1) < collidableObject.getX())
        {
            return false;
        }

        //Check for Y Overlap
        if(this.getY() > (collidableObject.getY() + collidableObject.getHeight() - 1) ||
                (this.getY() + this.height - 1) < collidableObject.getY())
        {
            return false;
        }

        //Theres Overlap
        return true;
    }

    /**
     * Sets if the object is Penetrable or not
     * 
     * @param isPenetrable Set is the object is Penetrable or not
     */
    public void setIsPenetrable(boolean isPenetrable)
    {
        this.isPenetrable = isPenetrable;
    }

    public boolean isPenetrable()
    {
        return isPenetrable;
    }

    private void checkLogicHash(int logicType)
    {
        if(gameLogic == null)
        {
            gameLogic = new HashMap();
        }

        if(!gameLogic.containsKey(logicType))
        {
            gameLogic.put(logicType, new ArrayList());
        }
    }

    private void checkLogicByClassHash(String className)
    {
        if(gameLogicByClass == null)
        {
            gameLogicByClass = new HashMap();
        }

        if(!gameLogicByClass.containsKey(className))
        {
            gameLogicByClass.put(className, new ArrayList());
        }
    }
    
    /**
     * Adds logic to the object
     *
     * @param logicType Logic type you want to add
     * @param logic Logic object you want to add
     */
    public void addGameLogic(int logicType, Logic logic)
    {
        //Make sure hash is not null
        checkLogicHash(logicType);

        //Add Logic to Object
        ArrayList logicArray = (ArrayList) gameLogic.get(logicType);
        logicArray.add(logic);
        
        //Store by Class
        String className = logic.getClass().getName();
        checkLogicByClassHash(className);
        
        //Add Logic to Object
        ArrayList logicByClassArray = (ArrayList) gameLogicByClass.get(className);
        logicByClassArray.add(logic);
    }

    /**
     * Adds logic to the object at a specific index
     * 
     * @param logicType Logic type you want to add
     * @param index Index to add logic at
     * @param logic Logic object you want to add
     */
    public void addGameLogic(int logicType, int index, Logic logic)
    {
        checkLogicHash(logicType);

        ArrayList logicArray = (ArrayList) gameLogic.get(logicType);
        logicArray.add(index, logic);
        
        //Store by Class
        String className = logic.getClass().getName();
        checkLogicByClassHash(className);
        
        //Add Logic to Object
        ArrayList logicByClassArray = (ArrayList) gameLogicByClass.get(className);
        logicByClassArray.add(logic);
    }

    /**
     * Gets all object logic for specific type
     *
     * @param logicType Type of logic to get
     * @return Return all logic for type
     */
    public ArrayList getGameLogic(int logicType)
    {
        return (ArrayList) gameLogic.get(logicType);
    }

    public ArrayList getGameLogicByClass(String className)
    {
        ArrayList logicByClass = null;
        
        if(gameLogicByClass.containsKey("com.tileengine.logic." + className))
        {
            logicByClass = (ArrayList) gameLogicByClass.get("com.tileengine.logic." + className);
        }
        
        return logicByClass;
    }
    
    /**
     * Gets object logic for specific index
     * 
     * @param logicType Type of logic to get
     * @param index Index of logic you want
     * @return Return logic at specific index
     */
    public Logic getGameLogic(int logicType, int index)
    {
        return (Logic) ((ArrayList) gameLogic.get(logicType)).get(index);
    }

    /**
     * Executes all logic of a specific type with no initiator
     *
     * @param logicType Logic type you wish to execute
     * @param gameController Game controll executeing logic
     * @param gameState Game state executeing logic
     * @param timeElapsed Time elasped since last call
     */
    public boolean executeLogic(int logicType, GameController gameController, GameState gameState, int timeElapsed)
    {
        return executeLogicType(logicType, gameController, gameState, null, timeElapsed);
    }

    /**
     * Executes all logic of a specific type with initiator
     *
     * @param logicType Logic type you wish to execute
     * @param gameController Game controll executeing logic
     * @param gameState Game state executeing logic
     * @param initiator Initiator of logic execute
     * @param timeElapsed Time elasped since last call
     */
    public boolean executeLogic(int logicType, GameController gameController, GameState gameState, GameObject initiator, int timeElapsed)
    {
        return executeLogicType(logicType, gameController, gameState, initiator, timeElapsed);
    }

    private boolean executeLogicType(int logicType, GameController gameController, GameState gameState, GameObject initiator, int timeElapsed)
    {
        boolean logicValid = true;
        
        if(gameLogic != null)
        {
            ArrayList logicObjects = (ArrayList) gameLogic.get(logicType);

            if(logicObjects != null)
            {
                for(int i=0; i<logicObjects.size(); i++)
                {
                    Logic logic = (Logic) logicObjects.get(i);
                    
                    boolean logicPassed = logic.executeLogic(gameController, gameState, this, initiator, timeElapsed);
                    
                    if(!logicPassed)
                    {
                        logicValid = false;
                    }
                }
            }
        }
        
        return logicValid;
    }
}
