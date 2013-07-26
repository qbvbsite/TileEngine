package com.tileengine;

import com.tileengine.gamestate.GameState;
import com.tileengine.logic.Logic;
import com.tileengine.object.Direction;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;
import com.tileengine.object.Placeable;
import com.tileengine.object.QuadTree;
import com.tileengine.object.QuadTreeQuery;
import com.tileengine.object.Sprite;
import com.tileengine.object.Tile;
import com.tileengine.object.TileSet;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * This class pretty much controls everything associated with the Map and game play
 *
 * @author James Keir
 */
public class GameController
{
    /**
     * Tiles for Map
     */
    private TileSet tileSet = null;

    /**
     * Game Objects
     */
    private ArrayList gameObjects = new ArrayList();

    /**
     * Used to store Placeable objects for easy retrival
     */
    private QuadTree gameObjectPlacements;
    
    /**
     * Hero for the map (Point of Referance for scrolling)
     */
    private MoveableObject heroObject = null;

    /**
     * GameState attached to the GameController
     */
    private GameState gameState = null;

    /**
     * Render Screen Size Width (Pixels)
     */
    private int renderWidth = 0;

    /**
     * Render Screen Size Height (Pixels)
     */
    private int renderHeight = 0;

    /**
     * Map Size Width (Pixels)
     */
    private int mapWidth = 0;

    /**
     * Map Size Height (Pixels)
     */
    private int mapHeight = 0;
    

    /**
     * Number of object layers (Z Axis)
     */
    private int numberOfLayers = 1;

    /**
     * Current Scroll X
     */
    int scrollX = 0;

    /**
     * Current Scroll Y
     */
    int scrollY = 0;

    /**
     * Used to turn on Screen Snapping (Zelda Like Scroll)
     */
    boolean screenSnap = false;
    
    /**
     * Used to turn on Scroll Box Scrolling (Box or Character)
     */
    boolean scrollActive = false;

    /**
     * Scroll Box X Location (Pixels)
     */
    int scrollBoxX = 0;

    /**
     * Scroll Box Y Location (Pixels)
     */
    int scrollBoxY = 0;

    /**
     * Scroll Box Width Location (Pixels)
     */
    int scrollBoxWidth = 0;

    /**
     * Scroll Box Height Location (Pixels)
     */
    int scrollBoxHeight = 0;

    /**
     * Scroll Buffer is used to load objects passed the render area (In Map Tiles)
     */
    int scrollBuffer = 1;

    /**
     * Creates a GameController with no scrolling which is used on your playable GameState
     *
     * @param renderWidth Screen width of the render window
     * @param renderHeight Screen height of the render window
     * @param tileSet The map of tiles for the controller
     * @param numberOfLayers Number of object layers on the map
     * @param heroObject Hero object of the map
     * @param gameState GameState that contains the controller
     */
    public GameController(int renderWidth, int renderHeight, TileSet tileSet, int numberOfLayers, MoveableObject heroObject, GameState gameState)
    {
        //Set Tile Set
        setTileSet(tileSet);

        //Set Map Size
        setMapSize(tileSet.getWidth() * getTileSize(), tileSet.getHeight() * getTileSize());

        //Set Render Size
        setRenderSize(renderWidth * getTileSize(), renderHeight * getTileSize());

        //Number of Layers
        setNumberOfLayers(numberOfLayers);

        //Setup Quad Tree
        gameObjectPlacements = new QuadTree(0, getMapWidth() - 1, 0, getMapHeight() - 1, getNumberOfLayers());
        
        //Set Hero
        setHeroObject(heroObject);

        //Set GameState
        setGameState(gameState);
    }

    /**
     * Creates a GameController with zelda like scrolling which is used on your playable GameState
     *
     * @param renderWidth Screen width of the render window
     * @param renderHeight Screen height of the render window
     * @param screenSnap Screen Snapping Scrolling (Like Orginal Zelda)
     * @param tileSet The map of tiles for the controller
     * @param numberOfLayers Number of object layers on the map
     * @param heroObject Hero object of the map
     * @param gameState GameState that contains the controller
     */
    public GameController(int renderWidth, int renderHeight, boolean screenSnap, TileSet tileSet, int numberOfLayers, MoveableObject heroObject, GameState gameState)
    {
        //Set Tile Set
        setTileSet(tileSet);

        //Set Map Size
        setMapSize(tileSet.getWidth() * getTileSize(), tileSet.getHeight() * getTileSize());

        //Set Render Size
        setRenderSize(renderWidth * getTileSize(), renderHeight * getTileSize());

        //Set Screen Snap
        setScreenSnap(screenSnap);

        //Number of Layers
        setNumberOfLayers(numberOfLayers);

        //Setup Quad Tree
        gameObjectPlacements = new QuadTree(0, getMapWidth() - 1, 0, getMapHeight() - 1, getNumberOfLayers());

        //Set Hero
        setHeroObject(heroObject);

        //Set GameState
        setGameState(gameState);
    }

