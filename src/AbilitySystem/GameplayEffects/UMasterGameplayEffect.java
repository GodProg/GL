/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.GameplayEffects;

import AbilitySystem.Component.UAbilitySystemComponent;
import AbilitySystem.GameplayEffects.Utils.EGameplayEffectDurationType;
import AbilitySystem.GameplayEffects.Utils.UGameplayEffectData;
import AbilitySystem.GameplayEffects.Utils.UGameplayEffectModifiers;
import AbilitySystem.Utils.FScalableFloat;
import AbilitySystem.Utils.UContextHandle;
import CoreUtils.UDataContainer;

import java.util.ArrayList;

/**
 * Gameplay Effect
 * <p>
 * This class is in charge of handling the modifications of attributes between ASCs
 * either through direct application or through a GA {@link AbilitySystem.Ability.UMasterAbility}.
 * <p>
 * GE - Gameplay Effect
 * GA - Gameplay Ability
 *
 * @see AbilitySystem.AttributeSet.UMasterAttributeSet
 * @see UAbilitySystemComponent
 */
public abstract class UMasterGameplayEffect implements Runnable {
    /**
     * Effect's context
     *
     * @see UContextHandle
     */
    private UContextHandle Context;
    /**
     * This effects data
     *
     * @see UGameplayEffectData
     */
    private UGameplayEffectData Data = null;

    /**
     * The list of all modifiers of this effect
     */
    private ArrayList<UGameplayEffectModifiers> Modifiers = new ArrayList<>();

    /**
     * Tells if this gameplay Effects is done
     * Used on duration based effects
     */
    private boolean IsDone = false;


    /**
     * Default constructor
     */
    private UMasterGameplayEffect() {
    }

    /**
     * Instantiate GE with context
     *
     * @param context the GE context
     */
    public UMasterGameplayEffect(UContextHandle context) {
        Context = context;
        MakeModifiers();
        Data = MakeEffectData();

    }

    /**
     * Allow children to override this method and add modifiers here
     */
    protected abstract void MakeModifiers();


    /**
     * Dynamically adds a new modifier to this GE
     *
     * @param Modifier the new modifier
     */
    public final void AddDynamicModifiers(UGameplayEffectModifiers Modifier) {
        Modifiers.add(Modifier);
    }

    /**
     * Allow children to manipulate the GE data
     *
     * @return the new effect Data
     * @see UGameplayEffectData
     */
    protected abstract UGameplayEffectData MakeEffectData();

    /**
     * Completely removes this gameplay effect
     * <p>
     * Removing an effect implies removing all the previously applied magnitudes to each attribute,
     * at the exception of instant effects, instant effects apply changes permanently to attributes
     */
    public void RemoveGameplayEffect() {
        IsDone = true;
        if (Data.getDurationType() != EGameplayEffectDurationType.INSTANT) {
            for (UGameplayEffectModifiers modifier : Modifiers) {
                RemoveModifier(modifier);
            }
        }

    }

    /**
     * Remove temporary values from the applied effect
     */
    private void StopEffectExecution() {
        if (Data.getDurationType() == EGameplayEffectDurationType.DURATION) {
            if (Data.getPeriod() <= 0) {
                for (UGameplayEffectModifiers modifier : Modifiers) {
                    RemoveModifier(modifier);
                }
            }
        }
    }

