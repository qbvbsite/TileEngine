/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.ResourceController;
import com.tileengine.dialog.Dialog;
import com.tileengine.dialog.DialogOption;
import com.tileengine.dialog.DialogPage;
import com.tileengine.gamescreen.DialogScreen;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jammer
 */
public class DialogLogic extends AbstractLogic 
{
    Dialog dialog = null;
    
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {     
        Object[] parameters = getParameters();

        if(parameters != null && parameters.length == 1)
        {
            //Load Path if Needed
            if(dialog == null && parameters[0] != null)
            {
                dialog = loadDialog((String) parameters[0]);
            }
            
            if(dialog != null)
            {
                //Stop Moving If Moveable
                if(initiator instanceof MoveableObject)
                {
                    ((MoveableObject) initiator).setMoving(false);
                }
                
                gameState.addGameScreen(new DialogScreen(gameController, gameState, initiator, caller, dialog));
            }
            return true;
        }
        
        return false;
    }
    
   /**
     * Loads a Dialog File
     *
     * @param dialogFile Dialog file to load
     * @return Return a Dialog object that was loaded
     */
    private Dialog loadDialog(String dialogFile)
    {        
        //ResourceController
        ResourceController resourceController = ResourceController.getInstance();
        
        //Used to store Dialog Object
        Dialog dialogObject = null;

        //Get Dialog Node
        Document xmlConfiguration = resourceController.parseXmlFile(resourceController.getResourceURL(dialogFile).toExternalForm(), false);
        Node dialogRootNode = xmlConfiguration.getDocumentElement();

        //Make sure its a Dialog Node and has Pages
        if(dialogRootNode.getNodeName().equals("Dialog") && dialogRootNode.hasChildNodes())
        {
            //Get Dialog Attributes
            NamedNodeMap dialogObjectAttributes = dialogRootNode.getAttributes();

            //Get Starting Dialog Page
            String dialogStart = dialogObjectAttributes.getNamedItem("nextPage").getNodeValue();

            //Get Dialog Pages
            NodeList dialogPageList = dialogRootNode.getChildNodes();
            ArrayList dialogPages = new ArrayList();
            
            //Load all Dialog Pages
            for(int i=1; i<dialogPageList.getLength(); i=i+2)
            {
                Node dialogPage = dialogPageList.item(i);

                //Make sure its a Page node
                if(dialogPage.getNodeName().equals("Page"))
                {
                    //Get Page Attributes
                    NamedNodeMap dialogPageAttributes = dialogPage.getAttributes();

                    //Set Page Attributes
                    String pageName = dialogPageAttributes.getNamedItem("name").getNodeValue();
                    String pageText = dialogPageAttributes.getNamedItem("text").getNodeValue();
                    String nextPage = null;

                    if(dialogPageAttributes.getNamedItem("nextPage") != null)
                    {
                        nextPage = dialogPageAttributes.getNamedItem("nextPage").getNodeValue();
                    }

                    String pageSuccessPage = null;
                    if(dialogPageAttributes.getNamedItem("successPage") != null)
                    {
                        pageSuccessPage = dialogPageAttributes.getNamedItem("successPage").getNodeValue();
                    }

                    String pageFailPage = null;
                    if(dialogPageAttributes.getNamedItem("failPage") != null)
                    {
                        pageFailPage = dialogPageAttributes.getNamedItem("failPage").getNodeValue();
                    }

                    //Create Dialog Page
                    DialogPage dialogPageObject = null;
                    
                    if(pageSuccessPage != null && pageFailPage != null)
                    {
                        dialogPageObject = new DialogPage(pageName, pageText, pageSuccessPage, pageFailPage);
                    }
                    else
                    {
                        dialogPageObject = new DialogPage(pageName, pageText, nextPage);
                    }

                    dialogPages.add(dialogPageObject);

                    //Check for Dialog Options
                    if(dialogPage.hasChildNodes())
                    {
                        //Get all Options
                        NodeList pageOptionLogicNodes = dialogPage.getChildNodes();

                        //Load Page Options
                        for(int x=1; x<pageOptionLogicNodes.getLength(); x=x+2)
                        {
                            Node pageOptionLogicNode = pageOptionLogicNodes.item(x);

                            //Make sure its an Option node
                            if(pageOptionLogicNode.getNodeName().equals("Option"))
                            {
                                //Get Option Attrobutes
                                NamedNodeMap dialogPageOptionAttributes = pageOptionLogicNode.getAttributes();
                                String optionNextPage = null;
                                if(dialogPageOptionAttributes.getNamedItem("nextPage") != null)
                                {
                                    optionNextPage = dialogPageOptionAttributes.getNamedItem("nextPage").getNodeValue();
                                }

                                String optionSuccessPage = null;
                                if(dialogPageOptionAttributes.getNamedItem("successPage") != null)
                                {
                                    optionSuccessPage = dialogPageOptionAttributes.getNamedItem("successPage").getNodeValue();
                                }

                                String optionFailPage = null;
                                if(dialogPageOptionAttributes.getNamedItem("failPage") != null)
                                {
                                    optionFailPage = dialogPageOptionAttributes.getNamedItem("failPage").getNodeValue();
                                }

                                String optionText = dialogPageOptionAttributes.getNamedItem("text").getNodeValue();

                                //Create Option
                                DialogOption option = null;

                                if(optionNextPage != null)
                                {
                                    option = new DialogOption(optionText, optionNextPage);
                                }
                                else if(optionSuccessPage != null && optionFailPage != null)
                                {
                                    option = new DialogOption(optionText, optionSuccessPage, optionFailPage);
                                }

                                if(option != null)
                                {
                                    //Load Logic If Any
                                    if(pageOptionLogicNode.hasChildNodes())
                                    {
                                        NodeList logicNodes = pageOptionLogicNode.getChildNodes();

                                        for(int l=1; l<logicNodes.getLength(); l=l+2)
                                        {
                                            //Logic Node
                                            Node logicNode = logicNodes.item(l);

                                            if(logicNode.getNodeName().equals("Logic"))
                                            {
                                                Logic logicObject = resourceController.loadLogicNode(logicNode);
                                                if(logicObject != null)
                                                {
                                                    option.addLogicObject(logicObject);
                                                }
                                            }
                                        }
                                    }

                                    //Add Option to Dialog Page
                                    dialogPageObject.addPageOption(option);
                                }
                            }
                            else if(pageOptionLogicNode.getNodeName().equals("Logic"))
                            {
                                Logic logicObject = resourceController.loadLogicNode(pageOptionLogicNode);
                                if(logicObject != null)
                                {
                                    dialogPageObject.addLogicObject(logicObject);
                                }
                            }
                        }
                    }
                }
            }

            //Create Dialog Object
            DialogPage[] dialogPageArray = new DialogPage[dialogPages.size()];
            dialogPages.toArray(dialogPageArray);
            dialogObject = new Dialog(dialogPageArray, dialogStart);
        }

        //Return Dialog Object
        return dialogObject;
    }
}
