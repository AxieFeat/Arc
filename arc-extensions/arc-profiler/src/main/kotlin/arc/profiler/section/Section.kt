package arc.profiler.section

import arc.annotations.ImmutableType

/**
 * This interface represents general section of profiler.
 */
@ImmutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface Section {

    /**
     * Name of this section.
     */
    @get:JvmName("name")
    val name: String

    /**
     * Start time of this section.
     */
    @get:JvmName("startTime")
    val startTime: Long

    /**
     * Children of this section.
     */
    @get:JvmName("child")
    val child: List<Section>

}