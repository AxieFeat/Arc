package arc.device

import arc.annotations.ImmutableType

/**
 * This interface represents motherboard of [Device].
 *
 * @see Device
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface MotherBoard {

    /**
     * Serial number of MotherBoard.
     */
    @get:JvmName("serial")
    val serial: String

    /**
     * Model name of MotherBoard.
     */
    @get:JvmName("model")
    val model: String

    /**
     * Version of MotherBoard. Return "unknown" if not found.
     */
    @get:JvmName("version")
    val version: String

    /**
     * Manufacturer of MotherBoard.
     */
    @get:JvmName("manufacturer")
    val manufacturer: String

}