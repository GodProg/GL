/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.Utils;

import CoreUtils.UDataContainer;

/**
 * A flexible float used on the Ability System, Mostly to represent attributes.
 * It contains a base value and a current value, the base value is the non
 * permanently modified base value of this float, where as the current value
 * represents the actual current value of this float accounting the temporary
 * values and bonuses.
 *
 * @see AbilitySystem.AttributeSet.UMasterAttributeSet
 * @see AbilitySystem.Component.UAbilitySystemComponent
 */
public final class FScalableFloat {
    /**
     * void name
     */
    public static final String NAME_NONE = UDataContainer.NAME_NONE;
    /**
     * The name of this float
     */
    private String Name;
    /**
     * This float's base value
     */
    private float BaseValue;
    /**
     * this floats current value
     */
    private float CurrentValue;

    /**
     * Default constructor
     */
    private FScalableFloat() {

    }

    /**
     * Construct with a base value and a name
     *
     * @param baseValue the base float value
     * @param Name      this float's name
     */
    public FScalableFloat(float baseValue, String Name) {
        BaseValue = baseValue;
        CurrentValue = baseValue;
        this.Name = Name;
    }

    /**
     * Check if this float instance is valid
     *
     * @return
     */
    public boolean IsValid() {
        return !getName().equals(NAME_NONE);
    }


    public String getName() {
        return Name;
    }

    /**
     * Make a new float from another float
     *
     * @param Other the other float
     */
    public FScalableFloat(FScalableFloat Other) {
        if (Other != null) {
            BaseValue = Other.getBaseValue();
            CurrentValue = Other.getCurrentValue();
            this.Name = Other.getName();
        } else {
            BaseValue = 0;
            CurrentValue = 0;
            this.Name = NAME_NONE;
        }
    }

    public float getBaseValue() {
        return BaseValue;
    }

    public float getCurrentValue() {
        return CurrentValue;
    }

    public float getBonus() {
        return CurrentValue - BaseValue;
    }

    public void setBaseValue(float baseValue) {
        BaseValue = baseValue;
    }

    public void setCurrentValue(float currentValue) {
        CurrentValue = currentValue;
    }

    @Override
    public String toString() {
        return "FScalableFloat{" +
                "Name='" + Name + '\'' +
                ", BaseValue=" + BaseValue +
                ", CurrentValue=" + CurrentValue +
                '}';
    }
}
