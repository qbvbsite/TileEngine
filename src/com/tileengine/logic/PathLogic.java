package com.tileengine.logic;

import com.tileengine.GameController;
import com.tileengine.ResourceController;
import com.tileengine.gamestate.GameState;
import com.tileengine.object.Direction;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;
import com.tileengine.pathing.Path;
import com.tileengine.pathing.PathGroup;
import com.tileengine.pathing.PathSearchNode;
import com.tileengine.pathing.PathSection;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PathLogic extends AbstractLogic 
{
    private Path objectPath;
  
    public boolean executeLogic(GameController gameController, GameState gameState, GameObject caller, GameObject initiator, int timeElapsed)
    {     
        Object[] parameters = getParameters();
        
        if(parameters != null && parameters.length == 1 && caller instanceof MoveableObject)
        {
            //Load Path if Needed
            if(objectPath == null && parameters[0] != null)
            {
                objectPath = loadPath((String) parameters[0]);
            }
          
            //Check Path is not Checked
            //Move Object
            //If Collide with Game Object Go Around to the right
            //Once Around Generate Temp path to end point
            //Once Reached ewnd point remove temp path
            
            MoveableObject moveableObject = (MoveableObject) caller;

            int currentX = moveableObject.getX();
            int currentY = moveableObject.getY();
            int direction = moveableObject.getFacingDirection();

            if (objectPath != null) 
            {
                //Get Currert Direction
                direction = objectPath.getCurrentDirection();
                
                //Get Current Section
                PathSection currentSection = objectPath.getCurrentSection();
                
                //Check to see if it has been calculated
                if(!currentSection.getCalculated())
                {
                    //Pause Time for Last Node
                    int pauseTime = currentSection.getPauseTime();
                    
                    //Path not Calculated so calculate path (Based on Object because se is at thenext setion)
                    PathSearchNode searchNode = objectPath.calculatePath(gameController, caller, currentSection.getEndX(), currentSection.getEndY());
                    
                    //Remove Current Section and Rebuild Path with Path Search Node
                    PathGroup currentPathGroup = objectPath.getCurrentPathGroup();
                    int currentSectionIndex = currentPathGroup.currentPathIndex();
                    currentPathGroup.removeSection(currentSection);
                    
                    while(searchNode.getParentNode() != null)
                    {
                        //Create New Path Section
                        currentSection = new PathSection(searchNode.getX() * gameController.getTileSize(), searchNode.getY() * gameController.getTileSize(), pauseTime, currentSection.getSpeed(), true);
                        
                        //Add Section to Current Index
                        currentPathGroup.addSection(currentSection, currentSectionIndex);
                                                
                        //Clear Pause Time
                        pauseTime = 0;
                        
                        //Goto Next Node
                        searchNode = searchNode.getParentNode();
                    }
                    
                    //Setup Direction
                    direction = objectPath.setDirection(currentX, currentY);
                    moveableObject.setFacingDirection(direction);
                }

                //Get End Point
                int[] end = currentSection.endPoint();
                
                if (objectPath.getPause() > 0) 
                {
                    objectPath.reducePauseTime(timeElapsed);
                    moveableObject.setMoving(false);
                } 
                else 
                {
                    boolean onPath = checkOnPath(currentX, currentY, objectPath.getCurrentSection(), direction);
                    
                    if(end[0] == moveableObject.getX() && end[1] == moveableObject.getY())
                    {
                        //Pause if Needed
                        objectPath.setPause(objectPath.getCurrentSection().getPauseTime() * 1000);
                        
                        //Goto Next Section
                        objectPath.next(); 
                        
                        //Setup Direction of Object
                        direction = objectPath.setDirection(currentX, currentY);
                        moveableObject.setFacingDirection(direction);
                        
                        //Clear Buffer For Next Section
                        moveableObject.setMovementBufferX(0);
                        moveableObject.setMovementBufferY(0);
                    }
                    else if(onPath)
                    {                
                        moveableObject.setMoving(true);
                        
                        gameController.moveObject(moveableObject, direction, timeElapsed);   
                        
                        //Object Didn't Move
                        if(currentX == moveableObject.getX() && currentY == moveableObject.getY())
                        {
                            
                        }
                    }
                    else
                    {
                        //Off Path Need to get on Path
                        //objectPath.calculatePath(gameController, moveableObject, currentX, currentY);
                    }
                }
            }  

            return true;
        }
        
        return false;
    }

    public boolean checkOnPath(int x, int y, PathSection section, int currentDirection)
    {
        boolean onPath = false;

        int[] currentEndPoint = section.endPoint();

        switch(currentDirection)
        {
           case Direction.DIRECTION_EAST:
           { 
               if(x < currentEndPoint[0])
               {
                   onPath = true;
               }

               break;
           }

           case Direction.DIRECTION_NORTH: 
           { 
               if (y > currentEndPoint[1])
               {
                   onPath = true;
               }

               break;
           }

           case Direction.DIRECTION_NORTHEAST:
           {
                if (x < currentEndPoint[0] && y > currentEndPoint[1])
                {
                    onPath = true;
                }

                break;
           }

           case Direction.DIRECTION_NORTHWEST:
           {
               if (x > currentEndPoint[0] && y > currentEndPoint[1]) 
               {
                    onPath = true;
               }  

               break;
           }  

           case Direction.DIRECTION_SOUTH: 
           {
               if (y < currentEndPoint[1])
               {
                   onPath = true;
               }

               break;
           }

           case Direction.DIRECTION_SOUTHEAST:
           {
               if (x < currentEndPoint[0] && y < currentEndPoint[1])
               {
                    onPath = true;
               }

               break;
           }

           case Direction.DIRECTION_SOUTHWEST:
           {
               if (x > currentEndPoint[0] && y < currentEndPoint[1])
               {
                   onPath = true;
               }

               break;
           }

           case Direction.DIRECTION_WEST:
           {
               if (x > currentEndPoint[0])
               {
                   onPath = true;
               }

               break;
           }
        }

        return onPath;
    }

    public Path loadPath(String resourceName)
    {
        ResourceController resourceController = ResourceController.getInstance();
        
        Node pathRootNode = null;
        Document xmlConfiguration = resourceController.parseXmlFile(resourceController.getResourceURL(resourceName).toExternalForm(), false);
        pathRootNode = xmlConfiguration.getDocumentElement();

        Path path = null;

        if (pathRootNode.getNodeName().equals("Path"))
        {
               /*
                * Need a start postion for the path
                */
               int startX = Integer.parseInt(pathRootNode.getAttributes().getNamedItem("startX").getNodeValue());
               int startY = Integer.parseInt(pathRootNode.getAttributes().getNamedItem("startY").getNodeValue());

               NodeList groups =  pathRootNode.getChildNodes();
               ArrayList pathGroups = new ArrayList();

               /*
                * Loop through the groups
                */
               for(int i=1; i<groups.getLength(); i=i+2)
               {                   
                    /*
                     * How many times will this group repeat
                     */
                    int group_repeat = Integer.parseInt(groups.item(i).getAttributes().getNamedItem("repeat").getNodeValue());


                    NodeList sections = groups.item(i).getChildNodes();
                    ArrayList path_sections = new ArrayList();

                    for (int ii=1; ii<sections.getLength(); ii=ii+2) {

                        int speed = Integer.parseInt(sections.item(ii).getAttributes().getNamedItem("speed").getNodeValue());
                        int moveToX = Integer.parseInt(sections.item(ii).getAttributes().getNamedItem("moveToX").getNodeValue());
                        int moveToY = Integer.parseInt(sections.item(ii).getAttributes().getNamedItem("moveToY").getNodeValue());
                        int pause = Integer.parseInt(sections.item(ii).getAttributes().getNamedItem("pause").getNodeValue());
                        boolean calculated = false;
                        if(sections.item(ii).getAttributes().getNamedItem("calculated") != null)
                        {
                            calculated = Boolean.parseBoolean(sections.item(ii).getAttributes().getNamedItem("calculated").getNodeValue());
                        }
                        
                        path_sections.add(new PathSection(moveToX, moveToY, pause, speed, calculated));

                    }

                    pathGroups.add(new PathGroup(path_sections, group_repeat));
               }

               path = new Path(pathGroups, startX, startY);
               path.setDirection(startX, startY);
        }

        return path;
    }
}
