/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.BaseActor;

import AbilitySystem.Component.UAbilitySystemComponent;
import AbilitySystem.Implementation.IAbilitySystemComponent;
import CoreUtils.FVector2;
import CoreUtils.UMasterFunctionLibrary;
import GameObjects.Actor;
import GraFX.Animation.UAnimationSequence;
import GraFX.USpriteSheet;
import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;
import PhysiX.UCollisionHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The base Actor that implements the Ability System
 */
public abstract class AASBaseActor extends Actor implements IAbilitySystemComponent {

    /**
     * Core Ability System Component
     */
    private UAbilitySystemComponent ASC = new UAbilitySystemComponent(this, null);


    /**
     * The Sprite sheet has all the animations within it and we need to know in
     * advance which row in the 2D table corresponds to which direction!
     */
    protected int MoveUpAnimIndex = 0;
    protected int MoveRightAnimIndex = 8;
    protected int MoveDownAnimIndex = 2;
    protected int MoveLeftAnimIndex = 3;
    protected int Idle = 14;
    protected int SpawnMontageIndex = 19;
    protected boolean PlaySpawnMontage = true;

    /**
     * Tells which animation we're currently playing
     */
    protected int CurrentAnimationIndex;


    /**
     * Basic Actor Implementation
     */
    protected FVector2 WorldPosition;
    protected int ActorSize;
    protected USpriteSheet ActorVisual;
    protected UAnimationSequence ActorAnimationSequence;

    /**
     * Movement Related!
     * <p>
     * All these below are here for testing, they shall be handled by another class!
     */
    protected boolean IsGoingUp;
    protected boolean IsGoingRight;
    protected boolean IsGoingDown;
    protected boolean IsGoingLeft;

    public float dx = 0.0f;
    public float dy = 0.0f;

    protected float MaxWalkSpeed = 3f;
    protected float Acceleration = 1f;
    protected float Deceleration = 0.2f;

    /**
     * AbilitySystem Debug!
     */
    protected int AttackSpeed;
    protected int AttackDuration;

    /**
     * Collision handler
     */
    protected UCollisionHandler ActorCollision;
    protected UCollisionHandler HitCollision;


    /**
     * Initialize the player
     *
     * @param Sheet       the player sprite sheet
     * @param SpawnOrigin where to spawn
     * @param ActorSize   the size of this actor
     */
    public AASBaseActor(USpriteSheet Sheet, FVector2 SpawnOrigin, int ActorSize) {
        PreConstruct();
        ActorVisual = Sheet;
        WorldPosition = SpawnOrigin;
        this.ActorSize = ActorSize;

        ActorCollision = new UCollisionHandler(SpawnOrigin, ActorSize, ActorSize);
        HitCollision = new UCollisionHandler(SpawnOrigin, ActorSize, ActorSize);
        HitCollision.setCollisionXOffset(ActorSize / 2);

        ActorAnimationSequence = new UAnimationSequence();


        if (PlaySpawnMontage) {
            BufferedImage[] SpawnSequence = ActorVisual.GetSpriteSequence(SpawnMontageIndex);
            ActorAnimationSequence.PlayAnimationMontage(SpawnSequence, UMasterFunctionLibrary.CountValidArrayElements(SpawnSequence), 5, SpawnMontageIndex);

        }

        SetCurrentAnimation(Idle, ActorVisual.GetSpriteSequence(Idle), 83);

    }

    /**
     * Allow children to run logic before the default construct is applied such has changing variables
     */
    protected void PreConstruct() {
    }

    /**
     * Change the animation sequence
     *
     * @param AnimationIndex    the new animation index
     * @param AnimationSequence the new animation sequence to play
     * @param AnimSequenceDelay the delay in Frames to waiut between each animation sequence
     */
    public void SetCurrentAnimation(int AnimationIndex, BufferedImage[] AnimationSequence, int AnimSequenceDelay) {
        CurrentAnimationIndex = AnimationIndex;
        ActorAnimationSequence.setAnimationSequence(AnimationSequence, CurrentAnimationIndex);
        ActorAnimationSequence.setAnimDelay(AnimSequenceDelay);

    }


