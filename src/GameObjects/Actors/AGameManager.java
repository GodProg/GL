/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GameObjects.Actors;

import CoreUtils.EGameState;
import GameObjects.Actor;
import GameObjects.Actors.GameModes.*;
import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;

import java.awt.*;
import java.util.ArrayList;

/**
 * Game manager
 */
public class AGameManager extends Actor {

    /**
     * All game states
     */
    private ArrayList<AStateOfGame> CurrentGameMode;

    public AGameManager() {

        CurrentGameMode = new ArrayList<>();
        CurrentGameMode.add(new Dungeon1(this));

    }

    public void RemoveModeAtIndex(int Index) {
        CurrentGameMode.remove(Index);
    }

    public void AddNewMode(EGameState State) {
        switch (State) {
            case PLAY:
                CurrentGameMode.add(new Dungeon1(this));
                break;
            case PAUSE:
                CurrentGameMode.add(new APause(this));
                break;
            case MENU:
                CurrentGameMode.add(new AMenu(this));
                break;
            case LOST:
                CurrentGameMode.add(new AGameOver(this));
                break;
            default:
                System.out.printf("Something went bad while adding a new State\nUnkownState: %s\n", State.name());
        }
    }

    /**
     * Add a new state and remove the first one on the list
     */
    public void AddAndRemoveFI(EGameState state) {
        CurrentGameMode.remove(0);
        AddNewMode(state);
    }

    @Override
    public void Tick(double DeltaTime) {

        /*
         * In our RPG game, to create a "camera effect" we will <MOSTLY> be moving The scene!
         * Most of the time our character will stay on the center of the screen except when we reach an edge
         */
        // FVector2.SetAbsolutePosition(GameMap.XPos, GameMap.YPos);

        for (AStateOfGame GM : CurrentGameMode) {
            GM.Tick(DeltaTime);
        }
    }

    @Override
    public void Input(InputMouseHandler Mouse, InputKeyHandler Key) {
        for (AStateOfGame GM : CurrentGameMode) {
            GM.Input(Mouse, Key);
        }
    }

    @Override
    public void PrepareSceneRender(Graphics2D g) {
        for (AStateOfGame controller : CurrentGameMode) {
            controller.PrepareSceneRender(g);
        }
    }
}
