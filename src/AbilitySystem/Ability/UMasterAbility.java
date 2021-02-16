/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.Ability;

import AbilitySystem.BaseActor.AASBaseActor;
import AbilitySystem.GameplayEffects.UMasterGameplayEffect;
import AbilitySystem.Utils.UContextHandle;

/**
 * Gameplay Ability
 * <p>
 * Abilities are objects that contains the logic of an ability behavior
 * they should only be instantiated once and then run multiple checks before
 * executing it.
 * <p>
 * Abilities may have tags to identify them so that it may block or cancel
 * other abilities.
 * <p>
 * An ability can have an effect that represents a cost and/or have an effect
 * that represents a cooldown.
 * <p>
 * The cooldown reads the GE {@link UMasterGameplayEffect} and checks for the effect's duration.
 * <p>
 * Any attempt of executing this ability while its in cooldown or the target has insufficient resources
 * will block the ability execution.
 * <p>
 * GA - Gameplay Ability
 */
public abstract class UMasterAbility {

    /**
     * The ability context
     */
    private UContextHandle Context;

    /**
     * tells if this ability has ended
     */
    private boolean AbilityEnded = false;


    /**
     * Hide default constructor
     */
    private UMasterAbility() {
    }

    /**
     * Initialize the ability with a context
     *
     * @param context The ability Context
     */
    public UMasterAbility(UContextHandle context) {
        Context = context;
    }


    /**
     * Starts the ability
     */
    public final void InvokeAbility() {
        AbilityEnded = false;
        OnAbilityBegin();
    }

    /**
     * terminated an ability
     */
    protected final void EndAbility() {
        AbilityEnded = true;
        OnAbilityEnd();
    }

    /**
     * Contains the ability Logic
     */
    protected abstract void OnAbilityBegin();

    /**
     * Called when ability has finished executing
     */
    protected abstract void OnAbilityEnd();

    /**
     * Check if ability has ended
     *
     * @return true if the ability has stopped all the logic
     */
    public boolean isAbilityEnded() {
        return AbilityEnded;
    }

    /**
     * Gets the ability Caster
     *
     * @return the caster
     */
    protected final AASBaseActor GetOwner() {
        return Context.Owner;
    }

    /**
     * Applies an effect to the owner
     *
     * @param Effect the effect to apply
     */
    protected final void ApplyGameplayEffectToOwner(Class<? extends UMasterGameplayEffect> Effect) {
        GetOwner().GetAbilitySystemComponent().ApplyGameplayEffectToSelf(Effect);
    }


}
