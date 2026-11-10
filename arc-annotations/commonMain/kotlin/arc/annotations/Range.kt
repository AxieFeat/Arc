package arc.annotations

/**
 * Documents inclusive bounds for a numeric value.
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
annotation class Range(val from: Long, val to: Long)
