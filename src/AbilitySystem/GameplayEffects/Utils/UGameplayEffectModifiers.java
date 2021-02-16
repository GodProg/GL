/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.GameplayEffects.Utils;

import AbilitySystem.Tags.FGameplayTag;
import AbilitySystem.Utils.FScalableFloat;
import CoreUtils.FQuadruplet;

import java.util.ArrayList;


/**
 * This class contains data about the affected attributes on the target, Such as:
 * which operation are we performing ? (addition/multiplication/...)
 * which attribute are we targeting?
 * For how much are we Striking?
 */
public class UGameplayEffectModifiers {
    /**
     * @see EGameplayEffectOperationType
     */
    private final EGameplayEffectOperationType OperationType;

    /**
     * The attribute we're targeting
     */
    private final FScalableFloat AffectedAttribute;

    /**
     * The magnitude we're applying in the target's attribute
     */
    private final float ModifierMagnitude;

    /**
     * The list of immunity tags, if the target contains any of these tags this modifier will not be applied
     */
    private final ArrayList<FGameplayTag> TargetImmuneTags;

    /**
     * Stores how much we've applied to the target's attribute so we can restore it later in case our Gameplay Effect is duration based
     */
    private float AppliedMagnitude = 0;

    /**
     * The Data to capture from the source Actor
     * <p>
     * This quadruplet is of the form (a,b,c,d) where abc are coefficients and d is the attribute to capture
     */
    private final FQuadruplet<Float, Float, Float, FScalableFloat> AttributeBasedData;

    /**
     * tells if the attribute to capture should be captured from the source, if this is false it will be captured from the target
     */
    private final boolean CaptureFromSource;

    /**
     * Default constructor
     */
    private UGameplayEffectModifiers() {
        ModifierMagnitude = 0;
        OperationType = EGameplayEffectOperationType.ADD;
        TargetImmuneTags = null;
        AffectedAttribute = null;
        AttributeBasedData = null;
        CaptureFromSource = true;
    }


    /**
     * Constructor
     *
     * @param operationType      the operation we're performing
     * @param modifierMagnitude  the amount we're applying
     * @param affectedAttribute  the attribute we're targeting
     * @param targetImmuneTags   the immunity tags
     * @param attributeBasedData the attribute to capture (Coefficient,PreMultiplyAdditiveValue, PostMultiplyAdditive, [captured attribute])
     */
    public UGameplayEffectModifiers(EGameplayEffectOperationType operationType,
                                    float modifierMagnitude, FScalableFloat affectedAttribute,
                                    ArrayList<FGameplayTag> targetImmuneTags, FQuadruplet<Float, Float, Float, FScalableFloat> attributeBasedData, boolean captureFromSource) {

        OperationType = operationType;
        ModifierMagnitude = modifierMagnitude;
        AffectedAttribute = affectedAttribute;
        TargetImmuneTags = targetImmuneTags;
        AttributeBasedData = attributeBasedData;
        CaptureFromSource = captureFromSource;
    }


    //getters/setters


    public FQuadruplet<Float, Float, Float, FScalableFloat> getAttributeBasedData() {
        return AttributeBasedData;
    }

    public boolean isCaptureFromSource() {
        return CaptureFromSource;
    }

    public EGameplayEffectOperationType getOperationType() {
        return OperationType;
    }

    public float getModifierMagnitude() {
        return ModifierMagnitude;
    }

    public ArrayList<FGameplayTag> getTargetImmuneTags() {
        return TargetImmuneTags;
    }

    public FScalableFloat getAffectedAttribute() {
        return AffectedAttribute;
    }

    public float getAppliedMagnitude() {
        return AppliedMagnitude;
    }


    /**
     * Adds up to the stored application to be restored afterwards
     *
     * @param appliedMagnitude the new application amount
     */
    public void AddAppliedMagnitude(float appliedMagnitude) {
        AppliedMagnitude += appliedMagnitude;
    }

    @Override
    public String toString() {
        return "UGameplayEffectModifiers{" +
                "OperationType=" + OperationType +
                ", AffectedAttribute=" + AffectedAttribute +
                ", ModifierMagnitude=" + ModifierMagnitude +
                ", TargetImmuneTags=" + TargetImmuneTags +
                ", AppliedMagnitude=" + AppliedMagnitude +
                ", AttributeBasedData=" + AttributeBasedData +
                ", CaptureFromSource=" + CaptureFromSource +
                '}';
    }
}