    //Creates a Thread to run on duration based effects
    @Override
    public void run() {
        double StartTime = System.nanoTime();

        //Apply the effect Once
        for (UGameplayEffectModifiers modifier : Modifiers) {
            DoInstantJob(modifier);
        }
        while (!IsDone) {
            if (Data != null) {

                /*
                 *this duration type is limitless therefore we only check if we should reapply this effect
                 *We could use this in a scenario where the player has an health regeneration effect
                 */
                if (Data.getDurationType() == EGameplayEffectDurationType.INFINITE) {
                    //if we should apply this effect every X Seconds
                    if (Data.getPeriod() > 0) {
                        if (((System.nanoTime() - StartTime) / UDataContainer.BILLION) > Data.getPeriod()) {
                            for (UGameplayEffectModifiers modifier : Modifiers) {
                                DoInstantJob(modifier);
                            }
                            try {
                                Thread.sleep((long) Data.getPeriod() * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        //we have applied our infinite effect, now we wait until its removed manually
                        IsDone = true;
                    }
                    //we are duration based
                } else {
                    //if we should apply this effect every X Seconds
                    if (Data.getPeriod() > 0 && ((System.nanoTime() - StartTime) / UDataContainer.BILLION) > Data.getPeriod()) {
                        for (UGameplayEffectModifiers modifier : Modifiers) {
                            DoInstantJob(modifier);
                        }
                        try {
                            Thread.sleep((long) Data.getPeriod() * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //check if the effect time has expired
                    if (((System.nanoTime() - StartTime) / UDataContainer.BILLION) > Data.getDuration()) {
                        IsDone = true;
                    }
                }
            } else {
                IsDone = true;
            }
        }
        //Since were duration based we want to remove all effects at the end of the execution
        StopEffectExecution();
    }

    /**
     * The function called to execute the effect
     */
    public final void ExecuteEffect() {

        if (Data == null) {
            System.out.printf("Gameplay Effect Data of [%s] was Invalid, falling back to defaults\n", this.getClass().toString());
            Data = new UGameplayEffectData(EGameplayEffectDurationType.INSTANT, 0, 0);
        }

        switch (Data.getDurationType()) {
            case INSTANT -> {
                System.out.println("Duration Base Effect Triggered");
                for (UGameplayEffectModifiers modifier : Modifiers) {
                    DoInstantJob(modifier);
                }

            }
            case DURATION -> {
                System.out.println("Duration Base Effect Triggered");
                DoDurationJob();

            }
            case INFINITE -> {
                System.out.println("Infinite Effect triggered ");
                DoInfiniteJob();

            }
            default -> {
                System.out.println("ERROR: invalid DurationType");
            }

        }


    }

    /**
     * Checks if the Target has valid data
     *
     * @param modifier the modifier to check
     * @return true if the target has valid data
     */
    private boolean Check(UGameplayEffectModifiers modifier) {
        if (Context.TargetOwner != null && Context.Owner != null) {
            if (Context.TargetOwner.GetAbilitySystemComponent() != null && Context.Owner.GetAbilitySystemComponent() != null) {
                if (Context.TargetOwner.GetAbilitySystemComponent().getAttributeSet() != null && Context.Owner.GetAbilitySystemComponent().getAttributeSet() != null) {
                    return true;
                } else System.out.println("GE Failed, NULL AttributeSet");
            } else System.out.println("GE Failed, NULL ASC");
        } else System.out.println("GE Failed, NULL Target or Owner");
        return false;
    }

    /**
     * Check if target is immune
     *
     * @param modifier the modifier to check
     * @return true if target is immune
     */
    private boolean IsTargetImmune(UGameplayEffectModifiers modifier) {
        boolean IsImmune = false;
        UAbilitySystemComponent TargetASC = Context.TargetOwner.GetAbilitySystemComponent();
        if (TargetASC != null) {
            IsImmune = TargetASC.getTagContainer().HasAnyTag(modifier.getTargetImmuneTags());
        }
        return IsImmune;
    }

    /**
     * Checks if the targeting attribute exists on the target
     *
     * @param modifier the modifier
     * @return true if the attribute exists
     */
    private boolean DoesAttributeExist(UGameplayEffectModifiers modifier) {
        boolean AttributeExist = false;
        UAbilitySystemComponent TargetASC = Context.TargetOwner.GetAbilitySystemComponent();
        if (TargetASC != null) {
            AttributeExist = (TargetASC.getAttributeSet().GetAttribute(modifier.getAffectedAttribute().getName()).IsValid());
        }
        return AttributeExist;
    }

    /**
     * Performs job of instant effects
     *
     * @param modifier the modifier to process
     */
    private void DoInstantJob(UGameplayEffectModifiers modifier) {
        if (Check(modifier)) {
            if (!IsTargetImmune(modifier)) {
                if (DoesAttributeExist(modifier)) {
                    UAbilitySystemComponent TargetASC = Context.TargetOwner.GetAbilitySystemComponent();
                    UAbilitySystemComponent OwnerASC = Context.Owner.GetAbilitySystemComponent();

                    switch (modifier.getOperationType()) {
                        case ADD -> {
                            FScalableFloat AffectedAttribute = TargetASC.getAttributeSet().GetAttribute(modifier.getAffectedAttribute().getName());
                            float base = AffectedAttribute.getBaseValue();
                            float curr = AffectedAttribute.getCurrentValue();
                            base += modifier.getModifierMagnitude();
                            curr += modifier.getModifierMagnitude();

                            modifier.AddAppliedMagnitude(base - AffectedAttribute.getBaseValue());

                            AffectedAttribute.setBaseValue(base);
                            AffectedAttribute.setCurrentValue(curr);
                            TargetASC.getAttributeSet().ChangeAttribute(AffectedAttribute);

                            break;
                        }
                        case DIVIDE -> {
                            FScalableFloat AffectedAttribute = TargetASC.getAttributeSet().GetAttribute(modifier.getAffectedAttribute().getName());
                            float base = AffectedAttribute.getBaseValue();
                            float curr = AffectedAttribute.getCurrentValue();
                            base /= modifier.getModifierMagnitude();
                            curr /= modifier.getModifierMagnitude();

                            modifier.AddAppliedMagnitude(base - AffectedAttribute.getBaseValue());

                            AffectedAttribute.setBaseValue(base);
                            AffectedAttribute.setCurrentValue(curr);
                            TargetASC.getAttributeSet().ChangeAttribute(AffectedAttribute);
                            break;
                        }
                        case MULTIPLY -> {
                            FScalableFloat AffectedAttribute = TargetASC.getAttributeSet().GetAttribute(modifier.getAffectedAttribute().getName());
                            float base = AffectedAttribute.getBaseValue();
                            float curr = AffectedAttribute.getCurrentValue();
                            base *= modifier.getModifierMagnitude();
                            curr *= modifier.getModifierMagnitude();

                            modifier.AddAppliedMagnitude(base - AffectedAttribute.getBaseValue());

                            AffectedAttribute.setBaseValue(base);
                            AffectedAttribute.setCurrentValue(curr);
                            TargetASC.getAttributeSet().ChangeAttribute(AffectedAttribute);
                            break;
                        }
                        case OVERRIDE -> {
                            FScalableFloat AffectedAttribute = TargetASC.getAttributeSet().GetAttribute(modifier.getAffectedAttribute().getName());

                            //modifier.AddAppliedMagnitude(0);

                            AffectedAttribute.setBaseValue(modifier.getModifierMagnitude());
                            AffectedAttribute.setCurrentValue(modifier.getModifierMagnitude());
                            TargetASC.getAttributeSet().ChangeAttribute(AffectedAttribute);
                            break;
                        }
                        case ATTRIBUTE_BASED -> {
                            if (modifier.getAttributeBasedData() != null) {
                                FScalableFloat AffectedAttribute = TargetASC.getAttributeSet().GetAttribute(modifier.getAffectedAttribute().getName());

                                /*
                                 * Here we use a special formula
                                 *
                                 * (Coefficient * (PreMultiplyAdditiveValue + [Evaluated Magnitude of captured attribute]) + PostMultiplyAdditive )
                                 */
                                float Coefficient = modifier.getAttributeBasedData().FirstValue;
                                float PreMultiplyAdditiveValue = modifier.getAttributeBasedData().SecondValue;
                                float PostMultiplyAdditive = modifier.getAttributeBasedData().ThirdValue;
                                FScalableFloat AttributeToCapture = modifier.getAttributeBasedData().FourthValue;

                                if (modifier.isCaptureFromSource()) {
                                    if (OwnerASC.getAttributeSet().GetAttribute(AttributeToCapture.getName()).IsValid()) {
                                        AttributeToCapture = OwnerASC.getAttributeSet().GetAttribute(AttributeToCapture.getName());

                                    } else {
                                        System.out.printf("Failed to apply gameplay effect : Attribute [%s] does not exist in [%s]\n", AttributeToCapture.getName(), TargetASC.getAttributeSet().getClass().getName());
                                        break;
                                    }
                                } else {
                                    if (TargetASC.getAttributeSet().GetAttribute(AttributeToCapture.getName()).IsValid()) {
                                        AttributeToCapture = TargetASC.getAttributeSet().GetAttribute(AttributeToCapture.getName());

                                    } else {
                                        System.out.printf("Failed to apply gameplay effect : Attribute [%s] does not exist in [%s]\n", AttributeToCapture.getName(), TargetASC.getAttributeSet().getClass().getName());
                                        break;
                                    }
                                }

                                float base = AffectedAttribute.getBaseValue();
                                float curr = AffectedAttribute.getCurrentValue();

                                float value = (Coefficient * (PreMultiplyAdditiveValue + AttributeToCapture.getCurrentValue()) + PostMultiplyAdditive);
                                base += value;
                                curr += value;

                                modifier.AddAppliedMagnitude(base - AffectedAttribute.getBaseValue());

                                AffectedAttribute.setBaseValue(base);
                                AffectedAttribute.setCurrentValue(curr);
                                TargetASC.getAttributeSet().ChangeAttribute(AffectedAttribute);

                            } else {
                                System.out.println("GE FATAL ERROR: Accessed NULL while reading Attribute to capture");
                            }
                        }
                        default -> System.out.println("Something Went wrong Applying the gameplayEffect!");
                    }

                } else {
                    System.out.printf("Failed to apply Gameplay Effect,Attribute [%s], Does not Exist in [%s]\n", modifier.getAffectedAttribute().getName(), Context.TargetOwner.GetAbilitySystemComponent().getAttributeSet().getClass());
                }

            } else {
                System.out.println("Target Was Immune To This Effect");
            }
        } else {
            System.out.println("ERROR: Failed to apply gameplay effect!");
        }
    }

    /**
     * Process modifiers that are duration based
     */
    private void DoDurationJob() {
        Thread T = new Thread(this);
        T.setPriority(Thread.NORM_PRIORITY);
        T.setName(this.getClass().getName());
        T.start();
    }

    private void RemoveModifier(UGameplayEffectModifiers modifier) {
        if (Check(modifier)) {
            if (DoesAttributeExist(modifier)) {
                UAbilitySystemComponent TargetASC = Context.TargetOwner.GetAbilitySystemComponent();
                FScalableFloat F = TargetASC.getAttributeSet().GetAttribute(modifier.getAffectedAttribute().getName());
                F.setBaseValue(F.getBaseValue() - modifier.getAppliedMagnitude());
                F.setCurrentValue(F.getCurrentValue() - modifier.getAppliedMagnitude());
                TargetASC.getAttributeSet().ChangeAttribute(F);

            } else {
                System.out.printf("Failed to remove Gameplay Effect,Attribute [%s], Does not Exist in [%s]\n", modifier.getAffectedAttribute().getName(), Context.TargetOwner.GetAbilitySystemComponent().getAttributeSet().getClass());
            }
        } else {
            System.out.println("ERROR: Failed to remove gameplay effect!");
        }
    }

    /**
     * Process modifiers that are infinite
     */
    private void DoInfiniteJob() {
        Thread T = new Thread(this);
        T.setPriority(Thread.NORM_PRIORITY);
        T.setName(this.getClass().getName());
        T.start();
    }


}
