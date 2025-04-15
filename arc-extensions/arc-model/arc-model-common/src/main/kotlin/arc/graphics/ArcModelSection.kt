package arc.graphics

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

internal data class ArcModelSection(
    private val drawer: Drawer,
    val groupName: String,
    private val model: Model,
    private val section: List<Element>
) {

    private val matrix = Matrix4f()
    private val rotation = Quaternionf()

    private val vertexFormat = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV)
        .add(VertexFormatElement.LIGHT)
        .build()

    private var currentAnimation: ArcAnimationProcessor? = null

    private val buffer = generateBuffer().use {
        it.build()
    }.also {
        println(it.vertices)
    }

    fun tick(delta: Float) {
        if(currentAnimation?.update(delta) == true) {
            currentAnimation = null
        }
    }

    fun render(rootMatrix: Matrix4f) {
        matrix.mul(rootMatrix)

        drawer.draw(buffer)

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

    private fun generateBuffer(): DrawBuffer {
        val buffer = drawer.begin(DrawerMode.TRIANGLES, vertexFormat, model.elements.size * 60)

        val tempVertices = FloatArray(12)

        section.forEach { cube ->
            if (cube is Cube) {
                val x1 = cube.from.x.toFloat()
                val y1 = cube.from.y.toFloat()
                val z1 = cube.from.z.toFloat()

                val x2 = cube.to.x.toFloat()
                val y2 = cube.to.y.toFloat()
                val z2 = cube.to.z.toFloat()

                cube.faces.forEach { (face, cubeFace) ->
                    val tex = model.textures.first { it.id == cubeFace.texture }

                    val uMin = cubeFace.uvMin.x.toFloat() / tex.width
                    val vMin = cubeFace.uvMin.y.toFloat() / tex.height
                    val uMax = cubeFace.uvMax.x.toFloat() / tex.width
                    val vMax = cubeFace.uvMax.y.toFloat() / tex.height

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

                    when(face) {

                        Face.NORTH -> {
                            buffer.addVertex(matrix, tempVertices[0], tempVertices[1], tempVertices[2])
                                .setTexture(uMin, vMax)
                                .setLight(packLight(8, 8))
                            buffer.addVertex(matrix, tempVertices[3], tempVertices[4], tempVertices[5])
                                .setTexture(uMin, vMin)
                                .setLight(packLight(8, 8))
                            buffer.addVertex(matrix, tempVertices[6], tempVertices[7], tempVertices[8])
                                .setTexture(uMax, vMin)
                                .setLight(packLight(8, 8))

                            buffer.addVertex(matrix, tempVertices[6], tempVertices[7], tempVertices[8])
                                .setTexture(uMax, vMin)
                                .setLight(packLight(8, 8))
                            buffer.addVertex(matrix, tempVertices[9], tempVertices[10], tempVertices[11])
                                .setTexture(uMax, vMax)
                                .setLight(packLight(8, 8))
                            buffer.addVertex(matrix, tempVertices[0], tempVertices[1], tempVertices[2])
                                .setTexture(uMin, vMax)
                                .setLight(packLight(8, 8))
                        }

                        Face.WEST -> {
                            buffer.addVertex(matrix, tempVertices[0], tempVertices[1], tempVertices[2])
                                .setTexture(uMin, vMax)
                                .setLight(packLight(10, 10))
                            buffer.addVertex(matrix, tempVertices[3], tempVertices[4], tempVertices[5])
                                .setTexture(uMin, vMin)
                                .setLight(packLight(10, 10))
                            buffer.addVertex(matrix, tempVertices[6], tempVertices[7], tempVertices[8])
                                .setTexture(uMax, vMin)
                                .setLight(packLight(10, 10))

                            buffer.addVertex(matrix, tempVertices[6], tempVertices[7], tempVertices[8])
                                .setTexture(uMax, vMin)
                                .setLight(packLight(10, 10))
                            buffer.addVertex(matrix, tempVertices[9], tempVertices[10], tempVertices[11])
                                .setTexture(uMax, vMax)
                                .setLight(packLight(10, 10))
                            buffer.addVertex(matrix, tempVertices[0], tempVertices[1], tempVertices[2])
                                .setTexture(uMin, vMax)
                                .setLight(packLight(10, 10))
                        }

                        else -> {
                            buffer.addVertex(matrix, tempVertices[0], tempVertices[1], tempVertices[2])
                                .setTexture(uMin, vMax)
                                .setLight(packLight(15, 15))
                            buffer.addVertex(matrix, tempVertices[3], tempVertices[4], tempVertices[5])
                                .setTexture(uMin, vMin)
                                .setLight(packLight(15, 15))
                            buffer.addVertex(matrix, tempVertices[6], tempVertices[7], tempVertices[8])
                                .setTexture(uMax, vMin)
                                .setLight(packLight(15, 15))

                            buffer.addVertex(matrix, tempVertices[6], tempVertices[7], tempVertices[8])
                                .setTexture(uMax, vMin)
                                .setLight(packLight(15, 15))
                            buffer.addVertex(matrix, tempVertices[9], tempVertices[10], tempVertices[11])
                                .setTexture(uMax, vMax)
                                .setLight(packLight(15, 15))
                            buffer.addVertex(matrix, tempVertices[0], tempVertices[1], tempVertices[2])
                                .setTexture(uMin, vMax)
                                .setLight(packLight(15, 15))
                        }
                    }
                }
            }
        }

        return buffer
    }

    private fun packLight(blockLight: Int, skyLight: Int): Int {
        return ((skyLight and 0xF) shl 16) or (blockLight and 0xF)
    }

}