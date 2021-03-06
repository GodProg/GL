/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GameObjects.Actors.GameModes;

import GameObjects.Actors.AGameManager;
import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;

import java.awt.*;

public class AGameOver extends AStateOfGame {
    public AGameOver(AGameManager aGameManager) {
        super(aGameManager);
    }

    @Override
    public void Tick(double DeltaTime) {

    }

    @Override
    public void Input(InputMouseHandler Mouse, InputKeyHandler Key) {

    }

    @Override
    public void PrepareSceneRender(Graphics2D g) {

    }
}
