package arc

import arc.annotations.ImmutableType
import arc.device.Device

/**
 * This interface represents platform of application.
 */
@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface Platform {

    /**
     * Identification of platform. You can set here name of your implementation.
     */
    @get:JvmName("id")
    val id: String

    /**
     * Represents the device where the application is running.
     */
    @get:JvmName("device")
    val device: Device

}