package com.tileengine.inputs;

/**
 * Input controller interface
 *
 * @author James Keir
 */
public interface InputControl
{
    /**
     * Gets a state of a key/button
     *
     * @param inputCode Input code
     * @return true/false based on state of key/button
     */
    public boolean getState(int inputCode);

    /**
     * Reset states of keys/buttons
     */
    public void resetStates();

    /**
     * Reset state of a specific key/button
     *
     * @param inputCode INput code to reset
     */
    public void resetState(int inputCode);
}
