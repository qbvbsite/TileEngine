package com.tileengine.pathing;

import java.util.ArrayList;

public class PathSearchTable
{
    private PathSearchNode[][] pathTileArea = null;
    private ArrayList pathOpenTiles = new ArrayList();
    private ArrayList pathClosedTiles = new ArrayList();
    
    public PathSearchTable(int mapWidth, int mapHeight)
    {
        pathTileArea = new PathSearchNode[mapHeight][mapWidth];
    }
    
    public void addTileToOpenList(PathSearchNode searchObject)
    {
        pathOpenTiles.add(searchObject);
        pathTileArea[searchObject.getY()][searchObject.getX()] = searchObject;
    }
    
    public void removeTileFromOpenList(PathSearchNode searchObject)
    {
        pathOpenTiles.remove(searchObject);
        pathTileArea[searchObject.getY()][searchObject.getX()] = null;
    }
    
    public boolean inOpenList(int locationX, int locationY)
    {
        boolean inOpenList = false;
        
        if(pathOpenTiles.contains(pathTileArea[locationY][locationX]))
        {
            inOpenList = true;
        }
        
        return inOpenList;
    }
    
    public void addTileToClosedList(PathSearchNode searchObject)
    {
        pathClosedTiles.add(searchObject);
        pathTileArea[searchObject.getY()][searchObject.getX()] = searchObject;
    }
    
    public void removeTileFromClosedList(PathSearchNode searchObject)
    {
        pathClosedTiles.remove(searchObject);
        pathTileArea[searchObject.getY()][searchObject.getX()] = null;
    }  
    
    public boolean inClosedList(int locationX, int locationY)
    {
        boolean inClosedList = false;
        
        if(pathClosedTiles.contains(pathTileArea[locationY][locationX]))
        {
            inClosedList = true;
        }
        
        return inClosedList;
    }
    
    public PathSearchNode getObjectByLocation(int locationX, int locationY)
    {
        return pathTileArea[locationY][locationX];
    }
            
    public PathSearchNode getLowestMoveCost()
    {
        PathSearchNode lowestCost = null;
        
        for(int i=0; i<pathOpenTiles.size(); i++)
        {
            PathSearchNode nextObject = (PathSearchNode) pathOpenTiles.get(i);
            
            if(lowestCost == null || nextObject.getMoveCost() <= lowestCost.getMoveCost())
            {
                lowestCost = nextObject;
            }
        }
        
        return lowestCost;
    }
}
