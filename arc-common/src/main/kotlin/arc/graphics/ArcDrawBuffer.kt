package arc.graphics

import arc.graphics.vertex.*
import arc.util.Color
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

internal data class ArcDrawBuffer(
    override var mode: DrawerMode,
    override var format: VertexFormat,
    override val bufferSize: Int,
) : DrawBuffer {

    override var byteBuffer: ByteBuffer = MemoryUtil.memAlloc(bufferSize * 4)
    private var baseAddress: Long = MemoryUtil.memAddress(byteBuffer)

    override var vertexCount = 0

    private var vertexFormatIndex = 0 // Current index of element in format.

    // Getter of current instance of element in format
    private val vertexFormatElement: VertexFormatElement
        get() = format.getElement(vertexFormatIndex)

    // Count of wrote elements at current vertex.
    private var elementsToFill = format.count

    // Used for auto-ending of vertices.
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

    override fun addVertex(x: Float, y: Float, z: Float): ArcDrawBuffer {
        if (needEnding) endVertex() else needEnding = true

        val address = beginElement(VertexFormatElement.POSITION)

        putPosition(address, x, y, z)

        return this
    }

    override fun addVertex(matrix: Matrix4f, x: Float, y: Float, z: Float): ArcDrawBuffer {
        val vector3f: Vector3f = matrix.transformPosition(x, y, z, Vector3f())

        return addVertex(vector3f.x, vector3f.y, vector3f.z)
    }

    override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): ArcDrawBuffer {
        val address = beginElement(VertexFormatElement.COLOR)
        putColor(address, red, green, blue, alpha)

        return this
    }

    override fun setColor(color: Color): ArcDrawBuffer {
        return setColor(
            color.red,
            color.green,
            color.blue,
            (color.alpha * 255).toInt().coerceIn(0, 255)
        )
    }

    override fun setTexture(u: Float, v: Float): ArcDrawBuffer {
        val address = beginElement(VertexFormatElement.UV)
        putTexture(address, u, v)

        return this
    }

    override fun setTranslation(x: Float, y: Float, z: Float): ArcDrawBuffer {
        this.xOffset = x
        this.yOffset = y
        this.zOffset = z

        return this
    }

    override fun setNormal(x: Float, y: Float, z: Float): ArcDrawBuffer {
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

        return (vertexCount * format.nextOffset + format.getOffset(vertexFormatIndex)).also {
            nextVertexFormatIndex()
        }
    }

    private fun endWriting(): ArcDrawBuffer {
        endVertex()

        byteBuffer.position(0)
        byteBuffer.limit(this.bufferSize * 4)

        return this
    }

    private fun endVertex(): ArcDrawBuffer {
        if(elementsToFill > 0) throw IllegalArgumentException("Can not build vertex! Missing element in vertex: ${
            leftElements().joinToString(
                ", "
            ) { it.name }
        }!")

        elementsToFill = format.count

        vertexCount++
        vertexFormatIndex = 0
        growBuffer(format.nextOffset)
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
     * Return elements, that yet not completed in current vertex.
     */
    private fun leftElements(): List<VertexFormatElement> {
        if(elementsToFill <= 0) return emptyList()

        val result = mutableListOf<VertexFormatElement>()

        for(index in vertexFormatIndex..<vertexFormatIndex + elementsToFill) {
            result.add(format.getElement(index))
        }

        return result
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

    override fun clear() {
        byteBuffer.clear()

        needEnding = false
        vertexCount = 0
        vertexFormatIndex = 0
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
            return ArcDrawBuffer(mode, format, bufferSize)
        }
    }
}
