package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.*
import arc.util.Color
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

internal data class GlDrawBuffer(
    override var mode: DrawerMode,
    override var format: VertexFormat,
    override val bufferSize: Int,
) : DrawBuffer {

    var byteBuffer: ByteBuffer = MemoryUtil.memAlloc(bufferSize * 4)
    private var baseAddress: Long = MemoryUtil.memAddress(byteBuffer)

    var vertexCount = 0
    private var vertexFormatIndex = 0
    private var vertexFormatElement: VertexFormatElement = format.getElement(vertexFormatIndex)
    private var needEnding = false

    private var xOffset = 0.0f
    private var yOffset = 0.0f
    private var zOffset = 0.0f

    override fun build(): VertexBuffer {
        endWriting()

        return VertexBuffer.of(this)
    }

    override fun cleanup() {
        MemoryUtil.memFree(byteBuffer)
    }

    override fun addVertex(x: Float, y: Float, z: Float): GlDrawBuffer {
        if (needEnding) endVertex() else needEnding = true

        val i = vertexCount * format.nextOffset + format.getOffset(vertexFormatIndex)
        putPosition(i, x, y, z)
        nextVertexFormatIndex()
        return this
    }

    override fun addVertex(matrix: Matrix4f, x: Float, y: Float, z: Float): GlDrawBuffer {
        val vector3f: Vector3f = matrix.transformPosition(x, y, z, Vector3f())
        return addVertex(vector3f.x, vector3f.y, vector3f.z)
    }

    override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): GlDrawBuffer {
        val i = vertexCount * format.nextOffset + format.getOffset(vertexFormatIndex)
        putColor(i, red, green, blue, alpha)
        nextVertexFormatIndex()
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
        val i = vertexCount * format.nextOffset + format.getOffset(vertexFormatIndex)
        putUV(i, vertexFormatElement, u, v)
        nextVertexFormatIndex()
        return this
    }

    override fun setTranslation(x: Float, y: Float, z: Float): GlDrawBuffer {
        this.xOffset = x
        this.yOffset = y
        this.zOffset = z
        return this
    }

    override fun setNormal(x: Float, y: Float, z: Float): GlDrawBuffer {
        val i = vertexCount * format.nextOffset + format.getOffset(vertexFormatIndex)
        putNormal(i, x, y, z)
        nextVertexFormatIndex()
        return this
    }

    override fun setLight(u: Float, v: Float): VertexConsumer {
        val i = vertexCount * format.nextOffset + format.getOffset(vertexFormatIndex)
        putUV(i, vertexFormatElement, u, v)
        nextVertexFormatIndex()
        return this
    }

    private fun endWriting(): GlDrawBuffer {
        endVertex()

        byteBuffer.position(0)
        byteBuffer.limit(this.bufferSize * 4)

        return this
    }

    private fun endVertex(): GlDrawBuffer {
        vertexCount++
        vertexFormatIndex = 0
        vertexFormatElement = format.getElement(vertexFormatIndex)
        growBuffer(format.nextOffset)
        return this
    }

    private fun nextVertexFormatIndex() {
        vertexFormatIndex++
        vertexFormatIndex %= format.elements.size
        vertexFormatElement = format.getElement(vertexFormatIndex)
        if (vertexFormatElement.usage === VertexUsage.PADDING) {
            nextVertexFormatIndex()
        }
    }

    private fun growBuffer(requiredBytes: Int) {
        if (byteBuffer.remaining() < requiredBytes) {
            val newCapacity = byteBuffer.capacity() * 2
            val newBuffer = MemoryUtil.memAlloc(newCapacity)
            MemoryUtil.memCopy(baseAddress, MemoryUtil.memAddress(newBuffer), byteBuffer.position().toLong())

            MemoryUtil.memFree(byteBuffer)

            byteBuffer = newBuffer
            baseAddress = MemoryUtil.memAddress(byteBuffer)
        }
    }

    fun reset() {
        byteBuffer.clear()

        needEnding = false
        vertexCount = 0
        vertexFormatIndex = 0
        vertexFormatElement = format.getElement(vertexFormatIndex)
    }

    private fun putPosition(i: Int, x: Float, y: Float, z: Float) {
        val addr = baseAddress + i
        MemoryUtil.memPutFloat(addr, x + xOffset)
        MemoryUtil.memPutFloat(addr + 4, y + yOffset)
        MemoryUtil.memPutFloat(addr + 8, z + zOffset)
    }

    private fun putColor(i: Int, red: Int, green: Int, blue: Int, alpha: Int) {
        val addr = baseAddress + i
        MemoryUtil.memPutByte(addr, red.toByte())
        MemoryUtil.memPutByte(addr + 1, green.toByte())
        MemoryUtil.memPutByte(addr + 2, blue.toByte())
        MemoryUtil.memPutByte(addr + 3, alpha.toByte())
    }

    private fun putUV(i: Int, element: VertexFormatElement, u: Float, v: Float) {
        val addr = baseAddress + i
        when (element.type) {
            VertexType.FLOAT -> {
                MemoryUtil.memPutFloat(addr, u)
                MemoryUtil.memPutFloat(addr + 4, v)
            }

            VertexType.UINT, VertexType.INT -> {
                MemoryUtil.memPutInt(addr, u.toInt())
                MemoryUtil.memPutInt(addr + 4, v.toInt())
            }

            VertexType.USHORT, VertexType.SHORT -> {
                MemoryUtil.memPutShort(addr, u.toInt().toShort())
                MemoryUtil.memPutShort(addr + 2, v.toInt().toShort())
            }

            VertexType.UBYTE, VertexType.BYTE -> {
                MemoryUtil.memPutByte(addr, u.toInt().toByte())
                MemoryUtil.memPutByte(addr + 1, v.toInt().toByte())
            }
        }
    }

    private fun putNormal(i: Int, x: Float, y: Float, z: Float) {
        val addr = baseAddress + i
        MemoryUtil.memPutByte(addr, ((x * Byte.MAX_VALUE).toInt() and 0xFF).toByte())
        MemoryUtil.memPutByte(addr + 1, ((y * Byte.MAX_VALUE).toInt() and 0xFF).toByte())
        MemoryUtil.memPutByte(addr + 2, ((z * Byte.MAX_VALUE).toInt() and 0xFF).toByte())
    }

    object Factory : DrawBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
            return GlDrawBuffer(mode, format, bufferSize)
        }
    }
}
