package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.*
import arc.util.Color
import java.lang.Float.floatToRawIntBits
import java.nio.*

internal data class GlDrawBuffer(
    override var mode: DrawerMode,
    override var format: VertexFormat,
    override val bufferSize: Int,
) : DrawBuffer {

    var byteBuffer: ByteBuffer = GLAllocation.createDirectByteBuffer(bufferSize * 4)
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

    override fun end() {
        endVertex()

        byteBuffer.position(0)
        byteBuffer.limit(this.bufferSize * 4)

        isEnded = true
    }

    override fun addVertex(x: Float, y: Float, z: Float): GlDrawBuffer {
        if(isEnded) return this

        endVertex()

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
        if(isEnded) return this
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

            VertexType.UINT, VertexType.INT -> {
                byteBuffer.putFloat(i, red.toFloat())
                byteBuffer.putFloat(i + 4, green.toFloat())
                byteBuffer.putFloat(i + 8, blue.toFloat())
                byteBuffer.putFloat(i + 12, alpha.toFloat())
            }

            VertexType.USHORT, VertexType.SHORT -> {
                byteBuffer.putShort(i, red.toShort())
                byteBuffer.putShort(i + 2, green.toShort())
                byteBuffer.putShort(i + 4, blue.toShort())
                byteBuffer.putShort(i + 6, alpha.toShort())
            }

            VertexType.UBYTE, VertexType.BYTE ->
                if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                    byteBuffer.put(i, red.toByte())
                    byteBuffer.put(i + 1, green.toByte())
                    byteBuffer.put(i + 2, blue.toByte())
                    byteBuffer.put(i + 3, alpha.toByte())
                } else {
                    byteBuffer.put(i, alpha.toByte())
                    byteBuffer.put(i + 1, blue.toByte())
                    byteBuffer.put(i + 2, green.toByte())
                    byteBuffer.put(i + 3, red.toByte())
                }
        }

        this.nextVertexFormatIndex()

        return this
    }

    override fun setColor(color: Color): GlDrawBuffer {
        if(isEnded) return this

        return setColor(
            color.red,
            color.green,
            color.blue,
            (color.alpha * 255).toInt().coerceIn(0, 255)
        )
    }

    override fun noColor(): VertexConsumer {
        if(isEnded) return this

        noColor = true
        return this
    }

    override fun setTexture(u: Int, v: Int): GlDrawBuffer {
        if(isEnded) return this

        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        when (vertexFormatElement.type) {
            VertexType.FLOAT -> {
                byteBuffer.putFloat(i, u.toFloat())
                byteBuffer.putFloat(i + 4, v.toFloat())
            }

            VertexType.UINT, VertexType.INT -> {
                byteBuffer.putInt(i, u)
                byteBuffer.putInt(i + 4, v)
            }

            VertexType.USHORT, VertexType.SHORT -> {
                byteBuffer.putShort(i, (v).toShort())
                byteBuffer.putShort(i + 2, (u).toShort())
            }

            VertexType.UBYTE, VertexType.BYTE -> {
                byteBuffer.put(i, (v).toByte())
                byteBuffer.put(i + 1, (u).toByte())
            }
        }

        this.nextVertexFormatIndex()
        return this
    }

    override fun setNormal(x: Float, y: Float, z: Float): GlDrawBuffer {
        if(isEnded) return this

        val i = (x * 127.0f).toInt() and 255
        val j = (y * 127.0f).toInt() and 255
        val k = (z * 127.0f).toInt() and 255
        val l = i or (j shl 8) or (k shl 16)

        val i1: Int = this.format.nextOffset shr 2
        val j1: Int = (this.vertexCount - 4) * i1 + this.format.normalElementOffset / 4

        rawIntBuffer.put(j1, l)
        rawIntBuffer.put(j1 + i1, l)
        rawIntBuffer.put(j1 + i1 * 2, l)
        rawIntBuffer.put(j1 + i1 * 3, l)

        return this
    }

    override fun setTranslation(x: Float, y: Float, z: Float): GlDrawBuffer {
        if(isEnded) return this

        this.xOffset = x
        this.yOffset = y
        this.zOffset = z

        return this
    }

    private fun growBuffer(size: Int) {
        if(isEnded) return

        if (size > rawIntBuffer.remaining()) {
            val i = byteBuffer.capacity()
            val j = i % 2097152
            val k = j + (((rawIntBuffer.position() + size) * 4 - j) / 2097152 + 1) * 2097152
            val l = rawIntBuffer.position()
            val bytebuffer = GLAllocation.createDirectByteBuffer(k)
            byteBuffer.position(0)
            bytebuffer.put(this.byteBuffer)
            bytebuffer.rewind()
            this.byteBuffer = bytebuffer
            this.rawFloatBuffer = byteBuffer.asFloatBuffer().asReadOnlyBuffer()
            this.rawIntBuffer = byteBuffer.asIntBuffer()
            rawIntBuffer.position(l)
            this.rawShortBuffer = byteBuffer.asShortBuffer()
            rawShortBuffer.position(l shl 1)
        }
    }

    fun reset() {
        this.vertexCount = 0
        this.vertexFormatIndex = 0
    }

    private fun endVertex() {
        if(isEnded) return

        vertexCount++
        growBuffer(this.format.nextOffset / 4)
    }

    private fun nextVertexFormatIndex() {
        if(isEnded) return

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