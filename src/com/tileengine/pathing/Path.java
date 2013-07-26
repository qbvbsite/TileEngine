package com.tileengine.pathing;

import com.tileengine.GameController;
import com.tileengine.object.Direction;
import com.tileengine.object.GameObject;
import com.tileengine.object.Tile;
import java.util.ArrayList;

public class Path 
{
    private ArrayList pathGroups;
    private int currentGroupIndex;
    private int startX;
    private int startY;
    private int pauseTime;
    private int currentDirection;

    public Path(ArrayList pathGroups, int startX, int startY)
    {
        this.pathGroups = pathGroups;
        this.startX = startX;
        this.startY = startY;
        this.currentGroupIndex = 0;
     }

    public void next()
    {
        PathGroup pathGroup = (PathGroup) this.pathGroups.get(this.currentGroupIndex);
        
        if (pathGroup.next())
        {
            if (this.currentGroupIndex < this.pathGroups.size() -1)
            {
                this.currentGroupIndex +=1;
            }
            else
            {
                this.currentGroupIndex = 0;
            }
        }
    }

    public void addPathGroup(PathGroup group)
    {
        this.pathGroups.add(group);
    }

    public PathGroup getCurrentPathGroup()
    {
        return (PathGroup) this.pathGroups.get(currentGroupIndex);
    }
    
    public void setStartPostion(int startX, int startY)
    {
        this.startX = startX;
        this.startY = startY;
    }

    public PathSection getCurrentSection()
    {
        return (PathSection) getCurrentPathGroup().getCurrentSection();
    }

    public int getPause()
    {
        return pauseTime;
    }

    public void setPause(int milliseconds)
    {
        this.pauseTime = milliseconds;
    }

    public void reducePauseTime(int milliseconds)
    {
        if (pauseTime > 0)
        {
            this.pauseTime -= milliseconds;
        }
    }

    public int getCurrentDirection()
    {
        return currentDirection;
    }

    public int setDirection(int startX, int startY)
    {
        int direction = 0;
        PathSection currentSection = getCurrentSection();
        int[] endPoint = currentSection.endPoint();
        
        if(startX == endPoint[0] && startY != endPoint[1]) 
        {    
            if (startY < endPoint[1])
            {                
                direction = Direction.DIRECTION_SOUTH;  
            }
            else
            {
                direction = Direction.DIRECTION_NORTH;
            }    
        }
        else if(startY == endPoint[1] && startX != endPoint[0])
        {
           if (startX < endPoint[0])
           {               
               direction = Direction.DIRECTION_EAST;               
           }
           else
           {
               direction = Direction.DIRECTION_WEST;
           } 
        }
        else if(startX != endPoint[0] && startY != endPoint[1])
        {
            int diffX = 0;
            int diffY = 0;

            if (startX > endPoint[0])
            {
                diffX = startX - endPoint[0];
            }
            else
            {
                diffX = endPoint[0] - startX;
            }

            if(startY > endPoint[1])
            {
                diffY = startY - endPoint[1];
            }
            else
            {
                diffY = endPoint[1] - startY;
            }

            if (diffX == diffY)
            {
                if (startX > endPoint[0] && startY > endPoint[1]) 
                {
                    direction = Direction.DIRECTION_NORTHWEST;
                }
                else if(startX < endPoint[0] && startY < endPoint[1])
                {
                    direction = Direction.DIRECTION_SOUTHEAST;
                }
                else if(startX < endPoint[0] && startY > endPoint[1])
                {
                    direction = Direction.DIRECTION_NORTHEAST;
                }
                else 
                {
                    direction = Direction.DIRECTION_SOUTHWEST;
                }
            }
        }

        currentDirection = direction;

        return currentDirection;
    }
    
