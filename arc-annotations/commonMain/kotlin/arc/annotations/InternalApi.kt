package arc.annotations

/**
 * Marks declarations intended for engine implementations rather than application code.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.BINARY)
annotation class InternalApi
