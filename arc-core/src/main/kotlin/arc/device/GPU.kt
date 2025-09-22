package arc.device

/**
 * This interface represents graphics processor of [Device].
 *
 * Note: On some OC values may not be present.
 *
 * @see Device
 */
interface GPU {

    /**
     * Name of GPU.
     */
    val name: String

    /**
     * Is this an integrated GPU (iGPU) or dedicated GPU (dGPU).
     *
     * `true` - integrated GPU (iGPU)
     * `false` - dedicated GPU (dGPU)
     */
    val integrated: Boolean

    /**
     * ID of GPU. Return "unknown" if not found.
     */
    val id: String

    /**
     * Size of video RAM. In bytes.
     */
    val vRam: Long

    /**
     * Vendor name of GPU.
     */
    val vendor: String

    /**
     * Info of GPU. Return "unknown" if not found.
     */
    val info: String
}
