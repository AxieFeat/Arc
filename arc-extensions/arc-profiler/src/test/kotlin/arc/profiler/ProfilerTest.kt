package arc.profiler

import arc.ArcFactoryProvider
import org.junit.jupiter.api.Test

class ProfilerTest {

    @Test
    fun test() {
        ArcFactoryProvider.install()
        ArcFactoryProvider.bootstrap()

        val profiler = Profiler.create()

        val active = profiler.start("math")
        var result = 1

        for (i in 0..100) {
            result *= 2
        }
        val sectionResult = active.end()

        println("=== ProfilerTest:test ====")
        println("Time of execution: ${sectionResult.duration / 1000000}")
        println("=== ProfilerTest:test ====")
    }

}