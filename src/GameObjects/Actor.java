/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GameObjects;

import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;

import java.awt.*;

/**
 * Our game base object
 */
public abstract class Actor implements IActorBase {


    @Override
    public abstract void Tick(double DeltaTime);

    @Override
    public abstract void Input(InputMouseHandler Mouse, InputKeyHandler Key);

    @Override
    public abstract void PrepareSceneRender(Graphics2D g);

}
