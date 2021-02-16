/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Heroes.Player.Classes.Tank.TankClass;

import CoreUtils.FVector2;
import GraFX.USpriteSheet;
import Heroes.Player.Base.APlayerParentDummy;
import Heroes.Player.Classes.Tank.Attributes.RandomAttr;

public class TankClass extends APlayerParentDummy {


    private TankClass() {
        super(null, null, 0);

    }

    public TankClass(USpriteSheet uSpriteSheet, FVector2 fVector2, int size) {
        super(uSpriteSheet, fVector2, size);
        //create an attribute set
        GetAbilitySystemComponent().CreatedAttributeSet(RandomAttr.class);

        //adapt collision position and size
        ActorCollision.setCollisionHeight(ActorSize * 0.2f);
        ActorCollision.setCollisionWidth(ActorSize * 0.25f);
        ActorCollision.setCollisionYOffset(ActorSize * 0.65f);
        ActorCollision.setCollisionXOffset(ActorSize * 0.38f);

    }

}
