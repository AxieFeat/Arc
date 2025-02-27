package arc.profiler

import arc.annotations.ImmutableType

/**
 * This interface represents result of some section in profiler.
 *
 * @see Profiler
 */
@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface SectionResult : Comparable<SectionResult> {

    /**
     * Name of this section.
     */
    @get:JvmName("name")
    val name: String

    /**
     * Duration of this section in nanoseconds.
     */
    @get:JvmName("time")
    val time: Long

}