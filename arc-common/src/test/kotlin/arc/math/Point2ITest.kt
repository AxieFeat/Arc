package arc.math

import arc.ArcFactoryProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Point2ITest  {
    @Test
    fun testPack() {
        ArcFactoryProvider.install()
        ArcFactoryProvider.bootstrap()

        val point = Point2i.of(92391, -18591)
        val packed = point.pack()
        val unpacked = Point2i.unpack(packed)

        println("=== Point2Test:testPack ====")
        println("Original point = $point")
        println("Packed point = $packed")
        println("Unpacked point = $unpacked")
        println("=== Point2Test:testPack ====")

        assertEquals(
            point,
            Point2i.unpack(packed)
        )
    }

}