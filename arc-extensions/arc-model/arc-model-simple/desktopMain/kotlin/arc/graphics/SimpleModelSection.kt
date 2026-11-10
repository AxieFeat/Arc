package arc.graphics

import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.model.Face
import arc.model.Model
import arc.model.animation.Animation
import arc.model.cube.Cube
import arc.texture.TextureAtlas
import arc.util.Color
import korlibs.math.geom.Matrix4
import korlibs.math.geom.Quaternion
import korlibs.math.geom.Vector3F
import korlibs.math.geom.degrees

internal data class SimpleModelSection(
    private val drawer: Drawer,
    val groupName: String,
    private val atlas: TextureAtlas,
    private val model: Model,
    private val section: List<Cube>
) {

    private val matrix = ModelMatrix()
    private var rotation = Quaternion()

    private val vertexFormat = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV)
        .add(VertexFormatElement.COLOR)
        .build()

    private var currentAnimation: SimpleAnimationProcessor? = null

    val buffer = generateBuffer(atlas).use {
        it.build()
    }

    fun tick(delta: Float) {
        if(currentAnimation?.update(delta) == true) {
            currentAnimation = null
        }
    }

    fun render(rootMatrix: Matrix4) {
        drawer.draw(matrix.value * rootMatrix, buffer)
        matrix.value = Matrix4()
    }

    fun playAnimation(animation: Animation) {
        currentAnimation = SimpleAnimationProcessor(
            matrix = matrix,
            animation = animation,
        )
    }

    fun stopAnimation() {
        currentAnimation = null
    }

    fun rotate(x: Float, y: Float, z: Float) {
        rotation = rotation * Quaternion.fromAxisAngle(Vector3F(1f, 0f, 0f), x.degrees) *
            Quaternion.fromAxisAngle(Vector3F(0f, 1f, 0f), y.degrees) *
            Quaternion.fromAxisAngle(Vector3F(0f, 0f, 1f), z.degrees)
        matrix.value = rotation.toMatrix()
    }

    fun scale(x: Float, y: Float, z: Float) {
        matrix.value = matrix.value.scaled(x, y, z)
    }

    fun position(x: Float, y: Float, z: Float) {
        matrix.value = matrix.value.translated(x, y, z)
    }

    private fun generateBuffer(atlas: TextureAtlas): DrawBuffer {
        val buffer = drawer.begin(DrawerMode.TRIANGLES, vertexFormat, model.cubes.size * 250)
        val tempVertices = FloatArray(12)
        val vertexMatrix = matrix.value

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

                buffer.addVertex(vertexMatrix, tempVertices[0], tempVertices[1], tempVertices[2])
                    .setTexture(uMin, vMax)
                    .setColor(Color.WHITE)
                buffer.addVertex(vertexMatrix, tempVertices[3], tempVertices[4], tempVertices[5])
                    .setTexture(uMin, vMin)
                    .setColor(Color.WHITE)
                buffer.addVertex(vertexMatrix, tempVertices[6], tempVertices[7], tempVertices[8])
                    .setTexture(uMax, vMin)
                    .setColor(Color.WHITE)

                buffer.addVertex(vertexMatrix, tempVertices[6], tempVertices[7], tempVertices[8])
                    .setTexture(uMax, vMin)
                    .setColor(Color.WHITE)
                buffer.addVertex(vertexMatrix, tempVertices[9], tempVertices[10], tempVertices[11])
                    .setTexture(uMax, vMax)
                    .setColor(Color.WHITE)
                buffer.addVertex(vertexMatrix, tempVertices[0], tempVertices[1], tempVertices[2])
                    .setTexture(uMin, vMax)
                    .setColor(Color.WHITE)
            }
        }

        return buffer
    }

}
