package arc.input

/**
 * This interface represents multi-binding.
 *
 * That is, all keys from the list must be pressed at the same time for this bind to work
 */
// TODO Add release for multibinding.
@Suppress("INAPPLICABLE_JVM_NAME")
interface MultiBinding : BindingLike {

    /**
     * ID of this binding.
     */
    @get:JvmName("id")
    override val id: String

    /**
     * Keys of this binding.
     *
     * NOTE: Not use [KeyCode.ANY], [KeyCode.ANY_KEY] and [KeyCode.ANY_MOUSE] here.
     */
    @get:JvmName("keys")
    val keys: Set<KeyCode>

    /**
     * Calls when these keys are pressed.
     */
    fun onPress()

}