    public PathSearchNode calculatePath(GameController gameController, GameObject gameObject, int targetX, int targetY)
    {
        //Get Current Y Base Tile (Based on center of object)
        int tileStartY = (int) (gameObject.getY() + (gameObject.getHeight() / 2)) / gameController.getTileSize();

        //Get Current X Base Tile (Based on center of object)
        int tileStartX = (int) (gameObject.getX() + (gameObject.getWidth() / 2)) / gameController.getTileSize();
        
        //End Tile Y
        int tileEndY = (int) (targetY + (gameObject.getHeight() / 2)) / gameController.getTileSize();
        
        //End Tile X
        int tileEndX = (int) (targetX + (gameObject.getWidth() / 2)) / gameController.getTileSize();
        
        //Load SearchTable
        PathSearchTable searchTable = new PathSearchTable(gameController.getMapWidth(), gameController.getMapHeight());
        
        //Starting Location
        PathSearchNode startingNode = new PathSearchNode(tileStartX, tileStartY);
        
        //Add Starting Square to Open List
        searchTable.addTileToOpenList(startingNode);
        
        //Start Search
        return searchForPath(gameController, searchTable, startingNode, tileEndX, tileEndY);
    }
    
    private PathSearchNode searchForPath(GameController gameController, PathSearchTable searchTable, PathSearchNode node, int targetX, int targetY)
    {
         //Drop Parent From Open List
        searchTable.removeTileFromOpenList(node);
        searchTable.addTileToClosedList(node);
        
        //Get All Adjacent Squares
        for(int locationY=node.getY()-1; locationY<=node.getY()+1; locationY++)
        {
            for(int locationX=node.getX()-1; locationX<=node.getX()+1; locationX++)
            {
                //Make Sure its not parent node
                if(locationX != node.getX() || locationY != node.getY())
                {
                    //Make Sure its within the map
                    if(locationY >= 0 && locationX >= 0 && 
                            locationY < (gameController.getMapHeight() / gameController.getTileSize()) && 
                            locationX < (gameController.getMapWidth() / gameController.getTileSize()))
                    {
                        //Make Sure It Valid and Not In the Closed List
                        if(validMove(gameController, node, locationX, locationY) && !searchTable.inClosedList(locationX, locationY))
                        {
                            //Calculate Cost
                            int cost = node.getCost();
                            if(locationY == node.getY() || locationX == node.getX())
                            {
                                //Vertical/Horizontal Move
                                cost += 10;
                            }
                            else
                            {
                                //Diagonal Move
                                cost += 14;
                            }
                            
                            //Check to see if its in Open List Already
                            if(!searchTable.inOpenList(locationX, locationY))
                            {
                                //Calculate Heuristic (Manhatten Meathod)
                                int heuristic = 10 * (Math.abs(locationX-targetX) + Math.abs(locationY-targetY));
                            
                                //Create Node
                                PathSearchNode validNode = new PathSearchNode(locationX, locationY, node, cost, heuristic);

                                //Add Node to Open List
                                searchTable.addTileToOpenList(validNode);
                            }
                            else
                            {
                                //Get Search Object By Location
                                PathSearchNode existingNode = searchTable.getObjectByLocation(locationX, locationY);
                                
                                //Already in Open List Check Score to See If its a better path
                                if(existingNode.getCost() < cost)
                                {
                                    PathSearchNode parentNode = node.getParentNode();
                                    
                                    //Change Parent Node If Beside It
                                    if(Math.abs(parentNode.getX() - existingNode.getX()) <= 1 && Math.abs(parentNode.getY() - existingNode.getY()) <= 1)
                                    {
                                        existingNode.setParentObject(parentNode);
                                    }
                                }
                                else
                                {
                                    //Calculated Cost is Lower
                                    existingNode.setCost(cost);
                                    existingNode.setParentObject(node);
                                }
                            }
                        }
                    }
                }
            }
        }
                
        //Get Next Lowest Score
        PathSearchNode nextMove = searchTable.getLowestMoveCost();
                
        //If Best Node Doesn't Match Continue Search
        if(nextMove != null && (nextMove.getX() != targetX || nextMove.getY() != targetY))
        {
            nextMove = searchForPath(gameController, searchTable, nextMove, targetX, targetY);
        }
        
        return nextMove;
    }
    
