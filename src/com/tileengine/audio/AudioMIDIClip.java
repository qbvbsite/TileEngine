package com.tileengine.audio;

import java.net.URL;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 *
 * @author Jammer
 */
public class AudioMIDIClip implements AudioClip
{
    private Sequencer audioClip;

    /**
     * Plays Music in a Loop
     */
    public AudioMIDIClip(URL resource)
    {
        try
        {
            // From file
            Sequence sequence = MidiSystem.getSequence(resource);

            // Create a sequencer for the sequence
            audioClip = MidiSystem.getSequencer();
            audioClip.open();
            audioClip.setSequence(sequence);
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
            audioClip.setTickPosition(0);
        }

        //Start Music
        audioClip.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
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
            audioClip.setTickPosition(0);
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
                audioClip.setTickPosition(0);

                //Start Clip
                audioClip.start();
            }
        }
    }
}
