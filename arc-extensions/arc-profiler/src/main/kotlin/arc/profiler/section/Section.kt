package arc.profiler.section

import arc.annotations.ImmutableType

/**
 * This interface represents general section of profiler.
 */
@ImmutableType
interface Section {

    /**
     * Name of this section.
     */
    val name: String

    /**
     * Start time of this section.
     */
    val startTime: Long

    /**
     * Children of this section.
     */
    val child: List<Section>

}