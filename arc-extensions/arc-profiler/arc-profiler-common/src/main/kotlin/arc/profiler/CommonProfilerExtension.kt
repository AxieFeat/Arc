package arc.profiler

import arc.ArcFactoryProvider
import arc.util.factory.FactoryProvider
import arc.util.factory.register

/**
 * This object class represents factory provider for `arc-profiler` extension.
 */
object CommonProfilerExtension {

    /**
     * Bootstrap factories of `arc-audio` extension.
     *
     * @param provider Provider for configuring.
     */
    @JvmStatic
    fun bootstrap(provider: FactoryProvider = ArcFactoryProvider) {
        provider.register<Profiler.Factory>(ArcProfiler.Factory)
    }

}