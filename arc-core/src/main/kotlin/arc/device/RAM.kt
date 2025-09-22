package arc.device

/**
 * This interface represents ram of [Device].
 *
 * Note: On some OC values may not be present.
 *
 * @see Device
 */
interface RAM {

    /**
     * Size of available memory. In bytes.
     */
    val available: Long

    /**
     * Total size of memory. In bytes.
     */
    val total: Long

    /**
     * Usage of SWAP. In bytes.
     */
    val swapUsed: Long

    /**
     * Total size of SWAP. In bytes.
     */
    val swapTotal: Long
}
