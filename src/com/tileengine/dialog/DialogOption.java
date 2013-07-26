package com.tileengine.dialog;

import com.tileengine.logic.Logic;
import java.util.ArrayList;

public class DialogOption
{
    /*
     * Options text
     */
    private String optionText;

    /*
     * Options Logic
     */
    private ArrayList logicObjects;

    /*
     * Options select page
     */
    private String selectPage;

    /*
     * Options success page
     */
    private String successPage;

    /*
     * Options fail page
     */
    private String failPage;

    /**
     * Create an DialogOption with a Success Page and Fail Page
     *
     * @param optionText Text of option
     * @param selectPage Option select page
     */
    public DialogOption(String optionText, String selectPage)
    {
        //Set Option Text
        setOptionText(optionText);

        //Set Select Page
        setSelectPage(selectPage);
    }

    /**
     * Create an DialogOption with a Success Page and Fail Page
     *
     * @param optionText Text of option
     * @param successPage Option success page
     * @param failPage Option fail page
     */
    public DialogOption(String optionText, String successPage, String failPage)
    {
        //Set Option Text
        setOptionText(optionText);

        //Set Success Page
        setSuccessPage(successPage);

        //Set Fail Page
        setFailPage(failPage);
    }

    /**
     * Sets the options text
     *
     * @param optionText Options text
     */
    public void setOptionText(String optionText)
    {
        this.optionText = optionText;
    }

    /**
     * Gets option text
     *
     * @return Returns options text
     */
    public String getOptionText()
    {
        return optionText;
    }

    /**
     * Set the options select page
     *
     * @param successPage Select page for option
     */
    public void setSelectPage(String selectPage)
    {
        this.selectPage = selectPage;
    }

    /**
     * Gets the select page for option
     *
     * @return Returns the options select page
     */
    public String getSelectPage()
    {
        return selectPage;
    }

    /**
     * Set the options success page
     *
     * @param successPage Success page for option
     */
    public void setSuccessPage(String successPage)
    {
        this.successPage = successPage;
    }

    /**
     * Gets the success page for option
     *
     * @return Returns the options success page
     */
    public String getSuccessPage()
    {
        return successPage;
    }

    /**
     * Set the options fail page
     *
     * @param failPage Fail page for option
     */
    public void setFailPage(String failPage)
    {
        this.failPage = failPage;
    }

    /**
     * Gets the fail page for option
     *
     * @return Returns the fail page
     */
    public String getFailPage()
    {
        return failPage;
    }

    /**
     * Adds logic object to DialogOption
     *
     * @param logicObject Logic object to add
     */
    public void addLogicObject(Logic logicObject)
    {
        if(logicObjects == null)
        {
            logicObjects = new ArrayList();
        }
        
        logicObjects.add(logicObject);
    }

    /**
     * Get a logic object based on index
     *
     * @param index Index of the logic object
     * @return Returns the logic object
     */
    public Logic getLogicObject(int index)
    {
        return (Logic) logicObjects.get(index);
    }

    /**
     * Get all the logic object for Dialog Option
     *
     * @return Array of all the logic objects
     */
    public Logic[] getLogicObjects()
    {
        Logic[] logicArray = null;

        if(logicObjects != null)
        {
            logicArray = new Logic[logicObjects.size()];

            for(int i=0; i<logicObjects.size(); i++)
            {
                logicArray[i] = (Logic) logicObjects.get(i);
            }
        }

        return logicArray;
    }
}
