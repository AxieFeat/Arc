package arc.device

import arc.OSPlatform
import arc.annotations.ImmutableType

/**
 * Represents the characteristics and specifications of a device.
 *
 * This interface provides details about a device's hardware and software configuration
 * including its operating system, CPU, GPU(s), power sources, motherboard, RAM, and other
 * vital information such as manufacturer and unique identifiers.
 *
 * All data exposed through this interface is immutable.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Device {

    /**
     * OS of device.
     */
    @get:JvmName("os")
    val os: OSPlatform

    /**
     * Version of Java in application.
     */
    @get:JvmName("java")
    val java: String

    /**
     * Central processor model name.
     */
    @get:JvmName("cpu")
    val cpu: CPU

    /**
     * Graphic card models of device.
     */
    @get:JvmName("gpu")
    val gpu: List<GPU>

    /**
     * Powers sources of device.
     */
    @get:JvmName("powerSources")
    val powerSources: List<PowerSource>

    /**
     * MotherBoard of device.
     */
    @get:JvmName("motherBoard")
    val motherBoard: MotherBoard

    /**
     * RAM of device.
     */
    @get:JvmName("ram")
    val ram: RAM

    /**
     * Model name of device.
     */
    @get:JvmName("model")
    val model: String

    /**
     * Name of serial number of device.
     */
    @get:JvmName("serial")
    val serial: String

    /**
     * Hardware UUID of device.
     */
    @get:JvmName("uuid")
    val uuid: String

    /**
     * Manufacturer of device.
     */
    @get:JvmName("manufacturer")
    val manufacturer: String

}