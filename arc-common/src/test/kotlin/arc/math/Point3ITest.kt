package arc.math

import arc.ArcFactoryProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Point3ITest {
    @Test
    fun testPack() {
        ArcFactoryProvider.install()
        ArcFactoryProvider.bootstrap()

        val point = Point3i.of(92391, -18591, 193)
        val packed = point.pack()
        val unpacked = Point3i.unpack(packed)

        println("=== Point3Test:testPack ====")
        println("Original point = $point")
        println("Packed point = $packed")
        println("Unpacked point = $unpacked")
        println("=== Point3Test:testPack ====")

        assertEquals(
            point,
            Point3i.unpack(packed)
        )
    }
}