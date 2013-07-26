package com.tileengine.object;

import java.util.ArrayList;

/**
 *
 * @author Jammer
 */
public class QuadTreeNode
{
    /*
     * Starting location X for QuadTree
     */
    private int startX = 0;

    /*
     * Ending location X for QuadTree
     */
    private int endX = 0;

    /*
     * Starting location Y for QuadTree
     */
    private int startY = 0;

    /*
     * Ending location Y for QuadTree
     */
    private int endY = 0;

    /*
     * Number of object layers
     */
    private int layers = 1;

    /*
     * Storage for all Object in Node Leaf
     */
    private ArrayList[] leaf = null;

    /*
     * If the Node has been split into 4 yet
     */
    private boolean isSpilt = false;

    /*
     * Nodes of QuadTree (Always 4)
     */
    private QuadTreeNode[] treeNodes = null;

    /*
     * If its the root of the QuadTree or not
     */
    private boolean root = false;

    /**
     * Creates a non root QuadTreeNode
     *
     * @param startX Starting x location
     * @param endX Ending x location
     * @param startY Starting y location
     * @param endY Ending y location
     * @param layers Number of object layers
     */
    public QuadTreeNode(int startX, int endX, int startY, int endY, int layers)
    {
        //Set Size of QuadTree
        setSize(startX, endX, startY, endY, layers);
    }

    /**
     * Creates a root QuadTreeNode
     *
     * @param startX Starting x location
     * @param endX Ending x location
     * @param startY Starting y location
     * @param endY Ending y location
     * @param layers Number of object layers
     * @param root Flags QuadTreNode as the root node
     */
    public QuadTreeNode(int startX, int endX, int startY, int endY, int layers, boolean root)
    {
        //Set Size of QuadTree
        setSize(startX, endX, startY, endY, layers);

        //Create leaf Arrays for each object layer
        for(int i=0; i<leaf.length; i++)
        {
            leaf[i] = new ArrayList();
        }

        //Split QuadTreeNode into 4 Quadrents
        createNodes();

        //Set as Root Node
        this.root = root;
    }

    /**
     * Sets the size of the QuadTreeNode
     *
     * @param startX Starting x location
     * @param endX Ending x location
     * @param startY Starting y location
     * @param endY Ending y location
     * @param layers Number of object layers
     */
    private void setSize(int startX, int endX, int startY, int endY, int layers)
    {
        //Set Size
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;

        //Set Number of Layers
        this.layers = layers;

        //Create Leaf Array based on Layers
        leaf = new ArrayList[layers];
    }

    /**
     * Return is this is the root node or not
     *
     * @return Returns true if its the root node
     */
    public boolean isRoot()
    {
        return root;
    }

    /**
     * Gets start x location
     *
     * @return Return start x location
     */
    public int getStartX()
    {
        return startX;
    }

    /**
     * Gets end x location
     *
     * @return Return end x location
     */
    public int getEndX()
    {
        return endX;
    }

    /**
     * Gets start y location
     *
     * @return Return start y location
     */
    public int getStartY()
    {
        return startY;
    }

    /**
     * Gets end y location
     *
     * @return Return end y location
     */
    public int getEndY()
    {
        return endY;
    }

    /**
     * Return number of object layers
     *
     * @return Returns number of object layers
     */
    public int getLayers()
    {
        return layers;
    }

    /**
     * Returns if the Node has been spilt or not
     *
     * @return Returns true in the node has been split
     */
    public boolean isSplit()
    {
        return isSplit();
    }

    /**
     * Get all objects in Leaf
     *
     * @return Returns all object in the left of the Node
     */
    public ArrayList[] getLeaf()
    {
        return leaf;
    }

    /**
     * Inserts an object into the Node
     *
     * @param placeableObject Object to place into the node
     */
    public void insertObject(Placeable placeableObject)
    {
        //Get the insert layer
        int insertLayer = placeableObject.getZ();
        if(insertLayer > (getLayers() - 1))
        {
            insertLayer = getLayers() - 1;
        }

        insertObject(placeableObject, insertLayer);
    }

