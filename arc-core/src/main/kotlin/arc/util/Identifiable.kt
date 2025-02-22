package arc.util

import arc.annotations.ImmutableType
import java.util.*

/**
 * Represents an identifiable object with a unique identifier.
 *
 * This interface provides a contract for objects that are uniquely
 * identified by a universally unique identifier (UUID).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Identifiable {

    /**
     * A universally unique identifier (UUID) associated with the object.
     *
     * This property provides a globally unique value intended to uniquely identify the
     * object among all other objects, ensuring consistency across various contexts.
     *
     * An instance of the UUID type guarantees immutability and uniqueness as per the RFC 4122 standard.
     */
    @get:JvmName("uuid")
    val uuid: UUID

}