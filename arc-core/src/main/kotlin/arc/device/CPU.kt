package arc.device

import arc.annotations.ImmutableType

/**
 * This interface represents central processor of [Device].
 *
 * Note: On some OC values may not be present.
 *
 * @see Device
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface CPU {

    /**
     * Name of CPU.
     */
    @get:JvmName("name")
    val name: String

    /**
     * ID of CPU.
     */
    @get:JvmName("id")
    val id: String

    /**
     * Family of CPU.
     */
    @get:JvmName("family")
    val family: String

    /**
     * Vendor name of CPU.
     */
    @get:JvmName("vendor")
    val vendor: String

    /**
     * Architecture name of CPU.
     */
    @get:JvmName("architecture")
    val architecture: String

    /**
     * Model name of CPU.
     */
    @get:JvmName("model")
    val model: String

}