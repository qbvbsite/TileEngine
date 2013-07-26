package com.tileengine.audio;

public interface AudioClip
{
    /**
     * Plays Clip in Loop
     */
    public void playLoop();

    /**
     * Stops Clip Loop
     */
    public void stopLoop();

    /**
     * Pauses Clip Loop
     */
    public void pauseLoop();

    /**
     * Starts Clip Loop
     */
    public void resumeLoop();

    /**
     * Plays Clip Once
     */
    public void playOnce();
}
