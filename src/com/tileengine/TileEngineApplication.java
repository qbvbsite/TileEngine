package com.tileengine;

import java.awt.BufferCapabilities;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import javax.swing.JFrame;

/**
 * Tile Engine main class which loads the graphic device and runs the engine loop
 *
 * @author James Keir
 */
public class TileEngineApplication extends JFrame
{
    /**
     * Graphic Device
     */
    private GraphicsDevice graphicDevice;

    /**
     * Engine Settings
     */
    private String[] settings = null;

    /**
     * Screen Insets
     */
    private Insets insets = null;

    /**
     * Main class on the engine
     *
     * @param settings Settings for the Engine
     */
    public static void main(String[] settings)
    {
        //Graphic Varibles
        GraphicsEnvironment graphicEnv;
        GraphicsDevice[] graphicScreens;
        GraphicsDevice graphicScreen;
        GraphicsConfiguration[] graphicConfigurations;
        GraphicsConfiguration graphicConfiguration;
        BufferCapabilities bufferCapabilities;

        //Load Graphics Environment (This is to get capiblities of the computer)
        graphicEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicScreen = graphicEnv.getDefaultScreenDevice();

        //If not a Raster Screen Find The First One
        if (graphicScreen == null || graphicScreen.getType() != GraphicsDevice.TYPE_RASTER_SCREEN)
        {
            //Get Graphic Devices (Screens) and Use First Screen
            graphicScreens = graphicEnv.getScreenDevices();

            for (int i = 0; i < graphicScreens.length; i++)
            {
                if (graphicScreens[i].getType() == GraphicsDevice.TYPE_RASTER_SCREEN)
                {
                    graphicScreen = graphicScreens[i];
                    break;
                }
            }
        }

        //Get Default Configuration
        graphicConfiguration = graphicScreen.getDefaultConfiguration();

        //Check Capabilities of Default Configuration
        bufferCapabilities = graphicConfiguration.getBufferCapabilities();

        //We Want Page Flipping, No Full Screen and Acelerated
        if (!bufferCapabilities.isPageFlipping() || bufferCapabilities.isFullScreenRequired() || !bufferCapabilities.getFrontBufferCapabilities().isAccelerated())
        {
            //No Supported Configuration Yet
            graphicConfiguration = null;

            //Get all Configurations
            graphicConfigurations = graphicScreen.getConfigurations();

            for (int i = 0; i < graphicConfigurations.length; i++)
            {
                bufferCapabilities = graphicConfigurations[i].getBufferCapabilities();

                if (bufferCapabilities.isPageFlipping() && !bufferCapabilities.isFullScreenRequired() && bufferCapabilities.getFrontBufferCapabilities().isAccelerated())
                {
                    //Found Good Configuration
                    graphicConfiguration = graphicConfigurations[i];

                    break;
                }
            }
        }

        if (graphicConfiguration != null)
        {
            //Show Game Panel with Configuration
            new TileEngineApplication("TileEngine", graphicConfiguration, settings);
        }
        else
        {
            //No Supported Configuration
            System.out.println("No Supported Configuration");
        }
    }

    /**
     * Creates an instance of the Tile Engine
     *
     * @param title Title of the Window
     * @param graphicConfiguration The graphics configuration to use
     * @param settings The settings for the engine
     */
    public TileEngineApplication(String title, GraphicsConfiguration graphicConfiguration, String[] settings)
    {
        super(title, graphicConfiguration);

        //Set Configuration
        this.graphicDevice = graphicConfiguration.getDevice();

        //Set the Engine Settingsw
        this.settings = settings;

        int windowWidth = 1024;
        int windowHeight = 764;
        int targetFPS = 1000;

        if(settings != null)
        {
            switch(settings.length)
            {
                case 1:
                {
                    targetFPS = Integer.parseInt(settings[0]);
                    break;
                }

                case 2:
                {
                    windowWidth = Integer.parseInt(settings[0]);
                    windowHeight = Integer.parseInt(settings[1]);
                    break;
                }
                case 3:
                {
                    windowWidth = Integer.parseInt(settings[0]);
                    windowHeight = Integer.parseInt(settings[1]);
                    targetFPS = Integer.parseInt(settings[2]);
                    break;
                }
            }
        }

        initialize(windowWidth, windowHeight);

        TileEngineCanvas tileEngineCanvas = new TileEngineCanvas(windowWidth, windowHeight, targetFPS);
        this.getContentPane().add(tileEngineCanvas);
        
        tileEngineCanvas.runGameLoop();
    }

 
    /**
     * Setups the Tile Engine for game loop
     */
    private void initialize(int windowWidth, int windowHeight)
    {
        //Setup Close Operation
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Setup Size of Frame
        this.setSize(windowWidth, windowHeight);

        //Make Frame Visable;
        this.setVisible(true);

        //Adjust for Insets
        this.insets = this.getInsets();
        this.setSize(windowWidth + insets.left + insets.right, windowHeight + insets.top + insets.bottom);
    }
}
