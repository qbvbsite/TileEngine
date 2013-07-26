
package com.tileengine;

import com.tileengine.inputs.InputControl;
import com.tileengine.inputs.Keyboard;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to handle all Input and Key/Button Mappings
 *
 * @author James Keir
 */
public class InputController
{
    private static InputController soleInstance = null;
    private Map currentInputs = new HashMap();
    private Map mappedInputs = new HashMap();

    /**
     * Token used for Keyboard Input
     */
    public final static int TYPE_KEYBOARD = 1;

    /**
     * Act as a singleton and prevent instancation
     */
    private InputController()
    {}

    /**
     * Returns a InputController Instance
     *
     * @return Instance of Input Controller
     */
    public static InputController getInstance()
    {
        //Check to See if an Instance has been created
        if (soleInstance == null)
        {
            //Instance doen't exist create on
            soleInstance = new InputController();
            soleInstance.currentInputs.put(TYPE_KEYBOARD, Keyboard.getInstance());
        }

        //Return InputController Instance
        return soleInstance;
    }

    /**
     * Maps a Key/Button to a Token
     *
     * @param inputControl Input control (Ex. Keyboard, Mouse, etc)
     * @param inputToken Token you wish to map the Key/Button to
     * @param input Key/Button you wish to map
     */
    public void setMapping(int inputControl, Integer inputToken, Integer input)
    {
        //Set Input Control Mapping
        mappedInputs.put(inputToken, new Integer[]{inputControl, input});
    }

    /**
     * Gets mapping based on a token
     *
     * @param inputToken Token you wish to get the mapping for
     * @return Mapping that is mapped to token
     */
    public Integer[] getMapping(Object inputToken)
    {
        //Get Mapping
        return (Integer[]) mappedInputs.get(inputToken);
    }

    /**
     * Removes mapping from mapping hash
     *
     * @param inputToken Token to remove in mapping hash
     */
    public void removeMapping(Integer inputToken)
    {
        //Remove Mapping
        mappedInputs.remove(inputToken);
    }

    /**
     * Clear mapping hash
     */
    public void clearMappings()
    {
        //Clear mapping hash
        mappedInputs.clear();
    }

    /**
     * Reset states of Keys/Buttons of all Inputs
     */
    public void resetStates()
    {
        Object[] inputControls = (Object[]) currentInputs.values().toArray();

        for (int i = 0; i < inputControls.length; i++)
        {
            ((InputControl) inputControls[i]).resetStates();
        }
    }

    /**
     * Get the state of the Key/Button for the token
     *
     * @param inputToken Token to check state of
     * @return State of Key/Button
     */
    public boolean getState(Object inputToken)
    {
        //Get Mapping
        Integer[] mapping = (Integer[]) mappedInputs.get(inputToken);

        //Return State of Key/Button
        return ((InputControl) currentInputs.get(mapping[0])).getState((Integer) mapping[1]);
    }

    /**
     * Reset state of a mapped Key/Button
     *
     * @param inputToken Token to reset state for
     */
    public void resetState(Object inputToken)
    {
        //Get Mapping
        Integer[] mapping = (Integer[]) mappedInputs.get(inputToken);

        //Reset State
        ((InputControl) currentInputs.get(mapping[0])).resetState((Integer) mapping[1]);
    }
}
