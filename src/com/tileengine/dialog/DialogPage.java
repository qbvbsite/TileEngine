package com.tileengine.dialog;

import com.tileengine.logic.Logic;
import java.util.ArrayList;

public class DialogPage
{
    /**
     * Dialog Page Name
     */
    private String dialogPageName;

    /**
     * Dialog Text
     */
    private String dialogPageText;

    /*
     * Options Logic
     */
    private ArrayList logicObjects;
    
    /**
     * Dialog Next Page
     */
    private String dialogNextPage = null;

    /*
     * Options success page
     */
    private String successPage;

    /*
     * Options fail page
     */
    private String failPage;

    /**
     * Dialog Page Options
     */
    private ArrayList dialogOptions;

    /**
     * Creates a dialog page with no options
     *
     * @param dialogPageName Page name for the Dialog Page
     * @param dialogText Text for the Dialog Page
     * @param dialogNextPage Next page name
     */
    public DialogPage(String dialogPageName, String dialogText, String dialogNextPage)
    {
        //Set Page Name
        setPageName(dialogPageName);

        //Set Page Text
        setPageText(dialogText);

        //Set Next Page
        setNextPage(dialogNextPage);
    }

    /**
     * Creates a dialog page with Success anf Fail Pages
     *
     * @param dialogPageName Page name for the Dialog Page
     * @param dialogText Text for the Dialog Page
     * @param dialogSuccessPage Success page name
     * @param dialogFailPage Fail page name
     */
    public DialogPage(String dialogPageName, String dialogText, String dialogSuccessPage, String dialogFailPage)
    {
        //Set Page Name
        setPageName(dialogPageName);

        //Set Page Text
        setPageText(dialogText);

        //Set Success Page
        setSuccessPage(dialogSuccessPage);

        //Set Fail Page
        setFailPage(dialogFailPage);
    }

    /**
     * Creates a dialog page with options
     *
     * @param dialogPageName Page name for the Dialog Page
     * @param dialogText Text for the Dialog Page
     * @param dialogPageOptions Dialog Options for the page
     */
    public DialogPage(String dialogPageName, String dialogText, String dialogNextPage, DialogOption[] dialogPageOptions)
    {
        //Set Page Name
        setPageName(dialogPageName);

        //Set Page Text
        setPageText(dialogText);

        //Set Next Page
        setNextPage(dialogNextPage);

        //Add Dialog Options to Page
        for(int i=0; i<dialogPageOptions.length; i++)
        {
            addPageOption(dialogPageOptions[i]);
        }
    }

    /**
     * Sets the page name
     *
     * @param dialogPageName Page name for the Dialog Page
     */
    public void setPageName(String dialogPageName)
    {
        this.dialogPageName = dialogPageName;
    }

    /**
     * Gets the page name
     *
     * @return Page name of the Dialog Page
     */
    public String getPageName()
    {
        return dialogPageName;
    }

    /**
     * Sets the next page
     *
     * @param dialogNextPage next page in the dialog
     */
    public void setNextPage(String dialogNextPage)
    {
        this.dialogNextPage = dialogNextPage;
    }

    /**
     * Gets the page name
     *
     * @return Page name of the Dialog Page
     */
    public String getNextPage()
    {
        return dialogNextPage;
    }

    /**
     * Sets the pages text
     *
     * @param dialogPageText Text for the Dialog Page
     */
    public void setPageText(String dialogPageText)
    {
        this.dialogPageText = dialogPageText;
    }

    /**
     * Gets the pages text
     *
     * @return Returns the pages text
     */
    public String getPageText()
    {
        return dialogPageText;
    }

    /**
     * Set the fail success page
     *
     * @param successPage Success page for page
     */
    public void setSuccessPage(String successPage)
    {
        this.successPage = successPage;
    }

    /**
     * Gets the success page for page
     *
     * @return Returns the options success page
     */
    public String getSuccessPage()
    {
        return successPage;
    }

    /**
     * Set the page fail page
     *
     * @param successPage Success page for page
     */
    public void setFailPage(String failPage)
    {
        this.failPage = failPage;
    }

    /**
     * Gets the fail page for page
     *
     * @return Returns the fail page
     */
    public String getFailPage()
    {
        return failPage;
    }
    
    /**
     * Adds a page option to the Dialog Page
     *
     * @param dialogOption Dialog option to add to page
     */
    public void addPageOption(DialogOption dialogOption)
    {
        //Create HashMap is null
        if(dialogOptions == null)
        {
            dialogOptions = new ArrayList();
        }

        //Add Dialog OPtion to Page
        dialogOptions.add(dialogOption);
    }

    /**
     * Gets all the options of the page
     *
     * @return Return all the Dialog Options
     */
    public DialogOption[] getPageOptions()
    {
        DialogOption[] pageOptions = null;

        if(dialogOptions != null)
        {
            pageOptions = new DialogOption[dialogOptions.size()];
            dialogOptions.toArray(pageOptions);
        }

        return pageOptions;
    }

    /**
     * Get a specific option from the dialog page
     *
     * @param optionName Name of the option to get
     * @return Return the request DialogOption
     */
    public DialogOption getPageOption(int index)
    {
        return (DialogOption) dialogOptions.get(index);
    }

    /**
     * Adds logic object to DialogPage
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
     * Get all the logic object for Dialog Page
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