    /**
     * Update the animation state
     */
    protected void PerformAnimation() {
        if (!getActorAnimationSequence().isPlayingMontage()) {
            if (IsGoingRight) {
                if (CurrentAnimationIndex != MoveRightAnimIndex || ActorAnimationSequence.getAnimDelay() == 1) {
                    SetCurrentAnimation(MoveRightAnimIndex, ActorVisual.GetSpriteSequence(MoveRightAnimIndex), 83);
                }
            } else if (IsGoingLeft) {
                if (CurrentAnimationIndex != MoveLeftAnimIndex || ActorAnimationSequence.getAnimDelay() == 1) {
                    SetCurrentAnimation(MoveLeftAnimIndex, ActorVisual.GetSpriteSequence(MoveLeftAnimIndex), 83);
                }
            } else if (IsGoingUp) {
                if (CurrentAnimationIndex != MoveUpAnimIndex || ActorAnimationSequence.getAnimDelay() == 1) {
                    SetCurrentAnimation(MoveUpAnimIndex, ActorVisual.GetSpriteSequence(MoveUpAnimIndex), 83);
                }
            } else if (IsGoingDown) {
                if (CurrentAnimationIndex != MoveDownAnimIndex || ActorAnimationSequence.getAnimDelay() == 1) {
                    SetCurrentAnimation(MoveDownAnimIndex, ActorVisual.GetSpriteSequence(MoveDownAnimIndex), 83);
                }
            } else {
                SetCurrentAnimation(Idle, ActorVisual.GetSpriteSequence(Idle), 83);

            }
        } else {
            //we are currently playing a montage
        }
    }


    /**
     * Update the hit collision position based on player's direction
     */
    public void SyncHitBoxDirection() {
        if (IsGoingUp) {
            HitCollision.setCollisionXOffset(0);
            HitCollision.setCollisionYOffset(-ActorSize / 2);
        } else if (IsGoingRight) {
            HitCollision.setCollisionXOffset(ActorSize / 2);
            HitCollision.setCollisionYOffset(0);
        } else if (IsGoingDown) {
            HitCollision.setCollisionXOffset(0);
            HitCollision.setCollisionYOffset(ActorSize / 2);
        } else if (IsGoingLeft) {
            HitCollision.setCollisionXOffset(-ActorSize / 2);
            HitCollision.setCollisionYOffset(0);
        }
    }

    //Getters


    public int getActorSize() {
        return ActorSize;
    }

    public USpriteSheet getActorVisual() {
        return ActorVisual;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public float getMaxWalkSpeed() {
        return MaxWalkSpeed;
    }

    public float getAcceleration() {
        return Acceleration;
    }

    public float getDeceleration() {
        return Deceleration;
    }

    public FVector2 getWorldPosition() {
        return WorldPosition;
    }

    public UAnimationSequence getActorAnimationSequence() {
        return ActorAnimationSequence;
    }

    public UCollisionHandler getActorCollision() {
        return ActorCollision;
    }

    //Setters
    public void setActorVisual(USpriteSheet actorVisual) {
        ActorVisual = actorVisual;
    }

    public void setActorSize(int actorSize) {
        ActorSize = actorSize;
    }

    public void setMaxWalkSpeed(float maxWalkSpeed) {
        MaxWalkSpeed = maxWalkSpeed;
    }

    public void setAcceleration(float acceleration) {
        Acceleration = acceleration;
    }

    public void setDeceleration(float deceleration) {
        Deceleration = deceleration;
    }


    @Override
    public void Tick(double DeltaTime) {
        PerformAnimation();
        SyncHitBoxDirection();
        ActorAnimationSequence.Tick(DeltaTime);
    }

    @Override
    public void Input(InputMouseHandler Mouse, InputKeyHandler Key) {

    }

    @Override
    public UAbilitySystemComponent GetAbilitySystemComponent() {
        return ASC;
    }

    @Override
    public abstract void PrepareSceneRender(Graphics2D g);

    @Override
    public String toString() {
        return "AASBaseActor{" +
                "MoveUpAnimIndex=" + MoveUpAnimIndex +
                ", MoveRightAnimIndex=" + MoveRightAnimIndex +
                ", MoveDownAnimIndex=" + MoveDownAnimIndex +
                ", MoveLeftAnimIndex=" + MoveLeftAnimIndex +
                ", CurrentAnimationIndex=" + CurrentAnimationIndex +
                ", WorldPosition=" + WorldPosition +
                ", ActorSize=" + ActorSize +
                ", ActorVisual=" + ActorVisual +
                ", ActorAnimationSequence=" + ActorAnimationSequence +
                ", IsGoingUp=" + IsGoingUp +
                ", IsGoingRight=" + IsGoingRight +
                ", IsGoingDown=" + IsGoingDown +
                ", IsGoingLeft=" + IsGoingLeft +
                ", dx=" + dx +
                ", dy=" + dy +
                ", MaxWalkSpeed=" + MaxWalkSpeed +
                ", Acceleration=" + Acceleration +
                ", Deceleration=" + Deceleration +
                ", AttackSpeed=" + AttackSpeed +
                ", AttackDuration=" + AttackDuration +
                ", ActorCollision=" + ActorCollision +
                ", HitCollision=" + HitCollision +
                '}';
    }
}
