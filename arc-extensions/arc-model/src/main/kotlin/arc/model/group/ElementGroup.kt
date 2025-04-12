package arc.model.group

import arc.annotations.ImmutableType
import arc.math.Point3d
import arc.model.animation.Animator
import arc.util.pattern.Identifiable
import java.util.UUID

/**
 * This interface represents group of elements.
 */
@ImmutableType
interface ElementGroup : Identifiable {

    /**
     * Name of group. Used by [Animator.target].
     */
    val name: String

    /**
     * All elements UUID's of this group.
     */
    val elements: Set<UUID>

    /**
     * Origin point of this group.
     */
    val origin: Point3d

}