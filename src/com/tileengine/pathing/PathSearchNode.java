package com.tileengine.pathing;

public class PathSearchNode
{
    private PathSearchNode parentObject = null;
    private int x;
    private int y;
    private int cost = 0;
    private int heuristic;
  
    public PathSearchNode(int x, int y)
    {
        setLocation(x, y);
    }
    
    public PathSearchNode(int x, int y, PathSearchNode parentObject)
    {
        setLocation(x, y);
        
        setParentObject(parentObject);
    }
    
    public PathSearchNode(int x, int y, PathSearchNode parentObject, int cost, int heuristic)
    {
        setLocation(x, y);
        
        setParentObject(parentObject);
        
        setCost(cost);
        
        setHeuristic(heuristic);
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public int getY()
    {
        return this.y;
    }   
    
    public void setCost(int cost)
    {
        this.cost = cost;
    }
    
    public int getCost()
    {
        return this.cost;
    }
    
    public void setHeuristic(int heuristic)
    {
        this.heuristic = heuristic;
    }
        
    public int getHeuristic()
    {
        return this.heuristic;
    }
    
    public int getMoveCost()
    {
        return this.cost + this.heuristic;
    }
    
    public void setParentObject(PathSearchNode parentObject)
    {
        this.parentObject = parentObject;
    }
    
    public PathSearchNode getParentNode()
    {
        return this.parentObject;
    }
    
    public void setLocation(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
