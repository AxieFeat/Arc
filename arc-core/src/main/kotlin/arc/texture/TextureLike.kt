package arc.texture

import arc.annotations.ImmutableType
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable

/**
 * This interface represents general texture.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface TextureLike : Bindable, Cleanable {

    /**
     * ID of this texture in render system.
     */
    @get:JvmName("id")
    val id: Int

}