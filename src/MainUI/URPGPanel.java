/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package MainUI;

import CoreUtils.UDataContainer;
import GameObjects.Actors.AGameManager;
import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * This component will serve as the master game instance
 * It will contain the base game loop and will control the game flow
 */
public class URPGPanel extends JPanel implements Runnable {

    /**
     * Dimensions of this pannel
     */
    public static int WIDTH;
    public static int HEIGHT;

    /**
     * The Core Game Thread
     */
    private Thread RPGThread = null;

    /**
     * The Background Image
     */
    private BufferedImage GameViewport;

    /**
     * Graphics controller.
     * Used to draw stuff onto the screen and managing coordinates
     *
     * @see java.awt.Graphics2D
     */
    private Graphics2D GameViewportGraphics2D;

    /**
     * Drives The Main Loop
     */
    private static boolean IsGameRunning = false;


    /**
     * How much Fps are we targeting
     */
    private static final double TARGET_FPS = 60;

    /**
     * Tells if the program should ignore the frame rate and run as fast as it can
     */
    public static final boolean FreeSync = true;

    /**
     * Optimal game Tick Rate 1/TargetFPS
     */
    public static final double OPTIMAL_TICK_RATE = UDataContainer.BILLION / TARGET_FPS;

    /**
     * Mouse Handler
     */
    private InputMouseHandler Mouse;

    /**
     * Key Handler
     */
    private InputKeyHandler Key;

    /**
     * Manages All Controllers
     */
    private AGameManager PCManager;

    /**
     * Keep track of FPS
     */
    private int CurrentFPS = 0;

    /**
     * Keeps Track Of The FPS on the last second
     */
    public static final boolean DEBUG = true;


