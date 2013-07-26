/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.tileengine;

import com.tileengine.object.GameObject;
import com.tileengine.object.Sprite;
import com.tileengine.object.Character;
import java.awt.Color;
import java.awt.Image;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author Jammer
 */
public class ResourceCharacterLoader implements ResourceObjectLoader
{
    public GameObject executeLogic(NamedNodeMap gameObjectAttributes, int index,
                String id, int x, int y, int width, int height, boolean isPenetrable, Color alpha)
    {
        //Get Resource Controller
        ResourceController resourceController = ResourceController.getInstance();
        
        //Game Object
        GameObject gameObject = null;
        
        //Set Movement Speed for Moveable Object
        int movementSpeed = Integer.parseInt(gameObjectAttributes.getNamedItem("movementSpeed").getNodeValue());

        //See if its a Sprite
        if(gameObjectAttributes.getNamedItem("isSprite") == null)
        {
            //Load Image for Moveable Object
            Image image = resourceController.loadImage(gameObjectAttributes.getNamedItem("image").getNodeValue(), alpha);

            //Create Non-Sprite Moveable Object
            Character characterObject = new Character(id, image, x, y, width, height, isPenetrable, movementSpeed);

            gameObject = characterObject;
        }
        else
        {
            //Load Sprite for Moveable Object
            int spriteSets = Integer.parseInt(gameObjectAttributes.getNamedItem("spriteSets").getNodeValue());
            int spriteFrames = Integer.parseInt(gameObjectAttributes.getNamedItem("spriteFrames").getNodeValue());
            Image[][] spriteImages = resourceController.loadSpriteImage(gameObjectAttributes.getNamedItem("image").getNodeValue(), spriteFrames, spriteSets, width);
            int spriteLoops = Integer.parseInt(gameObjectAttributes.getNamedItem("spriteLoops").getNodeValue());

            //Set Sprite Type
            int type = Sprite.SPRITE_4D;
            if(gameObjectAttributes.getNamedItem("spriteType").getNodeValue().equals("8D"))
            {
                type = Sprite.SPRITE_8D;
            }

            //Create Sprite Chacter Object
            Character characterObject = new Character(id, spriteImages, x, y, width, height, isPenetrable, movementSpeed, spriteLoops, type);

            gameObject = characterObject;
        }
        
        return gameObject;
    }
}
