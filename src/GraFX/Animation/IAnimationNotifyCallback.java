/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GraFX.Animation;

/**
 * Attach to object that are animated and are waiting animation notifies
 */
public interface IAnimationNotifyCallback {

    /**
     * Throw Callback to caller that a certain point of an animation Has Been Reached
     */
    void OnAnimationNotify(int AnimationIndex, String NotifyName);

}
