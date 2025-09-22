package arc.util.provider

/**
 * Thrown when a factory requested from the factory provider was not found.
 */
class TypeNotFoundException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)
