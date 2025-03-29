package arc.util

import arc.annotations.ImmutableType
import java.util.*

/**
 * Represents an identifiable object with a unique identifier.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Identifiable {

    /**
     * A universally unique identifier (UUID) associated with the object.
     */
    @get:JvmName("uuid")
    val uuid: UUID

}