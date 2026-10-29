package arc.font

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicReference

class NativeFontProviderTest {

    private fun bytes() = checkNotNull(javaClass.getResourceAsStream("/fonts/NotoSans-Regular.ttf")).use { it.readBytes() }

    @Test
    fun `real TTF metrics kerning ligatures and raster agree on glyph IDs`() {
        NativeFontProvider.of().use { provider ->
            val face = provider.load("noto", bytes())
            val metrics = face.metrics(32f)
            assertTrue(metrics.ascent > 0f && metrics.descent > 0f)
            val pair = face.shape("AV", 32f)
            val individual = face.shape("A", 32f).glyphs.sumOf { it.advanceX.toDouble() } +
                    face.shape("V", 32f).glyphs.sumOf { it.advanceX.toDouble() }
            assertTrue(pair.glyphs.sumOf { it.advanceX.toDouble() } < individual)
            assertTrue(face.shape("ffi", 32f).glyphs.size < 3)
            val glyph = pair.glyphs.first()
            assertTrue(glyph.glyphId > 0)
            val raster = face.rasterize(glyph.glyphId, 32f, 1.25f)
            assertTrue(raster.width > 0 && raster.height > 0 && raster.top > 0)
            assertEquals(raster.width * raster.height, raster.alpha.size)
            assertTrue(raster.alpha.any { it.toInt() and 255 > 0 })
            // Changing raster size cannot change a later shape's advance.
            assertEquals(pair.glyphs, face.shape("AV", 32f).glyphs)
        }
    }

    @Test
    fun `UTF16 clusters Cyrillic missing glyphs and cleanup are explicit`() {
        val provider = NativeFontProvider.of()
        val face = provider.load("noto", bytes())
        assertTrue(face.contains('Ж'.code))
        assertTrue(face.shape("Привет", 20f).glyphs.all { it.glyphId != 0 })
        val combining = face.shape("A\u0301", 20f)
        assertTrue(combining.glyphs.all { it.clusterStart == 0 && it.clusterEnd == 2 })
        assertFalse(face.contains(0x10FFFF))
        assertTrue(face.shape(String(Character.toChars(0x10FFFF)), 20f).glyphs.any { it.glyphId == 0 })
        assertThrows(IllegalArgumentException::class.java) { provider.load("noto", bytes()) }
        provider.close()
        provider.close()
        assertThrows(IllegalStateException::class.java) { face.metrics(16f) }
        assertThrows(IllegalStateException::class.java) { provider.load("other", bytes()) }
    }

    @Test
    fun `font resource versions are explicit nonnegative immutable identities`() {
        NativeFontProvider.of().use { provider ->
            assertThrows(IllegalArgumentException::class.java) { provider.load("invalid", bytes(), version = -1) }
            val face = provider.load("noto", bytes(), version = 7)
            assertEquals(7L, face.version)
            assertThrows(IllegalArgumentException::class.java) { provider.load("noto", bytes(), version = 8) }
        }
    }

    @Test
    fun `malformed font is rejected and a provider rejects cross thread use`() {
        NativeFontProvider.of().use { provider ->
            assertThrows(IllegalStateException::class.java) { provider.load("broken", byteArrayOf(1, 2, 3)) }
            val face = provider.load("noto", bytes())
            val failure = AtomicReference<Throwable>()
            val worker = Thread {
                try {
                    face.contains(65)
                } catch (error: Throwable) {
                    failure.set(error)
                }
            }
            worker.start()
            worker.join()
            assertInstanceOf(IllegalStateException::class.java, failure.get())
        }
    }
}
