package arc.input

import arc.annotations.MutableType

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
@MutableType
interface BindingProcessor {

    /**
     * List of all bindings.
     */
    @get:JvmName("bindings")
    val bindings: List<Binding>

    /**
     * Add new binding to processor.
     *
     * @param binding Binding to add.
     */
    fun bind(binding: Binding)

    /**
     * Remove binding from processor.
     *
     * @param binding Binding to remove.
     */
    fun unbind(binding: Binding)

    /**
     * Remove binding from processor.
     *
     * @param id ID of binding to remove.
     */
    fun unbind(id: String)

    /**
     * Get [Binding] by it [id].
     *
     * @param id ID of [Binding].
     *
     * @return Instance of [Binding] or null if not found.
     */
    operator fun get(id: String): Binding?

}