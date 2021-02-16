/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Heroes.Player.Classes.Tank.Attributes;

import AbilitySystem.AttributeSet.UMasterAttributeSet;
import AbilitySystem.Implementation.IAttributeSetCallable;
import AbilitySystem.Utils.FScalableFloat;

public class RandomAttr extends UMasterAttributeSet {


    public RandomAttr(IAttributeSetCallable callable) {
        super(callable);
    }

    @Override
    protected void MakeModifiers() {
        for (FScalableFloat attr : UMasterAttributeSet.LoadFromAttrFile("Resources/Attributes/Attributes.attr")) {
            AddDynamicAttribute(attr);
        }

        /*
        AddDynamicAttribute(new FScalableFloat(15, UDataContainer.ATTRIBUTE_Health));
        AddDynamicAttribute(new FScalableFloat(10, UDataContainer.ATTRIBUTE_HealthMax));
        AddDynamicAttribute(new FScalableFloat(35, UDataContainer.ATTRIBUTE_Mana));
        */
    }

    @Override
    public void OnAttributeChanged(FScalableFloat NewAttribute, FScalableFloat OldAttribute) {
        if (NewAttribute.getCurrentValue() >= GetAttribute("MaxHealth").getCurrentValue() && NewAttribute.getName().equals("Health")) {
            NewAttribute.setCurrentValue(GetAttribute("MaxHealth").getCurrentValue());
        }

        if (!NewAttribute.getName().equals("Health")) {
            System.out.printf("Attribute %s Changed: %f -> %f\n", NewAttribute.getName(), OldAttribute.getCurrentValue(), NewAttribute.getCurrentValue());

        }
    }
}
