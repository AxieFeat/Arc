package arc.profiler

import arc.ArcFactoryProvider
import org.junit.jupiter.api.Test

class ProfilerTest {

    @Test
    fun test() {
        ArcFactoryProvider.install()
        ArcFactoryProvider.bootstrap()

        val profiler = Profiler.create()

        profiler.startSection("math")
        var result = 1

        for (i in 0..100) {
            result *= 2
        }
        val sectionResult = profiler.endSection("math")

        println("=== ProfilerTest:test ====")
        println("Time of execution: ${sectionResult.time / 1000000}")
        println("=== ProfilerTest:test ====")
    }

}