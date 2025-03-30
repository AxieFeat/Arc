package arc.gl.graphics

import arc.graphics.DrawerMode
import arc.graphics.ModelRender
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.lwamodel.cube.LwamCube
import arc.math.Point3d
import arc.model.Face
import arc.model.Model
import arc.model.texture.ModelTexture
import arc.shader.ShaderInstance
import arc.texture.Texture
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import java.util.*

data class GlModelRender(
    override val model: Model
) : ModelRender {

    private val matrix = Matrix4f()
    private val vertexFormat = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV0)
        .build()

    override var position: Point3d = Point3d.ZERO
        set(value) {
            field = value

            matrix.translate(value.x.toFloat(), value.y.toFloat(), value.z.toFloat())
        }
    override var rotation: Quaternionf = Quaternionf()
        set(value) {
            field = value

            matrix.rotation(value)
        }
    override var scale: Float = 1f
        set(value) {
            field = value

            matrix.scale(value)
        }

    private val textures = model.textures.map { it.toTexture() }

    override fun tick(delta: Float) {

    }

    override fun render(shader: ShaderInstance) {
        shader.bind()
        textures.forEach { it.bind() }
        val buffer = generateBuffer()
        GlDrawer.draw(buffer)
        buffer.cleanup()
        textures.forEach { it.unbind() }
        shader.unbind()
    }

    override fun playAnimation(name: String) {

    }

    override fun stopAnimation(name: String) {

    }

    override fun rotate(x: Float, y: Float, z: Float) {
        rotation.rotateXYZ(
            Math.toRadians(x),
            Math.toRadians(y),
            Math.toRadians(z)
        )

        matrix.rotation(rotation)
    }

    private fun generateBuffer(): VertexBuffer {
        val buffer = GlDrawer.begin(DrawerMode.TRIANGLES, vertexFormat, model.elements.size * 6 * 30)

        model.elements.forEach { cube ->
            if (cube is LwamCube) {

                val x1 = cube.from.x.toFloat()
                val y1 = cube.from.y.toFloat()
                val z1 = cube.from.z.toFloat()

                val x2 = cube.to.x.toFloat()
                val y2 = cube.to.y.toFloat()
                val z2 = cube.to.z.toFloat()

                cube.faces.forEach { (face, cubeFace) ->
                    val tex = model.textures.find { it.id == cubeFace.texture }!!

                    val uMin = cubeFace.uvMin.x.toFloat() / tex.width
                    val vMin = cubeFace.uvMin.y.toFloat() / tex.height
                    val uMax = cubeFace.uvMax.x.toFloat() / tex.width
                    val vMax = cubeFace.uvMax.y.toFloat() / tex.height

                    val vertices = when (face) {
                        Face.UP -> arrayOf(
                            Triple(x1, y2, z1),
                            Triple(x1, y2, z2),
                            Triple(x2, y2, z2),
                            Triple(x2, y2, z1)
                        )
                        Face.DOWN -> arrayOf(
                            Triple(x1, y1, z2),
                            Triple(x1, y1, z1),
                            Triple(x2, y1, z1),
                            Triple(x2, y1, z2)
                        )
                        Face.NORTH -> arrayOf(
                            Triple(x1, y1, z1),
                            Triple(x1, y2, z1),
                            Triple(x2, y2, z1),
                            Triple(x2, y1, z1)
                        )
                        Face.SOUTH -> arrayOf(
                            Triple(x2, y1, z2),
                            Triple(x2, y2, z2),
                            Triple(x1, y2, z2),
                            Triple(x1, y1, z2)
                        )
                        Face.WEST -> arrayOf(
                            Triple(x1, y1, z2),
                            Triple(x1, y2, z2),
                            Triple(x1, y2, z1),
                            Triple(x1, y1, z1)
                        )
                        Face.EAST -> arrayOf(
                            Triple(x2, y1, z1),
                            Triple(x2, y2, z1),
                            Triple(x2, y2, z2),
                            Triple(x2, y1, z2)
                        )
                    }

                    buffer.addVertex(matrix, vertices[0].first, vertices[0].second, vertices[0].third)
                        .setTexture(uMin, vMax)
                        .endVertex()

                    buffer.addVertex(matrix, vertices[1].first, vertices[1].second, vertices[1].third)
                        .setTexture(uMin, vMin)
                        .endVertex()

                    buffer.addVertex(matrix, vertices[2].first, vertices[2].second, vertices[2].third)
                        .setTexture(uMax, vMin)
                        .endVertex()

                    buffer.addVertex(matrix, vertices[2].first, vertices[2].second, vertices[2].third)
                        .setTexture(uMax, vMin)
                        .endVertex()

                    buffer.addVertex(matrix, vertices[3].first, vertices[3].second, vertices[3].third)
                        .setTexture(uMax, vMax)
                        .endVertex()

                    buffer.addVertex(matrix, vertices[0].first, vertices[0].second, vertices[0].third)
                        .setTexture(uMin, vMax)
                        .endVertex()
                }
            }
        }

        buffer.end()
        return buffer.build()
    }

    private fun ModelTexture.toTexture(): Texture {
        val image = Base64.getDecoder().decode(base64Image)

        return Texture.from(
            bytes = image
        )
    }

    object Factory : ModelRender.Factory {
        override fun create(model: Model): ModelRender {
            return GlModelRender(model)
        }
    }

}