    /**
     * Creates a GameController with a scroll box which is used on your playable GameState
     *
     * @param renderWidth Screen width of the render window
     * @param renderHeight Screen height of the render window
     * @param scrollBoxX The X position (In Map Tiles no Pixels) of the start of the scroll box
     * @param scrollBoxY The Y position (In Map Tiles no Pixels) of the start of the scroll box
     * @param scrollBoxWidth The scroll box width (In Map Tiles no Pixels)
     * @param scrollBoxHeight The scroll box height (In Map Tiles no Pixels)
     * @param tileSet The map of tiles for the controller
     * @param numberOfLayers Number of object layers on the map
     * @param heroObject Hero object of the map
     * @param gameState GameState that contains the controller
     */
    public GameController(int renderWidth, int renderHeight, int scrollBoxX, int scrollBoxY, int scrollBoxWidth, int scrollBoxHeight, TileSet tileSet, int numberOfLayers, MoveableObject heroObject, GameState gameState)
    {
         //Set Tile Set
        setTileSet(tileSet);

        //Set Map Size
        setMapSize(tileSet.getWidth() * getTileSize(), tileSet.getHeight() * getTileSize());

        //Set Render Size
        setRenderSize(renderWidth * getTileSize(), renderHeight * getTileSize());

        //Set Scroll Size
        setScrollBox(scrollBoxX * getTileSize(), scrollBoxY * getTileSize(), scrollBoxWidth * getTileSize(), scrollBoxHeight * getTileSize());

        //Number of Layers
        setNumberOfLayers(numberOfLayers);

        //Setup Quad Tree
        gameObjectPlacements = new QuadTree(0, getMapWidth() - 1, 0, getMapHeight() - 1, getNumberOfLayers());
        
        //Set Hero
        setHeroObject(heroObject);

        //Set GameState
        setGameState(gameState);
    }

    /**
     * Returns the GameState associated with the GameController.
     *
     * @return GameState attached to GameController
     */
    public GameState getGameState()
    {
        return gameState;
    }

    /**
     * Associates a GameState to the GameController
     *
     * @param gameState Game state to attach to GameController
     */
    public void setGameState(GameState gameState)
    {
        this.gameState = gameState;
    }
    
    /**
     * Returns the current scroll buffer (In Map Tiles)
     *
     * @return Current scroll buffer
     */
    public int getScrollBuffer()
    {
        return scrollBuffer;
    }

    /**
     * Sets the scroll buffer (In Map Tiles)
     * Scroll buffer loads objects passed the render area so they can render smoothly when scrolling
     *
     * @param scrollBuffer Set the scroll buffer
     */
    public void setScrollBuffer(int scrollBuffer)
    {
        this.scrollBuffer = scrollBuffer;
    }

    /**
     * Set screen snapping (Zelda like scroll)
     *
     * @param scrollBuffer Set screen snapping
     */
    private void setScreenSnap(boolean screenSnap)
    {
        this.screenSnap = screenSnap;
    }

    /**
     * Sets up the scroll box. Can be used to setup constant scrolling if placed right around the hero.
     *
     * @param scrollBoxX Scroll box X location (Pixels)
     * @param scrollBoxY Scroll box Y location (Pixels)
     * @param scrollBoxWidth Scroll box width (Pixels)
     * @param scrollBoxHeight Scroll box height (Pixels)
     */
    public void setScrollBox(int scrollBoxX, int scrollBoxY, int scrollBoxWidth, int scrollBoxHeight)
    {
        //Set Scroll Box
        this.scrollBoxX = scrollBoxX;
        this.scrollBoxY= scrollBoxY;
        this.scrollBoxWidth = scrollBoxWidth;
        this.scrollBoxHeight = scrollBoxHeight;

        //Set Scroll Active Since Scroll Box is Set
        if(scrollBoxX != 0 && scrollBoxY != 0 && scrollBoxWidth != 0 && scrollBoxHeight != 0)
        {
            this.scrollActive = true;
        }
    }

    /**
     * Sets the number of object rendering layers for the map
     *
     * @param numberOfLayers Number of object rendering layers
     */
    private void setNumberOfLayers(int numberOfLayers)
    {
        if(numberOfLayers > 0)
        {
            this.numberOfLayers = numberOfLayers;
        }
    }

    /**
     * Returns the currently set number of layers
     *
     * @return Currently set number of objet rendering layers
     */
    public int getNumberOfLayers()
    {
        return numberOfLayers;
    }

    /**
     * Sets the Hero for the Map
     *
     * @param heroObject Hero object for the map
     */
    public void setHeroObject(MoveableObject heroObject)
    {
        this.heroObject = heroObject;
    }

    /**
     * Returns the current hero of the map
     *
     * @return Current hero of the map
     */
    public MoveableObject getHeroObject()
    {
        return heroObject;
    }

    /**
     * Return all the current game objects for the map
     *
     * @return All the game objects
     */
    public ArrayList getGameObjects()
    {
        return gameObjects;
    }

    /**
     * Sets the render areas width (Pixels)
     *
     * @param renderWidth Width of render area
     */
    public void setRenderWidth(int renderWidth)
    {
        this.renderWidth = renderWidth;
    }

    /**
     * Gets the current render area width (Pixels)
     *
     * @return The current render area width
     */
    public int getRenderWidth()
    {
        return renderWidth;
    }

    /**
     * Sets the render areas height (Pixels)
     *
     * @param renderHeight Width of render area
     */
    public void setRenderHeight(int renderHeight)
    {
        this.renderHeight = renderHeight;
    }

    /**
     * Gets the current render area height (Pixels)
     *
     * @return The current render area width
     */
    public int getRenderHeight()
    {
        return renderHeight;
    }

    /**
     * Sets both the width and height of the render area (Pixels)
     *
     * @param renderWidth Width of render area
     * @param renderHeight Height of render area
     */
    public void setRenderSize(int renderWidth, int renderHeight)
    {
        setRenderWidth(renderWidth);
        setRenderHeight(renderHeight);
    }

