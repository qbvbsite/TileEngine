/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tileengine.audio;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Jammer
 */
public class AudioWAVClip implements AudioClip
{
    private Clip audioClip;

    /**
     * Plays Music in a Loop
     */
    public AudioWAVClip(URL resourceName)
    {
        //Load Music Clip
        try
        {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(resourceName);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
        }
        catch(Exception e)
        {}
    }

    /**
     * Plays Clip in loop
     */
    public void playLoop()
    {
        if(audioClip != null && audioClip.isRunning())
        {
            //Stop Music and Reset Frame
            audioClip.stop();
            audioClip.setFramePosition(0);
        }

        //Start Music
        audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        audioClip.start();
    }

    /**
     * Stops Loop
     */
    public void stopLoop()
    {
        if(audioClip != null && audioClip.isRunning())
        {
            //Stop Music and Reset Frame
            audioClip.stop();
            audioClip.setFramePosition(0);
        }
    }

    /**
     * Pauses Loop
     */
    public void pauseLoop()
    {
        if(audioClip != null && audioClip.isRunning())
        {
            //Stop Music and Reset Frame
            audioClip.stop();
        }
    }

    /**
     * Starts Loop
     */
    public void resumeLoop()
    {
        if(audioClip != null)
        {
            //Stop Music and Reset Frame
            audioClip.start();
        }
    }

    /**
     * Plays Clip Once
     */
    public void playOnce()
    {
        if(audioClip != null)
        {
            //Play if Clip isn't running
            if(!audioClip.isRunning())
            {
                audioClip.setFramePosition(0);

                //Start Clip
                audioClip.start();
            }
        }
    }
}
