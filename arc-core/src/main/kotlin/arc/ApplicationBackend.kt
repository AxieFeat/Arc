package arc

import arc.device.Device

/**
 * This interface represents info about backend implementation of application.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface ApplicationBackend {

    /**
     * Name of backend implementation.
     */
    val name: String

    /**
     * Version of a backend library, that used for implement application.
     */
    val version: String

    /**
     * Represents the device where the application is running.
     */
    val device: Device
}
