package arc

import arc.annotations.ImmutableType
import arc.device.Device

/**
 * Represents a platform, providing essential information such as the platform's identification
 * and the associated device details. This interface is marked as immutable, ensuring that its
 * implementation is persistent and unchangeable after creation.
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
     * Represents the device where the application is running. Provides detailed information
     * about the hardware and software configuration of the device, such as the operating system,
     * CPU, GPU, RAM, manufacturer, and more. This property is immutable and always reflects the
     * current device's specifications.
     */
    @get:JvmName("device")
    val device: Device

}