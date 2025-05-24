package arc.window

/**
 * Some exception with a window.
 */
class WindowException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)