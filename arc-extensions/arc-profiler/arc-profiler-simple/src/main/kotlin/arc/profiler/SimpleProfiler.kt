package arc.profiler

import arc.profiler.section.*
import arc.profiler.section.SimpleRootSection

internal data class SimpleProfiler(
    override val root: RootSection = SimpleRootSection(),
) : Profiler {

    override fun start(name: String): ActiveSection {
        return root.start(name)
    }

    override fun end(): TreeSectionResult {
        return root.end()
    }

    object Factory : Profiler.Factory {
        override fun create(): Profiler {
            return SimpleProfiler()
        }
    }


}