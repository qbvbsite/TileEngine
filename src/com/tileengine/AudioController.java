package com.tileengine;

import com.tileengine.audio.AudioClip;

public class AudioController
{
    /**
     * Plays Clip in Loop
     */
    public static void playLoop(String resourceName)
    {
        AudioClip audioClip = ResourceController.getInstance().loadAudio(resourceName);
        audioClip.playLoop();
    }

    /**
     * Stops Clip Loop
     */
    public static void stopLoop(String resourceName)
    {
        AudioClip audioClip = ResourceController.getInstance().loadAudio(resourceName);
        audioClip.stopLoop();
    }

    /**
     * Pauses Clip Loop
     */
    public static void pauseLoop(String resourceName)
    {
        AudioClip audioClip = ResourceController.getInstance().loadAudio(resourceName);
        audioClip.pauseLoop();
    }

    /**
     * Starts Clip Loop
     */
    public static void startLoop(String resourceName)
    {
        AudioClip audioClip = ResourceController.getInstance().loadAudio(resourceName);
        audioClip.resumeLoop();
    }

    /**
     * Plays Clip Once
     */
    public static void playOnce(String resourceName)
    {
        AudioClip audioClip = ResourceController.getInstance().loadAudio(resourceName);
        audioClip.playOnce();
    }
}
