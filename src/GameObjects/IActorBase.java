/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GameObjects;

import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;

import java.awt.*;

/**
 * Contains core functions for a basic actor in our game
 */
public interface IActorBase {

    /**
     * Event Tick, Called Every Frame
     *
     * @param DeltaTime Time Elapsed Since Last Tick in nanoseconds
     */
    void Tick(double DeltaTime);

    /**
     * Input Listener
     *
     * @param Mouse Mouse
     * @param Key   Key
     */
    void Input(InputMouseHandler Mouse, InputKeyHandler Key);

    /**
     * Handle The Current Actor Rendering State
     *
     * @param g the game graphics
     */
    void PrepareSceneRender(Graphics2D g);


}
