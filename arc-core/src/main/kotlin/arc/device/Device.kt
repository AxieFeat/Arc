package arc.device

import arc.OSPlatform
import arc.annotations.ImmutableType

/**
 * Represents the characteristics and specifications of a device.
 */
@ImmutableType
interface Device {

    /**
     * OS of device.
     */
    val os: OSPlatform

    /**
     * Version of Java in application.
     */
    val java: String

    /**
     * Central processor model.
     */
    val cpu: CPU

    /**
     * Graphic cards of device.
     */
    val gpu: List<GPU>

    /**
     * Powers sources of device.
     */
    val powerSources: List<PowerSource>

    /**
     * MotherBoard of device.
     */
    val motherBoard: MotherBoard

    /**
     * RAM of device.
     */
    val ram: RAM

    /**
     * Model name of device.
     */
    val model: String

    /**
     * Name of serial number of device.
     */
    val serial: String

    /**
     * Hardware UUID of device.
     */
    val uuid: String

    /**
     * Manufacturer of device.
     */
    val manufacturer: String

}