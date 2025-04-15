package arc.model.animation

/**
 * This enum represents modes of animation looping.
 */
enum class AnimationLoopMode {

    /**
     * Animation will be played only once and all elements states will be reset to default.
     */
    PLAY_ONCE,

    /**
     * Animation will be played only once, but elements will take states from last frame of animation.
     */
    HOLD_ON_LAST_FRAME,

    /**
     * Animation will be played in loop.
     */
    LOOP

}