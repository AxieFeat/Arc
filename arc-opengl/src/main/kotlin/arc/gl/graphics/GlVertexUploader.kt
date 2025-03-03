package arc.gl.graphics

import arc.gl.GlHelper
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.graphics.vertex.VertexUsage
import org.lwjgl.opengl.GL41
import java.nio.ByteBuffer

internal object GlVertexUploader {

    @JvmStatic
    @Throws(RuntimeException::class)
    fun draw(buffer: GlDrawBuffer) {
        if(!buffer.isEnded) throw RuntimeException("Can not draw buffer! End it before.")

        if (buffer.vertexCount > 0) {
            val vertexFormat: VertexFormat = buffer.format
            val nextIndex: Int = vertexFormat.nextOffset
            val bytebuffer: ByteBuffer = buffer.byteBuffer
            val list: List<VertexFormatElement> = vertexFormat.elements

            list.forEachIndexed { index, vertexFormatElement ->
                val usage = vertexFormatElement.usage
                val vertexType: Int = vertexFormatElement.type.id
                val vertexIndex: Int = vertexFormatElement.index
                bytebuffer.position(vertexFormat.getOffset(index))

                when (usage) {
                    VertexUsage.POSITION -> {
                        GL41.glVertexPointer(vertexFormatElement.count, vertexType, nextIndex, bytebuffer)
                        GL41.glEnableClientState(GL41.GL_VERTEX_ARRAY)
                    }

                    VertexUsage.UV -> {
                        GlRenderSystem.bindTexture(GlHelper.DEFAULT_TEX + vertexIndex)
                        GL41.glTexCoordPointer(vertexFormatElement.count, vertexType, nextIndex, bytebuffer)
                        GL41.glEnableClientState(GL41.GL_TEXTURE_COORD_ARRAY)
                        GlRenderSystem.bindTexture(GlHelper.DEFAULT_TEX)
                    }

                    VertexUsage.COLOR -> {
                        GL41.glColorPointer(vertexFormatElement.count, vertexType, nextIndex, bytebuffer)
                        GL41.glEnableClientState(GL41.GL_COLOR_ARRAY)
                    }

                    VertexUsage.NORMAL -> {
                        GL41.glNormalPointer(vertexType, nextIndex, bytebuffer)
                        GL41.glEnableClientState(GL41.GL_NORMAL_ARRAY)
                    }

                    else -> {}
                }
            }

            GL41.glDrawArrays(buffer.mode.id, 0, buffer.vertexCount)

            list.forEach { vertexFormatElement ->
                val usage = vertexFormatElement.usage
                val vertexIndex: Int = vertexFormatElement.index

                when (usage) {
                    VertexUsage.POSITION -> GL41.glDisableClientState(GL41.GL_VERTEX_ARRAY)
                    VertexUsage.UV -> {
                        GlRenderSystem.bindTexture(GlHelper.DEFAULT_TEX + vertexIndex)
                        GL41.glDisableClientState(GL41.GL_TEXTURE_COORD_ARRAY)
                        GlRenderSystem.bindTexture(GlHelper.DEFAULT_TEX)
                    }

                    VertexUsage.COLOR -> {
                        GL41.glDisableClientState(GL41.GL_COLOR_ARRAY)

                        GlRenderSystem.setShaderColor(
                            -1.0f, -1.0f, -1.0f, -1.0f
                        )
                    }

                    VertexUsage.NORMAL -> GL41.glDisableClientState(GL41.GL_NORMAL_ARRAY)

                    else -> {}
                }
            }
        }

        buffer.reset()
    }

}