    /**
     * Insert an object into a specific layer of the wuad tree
     *
     * @param placeableObject Object to place into the node
     * @param insertLayer layer to insert it into
     */
    private void insertObject(Placeable placeableObject, int insertLayer)
    {
        //Check if the Node has been split
        if(isSpilt)
        {
            //Used to flag if the object has found a home
            boolean foundHome = false;

            //Find a Home for Object
            for(int i=0; i<4; i++)
            {
                if(checkBounds(treeNodes[i], placeableObject))
                {
                    //Store and Break
                    treeNodes[i].insertObject(placeableObject, insertLayer);
                    foundHome = true;
                    break;
                }
            }

            //Did we find a Home for Object
            if(!foundHome)
            {
                //No Home Store in Leaf
                if(leaf[insertLayer] == null)
                {
                    leaf[insertLayer] = new ArrayList();
                }

                leaf[insertLayer].add(placeableObject);
            }
        }
        else
        {
            //Node Not Split
            if(leaf[insertLayer] == null)
            {
                //Create Leaf Array
                leaf[insertLayer] = new ArrayList();
                leaf[insertLayer].add(placeableObject);
            }
            else
            {
                //Spilt Node if It can fit into a Quadrent
                if(placeableObject.getWidth() < (getEndX() + 1 / 2) && placeableObject.getHeight() < (getEndY() + 1 / 2))
                {
                    //Create Quadrents
                    createNodes();

                    //Try to find homes for all Leaf Objects
                    for(int z=0; z<getLayers(); z++)
                    {
                        if(leaf[z] != null)
                        {
                            Object[] leafObjects = leaf[z].toArray();

                            //Loop through each leaf Object
                            for(int x=0; x<leafObjects.length; x++)
                            {
                                Placeable leafObject = (Placeable) leafObjects[x];

                                //Try to place object into Quadrents
                                for(int i=0; i<4; i++)
                                {
                                    //Find Leaf New Home
                                    if(checkBounds(treeNodes[i], leafObject))
                                    {
                                        //Insert Leaf
                                        treeNodes[i].insertObject(leafObject, z);

                                        //Remove Leaf and Break
                                        leaf[z].remove(leafObject);

                                        break;
                                    }
                                }
                            }
                        }
                    }

                    //Add Object
                    boolean foundHome = false;
                    for(int i=0; i<4; i++)
                    {
                        //Find Object a Home
                        if(checkBounds(treeNodes[i], placeableObject))
                        {
                            //Store Object and Break
                            treeNodes[i].insertObject(placeableObject, insertLayer);
                            foundHome = true;
                            break;
                        }
                    }

                    if(!foundHome)
                    {
                        //No Home Must be on a Line store in Leaf
                        if(leaf[insertLayer] == null)
                        {
                            leaf[insertLayer] = new ArrayList();
                        }

                        leaf[insertLayer].add(placeableObject);
                    }
                }
                else
                {
                    //Cant Spilt Anymore Because Object is to Big Store in Leaf
                    if(leaf[insertLayer] == null)
                    {
                        leaf[insertLayer] = new ArrayList();
                    }
                    
                    leaf[insertLayer].add(placeableObject);
                }
            }
        }
    }

    /**
     * Removes an object from the QuadTreeNode
     *
     * @param placeableObject Object to remove from Node
     */
    public void removeObject(Placeable placeableObject)
    {
        int insertLayer = placeableObject.getZ();
        if(insertLayer > (getLayers() - 1))
        {
            insertLayer = getLayers() - 1;
        }

        removeObject(placeableObject, insertLayer);
    }

    /**
     * Removes an object from the QuadTreeNode
     *
     * @param placeableObject Object to remove from Node
     * @param removeLayer Layer to remove object from
     */
    private void removeObject(Placeable placeableObject, int removeLayer)
    {
        //Check if we have Nodes
        if(treeNodes != null)
        {
            boolean foundNode = false;

            //Look for Object in Quadrents
            for(int i=0; i<4; i++)
            {
                //Look for Object
                if(checkBounds(treeNodes[i], placeableObject))
                {
                    //Found Object Quandrent Remove it
                    treeNodes[i].removeObject(placeableObject, removeLayer);

                    //Found Object
                    foundNode = true;

                    //Break
                    break;
                }
            }

            if(!foundNode)
            {
                //Found Object
                if(leaf[removeLayer] != null)
                {
                    leaf[removeLayer].remove(placeableObject);
                }
            }
        }
        else
        {
            //Found Object
            if(leaf[removeLayer] != null)
            {
                leaf[removeLayer].remove(placeableObject);
            }
        }
    }

    /**
     * Querys tree for object collisions
     *
     * @param currentCollisionObjects Array of current collisions
     * @param queryObject Query Object
     * @param includeAll True if you want to include all objects (Because all object collide with queryObject)
     */
    public void queryTree(ArrayList[] currentCollisionObjects, Placeable queryObject, boolean includeAll)
    {
        queryTree(currentCollisionObjects, queryObject, false, includeAll);
    }

    /**
     * Querys tree for object collisions
     *
     * @param currentCollisionObjects Array of current collisions
     * @param queryObject Query Object
     * @param allLayers If to include objects from all layers
     * @param includeAll True if you want to include all objects (Because all object collide with queryObject)
     */
    public void queryTree(ArrayList[] currentCollisionObjects, Placeable placeableObject, boolean allLayers, boolean includeAll)
    {
        //Get Query Layer
        int queryLayer = placeableObject.getZ();
        if(allLayers || queryLayer > (getLayers() - 1))
        {
            queryLayer = getLayers() - 1;
        }

        queryTree(currentCollisionObjects, placeableObject, queryLayer, includeAll);
    }

