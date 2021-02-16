/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GraFX.Animation;

import CoreUtils.FTuple;
import CoreUtils.UDataContainer;
import Heroes.Enemy.ANormalEnemy;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles animation logics
 * <p>
 * This class does not display any animation but simply processes the animations
 */
public class UAnimationSequence {

    /**
     * The current animation we're playing
     */
    private BufferedImage[] AnimationSequence;
    /**
     * The frame index we're currently on on our animation sequence
     */
    private int CurrentAnimFrame;
    /**
     * The max frames of the current animation
     */
    private int MaxFrames;

    /**
     * Tells if we're playing an animation montage and not a simple animation
     */
    private boolean IsPlayingMontage = false;

    /**
     * The index of the animation we're playing
     * This information only serves a purpose for animation notifies
     */
    private int PlayingAnimationIndex = -1;

    /**
     * The object we should call when we've reached an animation notify
     *
     * @see IAnimationNotifyCallback
     */
    private IAnimationNotifyCallback Caller = null;

    /**
     * Stores animation notifies
     */
    private HashMap<String, FTuple<Integer, Integer>> AnimationNotifies;

    /**
     * Counts the real elapsed time for smooth animations independent of the framerate
     */
    private double RealTime = 0;

    /**
     * Default animation delay in milliseconds
     * <p>
     * Go to next animation frame every X milliseconds
     * <p>
     * 83 is default value, it means "go to next frame every 83ms"
     */
    private int AnimDelay = 500;


    /**
     * Default Constructor
     */
    public UAnimationSequence() {
        AnimationNotifies = new HashMap<String, FTuple<Integer, Integer>>();
    }

    /**
     * @param animationSequence the animation sequence to start right away
     */
    public UAnimationSequence(BufferedImage[] animationSequence) {
        AnimationNotifies = new HashMap<String, FTuple<Integer, Integer>>();
        AnimationSequence = animationSequence;
        MakeSequence(animationSequence);
    }


    /**
     * Add dynamic Caller
     *
     * @param NewCaller the new Caller
     */
    public void AddDynamicCaller(IAnimationNotifyCallback NewCaller) {
        Caller = NewCaller;
    }

    /**
     * Add Animation notifes at runtime
     *
     * @param NotifyName    the new notify name
     * @param IndexAndFrame the tuple of (animationIndex,frameToNotify)
     */
    public void AddDynamicNotify(String NotifyName, FTuple<Integer, Integer> IndexAndFrame) {
        AnimationNotifies.put(NotifyName, IndexAndFrame);
    }

    /**
     * make animation sequence
     *
     * @param InFrames the new frames
     */
    private void MakeSequence(BufferedImage[] InFrames) {
        AnimationSequence = InFrames;
        CurrentAnimFrame = 0;
        MaxFrames = AnimationSequence.length;
    }


    /**
     * Interrupts any playing animation and plays a full montage
     *
     * @param MontageSequence the montage to play
     * @param maxFrames       the max amount of frames
     * @param animDelay       the new delay in ms between each animation frame
     * @param IndexPlaying    the current playing index (for notify purposes)
     */
    public void PlayAnimationMontage(BufferedImage[] MontageSequence, int maxFrames, int animDelay, int IndexPlaying) {
        setAnimDelay(animDelay);
        setMaxFrames(maxFrames);
        IsPlayingMontage = true;
        AnimationSequence = MontageSequence;
        PlayingAnimationIndex = IndexPlaying;
    }

    public boolean isPlayingMontage() {
        return IsPlayingMontage;
    }

    public void setAnimDelay(int animDelay) {
        AnimDelay = animDelay;
    }

    public void setCurrentAnimFrame(int currentAnimFrame) {
        CurrentAnimFrame = currentAnimFrame;
    }

    public void setMaxFrames(int maxFrames) {
        MaxFrames = maxFrames;
    }

    /**
     * Sets the animation to play
     * <p>
     * this function's functionality will be ignored if we're playing a montage
     *
     * @param animationSequence the sequence to paly
     * @param IndexPlaying      the index playing
     */
    public void setAnimationSequence(BufferedImage[] animationSequence, int IndexPlaying) {
        if (!IsPlayingMontage) {
            PlayingAnimationIndex = IndexPlaying;
            AnimationSequence = animationSequence;
        }
    }

    /**
     * Function called each game update
     *
     * @param DeltaTime the time since last call
     */
    public void Tick(double DeltaTime) {
        if (AnimDelay == -1) return;
        RealTime += DeltaTime;

        if ((RealTime / UDataContainer.BILLION) * 1000 > AnimDelay) {
            CurrentAnimFrame++;
            //check if we've reached any animation notify
            for (Map.Entry<String, FTuple<Integer, Integer>> EntrySet : AnimationNotifies.entrySet()) {
                if (Caller instanceof ANormalEnemy) {

                }
                if (EntrySet.getValue().FirstPair == PlayingAnimationIndex && EntrySet.getValue().SecondPair == CurrentAnimFrame && Caller != null) {
                    Caller.OnAnimationNotify(EntrySet.getValue().FirstPair, EntrySet.getKey());
                }
            }
            RealTime = 0;
        }

        //loop animation back
        if (CurrentAnimFrame == MaxFrames || getCurrentAnimationSequence() == null) {
            CurrentAnimFrame = 0;
            IsPlayingMontage = false;
        }
    }

    /**
     * Get the current frame
     *
     * @return the current frame
     */
    public BufferedImage getCurrentAnimationSequence() {
        return AnimationSequence[CurrentAnimFrame];
    }

    public int getCurrentAnimFrame() {
        return CurrentAnimFrame;
    }

    public int getMaxFrames() {
        return MaxFrames;
    }

    public int getAnimDelay() {
        return AnimDelay;
    }
}
