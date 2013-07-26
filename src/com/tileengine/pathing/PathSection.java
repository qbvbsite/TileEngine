package com.tileengine.pathing;

public class PathSection 
{
    private int endX;
    private int endY;
    private int speed;
    private int pauseTime;
    private boolean calculated;

    public PathSection(int endX, int endY, int pauseTime, int speed, boolean calculated)
    {
        this.endX = endX;
        this.endY = endY;
        this.speed = speed;
        this.pauseTime = pauseTime;
        this.calculated = calculated;
    }

    public void setEndX(int endX)
    {
        this.endX = endX;
    }
    
    public int getEndX()
    {
        return endX;
    }
    
    public void setEndY(int endY)
    {
        this.endY = endY;
    }
    
    public int getEndY()
    {
        return endY;
    }
    
    public int[] endPoint()
    {
            return new int[]{this.endX,this.endY};
    }

    public void setPauseTime(int pauseTile)
    {
        this.pauseTime = pauseTime;
    }
    
    public int getPauseTime()
    {
        return pauseTime;
    }
    
    public void setSpeed(int speed)
    {
        this.speed = speed;
    }
    
    public int getSpeed()
    {
        return speed;
    }
    
    public void setCalculated(boolean calculated)
    {
        this.calculated = calculated;
    }
    
    public boolean getCalculated()
    {
        return this.calculated;
    }
}
