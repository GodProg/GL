/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.Component;


import AbilitySystem.AttributeSet.UMasterAttributeSet;
import AbilitySystem.BaseActor.AASBaseActor;
import AbilitySystem.GameplayEffects.UMasterGameplayEffect;
import AbilitySystem.Implementation.IAttributeSetCallable;
import AbilitySystem.Tags.UTagContainer;
import AbilitySystem.Utils.FScalableFloat;
import AbilitySystem.Utils.UContextHandle;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


/**
 * Ability system Component
 * <p>
 * This ASC is in charge of handling different aspects of this system of the object
 * its attached to.
 * <p>
 * This component is the core of the whole system, it handles the attribute sets,
 * gameplay effects and gameplay abilities.
 * <p>
 * For generic purposes this component handles effects, abilities and attribute
 * sets instantiation by class and executes them with a specific method,
 * this way the component can execute any user defined effect and/or ability.
 * <p>
 * ASC - Ability System Component
 * <p>
 * In order for an ability to be executed it must be granted first, else the ability
 * will never be instantiated.
 *
 * @see UMasterAttributeSet
 * @see AbilitySystem.Ability.UMasterAbility
 * @see UMasterGameplayEffect
 */
public class UAbilitySystemComponent {

    /**
     * The owner of the ASC
     */
    private AASBaseActor Owner = null;

    /**
     * The attribute set
     */
    private UMasterAttributeSet AttributeSet = null;

    /**
     * CallBack object to be invoked when an attribute has changed
     */
    private IAttributeSetCallable CallBackObject = null;

    /**
     * Tag container
     */
    private UTagContainer TagContainer = null;

    private HashMap<Class<? extends UMasterGameplayEffect>, UMasterGameplayEffect> AllEffects = new HashMap<>();

    /**
     * Default constructor
     */
    public UAbilitySystemComponent() {
        TagContainer = new UTagContainer();
    }

    /**
     * Constructs and assigns the ASC owner
     *
     * @param owner the new ASC owner
     */
    public UAbilitySystemComponent(AASBaseActor owner) {
        Owner = owner;
        TagContainer = new UTagContainer();

    }

    /**
     * Initialises with new Owner and Callback object for attribute change listener
     *
     * @param owner          the new owner
     * @param CallbackObject the new object listening for attribute changes
     */
    public UAbilitySystemComponent(AASBaseActor owner, IAttributeSetCallable CallbackObject) {
        Owner = owner;
        TagContainer = new UTagContainer();
        this.CallBackObject = CallbackObject;
    }

    /**
     * Constructs new Attribute set based on a user defined class
     *
     * @param Attr the attribute set class
     * @return the newly constructed attribute set Object. User may cast to its own class afterwards
     */
    public UMasterAttributeSet CreatedAttributeSet(Class<? extends UMasterAttributeSet> Attr) {

        try {
            AttributeSet = Attr.getConstructor(IAttributeSetCallable.class).newInstance(CallBackObject);
//            System.out.println(AttributeSet);
            //AttributeSet = new UMasterAttributeSet(this);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        return AttributeSet;

    }

    /**
     * Make context for abilities and effects
     *
     * @param Target the target to be added on the context
     * @return return the context
     */
    public UContextHandle MakeContext(AASBaseActor Target) {
        return new UContextHandle(Owner, Target);
    }

    /**
     * Applies a specific Gameplay effect to the owner of this ASC
     *
     * @param EffectClass the gameplay effect class to be instantiated
     */
    public void ApplyGameplayEffectToSelf(Class<? extends UMasterGameplayEffect> EffectClass) {
        if (Owner != null) {
            UContextHandle Context = MakeContext(Owner);
            try {
                if (AllEffects.containsKey(EffectClass)) {

                    AllEffects.get(EffectClass).RemoveGameplayEffect();
                    AllEffects.get(EffectClass).ExecuteEffect();
                } else {

                    //instantiate an object from its class
                    UMasterGameplayEffect effect = EffectClass.getConstructor(UContextHandle.class).newInstance(Context);
                    AllEffects.put(EffectClass, effect);
                    effect.ExecuteEffect();
                }

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Somehow the owner of this ASC is null!");
        }

    }

    /**
     * Removes a specific Gameplay effect to the owner of this ASC
     *
     * @param EffectClass the gameplay effect class to be removed
     */
    public void RemoveGameplayEffect(Class<? extends UMasterGameplayEffect> EffectClass) {
        if (AllEffects.containsKey(EffectClass)) {
            AllEffects.get(EffectClass).RemoveGameplayEffect();
        }
    }

    /**
     * Applies a specific Gameplay effect to a target
     *
     * @param Target      the target to apply the effect
     * @param Context     the effect context
     * @param EffectClass the gameplay effect class to be instantiated
     */
    public void ApplyGameplayEffectToTarget(AASBaseActor Target, UContextHandle Context, Class<? extends UMasterGameplayEffect> EffectClass) {
        if (Target != null) {
            UAbilitySystemComponent TargetASC = Target.GetAbilitySystemComponent();
            if (Context == null) {
                Context = MakeContext(Target);
            }
            try {

                if (AllEffects.containsKey(EffectClass)) {
                    AllEffects.get(EffectClass).RemoveGameplayEffect();
                    AllEffects.get(EffectClass).ExecuteEffect();
                } else {
                    //instantiate an object from its class
                    UMasterGameplayEffect effect = EffectClass.getConstructor(UContextHandle.class).newInstance(Context);
                    AllEffects.put(EffectClass, effect);
                    effect.ExecuteEffect();
                }

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Target is null!");
        }
    }


    //getters
    public AASBaseActor getOwner() {
        return Owner;
    }

    public float GetAttributeValue(String Attribute) {
        FScalableFloat F = AttributeSet.GetAttribute(Attribute);
        return F.getCurrentValue();
    }


    public UMasterAttributeSet getAttributeSet() {
        return AttributeSet;
    }

    public UTagContainer getTagContainer() {
        return TagContainer;
    }

    //setters
    public void setOwner(AASBaseActor owner) {
        Owner = owner;
    }

    public void setCallBackObject(IAttributeSetCallable callBackObject) {
        CallBackObject = callBackObject;
    }

}
