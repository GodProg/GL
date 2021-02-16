/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/


package Heroes.Player.Base;

import AbilitySystem.BaseActor.AASBaseActor;
import CoreUtils.FVector2;
import CoreUtils.UDataContainer;
import GraFX.Animation.IAnimationNotifyCallback;
import GraFX.USpriteSheet;
import InputHandler.InputKeyHandler;
import InputHandler.InputMouseHandler;
import MainUI.URPGPanel;

import java.awt.*;

/**
 * Abstract representation of physical human controlled player
 */
public class APlayerParentDummy extends AASBaseActor implements IAnimationNotifyCallback {


    public APlayerParentDummy(USpriteSheet Sheet, FVector2 SpawnOrigin, int ActorSize) {
        super(Sheet, SpawnOrigin, ActorSize);

    }

    @Override
    protected void PreConstruct() {
        super.PreConstruct();
        MoveRightAnimIndex = 32;
        MoveLeftAnimIndex = MoveRightAnimIndex + 1;
        MoveDownAnimIndex = MoveRightAnimIndex;
        MoveUpAnimIndex = MoveRightAnimIndex;
        Idle = 16;
        SpawnMontageIndex = 38;
        Acceleration=60f;
        IsGoingRight=true;

    }

    /**
     * Process movement
     *
     * @param DeltaTime the time since last call
     */
    protected void ProcessMovement(double DeltaTime) {

    }

    @Override
    public void Tick(double DeltaTime) {

        float deltaS = (float) (DeltaTime/ UDataContainer.BILLION);

        dx = Acceleration * deltaS;

        ActorAnimationSequence.Tick(DeltaTime);
        PerformAnimation();
        ProcessMovement(DeltaTime);

        if (!getActorAnimationSequence().isPlayingMontage()) {
            if (!ActorCollision.IsCollidingWithWorld(dx, 0)) {
                //Dungeon1.MapPosition.XPos+=dx ;
                WorldPosition.XPos += dx;
            }
            if (!ActorCollision.IsCollidingWithWorld(0, dy)) {
                //Dungeon1.MapPosition.YPos += dy ;
                WorldPosition.YPos += dy;
            }

        } else {
            //we are playing an animation
        }


    }

    @Override
    public void Input(InputMouseHandler Mouse, InputKeyHandler Key) {

    }

    @Override
    public void PrepareSceneRender(Graphics2D g) {
        if (URPGPanel.DEBUG) {
            //debug player image
            g.setColor(new Color(0, 194, 255));
            g.drawRect((int) (WorldPosition.GetScreenPosition().XPos), (int) (WorldPosition.GetScreenPosition().YPos), (int) ActorSize, (int) ActorSize);

            //debug player collision
            g.setColor(new Color(0, 255, 9));
            g.drawRect((int) (WorldPosition.GetScreenPosition().XPos + ActorCollision.getCollisionXOffset()), (int) (WorldPosition.GetScreenPosition().YPos + ActorCollision.getCollisionYOffset()), (int) ActorCollision.getCollisionWidth(), (int) ActorCollision.getCollisionHeight());

            //debug player attack box
            g.setColor(new Color(255, 0, 0));
            g.drawRect((int) (HitCollision.getPosition().GetScreenPosition().XPos + HitCollision.getCollisionXOffset()), (int) (HitCollision.getPosition().GetScreenPosition().YPos + HitCollision.getCollisionYOffset()), (int) HitCollision.getCollisionWidth(), (int) HitCollision.getCollisionHeight());

        }


        g.drawImage(ActorAnimationSequence.getCurrentAnimationSequence(), (int) WorldPosition.GetScreenPosition().XPos, (int) WorldPosition.GetScreenPosition().YPos, ActorSize, ActorSize, null);
    }


//    @Override
//    public void OnAttributeChanged(FScalableFloat NewAttribute, FScalableFloat OldAttribute) {
//        if (NewAttribute.getCurrentValue()>=20){
//            NewAttribute.setCurrentValue(20);
//            GetAbilitySystemComponent().getAttributeSet().ChangeAttribute(new FScalableFloat(20, NewAttribute.getName()),false);
//        }
//        System.out.printf("Attribute %s Changed: %f -> %f\n", NewAttribute.getName(), OldAttribute.getCurrentValue(), NewAttribute.getCurrentValue());
//        //System.out.printf("%s %s \n",NewAttribute,OldAttribute);
//    }

    /**
     * Throw Callback to caller that a certain point of an animation Has Been Reached
     *
     * @param AnimationIndex
     * @param NotifyName
     */
    @Override
    public void OnAnimationNotify(int AnimationIndex, String NotifyName) {

    }
}
