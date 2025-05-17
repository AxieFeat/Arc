package arc.device

/**
 * This interface represents motherboard of [Device].
 *
 * @see Device
 */
interface MotherBoard {

    /**
     * Serial number of MotherBoard.
     */
    val serial: String

    /**
     * Model name of MotherBoard.
     */
    val model: String

    /**
     * Version of MotherBoard. Return "unknown" if not found.
     */
    val version: String

    /**
     * Manufacturer of MotherBoard.
     */
    val manufacturer: String

}