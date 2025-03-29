package arc.input

/**
 * This interface represents general binding features.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface BindingLike {

    /**
     * ID of this binding.
     */
    @get:JvmName("id")
    val id: String

}