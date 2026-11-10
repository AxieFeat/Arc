package arc.profiler

import arc.Arc
import kotlin.jvm.JvmStatic
import arc.util.provider.ObjectProvider
import arc.util.provider.register

/**
 * This object class represents factory provider for `arc-profiler` extension.
 */
object SimpleProfilerExtension {

    /**
     * Bootstrap factories of `arc-audio` extension.
     *
     * @param provider Provider for configuring.
     */
    @JvmStatic
    fun bootstrap(provider: ObjectProvider = Arc.factoryProvider()) {
        provider.register<Profiler.Factory>(SimpleProfiler.Factory)
    }
}
