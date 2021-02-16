/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package AbilitySystem.Implementation;

import AbilitySystem.Utils.FScalableFloat;

/**
 * Interface to implement On attribute changed callback
 */
public interface IAttributeSetCallable {

    void OnAttributeChanged(FScalableFloat NewAttribute, FScalableFloat OldAttribute);

}
