package arc.input

import arc.annotations.ImmutableType

/**
 * This interface represents general binding features.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface BindingLike {

    /**
     * ID of this binding.
     */
    @get:JvmName("id")
    val id: String

}