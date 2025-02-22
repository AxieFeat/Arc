package arc.device

import arc.annotations.ImmutableType

/**
 * This interface represents ram of [Device].
 *
 * Note: On some OC values may not be present.
 *
 * @see Device
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface RAM {

    /**
     * Size of available memory. In bytes.
     */
    @get:JvmName("available")
    val available: Long

    /**
     * Total size of memory. In bytes.
     */
    @get:JvmName("total")
    val total: Long

    /**
     * Usage of SWAP. In bytes.
     */
    @get:JvmName("swapUsed")
    val swapUsed: Long

    /**
     * Total size of SWAP. In bytes.
     */
    @get:JvmName("swapTotal")
    val swapTotal: Long

}