/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Heroes.Enemy;

import AbilitySystem.BaseActor.AASBaseActor;
import AbilitySystem.Component.UAbilitySystemComponent;
import CoreUtils.FTuple;
import CoreUtils.FVector2;
import CoreUtils.UDataContainer;
import CoreUtils.UMasterFunctionLibrary;
import GraFX.Animation.IAnimationNotifyCallback;
import GraFX.USpriteSheet;
import MainUI.URPGPanel;
import PhysiX.UCollisionHandler;

import java.awt.*;

public class ANormalEnemy extends AASBaseActor implements IAnimationNotifyCallback {

    /**
     * Serves as sight for this unit
     */
    UCollisionHandler SightSense;
    /**
     * Sight radius
     */
    private final float SightRadius = 350;

    /**
     * AI acceptable radius in X Axis
     */
    private static final float AcceptableXRadius = 5;
    /**
     * AI acceptable radius in Y Axis
     */
    private static final float AcceptableYRadius = 5;


    /**
     * more animation related
     */
    private final int TurnAnimationRTFIndex = 12;
    private final int TurnAnimationLTRIndex = TurnAnimationRTFIndex + 1;

    /**
     * Initialize the player
     *
     * @param Sheet       the player sprite sheet
     * @param SpawnOrigin where to spawn
     * @param ActorSize   the size of this actor
     */
    public ANormalEnemy(USpriteSheet Sheet, FVector2 SpawnOrigin, int ActorSize) {
        super(Sheet, SpawnOrigin, ActorSize);

        ActorCollision.setCollisionWidth(ActorSize * 0.25f);
        ActorCollision.setCollisionHeight(ActorSize * 0.2f);
        ActorCollision.setCollisionXOffset(ActorSize * 0.35f);
        ActorCollision.setCollisionYOffset(ActorSize * 0.70f);


        SightSense = new UCollisionHandler(new FVector2(SpawnOrigin.XPos + ActorSize / 2 - SightRadius / 2, SpawnOrigin.YPos + ActorSize / 2 - SightRadius / 2), SightRadius);
        SightSense.setCircleCollisionRadius(SightRadius);

        Acceleration = 40f;
        MaxWalkSpeed = 150f;

        getActorAnimationSequence().AddDynamicCaller(this);

        getActorAnimationSequence().AddDynamicNotify("Left To Right End", new FTuple<>(TurnAnimationLTRIndex, UMasterFunctionLibrary.CountValidArrayElements(ActorVisual.GetSpriteSequence(TurnAnimationLTRIndex))));
        getActorAnimationSequence().AddDynamicNotify("Right To Left End", new FTuple<>(TurnAnimationRTFIndex, UMasterFunctionLibrary.CountValidArrayElements(ActorVisual.GetSpriteSequence(TurnAnimationRTFIndex))));


    }

    /**
     * Simple AI Movement logic this logic is temporary, it should be handled by the {@link PhysiX.UMovementComponent}
     *
     * @param DeltaTime       the time since last tick
     * @param PlayerCollision the player to track when in sight
     */
    protected void ProcessMovement(Double DeltaTime, UCollisionHandler PlayerCollision) {
        //get the delta time in seconds
        float Delta = (float) (DeltaTime / UDataContainer.BILLION);


        //we are underneath the player
        if (PlayerCollision.getPosition().YPos + 5 < WorldPosition.YPos) {

            dy = -Acceleration * Delta;
            IsGoingUp = true;
            IsGoingDown = false;
            if (dy < -MaxWalkSpeed) {
                dy = -MaxWalkSpeed;
            }
            //we are above the player
        } else if (PlayerCollision.getPosition().YPos - 5 > WorldPosition.YPos) {
            dy = Acceleration * Delta;
            IsGoingUp = false;
            IsGoingDown = true;
            if (dy > MaxWalkSpeed) {
                dy = MaxWalkSpeed;
            }
        } else {
            dy = 0;
            IsGoingUp = false;
            IsGoingDown = false;
        }
        //we are to the left of the player
        if (PlayerCollision.getPosition().XPos > WorldPosition.XPos) {
            dx = Acceleration * Delta;
            if (IsGoingLeft) {
                ActorAnimationSequence.PlayAnimationMontage(ActorVisual.GetSpriteSequence(TurnAnimationLTRIndex), ActorVisual.GetSpriteSequence(TurnAnimationLTRIndex).length, 83, TurnAnimationLTRIndex);
            } else {
                IsGoingLeft = false;
                IsGoingRight = true;
            }
            if (dx > MaxWalkSpeed) {
                dx = MaxWalkSpeed;
            }
            //we are to the right of the player
        } else if (PlayerCollision.getPosition().XPos < WorldPosition.XPos) {
            dx = -Acceleration * Delta;
            if (IsGoingRight) {
                ActorAnimationSequence.PlayAnimationMontage(ActorVisual.GetSpriteSequence(TurnAnimationRTFIndex), ActorVisual.GetSpriteSequence(TurnAnimationRTFIndex).length, 83, TurnAnimationRTFIndex);
            } else {
                IsGoingLeft = true;
                IsGoingRight = false;
            }
            if (dx < -MaxWalkSpeed) {
                dx = -MaxWalkSpeed;
            }
        } else {
            dx = 0;
            IsGoingLeft = false;
            IsGoingRight = false;
        }
    }


