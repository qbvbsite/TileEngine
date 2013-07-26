package com.tileengine;

import javax.swing.JApplet;

public class TileEngineApplet extends JApplet implements Runnable
{
    private Thread gameThread;

    private int windowWidth = 1024;
    private int windowHeight = 764;
    private int targetFPS = 100;

    public void init()
    {
        if(getParameter("width") != null)
        {
            windowWidth = Integer.parseInt(getParameter("width"));
        }

        if(getParameter("height") != null)
        {
            windowHeight = Integer.parseInt(getParameter("height"));
        }

        if(getParameter("targetFPS") != null)
        {
            targetFPS = Integer.parseInt(getParameter("targetFPS"));
        }

        //Make Frame Visable;
        this.setVisible(true);
    }

   public void start()
   {
        gameThread = new Thread(this);
        gameThread.start();
   }

   public void run()
   {
        TileEngineCanvas tileEngineCanvas = new TileEngineCanvas(windowWidth, windowHeight, targetFPS);
        this.getContentPane().add(tileEngineCanvas);
        tileEngineCanvas.runGameLoop();
   }

   public void destroy()
   {
       gameThread = null;
   }
}
