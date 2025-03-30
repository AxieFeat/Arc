package arc.model.group

import arc.annotations.ImmutableType
import arc.math.Point3d
import arc.model.animation.Animator
import arc.util.Identifiable
import java.util.UUID

/**
 * This interface represents group of elements.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface ElementGroup : Identifiable {

    /**
     * Name of group. Used by [Animator.target].
     */
    @get:JvmName("name")
    val name: String

    /**
     * All elements UUID's of this group.
     */
    @get:JvmName("elements")
    val elements: Set<UUID>

    /**
     * Origin point of this group.
     */
    @get:JvmName("origin")
    val origin: Point3d

}