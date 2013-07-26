
package com.tileengine.dialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Dialog object for Characters Dialog
 *
 * @author James Keir
 */
public class Dialog
{
    /*
     * Used to store Dialog Pages
     */
    private Map dialogPages = new HashMap();

    /*
     * Stores what dialog we are currently at
     */
    private String currentDialog = null;

    /*
     * Stores what dialog page to start the conversation at
     */
    private String startPage = null;

    /**
     * Create a basic dialog object with 1 page
     *
     * @param dialogPage Dialog Page
     */
    public Dialog(DialogPage dialogPage)
    {
        //Add Dialog Page
        addDialogPage(dialogPage);

        //Make it the starting Dialog page
        setCurrentDialog(dialogPage.getPageName());

        //Set Start Page
        startPage = getCurrentDialog();
    }

    /**
     * Creates a Dialog object with many Dialog Pages
     *
     * @param dialogPages Pages for dialog
     * @param currentDialog Starting dialog page
     */
    public Dialog(DialogPage[] dialogPages, String currentDialog)
    {
        //Add Pages to Dialog Object
        for(int i=0; i<dialogPages.length; i++)
        {
            addDialogPage(dialogPages[i]);
        }

        //Set Dialog Start Page
        setCurrentDialog(currentDialog);

        //Set Start Page
        startPage = getCurrentDialog();
    }

    /**
     * Adds a dialog aoge to the Dialog Object
     *
     * @param dialogPage Dialog page to add
     */
    public void addDialogPage(DialogPage dialogPage)
    {
        //Create HashMap if null
        if(dialogPages == null)
        {
            dialogPages = new HashMap();
        }

        //Add Dialog page to Dialog Object
        dialogPages.put(dialogPage.getPageName(), dialogPage);
    }

    /**
     * Gets all the Dialog Pages
     *
     * @return Return an array of Dialog Pages
     */
    public DialogPage[] getDialogPages()
    {
        return (DialogPage[]) dialogPages.values().toArray();
    }

    /**
     * Gets a single dialog pages
     *
     * @param dialogPageName Name of dialog page to get
     * @return Return the dialog page requested
     */
    public DialogPage getDialogPage(String dialogPageName)
    {
        return (DialogPage) dialogPages.get(dialogPageName);
    }

    /**
     * The Current Dialog Page
     *
     * @param currentDialog Page name of starting Dialog Page
     */
    public void setCurrentDialog(String currentDialog)
    {
        this.currentDialog = currentDialog;
    }

    /**
     * Get the Current dialog pages name
     *
     * @return The current dialog page name
     */
    public String getCurrentDialog()
    {
        return currentDialog;
    }

     /**
     * Get the current page object
     *
     * @return The current page object
     */
    public DialogPage getCurrentPage()
    {
        return (DialogPage) dialogPages.get(currentDialog);
    }

    /**
     * Starts the conversation
     *
     * @return The starting dialog page
     */
    public DialogPage startDialog()
    {
        return (DialogPage) dialogPages.get(currentDialog);
    }

    /**
     * Resets the conversion back to the start
     */
    public void resetDialog()
    {
        setCurrentDialog(startPage);
    }
}
