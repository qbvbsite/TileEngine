package com.tileengine.object;

import java.util.ArrayList;

/**
 * QuadTree Controller Class
 *
 * @author James Keir
 */
public class QuadTree
{
    /**
     * Stores the root node of the QuadTree
     */
    private QuadTreeNode rootNode;

    /**
     * Create a QuadTree Objects
     *
     * @param startX Start location X QuadTree
     * @param endX End location X of QuadTree
     * @param startY Start location Y of QuadTree
     * @param endY End location Y of QuadTree
     * @param layers Number of object layers
     */
    public QuadTree(int startX, int endX, int startY, int endY, int layers)
    {
        //Make Quad Tree Root Node
        rootNode = new QuadTreeNode(startX, endX, startY, endY, layers, true);
    }

    /**
     * Inserts an object into the QuadTree
     *
     * @param placeableObject Object to insert into QuadTree
     */
    public void insertObject(Placeable placeableObject)
    {
        //Insert Object
        rootNode.insertObject(placeableObject);
    }

    /**
     * Remove an object from the QuadTree
     *
     * @param placeableObject Object to remove from the QuadTree
     */
    public void removeObject(Placeable placeableObject)
    {
        //Remove Object
        rootNode.removeObject(placeableObject);
    }

    /**
     * Get all collisions that collide with an object (on layers on or below object)
     *
     * @param queryObject Object to query with
     * @return Return all objects that collide with query object
     */
    public ArrayList[] getCollisions(Placeable queryObject)
    {
        //Return Collisions
        return getCollisions(queryObject, false);
    }

    /**
     * Get all collisions that collide with an object
     *
     * @param queryObject Object to query with
     * @param allLayers set to true to ignore render layers
     * @return Return all objects that collide with query object
     */
    public ArrayList[] getCollisions(Placeable queryObject, boolean allLayers)
    {
        //Arrat List to Place Objects into
        ArrayList[] collisions = new ArrayList[rootNode.getLayers()];
        for(int i=0; i<collisions.length; i++)
        {
            collisions[i] = new ArrayList();
        }

        //Get Possible Collision Objects
        rootNode.queryTree(collisions, queryObject, allLayers, false);

        //Return Possible Collisions
        return collisions;
    }

    /**
     * Returns all objects that would be rendered in a specific area
     *
     * @param x Start x location
     * @param y Start y location
     * @param width Width of render area
     * @param height Height of render area
     * @return Return an all render object orginized by render layers (Ex. ArrayList[0] = z layer 0)
     */
    public ArrayList[] getObjectsToRender(int x, int y, int width, int height)
    {
        //Array List to Place Objects into
        ArrayList[] renderObjects = new ArrayList[rootNode.getLayers()];
        for(int i=0; i<renderObjects.length; i++)
        {
            renderObjects[i] = new ArrayList();
        }

        //Get Possible Collision Objects
        rootNode.queryTree(renderObjects, new QuadTreeQuery(x, y, width, height), true, false);

        //Return Possible Collisions
        return renderObjects;
    }
}