    private boolean validMove(GameController gameController, PathSearchNode node, int tileX, int tileY)
    {
        boolean moveValid = true;

        //Get Tile
        Tile tile = gameController.getTile(tileY, tileX);

        //Make sure we have a tile
        if(tile != null)
        {
            //Make sure you can walk through it
            if(tileY < node.getY() && tileX < node.getX())
            {
                if(!tile.getWalkThroughBottom() || !tile.getWalkThroughRight())
                {
                    moveValid = false;
                }
                else
                {
                    //Check X Corner to see if we are cutting corners
                    Tile cornerTile = gameController.getTile(node.getY(), tileX);

                    if(cornerTile == null || (!cornerTile.getWalkThroughTop() || !cornerTile.getWalkThroughRight()))
                    {
                        moveValid = false;
                    }
                    
                    //Check Y Corner to see if we are cutting corners
                    cornerTile = gameController.getTile(tileY, node.getX());

                    if(cornerTile == null || (!cornerTile.getWalkThroughBottom() || !cornerTile.getWalkThroughLeft()))
                    {
                        moveValid = false;
                    }
                }
            }
            else if(tileY < node.getY() && tileX == node.getX() && !tile.getWalkThroughBottom())
            {
                moveValid = false;
            }
            else if(tileY < node.getY() && tileX > node.getX())
            {
                if((!tile.getWalkThroughBottom() || !tile.getWalkThroughLeft()))
                {
                    moveValid = false;
                }
                else
                {
                    //Check X Corner to see if we are cutting corners
                    Tile cornerTile = gameController.getTile(node.getY(), tileX);

                    if(cornerTile == null || (!cornerTile.getWalkThroughTop() || !cornerTile.getWalkThroughLeft()))
                    {
                        moveValid = false;
                    }
                    
                    //Check Y Corner to see if we are cutting corners
                    cornerTile = gameController.getTile(tileY, node.getX());

                    if(cornerTile == null || (!cornerTile.getWalkThroughBottom() || !cornerTile.getWalkThroughRight()))
                    {
                        moveValid = false;
                    }
                }
            }
            else if(tileY == node.getY() && tileX < node.getX() && !tile.getWalkThroughRight())
            {
                moveValid = false;
            }
            else if(tileY == node.getY() && tileX > node.getX() && !tile.getWalkThroughLeft())
            {
                moveValid = false;
            }
            else if(tileY > node.getY() && tileX < node.getX())
            {
                if(!tile.getWalkThroughTop() || !tile.getWalkThroughRight())
                {
                    moveValid = false;
                }
                else
                {
                    //Check X Corner to see if we are cutting corners
                    Tile cornerTile = gameController.getTile(node.getY(), tileX);

                    if(cornerTile == null || (!cornerTile.getWalkThroughBottom() || !cornerTile.getWalkThroughRight()))
                    {
                        moveValid = false;
                    }
                    
                    //Check Y Corner to see if we are cutting corners
                    cornerTile = gameController.getTile(tileY, node.getX());

                    if(cornerTile == null || (!cornerTile.getWalkThroughBottom() || !cornerTile.getWalkThroughLeft()))
                    {
                        moveValid = false;
                    }
                }
            }
            else if(tileY > node.getY() && tileX == node.getX() && !tile.getWalkThroughTop())
            {
                moveValid = false;
            }
            else if(tileY > node.getY() && tileX > node.getX())
            {
                if(!tile.getWalkThroughTop() || !tile.getWalkThroughLeft())
                {
                    moveValid = false;
                }
                else
                {
                    //Check X Corner to see if we are cutting corners
                    Tile cornerTile = gameController.getTile(node.getY(), tileX);

                    if(cornerTile == null || (!cornerTile.getWalkThroughBottom() || !cornerTile.getWalkThroughLeft()))
                    {
                        moveValid = false;
                    }
                    
                    //Check Y Corner to see if we are cutting corners
                    cornerTile = gameController.getTile(tileY, node.getX());

                    if(cornerTile == null || (!cornerTile.getWalkThroughTop() || !cornerTile.getWalkThroughRight()))
                    {
                        moveValid = false;
                    }
                }
            }
        }
        else
        {
            moveValid = false;
        }
        
        return moveValid;
    }
}


