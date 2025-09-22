package arc.graphics

import arc.graphics.vertex.VertexArrayBuffer
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.graphics.vertex.VertexUsage
import arc.util.Color
import arc.util.SimpleColor
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

internal class NativeDrawBuffer(
    override val mode: DrawerMode,
    override val format: VertexFormat,
    override val bufferSize: Int,
) : DrawBuffer {

    override var byteBuffer: ByteBuffer = MemoryUtil.memAlloc(bufferSize * BYTES_PER_FLOAT)
    private var baseAddress: Long = MemoryUtil.memAddress(byteBuffer)

    override var vertexCount = 0

    private var vertexFormatIndex = 0 // Current index of an element in format.

    // Getter of current instance of an element in format.
    private val vertexFormatElement: VertexFormatElement
        get() = format.getElement(vertexFormatIndex)

    // Count of writing elements at current vertex.
    private var elementsToFill = format.count

    // Used for auto-ending of vertices.
    private var needEnding = false

    private var xOffset = 0.0f
    private var yOffset = 0.0f
    private var zOffset = 0.0f

    override fun build(): VertexBuffer {
        endWriting()

        return VertexBuffer.of(mode, format, byteBuffer, vertexCount)
    }

    override fun buildArray(): VertexArrayBuffer {
        endWriting()

        return VertexArrayBuffer.of(mode, format, byteBuffer, vertexCount)
    }

    override fun cleanup() {
        MemoryUtil.memFree(byteBuffer)
    }

    override fun addVertex(x: Float, y: Float, z: Float): NativeDrawBuffer {
        if (needEnding) endVertex() else needEnding = true

        val address = beginElement(VertexFormatElement.POSITION)

        putPosition(address, x, y, z)

        return this
    }

    override fun addVertex(matrix: Matrix4f, x: Float, y: Float, z: Float): NativeDrawBuffer {
        val vector3f = matrix.transformPosition(x, y, z, tempVector)

        return addVertex(vector3f.x, vector3f.y, vector3f.z)
    }

    override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): NativeDrawBuffer {
        val address = beginElement(VertexFormatElement.COLOR)
        putColor(address, red, green, blue, alpha)

        return this
    }

    override fun setColor(color: Color): NativeDrawBuffer {
        return setColor(
            color.red,
            color.green,
            color.blue,
            (color.alpha * SimpleColor.MAX_RGB_VALUE).toInt()
        )
    }

    override fun setTexture(u: Float, v: Float): NativeDrawBuffer {
        val address = beginElement(VertexFormatElement.UV)
        putTexture(address, u, v)

        return this
    }

    override fun setTranslation(x: Float, y: Float, z: Float): NativeDrawBuffer {
        this.xOffset = x
        this.yOffset = y
        this.zOffset = z

        return this
    }

    override fun setNormal(x: Float, y: Float, z: Float): NativeDrawBuffer {
        val address = beginElement(VertexFormatElement.NORMAL)
        putNormal(address, x, y, z)

        return this
    }

    /**
     * Get address for some element.
     */
    private fun beginElement(element: VertexFormatElement): Int {
        check(elementsToFill > 0) {
            "Can not begin element: ${element.name}, all elements already configured!"
        }
        check(vertexFormatElement == element) {
            "Can not begin element, waited ${element.name}, but receive ${vertexFormatElement.name}!"
        }

        val offset = (vertexCount * format.nextOffset + format.getOffset(vertexFormatIndex))

        ensureCapacity(offset, element.size)

        return offset.also {
            nextVertexFormatIndex()
        }
    }

    private fun endWriting(): NativeDrawBuffer {
        endVertex()

        byteBuffer.position(0)
        byteBuffer.limit(this.bufferSize * 4)

        return this
    }

    private fun endVertex(): NativeDrawBuffer {
        require(elementsToFill <= 0) {
            "Can not build vertex! Missing element in vertex: ${
                leftElements().joinToString(
                    ", "
                ) { it.name }
            }!"
        }

        elementsToFill = format.count

        vertexCount++
        vertexFormatIndex = 0
        return this
    }

    private fun nextVertexFormatIndex() {
        if(elementsToFill <= 0) return

        elementsToFill--

        vertexFormatIndex++
        vertexFormatIndex %= format.count
        if (vertexFormatElement.usage === VertexUsage.PADDING) {
            nextVertexFormatIndex()
        }
    }

    /**
     * Return elements, that yet not completed in the current vertex.
     */
    private fun leftElements(): List<VertexFormatElement> {
        if(elementsToFill <= 0) return emptyList()

        val result = mutableListOf<VertexFormatElement>()

        for(index in vertexFormatIndex..<vertexFormatIndex + elementsToFill) {
            result.add(format.getElement(index))
        }

        return result
    }

    private fun ensureCapacity(offset: Int, size: Int) {
        if (offset + size > byteBuffer.capacity()) {
            throw DrawBufferOverflowException("DrawBuffer overflow!" +
                    " Required ${(offset + size)} bytes," +
                    " but capacity is only ${byteBuffer.capacity()} bytes.")
        }
    }

    override fun clear() {
        byteBuffer.clear()

        needEnding = false
        vertexCount = 0
        vertexFormatIndex = 0
        elementsToFill = format.count
    }

    private fun putPosition(i: Int, x: Float, y: Float, z: Float) {
        val addr = baseAddress + i

        MemoryUtil.memPutFloat(addr, x + xOffset)
        MemoryUtil.memPutFloat(addr + BYTES_PER_FLOAT, y + yOffset)
        MemoryUtil.memPutFloat(addr + BYTES_PER_FLOAT * 2, z + zOffset)
    }

    private fun putColor(i: Int, red: Int, green: Int, blue: Int, alpha: Int) {
        val addr = baseAddress + i

        MemoryUtil.memPutInt(
            addr,
            (alpha shl COLOR_ALPHA_SHIFT) or
            (blue shl COLOR_BLUE_SHIFT) or
            (green shl COLOR_GREEN_SHIFT) or
            (red shl COLOR_RED_SHIFT)
        )
    }

    private fun putTexture(i: Int, u: Float, v: Float) {
        val addr = baseAddress + i

        MemoryUtil.memPutFloat(addr, u)
        MemoryUtil.memPutFloat(addr + BYTES_PER_FLOAT, v)
    }

    private fun putNormal(i: Int, x: Float, y: Float, z: Float) {
        val addr = baseAddress + i

        MemoryUtil.memPutByte(addr + NORMAL_X_OFFSET, ((x * Byte.MAX_VALUE).toInt() and BYTE_MASK).toByte())
        MemoryUtil.memPutByte(addr + NORMAL_Y_OFFSET, ((y * Byte.MAX_VALUE).toInt() and BYTE_MASK).toByte())
        MemoryUtil.memPutByte(addr + NORMAL_Z_OFFSET, ((z * Byte.MAX_VALUE).toInt() and BYTE_MASK).toByte())
    }

    class DrawBufferOverflowException(
        message: String
    ) : RuntimeException(message)

    object Factory : DrawBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
            return NativeDrawBuffer(mode, format, bufferSize)
        }
    }

    companion object {

        // Temp vector for transformation via matrix in addVertex() function
        @JvmStatic
        private val tempVector = Vector3f()
        private const val BYTES_PER_FLOAT = 4
        private const val COLOR_ALPHA_SHIFT = 24
        private const val COLOR_BLUE_SHIFT = 16
        private const val COLOR_GREEN_SHIFT = 8
        private const val COLOR_RED_SHIFT = 0
        private const val NORMAL_X_OFFSET = 0
        private const val NORMAL_Y_OFFSET = 1
        private const val NORMAL_Z_OFFSET = 2
        private const val BYTE_MASK = 0xFF
    }
}
