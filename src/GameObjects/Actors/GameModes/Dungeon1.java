/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GameObjects.Actors.GameModes;

import CoreUtils.FVector2;
import GameObjects.Actors.AGameManager;
import GraFX.USpriteSheet;
import Heroes.Enemy.ANormalEnemy;
import Heroes.Player.Classes.Tank.Effects.PlayerHeal;
import Heroes.Player.Classes.Tank.TankClass.TankClass;
import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;
import MainUI.URPGPanel;
import Map.UMapTileManager;

import java.awt.*;

/**
 * Represents a dungeon in our game
 */
public class Dungeon1 extends ADungeonOrLevel {


    /**
     * The human controllable entity
     */
    private TankClass Player;
    /**
     * Our player sprite sheet
     */
    private USpriteSheet sheet;

    /**
     * A single random dummy enemy
     */
    private ANormalEnemy enemy;


    /**
     * @param Manager the game manager
     */
    public Dungeon1(AGameManager Manager) {
        super(Manager);
        //adapt variables for this map
        GameTileWidthDisplay = 64;
        GameTileHeightDisplay = 64;
        InitialMapXPosition = 12f * GameTileWidthDisplay - URPGPanel.WIDTH / 2;
        InitialMapYPosition = 85.5f * GameTileHeightDisplay - URPGPanel.HEIGHT / 2;
        InitialPlayerSpawnX = InitialMapXPosition + URPGPanel.WIDTH / 2 - GameTileWidthDisplay;
        InitialPlayerSpawnY = InitialMapYPosition + URPGPanel.HEIGHT / 2 - GameTileHeightDisplay;

        /*
         * init map position
         */
        MapPosition = new FVector2(InitialMapXPosition, InitialMapYPosition);
        FVector2.SetAbsolutePosition(MapPosition.XPos, MapPosition.YPos);

        MapManager = new UMapTileManager("Resources/Map/map1.xml", GameTileWidthDisplay, GameTileHeightDisplay, getWorldCamera());
        /*
         * Make a player and a test enemy
         */
        sheet = new USpriteSheet("Resources/Sprite/character/PlayerSheet.png", 89, 76, false);
        USpriteSheet EnemySheet = new USpriteSheet("Resources/Sprite/character/EnemySheet.png", 78, 49, false);
        enemy = new ANormalEnemy(EnemySheet, new FVector2(InitialPlayerSpawnX + 300, InitialPlayerSpawnY), 128);
        Player = new TankClass(sheet, new FVector2(InitialPlayerSpawnX, InitialPlayerSpawnY), 128);
        getWorldCamera().AttachCameraToActor(Player);


        //Test healing effect
        //uncomment this line to test the healing effect, this effect grants HP every second based on the health regen attribute
        Player.GetAbilitySystemComponent().ApplyGameplayEffectToSelf(PlayerHeal.class);

        GameActors.add(Player);
        GameActors.add(enemy);

    }

    @Override
    public void Tick(double DeltaTime) {

        /*
         * In our RPG game, to create a "camera effect" we will <MOSTLY> be moving The scene!
         * Most of the time our character will stay on the center of the screen except when we reach an edge
         */
        FVector2.SetAbsolutePosition(MapPosition.XPos, MapPosition.YPos);
//        System.out.println(String.format("%s , %s, %s",Player.getWorldPosition().GetScreenPosition(),Player.getWorldPosition(),MapPosition));
        Player.Tick(DeltaTime);
        enemy.Tick(DeltaTime, Player.getActorCollision());
        getWorldCamera().Tick(DeltaTime);
    }

    @Override
    public void Input(InputMouseHandler Mouse, InputKeyHandler Key) {
        Player.Input(Mouse, Key);
    }

    @Override
    public void PrepareSceneRender(Graphics2D g) {
        MapManager.PrepareDataForRender(g);
        Player.PrepareSceneRender(g);
        enemy.PrepareSceneRender(g);
        getWorldCamera().DebugCameraBounds(g);
    }
}
