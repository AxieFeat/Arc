package arc.model.animation

import arc.annotations.ImmutableType
import arc.util.Identifiable

/**
 * This interface represents animation of some model.
 */
@ImmutableType
interface Animation : Identifiable {

    /**
     * Is this animation looped.
     */
    val isLoop: Boolean

    /**
     * Length of this animation in milliseconds.
     */
    val length: Long

}