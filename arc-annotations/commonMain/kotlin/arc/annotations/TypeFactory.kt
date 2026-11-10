package arc.annotations

/**
 * Indicates that a marked interface is a factory that creates other types.
 *
 * The annotation processor will ensure the following properties are met:
 * * The type is an interface
 * * The type is a member type
 * * The type has no declared properties
 * * The type has at least one declared function
 * * The type is marked with [org.jetbrains.annotations.ApiStatus.Internal]
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TypeFactory
