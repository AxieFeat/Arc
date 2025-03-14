package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.*
import arc.util.Color
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryUtil
import java.nio.*

internal data class GlDrawBuffer(
    override var mode: DrawerMode,
    override var format: VertexFormat,
    override val bufferSize: Int,
) : DrawBuffer {

    private val vbo = IntArray(2)
    private var currentVboIndex = 0

    private var byteBuffer: ByteBuffer = MemoryUtil.memAlloc(bufferSize * 4)
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

    init {
        glGenBuffers(vbo)

        for (buffer in vbo) {
            glBindBuffer(GL_ARRAY_BUFFER, buffer)
            glBufferData(GL_ARRAY_BUFFER, bufferSize * 4L, GL_STREAM_DRAW)
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    override fun end() {
        byteBuffer.position(0)
        byteBuffer.limit(this.bufferSize * 4)

        upload()
    }

    private fun upload() {
        swapBuffers()

        val currentVbo = vbo[currentVboIndex]
        glBindBuffer(GL_ARRAY_BUFFER, currentVbo)

        val ptr = glMapBufferRange(GL_ARRAY_BUFFER, 0, byteBuffer.capacity().toLong(),
            GL_MAP_WRITE_BIT or GL_MAP_UNSYNCHRONIZED_BIT)
        if (ptr != null) {
            MemoryUtil.memCopy(MemoryUtil.memAddress(byteBuffer), MemoryUtil.memAddress(ptr), byteBuffer.capacity().toLong())
            glUnmapBuffer(GL_ARRAY_BUFFER)
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0)

        currentVboIndex = 1 - currentVboIndex
    }

    private fun swapBuffers() {
        currentVboIndex = 1 - currentVboIndex
    }

    fun getRenderVbo(): Int {
        return vbo[1 - currentVboIndex]
    }

    override fun addVertex(x: Float, y: Float, z: Float): GlDrawBuffer {
        val i: Int = this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        byteBuffer.putFloat(i, (x + this.xOffset))
        byteBuffer.putFloat(i + 4, (y + this.yOffset))
        byteBuffer.putFloat(i + 8, (z + this.zOffset))

        this.nextVertexFormatIndex()
        return this
    }

    override fun addVertex(matrix: Matrix4f, x: Float, y: Float, z: Float): VertexConsumer {
        val vector3f: Vector3f = matrix.transformPosition(x, y, z, Vector3f())

        return addVertex(vector3f.x, vector3f.y, vector3f.z)
    }

    override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): GlDrawBuffer {
        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        byteBuffer.put(i, red.toByte())
        byteBuffer.put(i + 1, green.toByte())
        byteBuffer.put(i + 2, blue.toByte())
        byteBuffer.put(i + 3, alpha.toByte())

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

    override fun setTexture(u: Float, v: Float): VertexConsumer {
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
        this.xOffset = x
        this.yOffset = y
        this.zOffset = z

        return this
    }

    override fun setNormal(x: Float, y: Float, z: Float): GlDrawBuffer {
        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        byteBuffer.put(i, (x.toInt() * Byte.MAX_VALUE and UByte.MAX_VALUE.toInt()).toByte())
        byteBuffer.put(i + 1, (y.toInt() * Byte.MAX_VALUE and UByte.MAX_VALUE.toInt()).toByte())
        byteBuffer.put(i + 2, (z.toInt() * Byte.MAX_VALUE and UByte.MAX_VALUE.toInt()).toByte())

        this.nextVertexFormatIndex()
        return this
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

            glBindBuffer(GL_ARRAY_BUFFER, vbo[currentVboIndex])
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
        vertexCount++
        vertexFormatIndex = 0
        vertexFormatElement = format.getElement(vertexFormatIndex)
        growBuffer(this.format.nextOffset / 4)

        return this
    }

    override fun edit(id: Int): VertexConsumer {
        selectedVertex = id
        return this
    }

    override fun editPosition(x: Float, y: Float, z: Float): VertexConsumer {
        if (vertexCount == 0) return this

        val i: Int = selectedVertex * 20

        byteBuffer.putFloat(i, (x + this.xOffset))
        byteBuffer.putFloat(i + 4, (y + this.yOffset))
        byteBuffer.putFloat(i + 8, (z + this.zOffset))

        upload()

        return this
    }

    override fun editPosition(matrix: Matrix4f, x: Float, y: Float, z: Float): VertexConsumer {
        val vector3f: Vector3f = matrix.transformPosition(x, y, z, Vector3f())

        return editPosition(vector3f.x, vector3f.y, vector3f.z)
    }

    override fun editColor(red: Int, green: Int, blue: Int, alpha: Int): VertexConsumer {
        if (vertexCount == 0) return this

        val i: Int = selectedVertex * 20

        byteBuffer.put(i, red.toByte())
        byteBuffer.put(i + 1, green.toByte())
        byteBuffer.put(i + 2, blue.toByte())
        byteBuffer.put(i + 3, alpha.toByte())

        return this
    }

    override fun editColor(color: Color): VertexConsumer {
        return editColor(color.red, color.green, color.blue, (color.alpha * 255).toInt().coerceIn(0, 255))
    }

    override fun editTexture(u: Float, v: Float): VertexConsumer {
        if (vertexCount == 0) return this // Защита от выхода за границы

        val i: Int = selectedVertex * 20

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

    object Factory : DrawBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
            return GlDrawBuffer(mode, format, bufferSize)
        }
    }
}