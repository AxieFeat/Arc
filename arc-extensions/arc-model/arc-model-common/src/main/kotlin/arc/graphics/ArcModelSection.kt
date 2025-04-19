package arc.graphics

import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.model.Face
import arc.model.Model
import arc.model.animation.Animation
import arc.model.cube.Cube
import arc.texture.TextureAtlas
import arc.util.Color
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf

internal data class ArcModelSection(
    private val drawer: Drawer,
    val groupName: String,
    private val atlas: TextureAtlas,
    private val model: Model,
    private val section: List<Cube>
) {

    private val matrix = Matrix4f().translate(0f, 0f, 0f)
    private val rotation = Quaternionf()

    private val vertexFormat = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV)
        .add(VertexFormatElement.COLOR)
        .build()

    private var currentAnimation: ArcAnimationProcessor? = null

    val buffer = generateBuffer(atlas).use {
        it.build()
    }

    fun tick(delta: Float) {
        if(currentAnimation?.update(delta) == true) {
            currentAnimation = null
        }
    }

    fun render(rootMatrix: Matrix4f) {
        matrix.mul(rootMatrix)

        drawer.draw(matrix, buffer)

        matrix.identity()
    }

    fun playAnimation(animation: Animation) {
        currentAnimation = ArcAnimationProcessor(
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

    private fun generateBuffer(atlas: TextureAtlas): DrawBuffer {
        val buffer = drawer.begin(DrawerMode.TRIANGLES, vertexFormat, model.cubes.size * 250)
        val tempVertices = FloatArray(12)

        section.forEach { cube ->
            val (x1, y1, z1) = cube.from
            val (x2, y2, z2) = cube.to

            cube.faces.forEach { (face, cubeFace) ->

                val uMin = cubeFace.uvMin.x.toFloat() / atlas.width
                val vMin = cubeFace.uvMin.y.toFloat() / atlas.height
                val uMax = cubeFace.uvMax.x.toFloat() / atlas.width
                val vMax = cubeFace.uvMax.y.toFloat() / atlas.height

                when (face) {
                    Face.UP -> {
                        tempVertices[0] = x1; tempVertices[1] = y2; tempVertices[2] = z1
                        tempVertices[3] = x1; tempVertices[4] = y2; tempVertices[5] = z2
                        tempVertices[6] = x2; tempVertices[7] = y2; tempVertices[8] = z2
                        tempVertices[9] = x2; tempVertices[10] = y2; tempVertices[11] = z1
                    }

                    Face.DOWN -> {
                        tempVertices[0] = x1; tempVertices[1] = y1; tempVertices[2] = z2
                        tempVertices[3] = x1; tempVertices[4] = y1; tempVertices[5] = z1
                        tempVertices[6] = x2; tempVertices[7] = y1; tempVertices[8] = z1
                        tempVertices[9] = x2; tempVertices[10] = y1; tempVertices[11] = z2
                    }

                    Face.NORTH -> {
                        tempVertices[0] = x1; tempVertices[1] = y1; tempVertices[2] = z1
                        tempVertices[3] = x1; tempVertices[4] = y2; tempVertices[5] = z1
                        tempVertices[6] = x2; tempVertices[7] = y2; tempVertices[8] = z1
                        tempVertices[9] = x2; tempVertices[10] = y1; tempVertices[11] = z1
                    }

                    Face.SOUTH -> {
                        tempVertices[0] = x2; tempVertices[1] = y1; tempVertices[2] = z2
                        tempVertices[3] = x2; tempVertices[4] = y2; tempVertices[5] = z2
                        tempVertices[6] = x1; tempVertices[7] = y2; tempVertices[8] = z2
                        tempVertices[9] = x1; tempVertices[10] = y1; tempVertices[11] = z2
                    }

                    Face.WEST -> {
                        tempVertices[0] = x1; tempVertices[1] = y1; tempVertices[2] = z2
                        tempVertices[3] = x1; tempVertices[4] = y2; tempVertices[5] = z2
                        tempVertices[6] = x1; tempVertices[7] = y2; tempVertices[8] = z1
                        tempVertices[9] = x1; tempVertices[10] = y1; tempVertices[11] = z1
                    }

                    Face.EAST -> {
                        tempVertices[0] = x2; tempVertices[1] = y1; tempVertices[2] = z1
                        tempVertices[3] = x2; tempVertices[4] = y2; tempVertices[5] = z1
                        tempVertices[6] = x2; tempVertices[7] = y2; tempVertices[8] = z2
                        tempVertices[9] = x2; tempVertices[10] = y1; tempVertices[11] = z2
                    }
                }

                buffer.addVertex(matrix, tempVertices[0], tempVertices[1], tempVertices[2])
                    .setTexture(uMin, vMax)
                    .setColor(Color.WHITE)
                buffer.addVertex(matrix, tempVertices[3], tempVertices[4], tempVertices[5])
                    .setTexture(uMin, vMin)
                    .setColor(Color.WHITE)
                buffer.addVertex(matrix, tempVertices[6], tempVertices[7], tempVertices[8])
                    .setTexture(uMax, vMin)
                    .setColor(Color.WHITE)

                buffer.addVertex(matrix, tempVertices[6], tempVertices[7], tempVertices[8])
                    .setTexture(uMax, vMin)
                    .setColor(Color.WHITE)
                buffer.addVertex(matrix, tempVertices[9], tempVertices[10], tempVertices[11])
                    .setTexture(uMax, vMax)
                    .setColor(Color.WHITE)
                buffer.addVertex(matrix, tempVertices[0], tempVertices[1], tempVertices[2])
                    .setTexture(uMin, vMax)
                    .setColor(Color.WHITE)
            }
        }

        return buffer
    }

}