    /**
     * Sets the tile set for the map
     *
     * @param tileSet Tile set for the map
     */
    private void setTileSet(TileSet tileSet)
    {
        this.tileSet = tileSet;
    }

    /**
     * Gets the map width for the whole map
     *
     * @return Map width
     */
    public int getMapWidth()
    {
        return mapWidth;
    }

    /**
     * Sets the map width for the whole map
     *
     * @param mapWidth Width for the map
     */
    private void setMapWidth(int mapWidth)
    {
        this.mapWidth = mapWidth;
    }

    /**
     * Gets the map height for the whole map
     *
     * @return Map height
     */
    public int getMapHeight()
    {
        return mapHeight;
    }

    /**
     * Sets the map height for the whole map
     *
     * @param mapHeight Height for the map
     */
    private void setMapHeight(int mapHeight)
    {
        this.mapHeight = mapHeight;
    }

    /**
     * Sets the map height and width for the whole map
     *
     * @param mapWidth Width for the map
     * @param mapHeight Height for the map
     */
    private void setMapSize(int mapWidth, int mapHeight)
    {
        setMapWidth(mapWidth);
        setMapHeight(mapHeight);
    }

    /**
     * Gets the current scroll X offset. This is used to render things in the correct location on the render area
     *
     * @return Scroll offset X
     */
    public int getRenderOffsetX()
    {
        return scrollX;
    }

    /**
     * Gets the current scroll Y offset. This is used to render things in the correct location on the render area
     *
     * @return Scroll offset Y
     */
    public int getRenderOffsetY()
    {
        return scrollY;
    }

    /**
     * Gets the tile size of the map (32 is standard)
     *
     * @return Returns the tile size
     */
    public int getTileSize()
    {
        return tileSet.getTileSize();
    }

    /**
     * Changes a tile on the map to a different one
     *
     * @param y Tile Y location (In Map Tiles)
     * @param x Tile X location (In Map Tiles)
     * @param tileId ID of the tile resource you wish to change it to
     */
    public void changeTile(int y, int x, String tileId)
    {
        tileSet.setTile(y, x, tileId);
    }

    /**
     * Gets a tile from a specific location
     *
     * @param y Tile Y location (In Map Tiles)
     * @param x Tile X location (In Map Tiles)
     * @return Returns the tile from that location
     */
    public Tile getTile(int y, int x)
    {
        return tileSet.getTile(y, x);
    }

    /**
     * Adds a game object to the map. This will place it in the game object array and the Object placement QuadTree
     *
     * @param gameObject GameObject you wish to add to the map
     */
    public void addGameObject(GameObject gameObject)
    {
        if(!gameObjects.contains(gameObject))
        {
            gameObjects.add(gameObject);
            gameObjectPlacements.insertObject(gameObject);
        }
    }

    /**
     * Removes a game object from the map. This will remove the game object from the game object array and the Object placement QuadTree
     *
     * @param gameObject GameObject you wish to remove from the map
     */
    public void removeGameObject(GameObject gameObject)
    {
        if(gameObjects.contains(gameObject))
        {
            gameObjects.remove(gameObject);
            gameObjectPlacements.removeObject(gameObject);
        }
    }

    /**
     * Will return all object that collide with given object. This will only collide with object on its z layer or below.
     *
     * @param placeableObject Object that you which to query with
     * @return ArrayList of all colliding objects
     */
    public ArrayList objectsCollidingWith(Placeable placeableObject)
    {
        return objectsCollidingWith(placeableObject, false);
    }

    /**
     * Will return all object that collide with given object. This will only collide with object on its z layer or below if allLayers is false.
     * If allLayers is true it will return all collision regardless of z layer
     *
     * @param placeableObject Object that you which to query with
     * @param allLayers If you wish to return all collision regarless of z layers
     * @return ArrayList of all colliding objects
     */
    public ArrayList objectsCollidingWith(Placeable placeableObject, boolean allLayers)
    {
        ArrayList collidingObjects = new ArrayList();
        ArrayList[] collidingObjectsList = gameObjectPlacements.getCollisions(placeableObject, allLayers);

        for(int z=0; z<collidingObjectsList.length; z++)
        {
            collidingObjects.addAll(collidingObjectsList[z]);
        }

        return collidingObjects;
    }

    /**
     * Return all the objects that will be rendered to the render area
     *
     * @return Returns an Array of ArrayLists where each element holds all game objects to be rendered on that z layer (ArrayList[0] = z layer 0).
     */
    public ArrayList[] objectsThatWillBeRendered()
    {
        return gameObjectPlacements.getObjectsToRender(scrollX - (getTileSize() * scrollBuffer), scrollY - (getTileSize() * scrollBuffer), renderWidth + (getTileSize() * scrollBuffer), renderHeight + (getTileSize() * scrollBuffer));
    }

    /**
     * This will execute all general logic for all objects that will be rendered
     *
     * @param timeElapsed Time that has passed since last call
     */
    public void executeObjectLogic(int timeElapsed)
    {
        //Get all Rendered Objects
        ArrayList[] gameRenderObjects = objectsThatWillBeRendered();

        //Loop through each Z Layers
        for(int z=0; z<gameRenderObjects.length; z++)
        {
            for(int i=0; i<gameRenderObjects[z].size(); i++)
            {
                if(gameRenderObjects[z].get(i) instanceof GameObject)
                {
                    GameObject moveObject = (GameObject) gameRenderObjects[z].get(i);

                    //Execute Logic
                    moveObject.executeLogic(Logic.LOGIC_TYPE_GENERAL, this, gameState, null, timeElapsed);
                }
            }
        }
    }

