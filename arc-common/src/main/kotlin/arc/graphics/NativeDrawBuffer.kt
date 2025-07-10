package arc.graphics

import arc.graphics.vertex.*
import arc.util.Color
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

internal data class NativeDrawBuffer(
    override var mode: DrawerMode,
    override var format: VertexFormat,
    override val bufferSize: Int,
) : DrawBuffer {

    override var byteBuffer: ByteBuffer = MemoryUtil.memAlloc(bufferSize * 4)
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
            (color.alpha * 255).toInt().coerceIn(0, 255)
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
        if(elementsToFill <= 0) throw IllegalStateException("Can not begin element: ${element.name}, all elements already configured!")
        if(vertexFormatElement != element) throw IllegalArgumentException("Can not begin element, waited ${element.name}, but receive ${vertexFormatElement.name}!")

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
        MemoryUtil.memPutFloat(addr + 4, y + yOffset)
        MemoryUtil.memPutFloat(addr + 8, z + zOffset)
    }

    private fun putColor(i: Int, red: Int, green: Int, blue: Int, alpha: Int) {
        val addr = baseAddress + i

        MemoryUtil.memPutInt(addr, (alpha shl 24) or (blue shl 16) or (green shl 8) or red)
    }

    private fun putTexture(i: Int, u: Float, v: Float) {
        val addr = baseAddress + i

        MemoryUtil.memPutFloat(addr, u)
        MemoryUtil.memPutFloat(addr + 4, v)
    }

    private fun putNormal(i: Int, x: Float, y: Float, z: Float) {
        val addr = baseAddress + i

        MemoryUtil.memPutByte(addr, ((x * Byte.MAX_VALUE).toInt() and 0xFF).toByte())
        MemoryUtil.memPutByte(addr + 1, ((y * Byte.MAX_VALUE).toInt() and 0xFF).toByte())
        MemoryUtil.memPutByte(addr + 2, ((z * Byte.MAX_VALUE).toInt() and 0xFF).toByte())
    }

    object Factory : DrawBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat, bufferSize: Int): DrawBuffer {
            return NativeDrawBuffer(mode, format, bufferSize)
        }
    }

    companion object {
        // Temp vector for transformation via matrix in addVertex() function
        @JvmStatic
        private val tempVector = Vector3f()
    }

    class DrawBufferOverflowException(
        message: String
    ) : RuntimeException(message)

}
