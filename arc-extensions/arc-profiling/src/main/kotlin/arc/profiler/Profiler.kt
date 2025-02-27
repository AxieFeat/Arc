package arc.profiler

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents profiler for collecting debug info.
 */
@MutableType
@Suppress("INAPPLICABLE_JVM_NAME")
interface Profiler {

    /**
     * Set of all sections in profiler.
     */
    @get:JvmName("sections")
    val sections: Set<String>

    /**
     * Set of all section results in profiler.
     */
    @get:JvmName("results")
    val results: Set<SectionResult>

    /**
     * Start new section.
     *
     * @param name Name of section.
     */
    fun startSection(name: String)

    /**
     * End section by name.
     *
     * @param name Name of section.
     *
     * @return Result of section.
     */
    fun endSection(name: String): SectionResult

    /**
     * Clear profiler data.
     */
    fun clear()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create new instance of [Profiler].
         */
        fun create(): Profiler

    }

    companion object {

        /**
         * Create new instance of [Profiler].
         */
        @JvmStatic
        fun create(): Profiler {
            return Arc.factory<Factory>().create()
        }

    }

}