    /**
     * Used to move an object from one place on the map to another. Will return false if it was unable to move the object to specified location.
     *
     * @param gameObject Object you wish to move
     * @param locationX X location you wish to place the object
     * @param locationY Y location you wish to place the object
     * @param checkCollisions If true is will not place the object if it collides with another object
     * @return Return true is the object was placed successful and false if it failed
     */
    public boolean placeObject(GameObject gameObject, int locationX, int locationY, boolean checkCollisions)
    {
        boolean movedObject = true;

        //Check if Its a Vaild Map Location
        if(locationX < 0 || (locationX + gameObject.getWidth()) > getMapWidth() ||
                locationY < 0 || (locationY + gameObject.getHeight()) > getMapHeight())
        {
            movedObject = false;
        }
        else if(checkCollisions)
        {
            //Check Vaild Placement on Objects
            QuadTreeQuery quadTreeQuery = new QuadTreeQuery(locationX, locationY, gameObject.getZ(), gameObject.getWidth(), gameObject.getHeight());
            ArrayList collidingObjects = objectsCollidingWith(quadTreeQuery);

            for(int i=0; i<collidingObjects.size(); i++)
            {
                GameObject collideObject = (GameObject) collidingObjects.get(i);

                if(!collideObject.equals(gameObject) && !collideObject.isPenetrable())
                {
                    movedObject = false;

                    break;
                }
            }
        }

        //If Valid Move Object
        if(movedObject)
        { 
            updateObjectTree(gameObject, locationX, locationY);
        }

        return movedObject;
    }

    public void updateObjectTree(GameObject gameObject, int moveToX, int moveToY)
    {
        if(getGameObjects().contains(gameObject))
        {
            gameObjectPlacements.removeObject(gameObject);
            gameObject.setLocation(moveToX, moveToY);
            gameObjectPlacements.insertObject(gameObject);
        }
    }
    
    /**
     * Moves Object On Any Direction (8 Directional) based on time that has passed and movement speed.
     *
     * @param moveableObject Object you wish to move
     * @param direction Direction you wish to move the object
     * @param timeElasped Pass that has passed since last call
     */
    public void moveObject(MoveableObject moveableObject, int direction, int timeElasped)
    {
        //Current Game State
        GameState currentGameState = getGameState();
        
        //Total number opf pixels to move based on time that has passed
        double totalMovement = (((moveableObject.getMovementSpeed() * getTileSize()) / 1000) * timeElasped);  

        //Set Direction of object
        moveableObject.setFacingDirection(direction);

        //If Sprite Set Sprite Set Based on Direction
        if(moveableObject.isAnimated())
        {
            moveableObject.setCurrentSpriteSet(direction);
        }

        //Set Buffers and Test Moves
        switch(direction)
        {
            case Direction.DIRECTION_NORTH:
            case Direction.DIRECTION_SOUTH:
            {
                //Set Starting Point
                int moveToY = moveableObject.getY();
                
                //Calculate Y Buffer (For example we can move 1.28 pixels so we store the reminder 0.28 for later)
                moveToY += setBufferY(moveableObject, direction, totalMovement);

                //Move on Y
                moveObjectToLocation(moveableObject, direction, moveToY, timeElasped);
                
                break;
            }

            case Direction.DIRECTION_EAST:
            case Direction.DIRECTION_WEST:
            {
                //Set Starting Point
                int moveToX = moveableObject.getX();
                
                //Calculate X Buffer
                moveToX += setBufferX(moveableObject, direction, totalMovement);

                //Move on X
                moveObjectToLocation(moveableObject, direction, moveToX, timeElasped);
                
                break;
            }

            default:
            {
                //41% Movement
                totalMovement = totalMovement * 0.71;
                        
                // Get X and Y Direction
                int directionX = Direction.DIRECTION_EAST;
                int directionY = Direction.DIRECTION_NORTH;

                if(direction == Direction.DIRECTION_WEST || direction == Direction.DIRECTION_SOUTHWEST || direction == Direction.DIRECTION_NORTHWEST)
                {
                    directionX = Direction.DIRECTION_WEST;
                }

                if(direction == Direction.DIRECTION_SOUTH || direction == Direction.DIRECTION_SOUTHWEST || direction == Direction.DIRECTION_SOUTHEAST)
                {
                    directionY = Direction.DIRECTION_SOUTH;
                }
                
                //Set Direction of object
                moveableObject.setFacingDirection(directionX);
                            
                //Calculate X Buffer and Move on X
                int moveToX = moveableObject.getX();
                moveToX += setBufferX(moveableObject, directionX, totalMovement);
                moveObjectToLocation(moveableObject, directionX, moveToX, timeElasped);
                
                //Verify That Object Still Exists
                if(getGameObjects().contains(moveableObject))
                {
                    if(currentGameState == getGameState())
                    {
                        //Set Direction of object
                        moveableObject.setFacingDirection(directionY);

                        //Calculate Y Buffer and Move on Y
                        int moveToY = moveableObject.getY();
                        moveToY += setBufferY(moveableObject, directionY, totalMovement);
                        moveObjectToLocation(moveableObject, directionY, moveToY, timeElasped);

                        //Verify That Object Still Exists
                        if(getGameObjects().contains(moveableObject))
                        {
                            //Set Direction of object
                            moveableObject.setFacingDirection(direction);
                        }
                    }
                    else
                    {
                        //Set Direction of object
                        moveableObject.setFacingDirection(direction);
                    }
                }
            }
        }
    }
    
