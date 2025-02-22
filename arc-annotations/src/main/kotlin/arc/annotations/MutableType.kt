package arc.annotations

/**
 * Indicates that the marked type is mutable.
 *
 * The annotation processor will check annotated types for immutability, in the following ways:
 * * If the type is an interface, it will ensure that at least one field are available for write.
 * * If the type is an interface, it will ensure that at least one method, that change current instance.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class MutableType
