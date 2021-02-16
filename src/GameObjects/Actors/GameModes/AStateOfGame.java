/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GameObjects.Actors.GameModes;

import GameObjects.Actor;
import GameObjects.Actors.AGameManager;
import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;

import java.awt.*;

/**
 * Abstract state of game AKA game mode
 */
public abstract class AStateOfGame extends Actor {

    private AGameManager GManager;

    public AStateOfGame(AGameManager mngr) {
        GManager = mngr;
    }

    @Override
    public abstract void Tick(double DeltaTime);

    @Override
    public abstract void Input(InputMouseHandler Mouse, InputKeyHandler Key);

    @Override
    public abstract void PrepareSceneRender(Graphics2D g);
}