    /**
     * Used calculate the X Buffer and return the new total movement
     *
     * @param moveableObject Object you are moving
     * @param direction Direction you are moving the object
     * @param movement Current total movement that buffer will be added to
     * @return Returns new total movement for X
     */
    private int setBufferX(MoveableObject moveableObject, int direction, double movement)
    {
        //Add Current X Buffer to Total Movement
        double totalMovement = movement + (moveableObject.getMovementBufferX());

        //Set the X Buffer to the reminder of Total Movement
        moveableObject.setMovementBufferX((totalMovement % 1));

        //If going west turn total movement to a negitive number
        if(totalMovement >= 1 && direction == Direction.DIRECTION_WEST)
        {
            totalMovement = totalMovement * -1;
        }

        return (int) totalMovement;
    }

    /**
     * Used calculate the Y Buffer and return the new total movement
     *
     * @param moveableObject Object you are moving
     * @param direction Direction you are moving the object
     * @param movement Current total movement that buffer will be added to
     * @return Returns new total movement for Y
     */
    private int setBufferY(MoveableObject moveableObject, int direction, double movement)
    {
        //Add Current Y Buffer to Total Movement
        double totalMovement = movement + (moveableObject.getMovementBufferY());

        //Set the Y Buffer to the reminder of Total Movement
        moveableObject.setMovementBufferY((totalMovement % 1));

        //If going north turn total movement to a negitive number
        if(totalMovement >= 1 && direction == Direction.DIRECTION_NORTH)
        {
            totalMovement = totalMovement * -1;
        }

        return (int) totalMovement;
    }

