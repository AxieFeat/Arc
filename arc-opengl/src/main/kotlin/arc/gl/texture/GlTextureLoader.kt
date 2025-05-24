package arc.gl.texture

import arc.texture.TextureAtlas
import arc.texture.TextureAtlasLoader
import arc.texture.TextureFormat
import arc.texture.TextureLoader
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer
import kotlin.use

internal class GlTextureLoader(
    override val format: TextureFormat
) : TextureAtlasLoader {

    override fun load(buffer: ByteBuffer, width: Int, height: Int): TextureAtlas {
        val texture = GlTextureAtlas()
        val id = texture.id

        MemoryStack.stackPush().use { stack ->
            when(format) {
                TextureFormat.RAW -> {
                    texture.width = width
                    texture.height = height

                    TextureUtil.loadRGB(id, width, height, buffer)
                }
                TextureFormat.PNG -> {
                    val w = stack.mallocInt(1)
                    val h = stack.mallocInt(1)
                    val channels = stack.mallocInt(1)

                    val buf: ByteBuffer = STBImage.stbi_load_from_memory(buffer, w, h, channels, 4)
                        ?: throw RuntimeException("Image file not loaded: " + STBImage.stbi_failure_reason())

                    texture.width = w.get()
                    texture.height = h.get()

                    TextureUtil.loadRGB(id, texture.width, texture.height, buf)
                    STBImage.stbi_image_free(buf)
                }
            }
        }

        return texture
    }

    object Factory : TextureAtlasLoader.Factory, TextureLoader.Factory {

        @JvmField
        val PNG = GlTextureLoader(TextureFormat.PNG)

        @JvmField
        val RAW = GlTextureLoader(TextureFormat.RAW)

        override fun create(format: TextureFormat): TextureAtlasLoader {
            return when(format) {
                TextureFormat.PNG -> PNG
                TextureFormat.RAW -> RAW
            }
        }

    }

}