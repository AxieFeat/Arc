package arc.device

import arc.OSPlatform

/**
 * Represents the characteristics and specifications of a device.
 */
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
     * Graphic cards of a device.
     */
    val gpu: List<GPU>

    /**
     * What GPU is currently used by application for rendering.
     */
    val usedGpu: GPU

    /**
     * Powers sources of a device.
     */
    val powerSources: List<PowerSource>

    /**
     * MotherBoard of a device.
     */
    val motherBoard: MotherBoard

    /**
     * RAM of a device.
     */
    val ram: RAM

    /**
     * Model name of a device.
     */
    val model: String

    /**
     * Name of serial number of devices.
     */
    val serial: String

    /**
     * Hardware UUID of a device.
     */
    val uuid: String

    /**
     * Manufacturer of a device.
     */
    val manufacturer: String
}