    /**
     * Used to move an object from one location to another going North, South, West or East. To go NorthEast you would call
     * this function twice once with North (Y Axis) and a second time with East(X Axis). This will check collision agaisnt map along the
     * route the object is going to take. If it hits a wall during its movement is will stop there. This will also verify the final
     * destination will not collide with an object and if it does it will Snap to it accordingly. If the colliding object is pushable the
     * moving object will push the object as it moves.
     *
     * @param gameObject Object you are moving
     * @param direction Direction you are moving the object
     * @param newLocation The final desination of the object (x or y depending on the direction)
     * @param collisions Used to store any collision so we can execute them later
     * @return Returns new location for the X or Y axis depending on direction of movement
     */
    public void moveObjectToLocation(GameObject gameObject, int direction, int newLocation, int timeElasped)
    {
        //Process Map Movement
        newLocation = mapMoveValid(gameObject, direction, newLocation);

        //Setup Query Object for Object Collision
        QuadTreeQuery quadTreeQuery = null;

        //Move Coordinates
        int moveToX = gameObject.getX();
        int moveToY = gameObject.getY();
        
        //Move Charter For Collision Check
        if(direction == Direction.DIRECTION_NORTH || direction == Direction.DIRECTION_SOUTH)
        {
            moveToY = newLocation;
        }
        else
        {
            moveToX = newLocation;
        }
        
        //If Player Moved Update in Quad Tree
        if(moveToX != gameObject.getX() || moveToY != gameObject.getY())
        {
            //Move Object
            updateObjectTree(gameObject, moveToX, moveToY);
            
            //Build Query Based on Location returned from Map Validation
            quadTreeQuery = new QuadTreeQuery(gameObject.getX(), gameObject.getY(), gameObject.getZ(), gameObject.getWidth(), gameObject.getHeight());

            //Validate Move With Current Objects
            ArrayList collidingObjects = objectsCollidingWith(quadTreeQuery);

            //Collides With Objects (Thats not Us)
            collidingObjects.remove(gameObject);

            if(collidingObjects.size() > 0)
            {
                //Loop Through to See If We Can Pass Through Object or if They are Pushable
                for(int i=0; i<collidingObjects.size(); i++)
                {
                    GameObject collideObject = (GameObject) collidingObjects.get(i);

                    //Fire Collision Events for Colliding Objects (Both Objects)
                    boolean gameObjectMoved = gameObject.executeLogic(Logic.LOGIC_TYPE_COLLIDE, this, gameState, collideObject, timeElasped);
                    boolean collideObjectMoved = collideObject.executeLogic(Logic.LOGIC_TYPE_COLLIDE, this, gameState, gameObject, timeElasped);

                    //See If Collision is Valid and Is still in this Controller
                    if(!collideObject.isPenetrable())
                    {
                        //Object Hasn't Been Moved By Logic
                        if(moveToX == gameObject.getX() || moveToY == gameObject.getY())
                        {
                            //Snap Game Object to Colid Object
                            newLocation = snapToObject(direction, gameObject, collideObject);

                            //Update Move Position
                            if(direction == Direction.DIRECTION_NORTH || direction == Direction.DIRECTION_SOUTH)
                            {
                                moveToY = newLocation;
                            }
                            else
                            {
                                moveToX = newLocation;
                            }

                            if(moveToX != gameObject.getX() || moveToY != gameObject.getY())
                            {
                                //Move Object
                                updateObjectTree(gameObject, moveToX, moveToY);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Snaps to an Object to an Object
     *
     * @param direction direction of snap
     * @param gameObject Object that will be snapped
     * @param objectToSnapTo Object that object will be snapping to
     * @return Return the location on where to snap to
     */
    public int snapToObject(int direction, GameObject gameObject, GameObject objectToSnapTo)
    {
        int newLocation = 0;

        switch(direction)
        {
            case Direction.DIRECTION_NORTH:
            {
                //Snap to South Face
                newLocation = objectToSnapTo.getY() + objectToSnapTo.getWidth();
                break;
            }

            case Direction.DIRECTION_SOUTH:
            {
                //Snap to North Face
                newLocation = objectToSnapTo.getY() - gameObject.getHeight();
                break;
            }

            case Direction.DIRECTION_EAST:
            {
                //Snap to West Face
                newLocation = objectToSnapTo.getX() - gameObject.getWidth();
                break;
            }

            case Direction.DIRECTION_WEST:
            {
                //Snap to East Face
                newLocation = objectToSnapTo.getX() + objectToSnapTo.getWidth();
                break;
            }
        }

        //Return Snap Location
        return newLocation;
    }

    /**
     * Check to see if movement from one location to another is valid based on the map tiles
     *
     * @param moveableObject Object to be moved
     * @param direction direction to move object
     * @param newLocation Location to move object to
     * @return Returns the new location of object based on map (x or y axis depending on direction)
     */
    public int mapMoveValid(GameObject moveableObject, int direction, int newLocation)
    {
        //Move Object Based on Direction
        switch(direction)
        {
            case Direction.DIRECTION_NORTH:
            {
                //Make Sure we are in Map Bounds
                if(newLocation > 0)
                {
                    //Store points on the Object for calculations later
                    int topLeft = moveableObject.getX();
                    int topRight = moveableObject.getX() + moveableObject.getWidth() - 1;

                    //Get First Y Tile to Check Based on Current Location (Tile Above Us and not the Current Tile Hence the -1)
                    int tileStartY = (int) (moveableObject.getY() / getTileSize()) - 1;

                    //Get Last Y Tile Based on the New Location
                    int tileEndY = (int) newLocation / getTileSize();

                    //Get Left Most X Tile Based on Current Location
                    int tileStartX = (int) topLeft / getTileSize();

                    //Get Right Most X Tile Based on New Location
                    int tileEndX = (int) topRight / getTileSize();

                    checkForCollisions:
                    {
                        //Loop Through Tile Starting from the Bottom and Moving Up
                        //Stop when a Collision Occurs and Stick Object to Bottom of Tile
                        for(int y=tileStartY; y>=tileEndY && y>=0; y--)
                        {
                            //Process Tile Left to Right (Doesn't Really Matter)
                            for(int x=tileStartX; x<=tileEndX && x<tileSet.getWidth(); x++)
                            {
                                //See If Its Invalid Move
                                Tile tile = tileSet.getTile(y, x);

                                if(tile == null || !tile.getWalkThroughBottom())
                                {
                                    //Invalid Move Hug Object to Tile (Rememeber Location is Top Left of object)
                                    newLocation = (y + 1) * getTileSize();

                                    //Break from Loop
                                    break checkForCollisions;
                                }
                            }
                        }
                    }
                }
                else
                {
                    //Stick Object to Top of Map
                    newLocation = 0;
                }

                break;
            }

            case Direction.DIRECTION_SOUTH:
            {
                //Store Botton of Map Location
                int bottomMapLocation = getMapHeight();

                //Make sure we are within the bounds of the map
                if((newLocation + moveableObject.getHeight() - 1) < bottomMapLocation)
                {
                    //Store points on the Object for calculations later
                    int bottomLeft = moveableObject.getX();
                    int bottomRight = moveableObject.getX() + moveableObject.getWidth() - 1;

                    //First Y Tile to Check Based on Current Location (Tile Below Us and not the Current Tile Hence the +1)
                    int tileStartY = (int) ((moveableObject.getY() + moveableObject.getHeight() - 1) / getTileSize()) + 1;

                    //Last Y Tile to Check Based on New Location
                    int tileEndY = (int) (newLocation + moveableObject.getHeight() - 1) / getTileSize();

                    //First X Tile to Check Based on Current Location
                    int tileStartX = (int) bottomLeft / getTileSize();

                    //Last X Tile to Check Based on New Location
                    int tileEndX = (int) bottomRight / getTileSize();

                    checkForCollisions:
                    {
                        //Loop Through Tile Starting from the Top and Moving Down
                        //Stop when a Collision Occurs and Stick Object to Top of Tile
                        for(int y=tileStartY; y<=tileEndY && y<tileSet.getHeight(); y++)
                        {
                            for(int x=tileStartX; x<=tileEndX && x<tileSet.getWidth(); x++)
                            {
                                Tile tile = tileSet.getTile(y, x);

                                //Check to See if is a Walk Throughable Tile
                                if(tile == null || !tile.getWalkThroughTop())
                                {
                                    //Collision with Tile Snap to Top of Tile
                                    newLocation = (y - 1) * getTileSize();

                                    //Break Loop
                                    break checkForCollisions;
                                }
                            }
                        }
                    }
                }
                else
                {
                    //Snap to Bottom of Map
                    newLocation = bottomMapLocation - getTileSize();
                }

                break;
            }

            case Direction.DIRECTION_EAST:
            {
                //Store Right Most Map Location
                int rightMapLocation = getMapWidth();

                //Make sure we are within the bounds of the map
                if((newLocation + moveableObject.getWidth() - 1) < rightMapLocation)
                {
                    //Store points on the Object for calculations later
                    int rightTop = moveableObject.getY();
                    int rightBottom = moveableObject.getY() + moveableObject.getHeight() - 1;

                    //Start X Tile Based on Current Location (We Want the Right tile and not the tile we are on hence the +1)
                    int tileStartX = (int) ((moveableObject.getX() + moveableObject.getWidth() - 1) / getTileSize()) + 1;

                    //End X Tile based on new location
                    int tileEndX = (int) (newLocation + moveableObject.getWidth() - 1) / getTileSize();

                    //Start Y Tile Based on Current Location
                    int tileStartY = (int) rightTop / getTileSize();

                    //End Y Tile Based on New Location
                    int tileEndY = (int) rightBottom / getTileSize();

                    checkForCollisions:
                    {
                        //Loop Through Tile Starting from the Left and Moving Right
                        //Stop when a Collision Occurs and Stick Object to Left of Tile
                        for(int x=tileStartX; x<=tileEndX && x<tileSet.getWidth(); x++)
                        {
                            for(int y=tileStartY; y<=tileEndY && y<tileSet.getHeight(); y++)
                            {
                                Tile tile = tileSet.getTile(y, x);

                                //Check to See if is a Walk Throughable Tile
                                if(tile == null || !tile.getWalkThroughLeft())
                                {
                                    //Collision with Tile Snap to Left of Tile
                                    newLocation = (x - 1) * getTileSize();

                                    //Break Loop
                                    break checkForCollisions;
                                }
                            }
                        }
                    }
                }
                else
                {
                    //Snap to Right side of Map
                    newLocation = rightMapLocation - getTileSize();
                }

                break;
            }

            case Direction.DIRECTION_WEST:
            {
                //Make sure we are within bounds of map
                if(newLocation > 0)
                {
                    //Store points on the Object for calculations later
                    int leftTop = moveableObject.getY();
                    int leftBottom = moveableObject.getY() + moveableObject.getHeight() - 1;

                    //Start X Tile Based on Current Location (We Want the Left tile and not the tile we are on hence the -1)
                    int tileStartX = (int) (moveableObject.getX() / getTileSize()) - 1;

                    //End X Tile based on new location
                    int tileEndX = (int) newLocation / getTileSize();

                    //Start Y Tile Based on Current Location
                    int tileStartY = (int) leftTop / getTileSize();

                    //End Y Tile Based on New Location
                    int tileEndY = (int) leftBottom / getTileSize();

                    checkForCollisions:
                    {
                        //Loop Through Tile Starting from the Right and Moving Left
                        //Stop when a Collision Occurs and Stick Object to Right of Tile
                        for(int x=tileStartX; x>=tileEndX && x>=0; x--)
                        {
                            for(int y=tileStartY; y<=tileEndY && y<tileSet.getHeight(); y++)
                            {
                                Tile tile = tileSet.getTile(y, x);

                                //Check to See if is a Walk Throughable Tile
                                if(tile == null || !tile.getWalkThroughRight())
                                {
                                    //Collision with Tile Snap to Right of Tile
                                    newLocation = (x + 1) * getTileSize();

                                    //Break Loop
                                    break checkForCollisions;
                                }
                            }
                        }
                    }
                }
                else
                {
                    //Snap to Left of Map
                    newLocation = 0;
                }

                break;
            }
        }

        return newLocation;
    }

    /**
     * Used to calculation Scroll X and Y based on a object (Ussally Hero) and scroll box
     *
     * @param movingObject Object to base scroll X and Y on
     */
    private void calculateScroll(MoveableObject movingObject)
    {
        if(movingObject.getX() + movingObject.getWidth() > (scrollX + scrollBoxX + scrollBoxWidth))
        {
            //Right Bounds Hit
            if(movingObject.getX() < getMapWidth() - (getRenderWidth() - (scrollBoxX + scrollBoxWidth)) - movingObject.getWidth())
            {
                scrollX = (movingObject.getX() + movingObject.getWidth()) - (scrollBoxX + scrollBoxWidth);
            }
        }
        else if(movingObject.getX() < (scrollX + scrollBoxX) && movingObject.getX() > scrollBoxX)
        {
            //Left Bounds Hit
            scrollX = movingObject.getX() - scrollBoxX;
        }

        if(movingObject.getY() + movingObject.getHeight() > (scrollY + scrollBoxY + scrollBoxHeight))
        {
            //Down Bounds Hit
            if(movingObject.getY() < getMapHeight() - (getRenderHeight() - (scrollBoxY + scrollBoxHeight)) - movingObject.getHeight())
            {
                scrollY = (movingObject.getY() + movingObject.getHeight()) - (scrollBoxY + scrollBoxHeight);
            }
        }
        else if(movingObject.getY() <  (scrollY + scrollBoxY) && movingObject.getY() > scrollBoxY)
        {
            //Up Bounds Hit
            scrollY = movingObject.getY() - scrollBoxY;
        }
    }

    /**
     * Used to snap the screen (Zelda like scrolling) based on a object (Ussally Hero)
     *
     * @param movingObject Object to base the screen snap on
     */
    private void calculateSnap(MoveableObject movingObject)
    {
        if(movingObject.getX() + movingObject.getWidth() > scrollX + getRenderWidth() &&
                movingObject.getX() + movingObject.getWidth() < getMapWidth() &&
                (movingObject.getFacingDirection() == Direction.DIRECTION_EAST ||
                 movingObject.getFacingDirection() == Direction.DIRECTION_SOUTHEAST ||
                 movingObject.getFacingDirection() == Direction.DIRECTION_NORTHEAST))
        {
            //Right Bounds Hit
            scrollX = scrollX + getRenderWidth();
        }
        else if(movingObject.getX() < scrollX && movingObject.getX() > 0 &&
                (movingObject.getFacingDirection() == Direction.DIRECTION_WEST ||
                 movingObject.getFacingDirection() == Direction.DIRECTION_NORTHWEST ||
                 movingObject.getFacingDirection() == Direction.DIRECTION_SOUTHWEST))
        {
            //Left Bounds Hit
            scrollX = scrollX - getRenderWidth();
        }

        if(movingObject.getY() + movingObject.getHeight() > scrollY + getRenderHeight() &&
                movingObject.getY() + movingObject.getHeight() < getMapHeight() &&
                (movingObject.getFacingDirection() == Direction.DIRECTION_SOUTH ||
                 movingObject.getFacingDirection() == Direction.DIRECTION_SOUTHWEST ||
                 movingObject.getFacingDirection() == Direction.DIRECTION_SOUTHEAST))
        {
            //Down Bound Hit
            scrollY = scrollY + getRenderHeight();
        }
        else if(movingObject.getY() < scrollY && movingObject.getY() > 0 &&
                (movingObject.getFacingDirection() == Direction.DIRECTION_NORTH ||
                 movingObject.getFacingDirection() == Direction.DIRECTION_NORTHWEST ||
                 movingObject.getFacingDirection() == Direction.DIRECTION_NORTHEAST))
        {
            //Up Bounds Hit
            scrollY = scrollY - getRenderHeight();
        }
    }

    /**
     * Calculates scrolling and renders tiles to the render area
     *
     * @param parent Tile Engine
     * @param graphicBuffer Graphics buffer to write to
     * @param timeElasped Time elapsed since last call
     */
    public void renderTileObjects(TileEngineCanvas parent, Graphics2D graphicBuffer, int timeElasped)
    {
        //Verify Render Area
        if(getRenderWidth() != 0 && getRenderHeight() != 0)
        {
            //See if Scrolling is Enabled
            if(scrollActive)
            {
                //Calculate Scroll Box Scrolling
                calculateScroll(getHeroObject());
            }
            else if(screenSnap)
            {
                //Calculate Zelda Type Scrolling
                calculateSnap(getHeroObject());
            }

            //Get all Tiles
            Tile[][] tiles = tileSet.getTiles();

            //Calculate Tiles That Need to be rendered Based on Render Size and Scroll X and Y
            int startY = (int) (scrollY / getTileSize()) - 1;
            int startX = (int) (scrollX / getTileSize()) - 1;
            int endY = (int) (scrollY / getTileSize()) + (getRenderHeight() / getTileSize()) + 1;
            int endX = (int) (scrollX / getTileSize()) + (getRenderWidth() / getTileSize()) + 1;

            //Render Viewable Tiles
            for(int y=(startY < 0 ? 0 : startY); y<(endY < tileSet.getHeight() ? endY : tileSet.getHeight()) ; y++)
            {
                for(int x=(startX < 0 ? 0 : startX); x<(endX < tileSet.getWidth() ? endX : tileSet.getWidth()); x++)
                {
                    //Make sure its not a null tile
                    if(tiles[y][x] != null)
                    {
                        //Render Tile Offset by scroll X and Y
                        graphicBuffer.drawImage(tiles[y][x].getImage(), (x * 32) - scrollX, (y * 32) - scrollY, parent);
                    }
                }
            }
        }
    }

    /**
     * Renders all objects that are viewable in the render area
     *
     * @param parent Tile Engine
     * @param graphicBuffer Graphics buffer to write to
     * @param timeElasped Time elapsed since last call
     */
    public void renderObjects(TileEngineCanvas parent, Graphics2D graphicBuffer, int timeElasped)
    {
        //Verify Render Area
        if(getRenderWidth() != 0 && getRenderHeight() != 0)
        {
            //Get All Object that need to be Rendered
            ArrayList[] objectsToRender = objectsThatWillBeRendered();

            //Render all Object in Order of Z Layers
            for(int z=0; z<objectsToRender.length; z++)
            {
                for(int i=0; i<objectsToRender[z].size(); i++)
                {
                    GameObject objectToRender = (GameObject) objectsToRender[z].get(i);

                    //Run Sprite Animated if Object is a Sprite
                    if(objectToRender instanceof Sprite)
                    {
                        Sprite spriteObject = (Sprite) objectToRender;

                        //Make sure the Sprite is Animated
                        if(spriteObject.isAnimated())
                        {
                            //Animate Sprite Based on Time Elapsed
                            spriteObject.runSprite(timeElasped);
                        }
                    }

                    //Render Object to Render Area Offset by Scroll X and Y
                    graphicBuffer.drawImage(objectToRender.getImage(), objectToRender.getX() - scrollX, objectToRender.getY() - scrollY, parent);
                }

                //Make Sure to Render Hero On Top
                if(getHeroObject().getZ() == z)
                {
                    graphicBuffer.drawImage(getHeroObject().getImage(), getHeroObject().getX() - scrollX, getHeroObject().getY() - scrollY, parent);
                }
            }
        }
    }

    /**
     * Used to render map and object with 1 call
     *
     * @param parent Tile Engine
     * @param graphicBuffer Graphics buffer to write to
     * @param timeElasped Time elapsed since last call
     */
    public void renderAll(TileEngineCanvas parent, Graphics2D graphicBuffer , int timeElasped)
    {
        //Render Map
        renderTileObjects(parent, graphicBuffer , timeElasped);

        //Render Objects
        renderObjects(parent, graphicBuffer , timeElasped);
    }
}