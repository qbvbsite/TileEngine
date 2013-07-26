package com.tileengine.object;

public class QuadTreeQuery implements Placeable
{
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private int width;
    private int height;

    public QuadTreeQuery(int x, int y, int width, int height)
    {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    public QuadTreeQuery(int x, int y, int z, int width, int height)
    {
        setX(x);
        setY(y);
        setZ(z);
        setWidth(width);
        setHeight(height);
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getY()
    {
        return y;
    }

    public void setZ(int z)
    {
        this.z = z;
    }

    public int getZ()
    {
        return z;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getWidth()
    {
        return width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean isPenetrable()
    {
        return false;
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
}