    /**
     * Querys tree for object collisions
     *
     * @param currentCollisionObjects Array of current collisions
     * @param queryObject Query Object
     * @param queryLayer Layer to query (and below)
     * @param allLayers If to include objects from all layers
     * @param includeAll True if you want to include all objects (Because all object collide with queryObject)
     */
    private void queryTree(ArrayList[] currentCollisionObjects, Placeable placeableObject, int queryLayer, boolean includeAll)
    {
        if(leaf != null)
        {
            //Add all object from queryLayer and below
            for(int z=0; z<=queryLayer; z++)
            {
                if(leaf[z] != null)
                {
                    for(int i=0; i<leaf[z].size(); i++)
                    {
                        Placeable testCollibableObject = (Placeable) leaf[z].get(i);

                        //Test is the objects collide
                        if(!placeableObject.equals(testCollibableObject) && placeableObject.collidesWith(testCollibableObject))
                        {
                            //Object Collision All to Array
                            currentCollisionObjects[z].add(testCollibableObject);
                        }
                    }
                }
            }
        }

        //If we have Nodes
        if(treeNodes != null)
        {
            boolean putInABox = false;

            //Loop through each Node
            for(int i=0; i<4; i++)
            {
                if(!includeAll)
                {
                    //Check If Object Fits
                    if(checkBounds(treeNodes[i], placeableObject))
                    {
                        //Store and Break
                        treeNodes[i].queryTree(currentCollisionObjects, placeableObject, queryLayer, includeAll);
                        putInABox = true;
                        break;
                    }
                }
                else
                {
                    //Include All Set
                    treeNodes[i].queryTree(currentCollisionObjects, placeableObject, queryLayer, includeAll);
                    putInABox = true;
                }
            }

            //If Object Dont Fit in Any Quadrents Include all Objects from Now On
            if(!putInABox)
            {
                for(int i=0; i<4; i++)
                {
                    if(touchsBox(treeNodes[i], placeableObject))
                    {
                        treeNodes[i].queryTree(currentCollisionObjects, placeableObject, queryLayer, true);
                    }
                }
            }
        }
    }

    /**
     * Create Quadrent Nodes
     */
    private void createNodes()
    {
        //Half of X/Y
        int halfX = 0;
        int halfY = 0;

        //Get Half of X
        if(getStartX() != 0)
        {
            halfX = getStartX() / 2;
        }

        //Get Half of Y
        if(getStartY() != 0)
        {
            halfY = getStartY() / 2;
        }

        //Based On: 32 x 32
        treeNodes = new QuadTreeNode[4];

        //Y: 0 - 15, X: 0 - 15
        treeNodes[0] = new QuadTreeNode(getStartX(), halfX + (getEndX() / 2), getStartY(), halfY + (getEndY() / 2), layers);

        //Y: 0 - 15, X: 16 - 31
        treeNodes[1] = new QuadTreeNode(halfX + (getEndX() / 2) + 1, getEndX(), getStartY(), halfY + (getEndY() / 2), layers);

        //Y: 16 - 31, X: 0 - 15
        treeNodes[2] = new QuadTreeNode(getStartX(), halfX + (getEndX() / 2), halfY + (getEndY() / 2) + 1, getEndY(), layers);

        //Y: 16 - 31, X: 16 - 31
        treeNodes[3] = new QuadTreeNode(halfX + (getEndX() / 2) + 1, getEndX(), halfY + (getEndY() / 2) + 1, getEndY(), layers);

        //Set isSplit
        this.isSpilt = true;
    }

    /**
     * Check to see if query object fits into Quadrent
     *
     * @param treeNode Quadrent node
     * @param placeableObject Query object
     * @return Return true if fit and false if it doesn't
     */
    private boolean checkBounds(QuadTreeNode treeNode, Placeable queryObject)
    {
        boolean fitsInside = false;

        //Check to see if It Fits
        if(treeNode.getStartX() <= queryObject.getX() && treeNode.getEndX() >= (queryObject.getX() + queryObject.getWidth() - 1) &&
               treeNode.getStartY() <= queryObject.getY() && treeNode.getEndY() >= (queryObject.getY() + queryObject.getHeight() - 1))
        {
            fitsInside = true;
        }

        return fitsInside;
    }

    /**
     * Checks to see if query object touches a Quadrent
     *
     * @param treeNode Quadrent to see in query object touchs
     * @param queryObject Query object
     * @return
     */
    private boolean touchsBox(QuadTreeNode treeNode, Placeable queryObject)
    {
        //Check for X Over Lap
        if(treeNode.getStartX() > (queryObject.getX() + queryObject.getWidth() - 1) ||
                treeNode.getEndX() < queryObject.getX())
        {
            return false;
        }

        //Check for Y Overlap
        if(treeNode.getStartY() > (queryObject.getY() + queryObject.getHeight() - 1) ||
                treeNode.getEndY() < queryObject.getY())
        {
            return false;
        }

        //Theres Overlap
        return true;
    }
}
