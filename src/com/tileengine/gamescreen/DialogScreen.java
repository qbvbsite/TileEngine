package com.tileengine.gamescreen;

import com.tileengine.GameController;
import com.tileengine.InputController;
import com.tileengine.TileEngineCanvas;
import com.tileengine.dialog.Dialog;
import com.tileengine.dialog.DialogOption;
import com.tileengine.dialog.DialogPage;
import com.tileengine.gamestate.GameState;
import com.tileengine.logic.Logic;
import com.tileengine.object.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class DialogScreen extends AbstractGameScreen
{
    private GameController gameController;
    private GameObject talker;
    private GameObject talkingObject;
    private Dialog dialog;
    private GameState parentGameState;
    private InputController inputController = InputController.getInstance();
    private boolean nextPageToggle = false;
    private int optionState = 0;
    private boolean optionUpToggle = false;
    private boolean optionDownToggle = false;
    private boolean leaveDialog = false;

    private Color white = new Color(255,255,255);
    private Color darkblue = new Color(0,40,240);
    private Color blue = new Color(140,240,250);

    private final static int NEXT_PAGE = 60;
    private final static int OPTION_UP = 61;
    private final static int OPTION_DOWN = 62;
    private final static int LEAVE_DIALOG = 63;

    public DialogScreen(GameController gameController, GameState parentGameState, GameObject talker, GameObject talkingObject, Dialog dialog)
    {
        this.gameController = gameController;
        this.talkingObject = talkingObject;
        this.parentGameState = parentGameState;
        this.dialog = dialog;
        
        inputController.setMapping(InputController.TYPE_KEYBOARD, LEAVE_DIALOG, KeyEvent.VK_T);
        inputController.setMapping(InputController.TYPE_KEYBOARD, NEXT_PAGE, KeyEvent.VK_ENTER);
        inputController.setMapping(InputController.TYPE_KEYBOARD, OPTION_UP, KeyEvent.VK_UP);
        inputController.setMapping(InputController.TYPE_KEYBOARD, OPTION_DOWN, KeyEvent.VK_DOWN);
        
        dialog.startDialog();
    }

    public boolean overrideProcessInput()
    {
        return true;
    }

    public boolean overridePreformLogic()
    {
        return true;
    }

    public void preformLogic(TileEngineCanvas parent, long timeElasped)
    {
        DialogPage currentDialogPage = dialog.getCurrentPage();

        //If Enter Is Pressed Goto Next Page or Execute Selection
        if(inputController.getState(NEXT_PAGE))
        {
            nextPageToggle = true;
        }
        else
        {
            if(nextPageToggle)
            {
                String nextPage = currentDialogPage.getNextPage();

                //Check to see if there are Options
                if(currentDialogPage.getPageOptions() == null)
                {
                    //Check to See if We Have a Next Page
                    if(nextPage == null)
                    {
                        //Check for Logic
                        Logic[] optionLogic = currentDialogPage.getLogicObjects();

                        if(optionLogic != null)
                        {
                            //Execute Logic
                            boolean logicPassed = executeDialogLogic(optionLogic, (int) timeElasped);

                            //Set Success/Fail Pages
                            if(logicPassed)
                            {
                                //Logic was Successful goto Success Page
                                nextPage = currentDialogPage.getSuccessPage();
                            }
                            else
                            {
                                //Logic Failed goto Failed Page
                                nextPage = currentDialogPage.getFailPage();
                            }
                        }
                        else
                        {
                            //No Logic End of Dialog
                            leaveDialog = true;
                        }
                    }
                }
                else
                {
                    //Get the Selected Option
                    DialogOption selectedOption = currentDialogPage.getPageOption(optionState);

                    //Check for Next Page
                    nextPage = selectedOption.getSelectPage();
                    if(nextPage == null)
                    {
                        //Check for Logic
                        Logic[] optionLogic = selectedOption.getLogicObjects();

                        if(optionLogic != null)
                        {
                            boolean logicPassed = executeDialogLogic(optionLogic, (int) timeElasped);

                            //Set Success/Fail Pages
                            if(logicPassed)
                            {
                                //Logic was Successful goto Success Page
                                nextPage = selectedOption.getSuccessPage();
                            }
                            else
                            {
                                //Logic Failed goto Failed Page
                                nextPage = selectedOption.getFailPage();
                            }
                        }
                        else
                        {
                            //No Logic for Dialog and no Next Page End Dialog (ill formated Dialog file most likely)
                            leaveDialog = true;
                        }
                    }
                }

                //Set Next Page
                dialog.setCurrentDialog(nextPage);

                //Reset Toggle
                nextPageToggle = false;
            }
        }

        //This Moves Selected Option Up
        if(inputController.getState(OPTION_UP))
        {
            optionUpToggle = true;
        }
        else if(optionUpToggle)
        {
            DialogOption[] pageOptions = currentDialogPage.getPageOptions();
            if(pageOptions != null)
            {
                optionState -= 1;

                if(optionState < 0)
                {
                    optionState = pageOptions.length - 1;
                }
            }

            optionUpToggle = false;
        }

        //This Moves Selected Option Down
        if(inputController.getState(OPTION_DOWN))
        {
            optionDownToggle = true;
        }
        else if(optionDownToggle)
        {
            DialogOption[] pageOptions = currentDialogPage.getPageOptions();
            if(pageOptions != null)
            {
                optionState += 1;

                if(optionState >= pageOptions.length)
                {
                    optionState = 0;
                }
            }

            optionDownToggle = false;
        }

        //Check to See If Dialog Is Over
        if(inputController.getState(LEAVE_DIALOG))
        {
            leaveDialog = true;
        }
        else if(leaveDialog)
        {
            //Dialog Over
            dialog.resetDialog();
            inputController.removeMapping(NEXT_PAGE);
            inputController.removeMapping(LEAVE_DIALOG);
            parentGameState.removeGameScreen(this);
        }
    }

    private boolean executeDialogLogic(Logic[] logicArray, int timeElapsed)
    {
        //No Next Page So It Must Have Success/Fail Logic
        boolean logicPassed = true;

        //Execute Each Logic
        for(int i=0; i<logicArray.length; i++)
        {
            //Execute Logic
            logicPassed = logicArray[i].executeLogic(gameController, parentGameState, talkingObject, talker, (int) timeElapsed);

            //Did Logic Pass
            if(!logicPassed)
            {
                //Logic Failed
                logicPassed = false;
            }
        }

        return logicPassed;
    }

    public void updateScreen(TileEngineCanvas parent, Graphics2D graphicBuffer, long timeElasped)
    {
        Color startColor = graphicBuffer.getColor();
        DialogPage currentDialogPage = dialog.getCurrentPage();

        DialogOption[] pageOptions = currentDialogPage.getPageOptions();
        int rectangleHeight = 1;
        if(pageOptions != null)
        {
            rectangleHeight = pageOptions.length;
        }

        //Offset
        int yOffset = 20 * rectangleHeight;

        //Draw Bubble
        graphicBuffer.setColor(white);
        graphicBuffer.fillRoundRect(talkingObject.getX() - gameController.getRenderOffsetX(), talkingObject.getY() - gameController.getRenderOffsetY() - (yOffset + 20), currentDialogPage.getPageText().length() * 8 + 4, (25 * rectangleHeight) + 4, 10, 10);
        graphicBuffer.setColor(blue);
        graphicBuffer.fillRoundRect(talkingObject.getX() - gameController.getRenderOffsetX() + 2, talkingObject.getY() - gameController.getRenderOffsetY() - (yOffset + 18), currentDialogPage.getPageText().length() * 8, 25 * rectangleHeight, 10, 10);
        graphicBuffer.setColor(startColor);

        //Display Page Text
        graphicBuffer.drawString(currentDialogPage.getPageText(),  talkingObject.getX() - gameController.getRenderOffsetX() + 10, talkingObject.getY() - gameController.getRenderOffsetY() - yOffset - 2);

        //Display Options
        if(pageOptions != null && pageOptions.length > 0)
        {
            for(int i=0; i<pageOptions.length; i++)
            {
                yOffset -= 15;

                if(i == optionState)
                {
                    graphicBuffer.setColor(darkblue);
                }
                else
                {
                    graphicBuffer.setColor(startColor);
                }

                graphicBuffer.drawString(pageOptions[i].getOptionText(),  talkingObject.getX() - gameController.getRenderOffsetX() + 20, talkingObject.getY() - gameController.getRenderOffsetY() - yOffset - 2);
            }
        }

        graphicBuffer.setColor(startColor);
    }
}
