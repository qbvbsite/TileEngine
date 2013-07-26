package com.tileengine.object;

import java.awt.Image;
import java.util.HashMap;

/**
 * A Set of Map Tiles
 *
 * @author James Keir
 */
public class TileSet
{
    /*
     * Store all the Tile Resource
     */
    private HashMap tilesResources = new HashMap();

    /*
     * Store the map tiles
     */
    private Tile[][] tiles;

    /*
     * Size of tiles
     */
    private int tileSize = TILE_SIZE_32;

    /*
     * Width of tile set
     */
    private int width;

    /*
     * Height of tile set
     */
    private int height;

    //Tile Sizes
    public final static int TILE_SIZE_16 = 16;
    public final static int TILE_SIZE_32 = 32;
    public final static int TILE_SIZE_64 = 64;

    /**
     * Sets tile size
     *
     * @param tileSize Tile size to set tile set to
     */
    public void setTileSize(int tileSize)
    {
        this.tileSize = tileSize;
    }

    /**
     * Get tile size
     *
     * @return Returns size of tiles
     */
    public int getTileSize()
    {
        return tileSize;
    }

    /**
     * Returns width of tile set
     *
     * @return Returns with of tile set
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Returns height of tile set
     *
     * @return Return height of tile set
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Adds a tile resource to the Tile Set
     *
     * @param tileId Tile ID
     * @param image Tile image
     */
    public void addTileResource(String tileId, Image image)
    {
        if(!tilesResources.containsKey(tileId))
        {
            tilesResources.put(tileId, new Tile(tileId, image));
        }
    }

    /**
     * Adds a tile resource to the Tile Set
     *
     * @param tileId Tile ID
     * @param image Tile image
     * @param walkThroughTop if the top is passable
     * @param walkThroughBottom if the bottom is passable
     * @param walkThroughLeft if the left is passable
     * @param walkThroughRight if the right is passable
     */
    public void addTileResource(String tileId, Image image, boolean walkThroughTop, boolean walkThroughBottom, boolean walkThroughLeft, boolean walkThroughRight)
    {
        if(!tilesResources.containsKey(tileId))
        {
            tilesResources.put(tileId, new Tile(tileId, image, walkThroughTop, walkThroughBottom, walkThroughLeft, walkThroughRight));
        }
    }

    /**
     *
     * @param tileId Tile ID
     * @param image Tile image
     * @param walkThrough If you can walk through the whole tile or not
     */
    public void addTileResource(String tileId, Image image, boolean walkThrough)
    {
        if(!tilesResources.containsKey(tileId))
        {
            tilesResources.put(tileId, new Tile(tileId, image, walkThrough));
        }
    }

    /**
     * Gets a tile resource
     *
     * @param id Id of tile resource you wish to get
     * @return Return the tile
     */
    public Tile getTileResource(String id)
    {
        Tile tile = null;

        if(tilesResources.containsKey(id))
        {
            tile = (Tile) tilesResources.get(id);
        }

        return tile;
    }

    /**
     * Returns all tiles in tile set
     *
     * @return Return all tiles in tile set
     */
    public Tile[][] getTiles()
    {
        return tiles;
    }

    /**
     * Get a specific tile in the tile set
     *
     * @param y Y location of tile
     * @param x X location of tile
     * @return Return requested tile
     */
    public Tile getTile(int y, int x)
    {
        return tiles[y][x];
    }

    /**
     * Set a specfic tile
     *
     * @param y Y location of the tile
     * @param x X location of the tile
     * @param tileId ID of the tile resource
     */
    public void setTile(int y, int x, String tileId)
    {
        if(tilesResources.containsKey(tileId))
        {
            tiles[y][x] = (Tile) tilesResources.get(tileId);
        }
    }

    /**
     * Setsup the tile set based on a array of ids
     *
     * @param tileIds Array of tile IDs
     */
    public void setTiles(String[][] tileIds)
    {
        if(tileIds != null && tileIds[0] != null)
        {
            //Get Map Size
            width = tileIds[0].length;
            height = tileIds.length;

            //Create Array
            tiles = new Tile[height][width];

            //Load TileSet
            for(int y=0; y<tileIds.length; y++)
            {
                for(int x=0; x<tileIds[y].length; x++)
                {
                    tiles[y][x] = (Tile) tilesResources.get(tileIds[y][x]);
                }
            }
        }
    }
}
