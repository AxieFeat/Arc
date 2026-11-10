package arc.device

/**
 * This interface represents central processor of [Device].
 *
 * Note: On some OC values may not be present.
 *
 * @see Device
 */
interface CPU {

    /**
     * Name of CPU.
     */
    val name: String

    /**
     * ID of CPU.
     */
    val id: String

    /**
     * Family of CPU.
     */
    val family: String

    /**
     * Vendor name of CPU.
     */
    val vendor: String

    /**
     * Architecture name of CPU.
     */
    val architecture: String

    /**
     * Model name of CPU.
     */
    val model: String
}
