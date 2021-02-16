/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GameObjects;

import AbilitySystem.BaseActor.AASBaseActor;
import CoreUtils.FVector2;
import CoreUtils.UDataContainer;
import GameObjects.Actors.GameModes.Dungeon1;
import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;
import MainUI.URPGPanel;
import PhysiX.UCollisionHandler;

import java.awt.*;

/**
 * A component that defines a camera
 * Mainly used to render optimizations
 * <p>
 * This camera is a regular actor but without physical representation
 * Its only purpose is to serve as sight for the human controller
 */
public class ACameraComponent extends Actor {

    /**
     * Movement Related!
     * <p>
     * All these below are here for testing, they shall be handled by another class!
     */
    private boolean IsGoingUp;
    private boolean IsGoingRight;
    private boolean IsGoingDown;
    private boolean IsGoingLeft;

    private float dx = 2.0f;
    private float dy = 0.0f;

    private float MaxWalkSpeed = 3f;
    private float Acceleration = 1f;
    private float Deceleration = 0.2f;


    private int ViewWidth;
    private int ViewHeight;

    private AASBaseActor AttachedActor;

    public ACameraComponent(UCollisionHandler cameraCollision) {
        CameraCollision = cameraCollision;
    }

    /**
     * Collision handler
     */
    private UCollisionHandler CameraCollision;
    private UCollisionHandler CameraViewPort;

    public void Tick(double DeltaTime) {
        Acceleration = 150f;
        dy = 0f;
        dx = (float) (Acceleration * (DeltaTime / UDataContainer.BILLION));
        IsGoingRight = true;
        MoveCamera(DeltaTime);


        //if we're attached to an actor
        if (AttachedActor != null) {
            //check if Camera X position is not going out of bounds of the map
            if (AttachedActor.getActorCollision().getPosition().GetScreenPosition().XPos + AttachedActor.getDx() < ((ViewWidth - URPGPanel.WIDTH / 2) - FVector2.AbsoluteX) - AttachedActor.getActorSize()
                    && AttachedActor.getActorCollision().getPosition().XPos + AttachedActor.getDx() > ((URPGPanel.WIDTH / 2 - AttachedActor.getActorSize()) - FVector2.AbsoluteX)) {
                Dungeon1.MapPosition.XPos = AttachedActor.getWorldPosition().XPos - URPGPanel.WIDTH / 2 + AttachedActor.getActorSize() / 2;
            }
            //check if Camera Y position is not going out of bounds of the map
            if (AttachedActor.getActorCollision().getPosition().GetScreenPosition().YPos + AttachedActor.getDy() < ((ViewHeight - URPGPanel.HEIGHT / 2) - FVector2.AbsoluteY) - AttachedActor.getActorSize()
                    && AttachedActor.getActorCollision().getPosition().YPos + AttachedActor.getDy() > ((URPGPanel.HEIGHT / 2 - AttachedActor.getActorSize()) - FVector2.AbsoluteY)) {
                Dungeon1.MapPosition.YPos = AttachedActor.getWorldPosition().YPos - URPGPanel.HEIGHT / 2 + AttachedActor.getActorSize() / 2;
            }
        } else {
            //Allow spectator mode
            Dungeon1.MapPosition.XPos += dx;
            Dungeon1.MapPosition.YPos += dy;
        }

    }

    @Override
    public void Input(InputMouseHandler Mouse, InputKeyHandler Key) {

    }

    @Override
    public void PrepareSceneRender(Graphics2D g) {
        DebugCameraBounds(g);
    }

    public void AttachCameraToActor(AASBaseActor target) {
        if (target != null) {
            AttachedActor = target;
            MaxWalkSpeed = AttachedActor.getMaxWalkSpeed();
            Acceleration = AttachedActor.getAcceleration();
            Deceleration = AttachedActor.getDeceleration();
        } else {
            if (URPGPanel.DEBUG) System.out.println("Attempt to attach camera to null actor!");
        }


    }

    public void setCameraLimits(int viewWidth, int viewHeight) {
        ViewWidth = viewWidth;
        ViewHeight = viewHeight;
    }

    public void MoveCamera(double DeltaTime) {

    }

    public UCollisionHandler getCameraCollision() {
        return CameraCollision;
    }

    public UCollisionHandler getCameraViewPort() {
        return CameraViewPort;
    }

    public void DebugCameraBounds(Graphics2D g) {
        g.setColor(new Color(0, 64, 255));
        g.drawRect((int) CameraCollision.getPosition().XPos, (int) CameraCollision.getPosition().YPos, (int) CameraCollision.getCollisionWidth(), (int) CameraCollision.getCollisionHeight());
    }

}
