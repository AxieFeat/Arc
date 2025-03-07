package arc.profiler.section

import arc.annotations.ImmutableType

/**
 * This interface represents result of some section in profiler.
 *
 * @see Profiler
 */
@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface SectionResult : Section {

    /**
     * Children of this section.
     */
    override val child: List<SectionResult>

    /**
     * End time of this section.
     */
    @get:JvmName("endTime")
    val endTime: Long

    /**
     * Duration of this section in nanoseconds.
     */
    @get:JvmName("time")
    val duration: Long

}