    @Override
    protected void PreConstruct() {
        super.PreConstruct();
        MoveRightAnimIndex = 8;
        MoveLeftAnimIndex = MoveRightAnimIndex + 1;
        MoveDownAnimIndex = MoveRightAnimIndex;
        MoveUpAnimIndex = MoveRightAnimIndex;
        Idle = 4;
        SpawnMontageIndex = 0;
        PlaySpawnMontage = false;

    }

    /**
     * Called every frame
     *
     * @param DeltaTime       the time since last frame
     * @param PlayerCollision the Human controlled player
     */
    public void Tick(Double DeltaTime, UCollisionHandler PlayerCollision) {
        super.Tick(DeltaTime);
        if (SightSense.IsCircleColliding(PlayerCollision)) {
//            System.out.println("I can see a player");
            ProcessMovement(DeltaTime, PlayerCollision);
        } else {
            dx = 0;
            dy = 0;
            IsGoingLeft = false;
            IsGoingRight = false;
            IsGoingUp = false;
            IsGoingDown = false;
        }

        if (!getActorAnimationSequence().isPlayingMontage()) {
            if (!ActorCollision.IsCollidingWithWorld(dx, 0)) {
                SightSense.getPosition().XPos += dx;
                WorldPosition.XPos += dx;
            }


            if (!ActorCollision.IsCollidingWithWorld(0, dy)) {
                SightSense.getPosition().YPos += dy;
                WorldPosition.YPos += dy;
            }
        }
    }

    @Override
    public void PrepareSceneRender(Graphics2D g) {
        if (URPGPanel.DEBUG) {
            //debug Enemy image
            g.setColor(new Color(0, 194, 255));
            g.drawRect((int) (WorldPosition.GetScreenPosition().XPos), (int) (WorldPosition.GetScreenPosition().YPos), ActorSize, ActorSize);

            //debug enemy collision
            g.setColor(new Color(255, 0, 0));
            g.drawRect((int) (WorldPosition.GetScreenPosition().XPos + getActorCollision().getCollisionXOffset()), (int) (WorldPosition.GetScreenPosition().YPos + getActorCollision().getCollisionYOffset()), (int) getActorCollision().getCollisionWidth(), (int) getActorCollision().getCollisionHeight());

            //Debug enemy sight
            g.setColor(new Color(255, 231, 0));
            g.drawOval((int) (SightSense.getPosition().GetScreenPosition().XPos), (int) (SightSense.getPosition().GetScreenPosition().YPos), (int) SightRadius, (int) SightRadius);

        }
        g.drawImage(ActorAnimationSequence.getCurrentAnimationSequence(), (int) WorldPosition.GetScreenPosition().XPos, (int) WorldPosition.GetScreenPosition().YPos, ActorSize, ActorSize, null);

    }

    @Override
    public UAbilitySystemComponent GetAbilitySystemComponent() {
        return null;
    }

    /**
     * Throw Callback to caller that a certain point of an animation Has Been Reached
     *
     * @param AnimationIndex the index where the notify is coming from
     * @param NotifyName     the notify name
     */
    @Override
    public void OnAnimationNotify(int AnimationIndex, String NotifyName) {
        if (URPGPanel.DEBUG) System.out.printf("notify received %s -> at %d\n", NotifyName, AnimationIndex);

        if (AnimationIndex == TurnAnimationLTRIndex) {
            IsGoingLeft = false;
            IsGoingRight = true;
        } else if (AnimationIndex == TurnAnimationRTFIndex) {
            IsGoingLeft = true;
            IsGoingRight = false;
        }
    }


}
