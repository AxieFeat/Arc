package arc.gl.graphics

import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.ShaderInstance
import arc.graphics.vertex.VertexFormat
import arc.math.Point3i
import arc.util.Color

data class GlDrawBuffer(
    override var mode: DrawerMode,
    override var format: VertexFormat
) : DrawBuffer {

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
        return this
    }

    override fun setColor(red: Int, green: Int, blue: Int, alpha: Int): GlDrawBuffer {
        return this
    }

    override fun setColor(color: Color): GlDrawBuffer {
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

    object Factory : DrawBuffer.Factory {
        override fun create(mode: DrawerMode, format: VertexFormat): DrawBuffer {
            return GlDrawBuffer(mode, format)
        }
    }
}