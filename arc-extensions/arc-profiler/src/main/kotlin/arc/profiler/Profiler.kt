package arc.profiler

import arc.Arc
import arc.annotations.TypeFactory
import arc.profiler.section.ActiveSection
import arc.profiler.section.RootSection
import arc.profiler.section.TreeSectionResult
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents profiler for collecting debug info.
 */
interface Profiler {

    /**
     * Root section of profiler.
     */
    val root: RootSection

    /**
     * Create section in [root].
     *
     * @param name Name of section.
     *
     * @return Instance of [ActiveSection].
     */
    fun start(name: String): ActiveSection

    /**
     * End [root] section.
     *
     * @return Instance of [TreeSectionResult]
     */
    fun end(): TreeSectionResult

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