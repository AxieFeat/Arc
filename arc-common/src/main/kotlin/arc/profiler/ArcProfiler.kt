package arc.profiler

import arc.profiler.section.*
import arc.profiler.section.ArcRootSection

internal data class ArcProfiler(
    override val root: RootSection = ArcRootSection(),
) : Profiler {

    override fun start(name: String): ActiveSection {
        return root.start(name)
    }

    override fun end(): TreeSectionResult {
        return root.end()
    }

    object Factory : Profiler.Factory {
        override fun create(): Profiler {
            return ArcProfiler()
        }
    }


}