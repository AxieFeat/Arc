package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.*
import arc.math.Point3i
import arc.shader.ShaderInstance
import arc.util.Color
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer

internal data class GlDrawBuffer(
    val bufferSize: Int,
    override var mode: DrawerMode,
    override var format: VertexFormat,
) : DrawBuffer {

    private val byteBuffer: ByteBuffer = GLAllocation.createDirectByteBuffer(bufferSize * 4);
    private val rawIntBuffer: IntBuffer = byteBuffer.asIntBuffer();
    private val rawShortBuffer: ShortBuffer = byteBuffer.asShortBuffer();
    private val rawFloatBuffer: FloatBuffer = byteBuffer.asFloatBuffer();
    private var vertexCount = 0
    private var vertexFormatElement: VertexFormatElement? = null
    private var vertexFormatIndex = 0

    /** None  */
    private var noColor = false
    private var xOffset = 0.0
    private var yOffset = 0.0
    private var zOffset = 0.0
    private var isDrawing = false

    override fun draw() {

    }

    override fun draw(shaderInstance: ShaderInstance) {
        GlRenderSystem.bindShader(shaderInstance)

        draw()
    }

    override fun addVertex(point: Point3i): GlDrawBuffer {
        return this
    }

    override fun addVertex(x: Int, y: Int, z: Int): GlDrawBuffer {
        val i: Int =
            this.vertexCount * this.format.nextOffset + this.format.getOffset(this.vertexFormatIndex)

        when (vertexFormatElement.type) {
            VertexType.FLOAT -> {
                byteBuffer!!.putFloat(i, (x + this.xOffset).toFloat())
                byteBuffer!!.putFloat(i + 4, (y + this.yOffset).toFloat())
                byteBuffer!!.putFloat(i + 8, (z + this.zOffset).toFloat())
            }

            VertexType.UINT, VertexType.INT -> {
                byteBuffer!!.putInt(i, java.lang.Float.floatToRawIntBits((x + this.xOffset).toFloat()))
                byteBuffer!!.putInt(i + 4, java.lang.Float.floatToRawIntBits((y + this.yOffset).toFloat()))
                byteBuffer!!.putInt(i + 8, java.lang.Float.floatToRawIntBits((z + this.zOffset).toFloat()))
            }

            VertexType.USHORT, VertexType.SHORT -> {
                byteBuffer!!.putShort(i, ((x + this.xOffset).toInt()).toShort())
                byteBuffer!!.putShort(i + 2, ((y + this.yOffset).toInt()).toShort())
                byteBuffer!!.putShort(i + 4, ((z + this.zOffset).toInt()).toShort())
            }

            VertexType.UBYTE, VertexType.BYTE -> {
                byteBuffer!!.put(i, ((x + this.xOffset).toInt()).toByte())
                byteBuffer!!.put(i + 1, ((y + this.yOffset).toInt()).toByte())
                byteBuffer!!.put(i + 2, ((z + this.zOffset).toInt()).toByte())
            }
        }

        this.nextVertexFormatIndex()
        return this
    }

    override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): GlDrawBuffer {
        return this
    }

    override fun setColor(color: Color): GlDrawBuffer {
        return this
    }

    override fun noColor(): VertexConsumer {
        return this
    }

    override fun setUv(u: Int, v: Int): GlDrawBuffer {
        return this
    }

    override fun setUv1(u: Int, v: Int): GlDrawBuffer {
        return this
    }

    override fun setUv2(u: Int, v: Int): GlDrawBuffer {
        return this
    }

    override fun setNormal(point: Point3i): GlDrawBuffer {
        return this
    }

    override fun setNormal(x: Int, y: Int, z: Int): GlDrawBuffer {
        return this
    }

    override fun setTranslation(x: Double, y: Double, z: Double): GlDrawBuffer {
        return this
    }

    private fun nextVertexFormatIndex() {
        this.vertexFormatIndex++
        this.vertexFormatIndex %= this.format.getElementCount()
        this.vertexFormatElement = this.format.getElement(this.vertexFormatIndex)

        if (vertexFormatElement.usage === VertexUsage.PADDING) {
            this.nextVertexFormatIndex()
        }
    }

    object Factory : DrawBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat): DrawBuffer {
            return GlDrawBuffer(mode, format)
        }
    }
}