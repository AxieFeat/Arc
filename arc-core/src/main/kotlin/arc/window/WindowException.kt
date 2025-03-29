package arc.window

/**
 * Some exception with window.
 */
class WindowException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)