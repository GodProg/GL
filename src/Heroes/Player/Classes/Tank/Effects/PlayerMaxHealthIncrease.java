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

public class PlayerMaxHealthIncrease extends UMasterGameplayEffect {

    public PlayerMaxHealthIncrease(UContextHandle context) {
        super(context);
    }

    @Override
    protected void MakeModifiers() {
        AddDynamicModifiers(new UGameplayEffectModifiers(EGameplayEffectOperationType.ADD, 5,
                new FScalableFloat(0, "MaxHealth"), null, new FQuadruplet<>(1.0f, 0.0f,
                0.0f, new FScalableFloat(0, "MaxHealth")), true));

        AddDynamicModifiers(new UGameplayEffectModifiers(EGameplayEffectOperationType.ADD, 5,
                new FScalableFloat(0, "Mana"), null, new FQuadruplet<>(1.0f, 0.0f,
                0.0f, new FScalableFloat(0, "Mana")), true));

    }


    @Override
    public UGameplayEffectData MakeEffectData() {
        return new UGameplayEffectData(EGameplayEffectDurationType.INFINITE, 0, 5);
    }
}
