package arc.profiler.section

import arc.annotations.MutableType

/**
 * This interface represents general section of profiler.
 */
@MutableType
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