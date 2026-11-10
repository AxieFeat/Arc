package arc.util.pattern

import kotlin.uuid.Uuid

/**
 * Represents an identifiable object with a unique identifier.
 */
interface Identifiable {

    /**
     * A universally unique identifier (Uuid) associated with the object.
     */
    val uuid: Uuid
}