    /**
     * The base panel of our game
     *
     * @param width  panel width
     * @param height panel height
     */
    public URPGPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        WIDTH = width;
        HEIGHT = height;
        setFocusable(true);
        requestFocus();


    }

    /**
     * From the documentation this method is called when this panel has been initialized onto the screen
     * so we're gonna make use of it to start the main thread, we could also use the constructor for this
     */
    @Override
    public void addNotify() {
        super.addNotify();

        if (RPGThread == null) {
            RPGThread = new Thread(this, "Main Game Thread");
            RPGThread.setPriority(Thread.MAX_PRIORITY);
            RPGThread.start();

        }

    }

    private void RunPreSetUp() {
        IsGameRunning = true;
        //Allow Alpha
        GameViewport = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        //allow us to draw on top of the image
        GameViewportGraphics2D = (Graphics2D) GameViewport.getGraphics();

        Mouse = new InputMouseHandler(this);
        Key = new InputKeyHandler();
        PCManager = new AGameManager();
        System.gc();
    }

    /**
     * Main Thread job
     */
    @Override
    public void run() {
        RunPreSetUp();


        //Keep track of the last time we updated
        double LastTickTime = System.nanoTime();
        double ScenePrepareTime = 0;
        double DrawSceneTime = 0;
        double TickTime = 0;

        //Keep track of frame counter
        int FrameCount = 0;
        //delta time
        double StartPoint = LastTickTime;
        int TickCounter = 0;


        while (IsGameRunning) {
//
            //current time
            double CurrentNanoTime = System.nanoTime();

            //delta time between last loop
            double DeltaTime = (CurrentNanoTime - LastTickTime);

            LastTickTime = CurrentNanoTime;

            //World Tick
            InputListener(Mouse, Key);
            Tick(DeltaTime);
            TickTime = System.nanoTime() - LastTickTime;
            TickCounter++;
            ScenePrepareTime = System.nanoTime();
            PrepareScene();
            ScenePrepareTime = System.nanoTime() - ScenePrepareTime;
            DrawSceneTime = System.nanoTime();
            DrawScene();
            DrawSceneTime = System.nanoTime() - DrawSceneTime;
            FrameCount++;


            if (System.nanoTime() - StartPoint > UDataContainer.BILLION) {

                //Get how many Seconds have passed, Maybe it wasn't only 1second , maybe our machine is slow af
                int RealSeconds = (int) ((System.nanoTime() - StartPoint) / UDataContainer.BILLION);

                //one second has elapsed
                StartPoint += UDataContainer.BILLION * RealSeconds;

                if (DEBUG)
                    System.out.printf("%d Second(s) Passed -> Processed %d Ticks  |  Rendered %d frames \n\tTick Took: %fms\n\tScene Render Took: %fms\n\tScene Draw Took: %fms\n\n",
                            RealSeconds, TickCounter, FrameCount, (float) TickTime / UDataContainer.BILLION * 1000, (float) ScenePrepareTime / UDataContainer.BILLION * 1000, (float) DrawSceneTime / UDataContainer.BILLION * 1000);


                CurrentFPS = FrameCount;

                TickCounter = 0;
                FrameCount = 0;

            }

            if (!FreeSync) {
                /*
                 * From this source https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
                 * Swing is intended to be Single Threaded, meaning that any Threads running in parallel will
                 * cause Concurrency with the swing Thread causing it to "freeze".
                 * So we put this thread temporarily to sleep to allow the main thread to carry on and also
                 * allowing the thread to be on sync with the Target FPS
                 *
                 * if we're late we will completely ignore the sleep and try to catch up with the target frame rate
                 */
                while (System.nanoTime() - LastTickTime < OPTIMAL_TICK_RATE) {
                    try {
                        //Tell other Threads we are willing to yield the use of the CPU
                        Thread.yield();
                        //super duper small sleep
                        //we dont want to be late!
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

//            else
//            {
//
//                //We want these two variables to reset on every iteration
//                double CurrentNanoTime = System.nanoTime();
//
//                //Delta time between current time and the last loop
//                double FrameTime = (CurrentNanoTime - LastTickTime);
//
//                //we no longer need this variable so we can safely assign here its new value for the next loop
//                //REMINDER: in case this variable is used later we must assign its value at the end of the loop
//                LastTickTime = CurrentNanoTime;
//
//                //Tick as much as necessary to catch up with the frame rate
//                while (FrameTime > OPTIMAL_TICK_RATE) {
//                    double DeltaTime = Math.min(FrameTime, OPTIMAL_TICK_RATE);
//                    Tick(DeltaTime);
//                    InputListener(Mouse, Key);
//                    FrameTime -= DeltaTime;
//                    TickCounter++;
//                }
//
//                //Render scene
//                InputListener(Mouse, Key);
//                PrepareScene();
//                DrawScene();
//                LastPrepareSceneTime = CurrentNanoTime;
//                FrameCount++;
//
//                if (System.nanoTime() - StartPoint > UDataContainer.BILLION) {
//
//                    //Get how many Seconds have passed, Maybe it wasn't only 1second , maybe our machine is slow af
//                    int RealSeconds = (int) ((System.nanoTime() - StartPoint) / UDataContainer.BILLION);
//
//                    //one second has elapsed
//                    StartPoint += UDataContainer.BILLION * RealSeconds;
//
//                    System.out.printf("%d Second(s) Passed -> Processed %d Ticks  |  Rendered %d frames \n", RealSeconds, TickCounter, FrameCount);
//                    CurrentFPS = FrameCount;
//
//                    TickCounter = 0;
//                    FrameCount = 0;
//
//                }
//
//                CurrentNanoTime = System.nanoTime();
//
//
//            }

        }


    }

    /**
     * Listen For Inputs
     */
    private void InputListener(InputMouseHandler mouse, InputKeyHandler Key) {
        PCManager.Input(mouse, Key);

    }

    /**
     * Run necessary game calculations.
     * NOTE: this function number of calls may not correspond to game fps!
     *
     * @param DeltaTime The Time Elapsed Since Last Tick
     */
    private void Tick(double DeltaTime) {
        PCManager.Tick(DeltaTime);

    }


    //The PrepareSceneRender function makes the visuals and the draw function puts them into the screen

    /**
     * Prepares the Data For Drawing The Scene
     */
    private void PrepareScene() {
        if (GameViewportGraphics2D != null) {
            GameViewportGraphics2D.setColor(new Color(3, 46, 64));
            //fill the whole screen
            GameViewportGraphics2D.fillRect(0, 0, WIDTH, HEIGHT);
            PCManager.PrepareSceneRender(GameViewportGraphics2D);
            if (DEBUG) {
                String FPS_Display = String.format("FPS :%d", CurrentFPS);
                GameViewportGraphics2D.setColor(Color.GREEN);
                GameViewportGraphics2D.drawString(FPS_Display, 0, 30);
            }
        }

    }

    /**
     * Handles The Scene Drawing
     */
    private void DrawScene() {
        //Get this panel's graphics
        Graphics PanelGraphics = getGraphics();
        PanelGraphics.drawImage(GameViewport, 0, 0, null);
        //Display The GameViewport
        PanelGraphics.dispose();

    }


}
