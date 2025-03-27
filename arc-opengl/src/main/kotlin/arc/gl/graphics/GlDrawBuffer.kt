package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.VertexBuffer
import arc.graphics.vertex.*
import arc.util.Color
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.*

internal data class GlDrawBuffer(
    override var mode: DrawerMode,
    override var format: VertexFormat,
    override val bufferSize: Int,
) : DrawBuffer {

    var byteBuffer: ByteBuffer = MemoryUtil.memAlloc(bufferSize * 4)
    private var rawIntBuffer: IntBuffer = byteBuffer.asIntBuffer()
    private var rawShortBuffer: ShortBuffer = byteBuffer.asShortBuffer()
    private var rawFloatBuffer: FloatBuffer = byteBuffer.asFloatBuffer()

    var vertexCount = 0
    private var vertexFormatIndex = 0
    private var vertexFormatElement: VertexFormatElement = format.getElement(vertexFormatIndex)

    private var xOffset = 0.0f
    private var yOffset = 0.0f
    private var zOffset = 0.0f

    private var selectedVertex = 0

    override fun end(): GlDrawBuffer {
        byteBuffer.position(0)
        byteBuffer.limit(this.bufferSize * 4)

        return this
    }

    override fun build(): VertexBuffer {
        return VertexBuffer.create(this)
    }

    override fun cleanup() {
        reset()
    }

    override fun addVertex(x: Float, y: Float, z: Float): GlDrawBuffer {
        val i: Int = this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        putPosition(i, x, y, z)

        this.nextVertexFormatIndex()
        return this
    }

    override fun addVertex(matrix: Matrix4f, x: Float, y: Float, z: Float): GlDrawBuffer {
        val vector3f: Vector3f = matrix.transformPosition(x, y, z, Vector3f())

        return addVertex(vector3f.x, vector3f.y, vector3f.z)
    }

    override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): GlDrawBuffer {
        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        putColor(i, red, green, blue, alpha)

        this.nextVertexFormatIndex()
        return this
    }

    override fun setColor(color: Color): GlDrawBuffer {
        return setColor(
            color.red,
            color.green,
            color.blue,
            (color.alpha * 255).toInt().coerceIn(0, 255)
        )
    }

    override fun setTexture(u: Float, v: Float): GlDrawBuffer {
        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        putTexture(i, vertexFormatElement, u, v)

        this.nextVertexFormatIndex()
        return this
    }

    override fun setTranslation(x: Float, y: Float, z: Float): GlDrawBuffer {
        this.xOffset = x
        this.yOffset = y
        this.zOffset = z

        return this
    }

    override fun setNormal(x: Float, y: Float, z: Float): GlDrawBuffer {
        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        putNormal(i, x, y, z)

        this.nextVertexFormatIndex()
        return this
    }

    override fun endVertex(): GlDrawBuffer {
        vertexCount++
        vertexFormatIndex = 0
        vertexFormatElement = format.getElement(vertexFormatIndex)
        growBuffer(this.format.nextOffset / 4)

        return this
    }

    override fun edit(id: Int): GlDrawBuffer {
        selectedVertex = id
        return this
    }

    override fun editPosition(x: Float, y: Float, z: Float): GlDrawBuffer {
        if (vertexCount == 0) return this

        val i: Int = selectedVertex * 20

        putPosition(i, x, y, z)

        return this
    }

    override fun editPosition(matrix: Matrix4f, x: Float, y: Float, z: Float): GlDrawBuffer {
        val vector3f: Vector3f = matrix.transformPosition(x, y, z, Vector3f())

        return editPosition(vector3f.x, vector3f.y, vector3f.z)
    }

    override fun editColor(red: Int, green: Int, blue: Int, alpha: Int): GlDrawBuffer {
        if (vertexCount == 0) return this

        val i: Int = selectedVertex * 20

        putColor(i, red, green, blue, alpha)

        return this
    }

    override fun editColor(color: Color): GlDrawBuffer {
        return editColor(color.red, color.green, color.blue, (color.alpha * 255).toInt().coerceIn(0, 255))
    }

    override fun editTexture(formatElement: VertexFormatElement, u: Float, v: Float): GlDrawBuffer {
        if (vertexCount == 0) return this

        val i: Int = selectedVertex * 20

        putTexture(i, formatElement, u, v)

        return this
    }

    private fun nextVertexFormatIndex() {
        this.vertexFormatIndex++
        this.vertexFormatIndex %= this.format.elements.size
        this.vertexFormatElement = this.format.getElement(this.vertexFormatIndex)

        if (vertexFormatElement.usage === VertexUsage.PADDING) {
            this.nextVertexFormatIndex()
        }
    }

    private fun growBuffer(size: Int) {
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
        }
    }

    private fun reset() {
        this.vertexCount = 0
        this.vertexFormatIndex = 0
        this.vertexFormatElement = format.getElement(vertexFormatIndex)
    }

    private fun putPosition(i: Int, x: Float, y: Float, z: Float) {
        byteBuffer.putFloat(i, (x + this.xOffset))
        byteBuffer.putFloat(i + 4, (y + this.yOffset))
        byteBuffer.putFloat(i + 8, (z + this.zOffset))
    }

    private fun putColor(i: Int, red: Int, green: Int, blue: Int, alpha: Int) {
        byteBuffer.put(i, red.toByte())
        byteBuffer.put(i + 1, green.toByte())
        byteBuffer.put(i + 2, blue.toByte())
        byteBuffer.put(i + 3, alpha.toByte())
    }

    private fun putTexture(i: Int, element: VertexFormatElement, u: Float, v: Float) {
        when (element.type) {
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
    }

    private fun putNormal(i: Int, x: Float, y: Float, z: Float) {
        byteBuffer.put(i, (x.toInt() * Byte.MAX_VALUE and UByte.MAX_VALUE.toInt()).toByte())
        byteBuffer.put(i + 1, (y.toInt() * Byte.MAX_VALUE and UByte.MAX_VALUE.toInt()).toByte())
        byteBuffer.put(i + 2, (z.toInt() * Byte.MAX_VALUE and UByte.MAX_VALUE.toInt()).toByte())
    }

    object Factory : DrawBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
            return GlDrawBuffer(mode, format, bufferSize)
        }
    }
}