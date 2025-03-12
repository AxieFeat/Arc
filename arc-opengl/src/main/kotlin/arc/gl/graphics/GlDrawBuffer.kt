package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.*
import arc.util.Color
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryUtil
import java.lang.Float.floatToRawIntBits
import java.nio.*

internal data class GlDrawBuffer(
    override var mode: DrawerMode,
    override var format: VertexFormat,
    override val bufferSize: Int,
) : DrawBuffer {

    var vbo: Int = glGenBuffers()
    private var byteBuffer: ByteBuffer = MemoryUtil.memAlloc(bufferSize * 4)
    private var rawIntBuffer: IntBuffer = byteBuffer.asIntBuffer()
    private var rawShortBuffer: ShortBuffer = byteBuffer.asShortBuffer()
    private var rawFloatBuffer: FloatBuffer = byteBuffer.asFloatBuffer()

    var vertexCount = 0
    private var vertexFormatIndex = 0
    private var vertexFormatElement: VertexFormatElement = format.getElement(vertexFormatIndex)

    private var noColor = false
    private var xOffset = 0.0f
    private var yOffset = 0.0f
    private var zOffset = 0.0f

    override var isEnded: Boolean = false

    init {
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, bufferSize * 4L, GL_DYNAMIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    override fun end() {
        byteBuffer.position(0)
        byteBuffer.limit(this.bufferSize * 4)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferSubData(GL_ARRAY_BUFFER, 0, byteBuffer)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        isEnded = true
    }

    override fun addVertex(x: Float, y: Float, z: Float): GlDrawBuffer {
        if (isEnded) return this

        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        when (vertexFormatElement.type) {
            VertexType.FLOAT -> {
                byteBuffer.putFloat(i, (x + this.xOffset))
                byteBuffer.putFloat(i + 4, (y + this.yOffset))
                byteBuffer.putFloat(i + 8, (z + this.zOffset))
            }

            VertexType.UINT, VertexType.INT -> {
                byteBuffer.putInt(i, floatToRawIntBits((x + this.xOffset)))
                byteBuffer.putInt(i + 4, floatToRawIntBits((y + this.yOffset)))
                byteBuffer.putInt(i + 8, floatToRawIntBits((z + this.zOffset)))
            }

            VertexType.USHORT, VertexType.SHORT -> {
                byteBuffer.putShort(i, ((x + this.xOffset).toInt()).toShort())
                byteBuffer.putShort(i + 2, ((y + this.yOffset).toInt()).toShort())
                byteBuffer.putShort(i + 4, ((z + this.zOffset).toInt()).toShort())
            }

            VertexType.UBYTE, VertexType.BYTE -> {
                byteBuffer.put(i, ((x + this.xOffset).toInt()).toByte())
                byteBuffer.put(i + 1, ((y + this.yOffset).toInt()).toByte())
                byteBuffer.put(i + 2, ((z + this.zOffset).toInt()).toByte())
            }
        }

        this.nextVertexFormatIndex()
        return this
    }

    override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): GlDrawBuffer {
        if (isEnded) return this
        if (this.noColor) return this

        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        when (vertexFormatElement.type) {
            VertexType.FLOAT -> {
                byteBuffer.putFloat(i, red.toFloat() / 255.0f)
                byteBuffer.putFloat(i + 4, green.toFloat() / 255.0f)
                byteBuffer.putFloat(i + 8, blue.toFloat() / 255.0f)
                byteBuffer.putFloat(i + 12, alpha.toFloat() / 255.0f)
            }

            VertexType.UBYTE, VertexType.BYTE -> {
                byteBuffer.put(i, red.toByte())
                byteBuffer.put(i + 1, green.toByte())
                byteBuffer.put(i + 2, blue.toByte())
                byteBuffer.put(i + 3, alpha.toByte())
            }

            else -> {}
        }

        this.nextVertexFormatIndex()
        return this
    }

    override fun setColor(color: Color): GlDrawBuffer {
        if (isEnded) return this

        return setColor(
            color.red,
            color.green,
            color.blue,
            (color.alpha * 255).toInt().coerceIn(0, 255)
        )
    }

    override fun noColor(): VertexConsumer {
        if (isEnded) return this

        noColor = true
        return this
    }

    override fun setTexture(u: Float, v: Float): VertexConsumer {
        if (isEnded) return this

        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        when (vertexFormatElement.type) {
            VertexType.FLOAT -> {
                byteBuffer.putFloat(i, u)
                byteBuffer.putFloat(i + 4, v)
            }

            VertexType.UINT, VertexType.INT -> {
                byteBuffer.putInt(i, u.toInt())
                byteBuffer.putInt(i + 4, v.toInt())
            }

            VertexType.USHORT, VertexType.SHORT -> {
                byteBuffer.putShort(i, u.toInt().toShort())
                byteBuffer.putShort(i + 2, v.toInt().toShort())
            }

            VertexType.UBYTE, VertexType.BYTE -> {
                byteBuffer.put(i, u.toInt().toByte())
                byteBuffer.put(i + 1, v.toInt().toByte())
            }
        }

        this.nextVertexFormatIndex()
        return this
    }

    override fun setTranslation(x: Float, y: Float, z: Float): VertexConsumer {
        if (isEnded) return this

        this.xOffset = x
        this.yOffset = y
        this.zOffset = z

        return this
    }

    override fun setNormal(x: Float, y: Float, z: Float): GlDrawBuffer {
        if (isEnded) return this

        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        when (vertexFormatElement.type) {
            VertexType.FLOAT -> {
                byteBuffer.putFloat(i, x)
                byteBuffer.putFloat(i + 4, y)
                byteBuffer.putFloat(i + 8, z)
            }

            VertexType.UINT, VertexType.INT -> {
                byteBuffer.putInt(i, x.toInt())
                byteBuffer.putInt(i + 4, y.toInt())
                byteBuffer.putInt(i + 8, z.toInt())
            }

            VertexType.USHORT, VertexType.SHORT -> {
                byteBuffer.putShort(i, (x.toInt() * Short.MAX_VALUE and UShort.MAX_VALUE.toInt()).toShort())
                byteBuffer.putShort(i + 2, (y.toInt() * Short.MAX_VALUE and UShort.MAX_VALUE.toInt()).toShort())
                byteBuffer.putShort(i + 4, (z.toInt() * Short.MAX_VALUE and UShort.MAX_VALUE.toInt()).toShort())
            }

            VertexType.UBYTE, VertexType.BYTE -> {
                byteBuffer.put(i, (x.toInt() * Byte.MAX_VALUE and UByte.MAX_VALUE.toInt()).toByte())
                byteBuffer.put(i + 1, (y.toInt() * Byte.MAX_VALUE and UByte.MAX_VALUE.toInt()).toByte())
                byteBuffer.put(i + 2, (z.toInt() * Byte.MAX_VALUE and UByte.MAX_VALUE.toInt()).toByte())
            }
        }

        this.nextVertexFormatIndex()
        return this
    }

    private fun growBuffer(size: Int) {
        if (isEnded) return

        if (size > rawIntBuffer.remaining()) {
            val newCapacity = byteBuffer.capacity() * 2
            val newBuffer = MemoryUtil.memAlloc(newCapacity)
            byteBuffer.position(0)
            newBuffer.put(byteBuffer)
            newBuffer.rewind()
            byteBuffer = newBuffer
            rawFloatBuffer = byteBuffer.asFloatBuffer()
            rawIntBuffer = byteBuffer.asIntBuffer()
            rawShortBuffer = byteBuffer.asShortBuffer()

            glBindBuffer(GL_ARRAY_BUFFER, vbo)
            glBufferData(GL_ARRAY_BUFFER, newCapacity.toLong(), GL_DYNAMIC_DRAW)
            glBufferSubData(GL_ARRAY_BUFFER, 0, byteBuffer)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
        }
    }

    fun reset() {
        this.vertexCount = 0
        this.vertexFormatIndex = 0
        this.vertexFormatElement = format.getElement(vertexFormatIndex)
    }

    override fun endVertex(): GlDrawBuffer {
        if (isEnded) return this

        vertexCount++
        vertexFormatIndex = 0
        vertexFormatElement = format.getElement(vertexFormatIndex)
        growBuffer(this.format.nextOffset / 4)

        return this
    }

    private fun nextVertexFormatIndex() {
        if (isEnded) return

        this.vertexFormatIndex++
        this.vertexFormatIndex %= this.format.elements.size
        this.vertexFormatElement = this.format.getElement(this.vertexFormatIndex)

        if (vertexFormatElement.usage === VertexUsage.PADDING) {
            this.nextVertexFormatIndex()
        }
    }

    object Factory : DrawBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
            return GlDrawBuffer(mode, format, bufferSize)
        }
    }
}