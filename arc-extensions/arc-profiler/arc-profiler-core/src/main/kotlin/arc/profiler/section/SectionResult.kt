package arc.profiler.section

import arc.annotations.ImmutableType

/**
 * This interface represents result of some section in profiler.
 */
@ImmutableType
interface SectionResult : Section {

    /**
     * Children of this section.
     */
    override val child: List<SectionResult>

    /**
     * End time of this section.
     */
    val endTime: Long

    /**
     * Duration of this section in nanoseconds.
     */
    val duration: Long
}
