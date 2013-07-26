package com.tileengine;

import com.tileengine.audio.AudioClip;
import com.tileengine.audio.AudioMIDIClip;
import com.tileengine.audio.AudioWAVClip;
import com.tileengine.gamestate.GameState;
import com.tileengine.logic.Logic;
import com.tileengine.object.GameObject;
import com.tileengine.object.MoveableObject;
import com.tileengine.object.Sprite;
import com.tileengine.object.SpriteObject;
import com.tileengine.object.TileSet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This controller is used to handle all the resources for the engine
 *
 * @author James Keir
 */
public class ResourceController
{
    private static ResourceController soleInstance = null;

    /*
     * Used to store/cache all images
     */
    private static HashMap images = new HashMap();

    /*
     * Used to store/cache all audio clips
     */
    private static HashMap audioClips = new HashMap();

    /**
     * Act as a singleton and prevent instancation
     */
    private ResourceController()
    {}

    /**
     * Returns a InputController Instance
     *
     * @return Instance of Input Controller
     */
    public static ResourceController getInstance()
    {
        //Check to See if an Instance has been created
        if (soleInstance == null)
        {
            //Instance doen't exist create on
            soleInstance = new ResourceController();
        }

        //Return InputController Instance
        return soleInstance;
    }

    /**
     * Loads/Retrieves and image based on resource name
     *
     * @param resourceName Resource/Location of image
     * @return Return the loaded/retrieved image
     */
    public Image loadImage(String resourceName)
    {
        //Check image has for image
        Image image = (Image) images.get(resourceName);

        //If you didn't find it in the has load it from file system
        if(image == null)
        {
            //Load Image
            image = new ImageIcon(getResourceURL(resourceName)).getImage();

            //Store in Hash
            images.put(resourceName, image);
        }

        //Return Image
        return image;
    }

    /**
     * Load image and make a color on it transparent (Used to make a background transparent)
     *
     * @param resourceName Resource/Location of image
     * @param tranparentColor Color to make transparent
     * @return Return the loaded/retrieved image
     */
    public Image loadImage(String resourceName, Color tranparentColor)
    {
        Image image = null;

        //If we are provided with a Color
        if(tranparentColor != null)
        {
            //Attempt to load image from hash
            image = (Image) images.get(resourceName);

            //If Image was in hash load it from file system
            if(image == null)
            {
                try
                {
                    //Load Image
                    BufferedImage bufferedImage = ImageIO.read(getResourceURL(resourceName));

                    //Make color on image Transparent
                    image = makeColorTransparent(bufferedImage, tranparentColor);

                    //Store image in hash
                    images.put(resourceName, image);
                }
                catch (Exception e)
                {}
            }
        }
        else
        {
            //No Transparent Color Get Image The Normal Way
            image = loadImage(resourceName);
        }

        //Return image
        return image;
    }

    /**
     * Makes a color on an image transparent
     *
     * @param bufferImage Image to process
     * @param color Color to make transparent
     * @return Return the processed image
     */
    private Image makeColorTransparent(BufferedImage bufferImage, final Color color)
    {
        //Create RGB Filter
        ImageFilter filter = new RGBImageFilter()
        {
            //The color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb)
            {
                if ((rgb | 0xFF000000) == markerRGB)
                {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                }
                else
                {
                    // nothing to do
                    return rgb;
                }
            }
        };

        //Apply Filter to Image
        ImageProducer imageProducer = new FilteredImageSource(bufferImage.getSource(), filter);

        //Return Processed Image
        return Toolkit.getDefaultToolkit().createImage(imageProducer);
    }

