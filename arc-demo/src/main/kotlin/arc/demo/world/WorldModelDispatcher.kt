package arc.demo.world

import arc.demo.world.block.Block
import arc.graphics.DrawBuffer
import arc.graphics.Drawer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.model.Face
import arc.texture.TextureAtlas
import arc.util.Color
import arc.util.pattern.Cleanable
import org.joml.Matrix4f

class WorldModelDispatcher(
    val world: World,
    val drawer: Drawer,
    var cubes: Array<Block?>,
    val texture: TextureAtlas
) : Cleanable {

    private val matrix = Matrix4f()

    private val vertexFormat = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV)
        .add(VertexFormatElement.COLOR)
        .build()

    private var buffer: VertexBuffer? = generateBuffer(texture).use {
        it.build()
    }

    fun render() {
        buffer?.let {
            texture.bind()
            drawer.draw(it)
        }
    }

    private fun generateBuffer(atlas: TextureAtlas): DrawBuffer {
        val buffer = drawer.begin(DrawerMode.TRIANGLES, vertexFormat, cubes.size * 250)

        cubes.filterNotNull().forEach { block ->
            block.model.cubes.forEach { cube ->
                val (x1, y1, z1) = cube.from
                val (x2, y2, z2) = cube.to

                val posX = block.x
                val posY = block.y
                val posZ = block.z

                cube.faces.forEach { (face, cubeFace) ->

                    val neighbor = getNeighborOffset(face)
                    if (!shouldRenderFace(posX + neighbor.first, posY + neighbor.second, posZ + neighbor.third)) return@forEach

                    val uMin = cubeFace.uvMin.x.toFloat() / atlas.width
                    val vMin = cubeFace.uvMin.y.toFloat() / atlas.height
                    val uMax = cubeFace.uvMax.x.toFloat() / atlas.width
                    val vMax = cubeFace.uvMax.y.toFloat() / atlas.height

                    val vertices = when (face) {
                        Face.UP -> arrayOf(
                            floatArrayOf(x1, y2, z1),
                            floatArrayOf(x1, y2, z2),
                            floatArrayOf(x2, y2, z2),
                            floatArrayOf(x2, y2, z1)
                        )

                        Face.DOWN -> arrayOf(
                            floatArrayOf(x1, y1, z2),
                            floatArrayOf(x1, y1, z1),
                            floatArrayOf(x2, y1, z1),
                            floatArrayOf(x2, y1, z2)
                        )

                        Face.NORTH -> arrayOf(
                            floatArrayOf(x1, y1, z1),
                            floatArrayOf(x1, y2, z1),
                            floatArrayOf(x2, y2, z1),
                            floatArrayOf(x2, y1, z1)
                        )

                        Face.SOUTH -> arrayOf(
                            floatArrayOf(x2, y1, z2),
                            floatArrayOf(x2, y2, z2),
                            floatArrayOf(x1, y2, z2),
                            floatArrayOf(x1, y1, z2)
                        )

                        Face.WEST -> arrayOf(
                            floatArrayOf(x1, y1, z2),
                            floatArrayOf(x1, y2, z2),
                            floatArrayOf(x1, y2, z1),
                            floatArrayOf(x1, y1, z1)
                        )

                        Face.EAST -> arrayOf(
                            floatArrayOf(x2, y1, z1),
                            floatArrayOf(x2, y2, z1),
                            floatArrayOf(x2, y2, z2),
                            floatArrayOf(x2, y1, z2)
                        )
                    }

                    val ao = computeAOForFace(posX, posY, posZ, face)

                    buffer.addVertex(matrix, vertices[0][0], vertices[0][1], vertices[0][2])
                        .setTexture(uMin, vMax)
                        .setColor(ao[0])
                    buffer.addVertex(matrix, vertices[1][0], vertices[1][1], vertices[1][2])
                        .setTexture(uMin, vMin)
                        .setColor(ao[1])
                    buffer.addVertex(matrix, vertices[2][0], vertices[2][1], vertices[2][2])
                        .setTexture(uMax, vMin)
                        .setColor(ao[2])

                    buffer.addVertex(matrix, vertices[2][0], vertices[2][1], vertices[2][2])
                        .setTexture(uMax, vMin)
                        .setColor(ao[2])
                    buffer.addVertex(matrix, vertices[3][0], vertices[3][1], vertices[3][2])
                        .setTexture(uMax, vMax)
                        .setColor(ao[3])
                    buffer.addVertex(matrix, vertices[0][0], vertices[0][1], vertices[0][2])
                        .setTexture(uMin, vMax)
                        .setColor(ao[0])
                }
            }
        }

        return buffer
    }

    private fun getNeighborOffset(face: Face): Triple<Int, Int, Int> = when (face) {
        Face.UP -> Triple(0, 1, 0)
        Face.DOWN -> Triple(0, -1, 0)
        Face.NORTH -> Triple(0, 0, -1)
        Face.SOUTH -> Triple(0, 0, 1)
        Face.WEST -> Triple(-1, 0, 0)
        Face.EAST -> Triple(1, 0, 0)
    }

    private fun shouldRenderFace(x: Int, y: Int, z: Int): Boolean {
        return world.getBlock(x, y, z) == null
    }

    private fun computeAOForFace(x: Int, y: Int, z: Int, face: Face): Array<Color> {
        fun ao(side1: Boolean, side2: Boolean, corner: Boolean): Int {
            return when {
                side1 && side2 -> 0
                side1 || side2 -> if (corner) 85 else 100
                else -> if (corner) 170 else 255
            }
        }

        fun block(x: Int, y: Int, z: Int) = world.getBlock(x, y, z) != null

        val dirs = when (face) {
            Face.UP -> arrayOf(
                Triple(-1, 1, -1), Triple(0, 1, -1), Triple(1, 1, -1),
                Triple(1, 1, 0), Triple(1, 1, 1), Triple(0, 1, 1),
                Triple(-1, 1, 1), Triple(-1, 1, 0)
            )
            Face.DOWN -> arrayOf(
                Triple(-1, -1, 1), Triple(0, -1, 1), Triple(1, -1, 1),
                Triple(1, -1, 0), Triple(1, -1, -1), Triple(0, -1, -1),
                Triple(-1, -1, -1), Triple(-1, -1, 0)
            )
            Face.NORTH -> arrayOf(
                Triple(-1, -1, -1), Triple(0, -1, -1), Triple(1, -1, -1),
                Triple(1, 0, -1), Triple(1, 1, -1), Triple(0, 1, -1),
                Triple(-1, 1, -1), Triple(-1, 0, -1)
            )
            Face.SOUTH -> arrayOf(
                Triple(1, -1, 1), Triple(0, -1, 1), Triple(-1, -1, 1),
                Triple(-1, 0, 1), Triple(-1, 1, 1), Triple(0, 1, 1),
                Triple(1, 1, 1), Triple(1, 0, 1)
            )
            Face.WEST -> arrayOf(
                Triple(-1, -1, 1), Triple(-1, -1, 0), Triple(-1, -1, -1),
                Triple(-1, 0, -1), Triple(-1, 1, -1), Triple(-1, 1, 0),
                Triple(-1, 1, 1), Triple(-1, 0, 1)
            )
            Face.EAST -> arrayOf(
                Triple(1, -1, -1), Triple(1, -1, 0), Triple(1, -1, 1),
                Triple(1, 0, 1), Triple(1, 1, 1), Triple(1, 1, 0),
                Triple(1, 1, -1), Triple(1, 0, -1)
            )
        }

        val blocks = dirs.map { (dx, dy, dz) -> block(x + dx, y + dy, z + dz) }

        val indices = when (face) {
            Face.UP -> listOf(
                Triple(0, 1, 7), Triple(2, 3, 1),
                Triple(4, 5, 3), Triple(6, 7, 5)
            )
            Face.DOWN -> listOf(
                Triple(4, 5, 3), Triple(6, 7, 5),
                Triple(0, 1, 7), Triple(2, 3, 1)
            )
            Face.NORTH -> listOf(
                Triple(0, 1, 7), Triple(2, 3, 1),
                Triple(4, 5, 3), Triple(6, 7, 5)
            )
            Face.SOUTH -> listOf(
                Triple(4, 5, 3), Triple(6, 7, 5),
                Triple(0, 1, 7), Triple(2, 3, 1)
            )
            Face.WEST -> listOf(
                Triple(4, 5, 3), Triple(6, 7, 5),
                Triple(0, 1, 7), Triple(2, 3, 1)
            )
            Face.EAST -> listOf(
                Triple(0, 1, 7), Triple(2, 3, 1),
                Triple(4, 5, 3), Triple(6, 7, 5)
            )
        }

        return indices.map { (s1, s2, c) ->
            val side1 = blocks[s1]
            val side2 = blocks[s2]
            val corner = blocks[c]

            val light = ao(side1, side2, corner)
            Color.of(light, light, light)
        }.toTypedArray()
    }

    fun rebuild(cubes: Array<Block?>) {
        this.cubes = cubes
        buffer?.cleanup()

        buffer = generateBuffer(texture).use {
            if(it.vertexCount > 0) {
                it.build()
            } else null
        }
    }

    override fun cleanup() {
        texture.cleanup()
        buffer?.cleanup()
    }
}
