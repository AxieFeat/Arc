package arc.window

/**
 * Exception class representing errors specific to the windowing system.
 *
 * This exception is used to indicate issues or failures related to window operations
 * such as creation, resizing, event handling, or any other functionality associated
 * with the `Window` interface. It acts as a specialization of the `RuntimeException`
 * to provide more context about window-related errors.
 *
 * @param message The detailed message explaining the cause of the exception. This
 *                parameter is optional and can be null.
 * @param cause   The underlying cause of the exception, represented by a `Throwable`.
 *                This parameter is optional and can be null.
 */
class WindowException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)