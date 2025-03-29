package arc.input

/**
 * This interface represents processor for binding keys.
 *
 * Note that if you have multiple [InputDevice] and the [Binding] registration is only
 * for a specific [InputDevice], not globally for all devices.
 *
 * @see InputDevice
 * @see Binding
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface BindingProcessor {

    /**
     * List of all bindings.
     */
    @get:JvmName("bindings")
    val bindings: List<BindingLike>

    /**
     * Add new binding to processor.
     *
     * @param binding Binding to add.
     */
    fun bind(binding: BindingLike)

    /**
     * Remove binding from processor.
     *
     * @param binding Binding to remove.
     */
    fun unbind(binding: BindingLike)

    /**
     * Remove binding from processor.
     *
     * @param id ID of binding to remove.
     */
    fun unbind(id: String)

    /**
     * Get [BindingLike] by it [id].
     *
     * @param id ID of [BindingLike].
     *
     * @return Instance of [BindingLike] or null if not found.
     */
    operator fun get(id: String): BindingLike?

}