/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.GameplayEffects.Utils;

/**
 * Data container for each gameplay effect
 */
public class UGameplayEffectData {

    /**
     * @see EGameplayEffectOperationType
     */
    private final EGameplayEffectDurationType DurationType;
    /**
     * The period is used on effects based on duration
     * Reapplies the effect to the target every X seconds
     * where X is the period just like a "poison" effects
     * which would hurt the target every 1 second.
     * <p>
     * A value of X<=0 means not period
     */
    private final float Period;
    /**
     * If this effect is duration based how long does it last?
     */
    private final float Duration;

    /**
     * Do not allow default instantiating
     */
    private UGameplayEffectData() {
        DurationType = EGameplayEffectDurationType.INSTANT;
        Period = 0;
        Duration = 0;
    }

    /**
     * Instantiate a new Gameplay Effect Data container
     *
     * @param durationType the duration type
     * @param period       the period of this effect
     * @param duration     the duration of this effect in case its duration based
     */
    public UGameplayEffectData(EGameplayEffectDurationType durationType, float period, float duration) {
        DurationType = durationType;
        Period = period;
        Duration = duration;
    }

    //getters
    public EGameplayEffectDurationType getDurationType() {
        return DurationType;
    }

    public float getPeriod() {
        return Period;
    }

    public float getDuration() {
        return Duration;
    }
}
