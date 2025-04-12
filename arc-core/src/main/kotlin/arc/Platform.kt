package arc

import arc.annotations.ImmutableType
import arc.device.Device

/**
 * This interface represents platform of application.
 */
@ImmutableType
interface Platform {

    /**
     * Identification of platform. You can set here name of your implementation.
     */
    val id: String

    /**
     * Represents the device where the application is running.
     */
    val device: Device

}