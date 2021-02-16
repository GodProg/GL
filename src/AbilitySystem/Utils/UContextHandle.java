/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.Utils;

import AbilitySystem.BaseActor.AASBaseActor;

/**
 * Context class
 */
public class UContextHandle {
    /**
     * owner
     */
    public final AASBaseActor Owner;
    /**
     * Target
     */
    public final AASBaseActor TargetOwner;

    /**
     * Default constructor
     */
    private UContextHandle() {

        TargetOwner = null;
        Owner = null;
    }

    /**
     * Default constructor
     *
     * @param owner       the owner
     * @param targetOwner the target
     */
    public UContextHandle(AASBaseActor owner, AASBaseActor targetOwner) {
        Owner = owner;
        TargetOwner = targetOwner;
    }

    @Override
    public String toString() {
        return "UContextHandle{" +
                "Owner=" + Owner +
                ", TargetOwner=" + TargetOwner +
                '}';
    }
}
