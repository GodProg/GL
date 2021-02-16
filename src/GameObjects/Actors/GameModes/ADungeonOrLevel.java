/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GameObjects.Actors.GameModes;

import AbilitySystem.BaseActor.AASBaseActor;
import CoreUtils.FVector2;
import GameObjects.ACameraComponent;
import GameObjects.Actors.AGameManager;
import MainUI.URPGPanel;
import Map.UMapTileManager;
import PhysiX.UCollisionHandler;

import java.util.ArrayList;

public abstract class ADungeonOrLevel extends AStateOfGame {


    /**
     * The map manager in charge of handling the display
     */
    protected UMapTileManager MapManager;

    /**
     * Map position controller
     */
    public static FVector2 MapPosition;

    /**
     * map initial X position
     */
    protected float InitialMapXPosition = 0.0f;
    /**
     * Map initial Y position
     */
    protected float InitialMapYPosition = 0.0f;
    /**
     * Player initial X position
     */
    protected float InitialPlayerSpawnX = 0.0f;
    /**
     * Player initial Y position
     */
    protected float InitialPlayerSpawnY = 0.0f;

    /**
     * Our World camera
     */
    private ACameraComponent WorldCamera;

    /**
     * Tile displaySize
     */
    protected int GameTileWidthDisplay = 0;
    /**
     * Tile displaySize
     */
    protected int GameTileHeightDisplay = 0;

    /**
     * Stores all living entities in a list
     */
    protected static ArrayList<AASBaseActor> GameActors = new ArrayList<>();


    public ADungeonOrLevel(AGameManager mngr) {
        super(mngr);
        WorldCamera = new ACameraComponent(new UCollisionHandler(new FVector2(0, 0), URPGPanel.WIDTH, URPGPanel.HEIGHT));

    }

    public ACameraComponent getWorldCamera() {
        return WorldCamera;
    }


    /**
     * perform a trace and return all actors inside a shape
     *
     * @param Shape the shape to test against
     * @return the actors within the shape
     */
    public ArrayList<AASBaseActor> PerformTrace(UCollisionHandler Shape) {
        ArrayList<AASBaseActor> actors = new ArrayList<>();
        for (AASBaseActor CurrentActor : GameActors) {
            if (Shape.getCircleCollisionRadius() > 0) {
                if (Shape.IsCircleColliding(CurrentActor.getActorCollision())) actors.add(CurrentActor);

            } else {
                if (Shape.IsColliding(CurrentActor.getActorCollision())) actors.add(CurrentActor);
            }

        }
        return actors;
    }
}
