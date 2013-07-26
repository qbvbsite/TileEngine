package com.tileengine.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Jammer
 */
public class Keyboard implements KeyListener, InputControl
{
    private static Keyboard uniqueInstance = null;
    private final static int STATE_UP = 0;
    private final static int STATE_STUCK = 1;
    private final static int STATE_DOWN = 2;

    private boolean stickyKeys = true;
    private int[] keyStates;

    /**
     * Singleton of Keyboard
     *
     * @return Keyboard instance
     */
    public static Keyboard getInstance()
    {
        //If Instance null create one
        if (uniqueInstance == null)
        {
          uniqueInstance = new Keyboard();
        }

        //Return Keyboard Instance
        return uniqueInstance;
    }

    /**
     * Create a Keyboard InputControl
     */
    private Keyboard()
    {
        //Create State Array
        keyStates = new int[256];

        //Setup States
        for (int i = 0; i < 256; i++)
        {
            //Key State
            keyStates[i] = STATE_UP;
        }
    }

    public void keyPressed(KeyEvent keyEvent)
    {
        //Key Pressed Update State
        keyStates[keyEvent.getKeyCode()] = STATE_DOWN;
    }

    public void keyReleased(KeyEvent keyEvent)
    {
        //Key Released State State (Stick Keys to Make sure the State is polled)
        if(stickyKeys)
        {
            keyStates[keyEvent.getKeyCode()] = STATE_STUCK;
        }
        else
        {
            keyStates[keyEvent.getKeyCode()] = STATE_UP;
        }
    }

    public void keyTyped(KeyEvent keyEvent)
    {
    }

    public boolean getState(int keyCode)
    {
        //Return current Key State
        return (keyStates[keyCode] != STATE_UP);
    }

    public void resetState(int keyCode)
    {
        //Reset Key
        keyStates[keyCode] = STATE_UP;
    }

    public void resetStates()
    {
        //Reset Keys (Used to reset Sticky Keys)
        if (stickyKeys)
        {
            for (int i = 0; i < 256; i++)
            {
                if (keyStates[i] == STATE_STUCK)
                {
                    keyStates[i] = STATE_UP;
                }
            }
        }
    }
}
