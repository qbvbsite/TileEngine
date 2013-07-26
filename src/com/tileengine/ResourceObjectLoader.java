/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.tileengine;

import com.tileengine.object.GameObject;
import java.awt.Color;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author Jammer
 */
public interface ResourceObjectLoader
{
    public GameObject executeLogic(NamedNodeMap gameObjectAttributes, int index,
                String id, int x, int y, int width, int height, boolean isPenetrable, Color alpha);
}
