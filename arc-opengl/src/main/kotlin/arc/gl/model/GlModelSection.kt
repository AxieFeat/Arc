package arc.gl.model

import arc.gl.graphics.GlDrawer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.model.Element
import arc.model.Face
import arc.model.Model
import arc.model.animation.Animation
import arc.model.cube.Cube
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf

internal data class GlModelSection(
    val groupName: String,
    private val model: Model,
    private val section: Set<Element>
) {

    private val matrix = Matrix4f()
    private val rotation = Quaternionf()

    private val vertexFormat = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV0)
        .build()

    private var currentAnimation: GlAnimationProcessor? = null

    fun tick(partial: Float) {
        if(currentAnimation?.update(partial) == true) {
            currentAnimation = null
        }
    }

    fun render(rootMatrix: Matrix4f) {
        matrix.mul(rootMatrix)
        val buffer = generateBuffer()
        GlDrawer.draw(buffer)
        buffer.cleanup()
    }

    fun playAnimation(animation: Animation) {
        currentAnimation = GlAnimationProcessor(
            matrix = matrix,
            animation = animation,
        )
    }

    fun stopAnimation() {
        currentAnimation = null
    }

    fun rotate(x: Float, y: Float, z: Float) {
        rotation.rotateXYZ(
            Math.toRadians(x),
            Math.toRadians(y),
            Math.toRadians(z)
        )

        matrix.rotation(rotation)
    }

    fun scale(x: Float, y: Float, z: Float) {
        matrix.scale(x, y, z)
    }

    fun position(x: Float, y: Float, z: Float) {
        matrix.translate(x, y, z)
    }

    private fun generateBuffer(): VertexBuffer {
        val buffer = GlDrawer.begin(DrawerMode.TRIANGLES, vertexFormat, model.elements.size * 6 * 30)

        section.forEach { cube ->
            if (cube is Cube) {

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

}