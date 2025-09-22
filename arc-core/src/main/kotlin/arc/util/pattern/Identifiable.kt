package arc.util.pattern

import java.util.*

/**
 * Represents an identifiable object with a unique identifier.
 */
interface Identifiable {

    /**
     * A universally unique identifier (UUID) associated with the object.
     */
    val uuid: UUID
}
