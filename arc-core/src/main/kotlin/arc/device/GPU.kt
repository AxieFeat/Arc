package arc.device

import arc.annotations.ImmutableType

/**
 * This interface represents graphics processor of [Device].
 *
 * Note: On some OC values may not be present.
 *
 * @see Device
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface GPU {

    /**
     * Name of GPU.
     */
    @get:JvmName("name")
    val name: String

    /**
     * ID of GPU. Return "unknown" if not found.
     */
    @get:JvmName("id")
    val id: String

    /**
     * Size of video RAM. In bytes.
     */
    @get:JvmName("vRam")
    val vRam: Long

    /**
     * Vendor name of GPU.
     */
    @get:JvmName("vendor")
    val vendor: String

    /**
     * Info of GPU. Return "unknown" if not found.
     */
    @get:JvmName("info")
    val info: String

}