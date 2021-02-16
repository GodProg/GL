/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Heroes.Player.Classes.Tank.Effects;

import AbilitySystem.GameplayEffects.UMasterGameplayEffect;
import AbilitySystem.GameplayEffects.Utils.EGameplayEffectDurationType;
import AbilitySystem.GameplayEffects.Utils.EGameplayEffectOperationType;
import AbilitySystem.GameplayEffects.Utils.UGameplayEffectData;
import AbilitySystem.GameplayEffects.Utils.UGameplayEffectModifiers;
import AbilitySystem.Utils.FScalableFloat;
import AbilitySystem.Utils.UContextHandle;
import CoreUtils.FQuadruplet;
import CoreUtils.UDataContainer;

public class PlayerHeal extends UMasterGameplayEffect {

    public PlayerHeal(UContextHandle context) {
        super(context);
    }

    @Override
    protected void MakeModifiers() {
        AddDynamicModifiers(new UGameplayEffectModifiers(EGameplayEffectOperationType.ATTRIBUTE_BASED, 15.0f,
                new FScalableFloat(0, UDataContainer.ATTRIBUTE_Health), null, new FQuadruplet<>(1.0f, 0.0f,
                0.0f, new FScalableFloat(0, "HealthRegen")), true));
    }


    @Override
    public UGameplayEffectData MakeEffectData() {
        return new UGameplayEffectData(EGameplayEffectDurationType.INFINITE, 1, 5);
    }
}
