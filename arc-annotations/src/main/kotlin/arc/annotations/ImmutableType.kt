package arc.annotations

/**
 * Indicates that the marked type is immutable.
 *
 * The annotation processor will check annotated types for immutability, in the following ways:
 * * If the type is an interface, it will ensure that all fields are read-only.
 * * If the type is a class, it will ensure it is a final data class annotated with `@JvmRecord`.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ImmutableType
