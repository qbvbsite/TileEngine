package com.tileengine.object;

/**
 * Sprite Interface for Sprite Objects
 *
 * @author James Keir
 */
public interface Sprite
{
    //Sprite Type
    public final static int SPRITE_ANIMATION = 2;
    public final static int SPRITE_4D = 4;
    public final static int SPRITE_8D = 8;

    /**
     * If the sprite is animated or not
     *
     * @return Return is the aprite is animated or not
     */
    public boolean isAnimated();

    /**
     * Runs sprite animation based on time elapsed
     *
     * @param timeElasped Time that has elasped since last call
     */
    public void runSprite(int timeElasped);
}