    /**
     * Loaded and parses a Sprite Image
     *
     * @param resourceName Sprite image resource/location
     * @param width Number of frames per set
     * @param height Number of sets
     * @param tileSize Size of tiles
     * @return Returns an image arry of the cut up sprite sheet
     */
    public Image[][] loadSpriteImage(String resourceName, int width, int height, int tileSize)
    {
        //Look in hash for sprite
        Image[][] spriteImages = (Image[][]) images.get(resourceName);

        //Didn't Find Sprite load it from file system
        if(spriteImages == null)
        {
            //Get Sprite Sheet
            Image spriteSetImage = new ImageIcon(getResourceURL(resourceName)).getImage();

            //Create Sprite Array
            spriteImages = new BufferedImage[height][width];

            //Spilt up Sprite Image
            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    //Create Buffer image for single frame
                    BufferedImage spriteImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);

                    //Split Out a single frame
                    Graphics2D g = spriteImage.createGraphics();
                    g.drawImage(spriteSetImage, 0, 0, tileSize, tileSize, tileSize*x, tileSize*y, tileSize*x+tileSize, tileSize*y+tileSize, null);
                    g.dispose();

                    //Store Frame into Sprite Array
                    spriteImages[y][x] = spriteImage;
                }
            }

            //Store Sprite Array in Hash
            images.put(resourceName, spriteImages);
        }

        //Return Sprite Image Array
        return spriteImages;
    }

    /**
     * Used to load a map file (XML) and return the GameController for it
     *
     * @param resourceName Map you wish to load
     * @param gameState Game state loading the map
     * @return Return the created game controller
     */
    public GameController loadMap(String resourceName, GameState gameState)
    {

        GameController gameController = null;

        //Get Map Node
        Node mapRootNode = null;
        Document xmlConfiguration = parseXmlFile(getResourceURL(resourceName).toExternalForm(), false);
        mapRootNode = xmlConfiguration.getDocumentElement();

        //Store Game Objects
        ArrayList gameObjects = new ArrayList();

        //Make sure Root Node is a Map
        if(mapRootNode.getNodeName().equals("Map"))
        {
            //Get Game Object Node
            Node gameObjectBaseNode = mapRootNode.getFirstChild().getNextSibling();

            //Load Game Objects
            if(gameObjectBaseNode.getNodeName().equals("GameObjects"))
            {
                //Get All Game Objects
                NodeList gameObjectNodes = gameObjectBaseNode.getChildNodes();

                //Load all Objects
                for(int i=1; i<gameObjectNodes.getLength(); i=i+2)
                {
                    //Retrive Game Object
                    Node gameObjectNode = gameObjectNodes.item(i);
                    String nodeName = gameObjectNode.getNodeName();

                    //Get Attributes
                    NamedNodeMap gameObjectAttributes = gameObjectNode.getAttributes();

                    //Used to store GameObject
                    GameObject gameObject = null;

                    //Load all Common Attributes for GameObjects
                    int index = Integer.parseInt(gameObjectAttributes.getNamedItem("index").getNodeValue());
                    String id = gameObjectAttributes.getNamedItem("id").getNodeValue();
                    int x = Integer.parseInt(gameObjectAttributes.getNamedItem("x").getNodeValue());
                    int y = Integer.parseInt(gameObjectAttributes.getNamedItem("y").getNodeValue());
                    int width = Integer.parseInt(gameObjectAttributes.getNamedItem("width").getNodeValue());
                    int height = Integer.parseInt(gameObjectAttributes.getNamedItem("height").getNodeValue());
                    boolean isPenetrable = false;
                    if(gameObjectAttributes.getNamedItem("isPenetrable") != null)
                    {
                        isPenetrable = Boolean.parseBoolean(gameObjectAttributes.getNamedItem("isPenetrable").getNodeValue());
                    }

                    Color alpha = null;

                    if(gameObjectAttributes.getNamedItem("alpha") != null)
                    {
                        String[] alphaColors = gameObjectAttributes.getNamedItem("alpha").getNodeValue().split(",");
                        alpha = new Color(Integer.parseInt(alphaColors[0]), Integer.parseInt(alphaColors[1]), Integer.parseInt(alphaColors[2]));
                    }

                    String objectClass = null;
                    if(gameObjectAttributes.getNamedItem("objectClass") != null)
                    {
                        objectClass = gameObjectAttributes.getNamedItem("objectClass").getNodeValue();
                    }
                    
                    //Load Game Object
                    if(objectClass == null)
                    {
                        if(nodeName.equals("GameObject"))
                        {
                            //Load Image for Game Object
                            Image image = loadImage(gameObjectAttributes.getNamedItem("image").getNodeValue(), alpha);

                            //Create GameObject
                            gameObject = new GameObject(id, image, x, y, width, height, isPenetrable);
                        }
                        else if(nodeName.equals("SpriteObject"))
                        {
                            //Load Sprite for Sprite Object
                            int spriteSets = Integer.parseInt(gameObjectAttributes.getNamedItem("spriteSets").getNodeValue());
                            int spriteFrames = Integer.parseInt(gameObjectAttributes.getNamedItem("spriteFrames").getNodeValue());
                            Image[][] spriteImages = loadSpriteImage(gameObjectAttributes.getNamedItem("image").getNodeValue(), spriteFrames, spriteSets, width);
                            int spriteLoops = Integer.parseInt(gameObjectAttributes.getNamedItem("spriteLoops").getNodeValue());

                            //Create Sprite Object
                            SpriteObject spriteObject = new SpriteObject(id, spriteImages, x, y, width, height, isPenetrable, spriteLoops);

                            //Set isAnimated for SpriteObject
                            if(gameObjectAttributes.getNamedItem("isAnimated") != null)
                            {
                                boolean isAnimated = Boolean.parseBoolean(gameObjectAttributes.getNamedItem("isAnimated").getNodeValue());
                                spriteObject.setIsAnimated(isAnimated);
                            }

                            //Store SpriteObject into GameObject
                            gameObject = spriteObject;
                        }
                        else if(nodeName.equals("MoveableObject"))
                        {
                            //Set Movement Speed for Moveable Object
                            int movementSpeed = Integer.parseInt(gameObjectAttributes.getNamedItem("movementSpeed").getNodeValue());

                            //See if its a Sprite
                            if(gameObjectAttributes.getNamedItem("isSprite") == null)
                            {
                                //Load Image for Moveable Object
                                Image image = loadImage(gameObjectAttributes.getNamedItem("image").getNodeValue(), alpha);

                                //Create Non-Sprite Moveable Object
                                gameObject = new MoveableObject(id, image, x, y, width, height, isPenetrable, movementSpeed);

                            }
                            else
                            {
                                //Load Sprite for Moveable Object
                                int spriteSets = Integer.parseInt(gameObjectAttributes.getNamedItem("spriteSets").getNodeValue());
                                int spriteFrames = Integer.parseInt(gameObjectAttributes.getNamedItem("spriteFrames").getNodeValue());
                                Image[][] spriteImages = loadSpriteImage(gameObjectAttributes.getNamedItem("image").getNodeValue(), spriteFrames, spriteSets, width);
                                int spriteLoops = Integer.parseInt(gameObjectAttributes.getNamedItem("spriteLoops").getNodeValue());

                                //Set Sprite Type
                                int type = Sprite.SPRITE_4D;
                                if(gameObjectAttributes.getNamedItem("spriteType").getNodeValue().equals("8D"))
                                {
                                    type = Sprite.SPRITE_8D;
                                }

                                //Create Sprite Moveable Object
                                gameObject = new MoveableObject(id, spriteImages, x, y, width, height, isPenetrable, movementSpeed, spriteLoops, type);
                            }
                        }
                    }
                    else
                    {
                        //Try to Load With Custom Resource Class
                        try
                        {
                            Class gameObjectClass = Class.forName(objectClass);
                            Constructor gameObjectConstructor = gameObjectClass.getDeclaredConstructor(new Class[] {});
                            Object gameObjectLoader = (Object) gameObjectConstructor.newInstance(new Object[] {});
                            
                            if(gameObjectLoader instanceof ResourceObjectLoader)
                            {
                                gameObject = ((ResourceObjectLoader) gameObjectLoader).executeLogic(gameObjectAttributes, index, id, x, y, width, height, isPenetrable, alpha);
                            }
                        }
                        catch(Exception e)
                        {}
                    }

                    //If an Object Was Created
                    if(gameObject != null)
                    {
                        //Set Z Layer
                        if(gameObjectAttributes.getNamedItem("z") != null)
                        {
                            int z = Integer.parseInt(gameObjectAttributes.getNamedItem("z").getNodeValue());
                            gameObject.setZ(z);
                        }

                        //Load all Logic Attached to GameObject
                        if(gameObjectNode.hasChildNodes())
                        {
                            NodeList gameLogicNodes = gameObjectNode.getChildNodes();

                            for(int h=1; h<gameLogicNodes.getLength(); h=h+2)
                            {
                                //Logic Node
                                Node gameLogicNode = gameLogicNodes.item(h);

                                //Make Sure its a Logic Node
                                if(gameLogicNode.getNodeName().equals("Logic"))
                                {
                                    Logic logicObject = loadLogicNode(gameLogicNode);

                                    if(logicObject != null)
                                    {
                                        //Get Attributed
                                        NamedNodeMap gameLogicNodeAttributes = gameLogicNode.getAttributes();

                                        //Get Logic Type (General or Collide)
                                        String logicTypeString = gameLogicNodeAttributes.getNamedItem("type").getNodeValue();
                                        int logicType = Logic.LOGIC_TYPE_GENERAL;
                                        if(logicTypeString.equals("Collide"))
                                        {
                                            logicType = Logic.LOGIC_TYPE_COLLIDE;
                                        }
                                        else if(logicTypeString.equals("Custom"))
                                        {
                                            logicType = Logic.LOGIC_TYPE_CUSTOM;
                                        }

                                        //Add Logic Object to Game Object
                                        gameObject.addGameLogic(logicType, logicObject);
                                    }
                                }
                            }
                        }

                        //Add GameObject to Objects Array
                        gameObjects.add(index, gameObject);
                    }
                }
            }

            //Get Map Area
            Node gameMapAreaBaseNode = gameObjectBaseNode.getNextSibling().getNextSibling();

            //Make sure its a Map Area Node
            if(gameMapAreaBaseNode.getNodeName().equals("MapArea"))
            {
                //Map Area Attributes
                NamedNodeMap mapAreaAttributes = gameMapAreaBaseNode.getAttributes();

                //Set Map Area Attributes
                int mapWidth = Integer.parseInt(mapAreaAttributes.getNamedItem("mapWidth").getNodeValue());
                int mapHeight = Integer.parseInt(mapAreaAttributes.getNamedItem("mapHeight").getNodeValue());
                int renderWidth = Integer.parseInt(mapAreaAttributes.getNamedItem("renderWidth").getNodeValue());
                int renderHeight = Integer.parseInt(mapAreaAttributes.getNamedItem("renderHeight").getNodeValue());
                int layers = Integer.parseInt(mapAreaAttributes.getNamedItem("layers").getNodeValue());
                int scrollBuffer = 0;

                //Set Scroll Buffer if Set
                if(mapAreaAttributes.getNamedItem("scrollBuffer") != null)
                {
                    scrollBuffer = Integer.parseInt(mapAreaAttributes.getNamedItem("scrollBuffer").getNodeValue());
                }

                //Load Hero Object from Game Object Array if Set
                MoveableObject heroObject = null;                
                if(mapAreaAttributes.getNamedItem("heroObject") != null)
                {
                    heroObject = (MoveableObject) gameObjects.get(Integer.parseInt(mapAreaAttributes.getNamedItem("heroObject").getNodeValue()));
                }

                //Create Tile Set Object
                TileSet tileSet = new TileSet();

                //Get Tile Resource Node
                Node tileResourceBaseNode = gameMapAreaBaseNode.getFirstChild().getNextSibling();

                //Make sure its a Tile Resource Node
                if(tileResourceBaseNode.getNodeName().equals("TileResources"))
                {
                    //Get All Tile Resource Objects
                    NodeList tileNodes = tileResourceBaseNode.getChildNodes();

                    //Load All Tile Resource
                    for(int x=1; x<tileNodes.getLength(); x=x+2)
                    {
                        //Retrive Tile Resource Node
                        Node tileNode = tileNodes.item(x);

                        //Get Tile Resource Attributes
                        NamedNodeMap tileAttributes = tileNode.getAttributes();

                        //Set Tile Resource Attributes
                        String index = tileAttributes.getNamedItem("index").getNodeValue();
                        Image image = loadImage(tileAttributes.getNamedItem("image").getNodeValue());

                        //Set Up Boundries if Any
                        if(tileAttributes.getNamedItem("walkThroughTop") != null && tileAttributes.getNamedItem("walkThroughRight") != null &&
                                tileAttributes.getNamedItem("walkThroughBottom") != null && tileAttributes.getNamedItem("walkThroughLeft") != null)
                        {
                                boolean walkThroughTop = Boolean.parseBoolean(tileAttributes.getNamedItem("walkThroughTop").getNodeValue());
                                boolean walkThroughRight = Boolean.parseBoolean(tileAttributes.getNamedItem("walkThroughRight").getNodeValue());
                                boolean walkThroughBottom = Boolean.parseBoolean(tileAttributes.getNamedItem("walkThroughBottom").getNodeValue());
                                boolean walkThroughLeft = Boolean.parseBoolean(tileAttributes.getNamedItem("walkThroughLeft").getNodeValue());

                                //Add Tile Resource to Tile Set
                                tileSet.addTileResource(index, image, walkThroughTop, walkThroughRight, walkThroughBottom, walkThroughLeft);
                        }
                        else
                        {
                            //Add Tile Resource to Tile Set
                            tileSet.addTileResource(index, image);
                        }
                    }
                }

                //Get Tile Set Node
                Node tileSetBaseNode = tileResourceBaseNode.getNextSibling().getNextSibling();

                //Make Sure its a TileSet Node
                if(tileSetBaseNode.getNodeName().equals("TileSet"))
                {
                    //Get Map String
                    Node tileSetTextNode = tileSetBaseNode.getFirstChild();
                    String mapString = tileSetTextNode.getTextContent();

                    //Get Map Tile Indexs from Map String
                    String[] mapTiles = mapString.split(",");

                    //Create Tile Area with mapHeight and mapWidth
                    String[][] tiles = new String[mapHeight][mapWidth];

                    int tileCounter = 0;

                    //Load Tile Set from Map String Array
                    for(int y=0; y<mapHeight; y++)
                    {
                        for(int x=0; x<mapWidth; x++)
                        {
                            //Make Null Tile
                            String tile = null;

                            //Trim White Space
                            String mapTile = mapTiles[tileCounter].trim();
                            if(!mapTile.equals(""))
                            {
                                //Store Tile Index
                                tile = mapTile;
                            }

                            //Set Tile
                            tiles[y][x] = tile;

                            tileCounter++;
                        }
                    }

                    //Add Tiles to Tile Set
                    tileSet.setTiles(tiles);
                }

                //Setup Scroll if Enabled
                if(mapAreaAttributes.getNamedItem("screenSnapping") != null &&
                        Boolean.parseBoolean(mapAreaAttributes.getNamedItem("screenSnapping").getNodeValue()) != false)
                {
                    //Create GameController with Zelda Like Scrolling
                    gameController = new GameController(renderWidth, renderHeight, true, tileSet, layers, heroObject, gameState);
                }
                else if(mapAreaAttributes.getNamedItem("scrollX") != null)
                {
                    //Setup Scroll Box
                    int scrollX = Integer.parseInt(mapAreaAttributes.getNamedItem("scrollX").getNodeValue());
                    int scrollY = Integer.parseInt(mapAreaAttributes.getNamedItem("scrollY").getNodeValue());
                    int scrollWidth = Integer.parseInt(mapAreaAttributes.getNamedItem("scrollWidth").getNodeValue());
                    int scrollHeight = Integer.parseInt(mapAreaAttributes.getNamedItem("scrollHeight").getNodeValue());

                    //Create GameController with Scroll Box
                    gameController = new GameController(renderWidth, renderHeight, scrollX, scrollY, scrollWidth, scrollHeight, tileSet, layers, heroObject, gameState);
                }
                else
                {
                    //Create Game Controller with no Scrolling
                    gameController = new GameController(renderWidth, renderHeight, tileSet, layers, heroObject, gameState);
                }

                //Set Scroll Buffer of GameController
                gameController.setScrollBuffer(scrollBuffer);

                //Add Game Objects to Game Controller
                for(int i=0; i<gameObjects.size(); i++)
                {
                    gameController.addGameObject((GameObject) gameObjects.get(i));
                }
            }
        }

        //Return created game controller
        return gameController;
    }

    /**
     * Load the Logic node of an XML
     *
     * @param logicNode The logic Node
     * @return Returns the logic class
     */
    public Logic loadLogicNode(Node logicNode)
    {
        //Logic Return Object
        Logic logicObject = null;

        //Get Logic Attributes
        NamedNodeMap logicAttributes = logicNode.getAttributes();

        //Create Logic Object
        try
        {
            Class logicClass = Class.forName(logicAttributes.getNamedItem("class").getNodeValue());
            Constructor logicConstructor = logicClass.getDeclaredConstructor(new Class[] {});
            logicObject = (Logic) logicConstructor.newInstance(new Object[] {});

            //Get Parameters
            if(logicNode.hasChildNodes())
            {
                    //Load Parameters
                NodeList logicParameters = logicNode.getChildNodes();

                //Used to Store Parameters
                ArrayList objectParameters = new ArrayList();

                //Loop Through Parameters
                for(int k=1; k<logicParameters.getLength(); k=k+2)
                {
                    //Parameter Node
                    Node logicParameter = logicParameters.item(k);

                    //Make sure its a Parameter
                    if(logicParameter.getNodeName().equals("Parameter"))
                    {
                        //Load Parameter Attrobutes
                        NamedNodeMap paramNodeAttributes = logicParameter.getAttributes();

                        //Get Parameter Type
                        String paramType = paramNodeAttributes.getNamedItem("type").getNodeValue();

                        //Get Parameter Value
                        Object paramValue = paramNodeAttributes.getNamedItem("value").getNodeValue();

                        //Parse Value Based on Type
                        if(paramType.equals("Integer"))
                        {
                            paramValue = Integer.parseInt((String) paramValue);
                        }
                        else if(paramType.equals("Boolean"))
                        {
                            paramValue = Boolean.parseBoolean((String) paramValue);
                        }

                        //Add Parmeters to Parameter Array
                        objectParameters.add(paramValue);
                    }
                }

                //Add Parmeters to Logic Object
                logicObject.setParameters(objectParameters.toArray());
            }
        }
        catch(Exception e)
        {}

        return logicObject;
    }


    /**
     * Load
     * @param resourceName
     * @return
     */
    public AudioClip loadAudio(String resourceName)
    {
        //Check hash for audio clip
        AudioClip audioClip = (AudioClip) audioClips.get(resourceName);

        //If you didn't find it in the has load it from file system
        if(audioClip == null)
        {
            if(resourceName.endsWith(".wav"))
            {
                //Load WAV Audio Clip
                audioClip = new AudioWAVClip(getResourceURL(resourceName));
            }
            else if(resourceName.endsWith(".mid"))
            {
                //Load MIDI Audio Clip
                audioClip = new AudioMIDIClip(getResourceURL(resourceName));
            }
            
            //Store in Hash
            audioClips.put(resourceName, audioClip);
        }

        //Return Image
        return audioClip;
    }

    /**
     * Used to Parse XML Files
     *
     * @param xmlData XML File
     * @param xmlValidate To flag validation of the XML
     * @return Return the parsed XML Document
     */
    public Document parseXmlFile(String xmlData, boolean xmlValidate)
    {
	try
	{
	    // Create A Builder Factory
	    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	    documentBuilderFactory.setValidating(xmlValidate);
	    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

	    // Create The Builder And Parse The File
	    Document xmlDocument = null;
            xmlDocument = documentBuilder.parse(xmlData);

	    return xmlDocument;
	}
	catch(SAXException saxe)
	{
            System.out.println(saxe.toString());
        }
	catch(ParserConfigurationException pce)
	{
            System.out.println(pce.toString());
        }
	catch(IOException ioe)
	{
            System.out.println(ioe.toString());
        }

	return null;
    }

    /**
     * Create a Resource URL so it can work as a Applet and Appication
     * @param resourceName The resource name
     * @return The URL location of the resorce
     */
    public URL getResourceURL(String resourceName)
    {
        URL url = null;

        //Get URL From Subfolder
        url = this.getClass().getResource('/' + resourceName);
        if(url == null)
        {
            //Didnt Find it in subfolder try parent folder
            String noFolder = resourceName.substring(resourceName.lastIndexOf("/") + 1, resourceName.length());
            url = this.getClass().getResource('/' + noFolder);
        }

        //Return the URL of the resource
        return url;
    }
}
