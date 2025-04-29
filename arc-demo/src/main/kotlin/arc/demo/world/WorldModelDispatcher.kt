package arc.demo.world

import arc.demo.world.block.Block
import arc.graphics.DrawBuffer
import arc.graphics.Drawer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexArrayBuffer
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
    val texture: TextureAtlas,
    val ambientOcclusion: Boolean = true
) : Cleanable {

    private val matrix = Matrix4f()
    private val aoFactor = 0.15f

    private val vertexFormat = VertexFormat.builder()
        .add(VertexFormatElement.POSITION)
        .add(VertexFormatElement.UV)
        .add(VertexFormatElement.COLOR)
        .build()

    private var vertexBuffer: VertexBuffer? = generateBuffer(texture).use { it.build() }

    fun render() {
        vertexBuffer?.let {
            texture.bind()
            drawer.draw(it)
        }
    }

    private fun generateBuffer(atlas: TextureAtlas): DrawBuffer {
        val buffer = drawer.begin(DrawerMode.TRIANGLES, vertexFormat, cubes.size * 250)

        cubes.filterNotNull().forEach { block ->
            val posX = block.x
            val posY = block.y
            val posZ = block.z

            block.model.cubes.forEach { cube ->
                val (x1, y1, z1) = cube.from
                val (x2, y2, z2) = cube.to

                cube.faces.forEach { (face, cubeFace) ->
                    val (dx, dy, dz) = getNeighborOffset(face)
                    if (isBlocked(posX + dx, posY + dy, posZ + dz)) return@forEach

                    val uMin = cubeFace.uvMin.x.toFloat() / atlas.width
                    val vMin = cubeFace.uvMin.y.toFloat() / atlas.height
                    val uMax = cubeFace.uvMax.x.toFloat() / atlas.width
                    val vMax = cubeFace.uvMax.y.toFloat() / atlas.height

                    val light = when (face) {
                        Face.UP -> 1.0f
                        Face.DOWN -> 0.75f
                        Face.NORTH -> 0.8f
                        Face.SOUTH -> 0.9f
                        Face.WEST -> 0.85f
                        Face.EAST -> 0.95f
                    }

                    val ao = if (ambientOcclusion) calculateAO(face, posX, posY, posZ) else FloatArray(8)

                    val vertices = getFaceVertices(face, x1, y1, z1, x2, y2, z2, ao, light)

                    addQuad(buffer, uMin, vMin, uMax, vMax, vertices)
                }
            }
        }

        return buffer
    }

    private fun calculateAO(face: Face, x: Int, y: Int, z: Int): FloatArray {
        val offsets = faceAOOffsets(face)
        return FloatArray(8) { i ->
            if (isBlocked(x + offsets[i][0], y + offsets[i][1], z + offsets[i][2])) aoFactor else 0f
        }
    }

    private fun getFaceVertices(
        face: Face,
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        ao: FloatArray,
        light: Float
    ): Array<FloatArray> = when (face) {
        Face.UP -> arrayOf(
            floatArrayOf(x1, y2, z1, light * (1f - ao[2] - ao[3] - ao[4])),
            floatArrayOf(x1, y2, z2, light * (1f - ao[2] - ao[1] - ao[5])),
            floatArrayOf(x2, y2, z2, light * (1f - ao[0] - ao[1] - ao[6])),
            floatArrayOf(x2, y2, z1, light * (1f - ao[0] - ao[3] - ao[7]))
        )
        Face.DOWN -> arrayOf(
            floatArrayOf(x1, y1, z2, light * (1f - ao[2] - ao[1] - ao[5])),
            floatArrayOf(x1, y1, z1, light * (1f - ao[2] - ao[3] - ao[4])),
            floatArrayOf(x2, y1, z1, light * (1f - ao[0] - ao[3] - ao[7])),
            floatArrayOf(x2, y1, z2, light * (1f - ao[0] - ao[1] - ao[6]))
        )
        Face.NORTH -> arrayOf(
            floatArrayOf(x1, y1, z1, light * (1f - ao[2] - ao[3] - ao[4])),
            floatArrayOf(x1, y2, z1, light * (1f - ao[0] - ao[3] - ao[7])),
            floatArrayOf(x2, y2, z1, light * (1f - ao[0] - ao[1] - ao[6])),
            floatArrayOf(x2, y1, z1, light * (1f - ao[2] - ao[1] - ao[5]))
        )
        Face.SOUTH -> arrayOf(
            floatArrayOf(x2, y1, z2, light * (1f - ao[2] - ao[1] - ao[5])),
            floatArrayOf(x2, y2, z2, light * (1f - ao[0] - ao[1] - ao[6])),
            floatArrayOf(x1, y2, z2, light * (1f - ao[0] - ao[3] - ao[7])),
            floatArrayOf(x1, y1, z2, light * (1f - ao[2] - ao[3] - ao[4]))
        )
        Face.WEST -> arrayOf(
            floatArrayOf(x1, y1, z2, light * (1f - ao[1] - ao[2] - ao[5])),
            floatArrayOf(x1, y2, z2, light * (1f - ao[0] - ao[1] - ao[6])),
            floatArrayOf(x1, y2, z1, light * (1f - ao[0] - ao[3] - ao[7])),
            floatArrayOf(x1, y1, z1, light * (1f - ao[2] - ao[3] - ao[4]))
        )
        Face.EAST -> arrayOf(
            floatArrayOf(x2, y1, z1, light * (1f - ao[2] - ao[3] - ao[4])),
            floatArrayOf(x2, y2, z1, light * (1f - ao[0] - ao[3] - ao[7])),
            floatArrayOf(x2, y2, z2, light * (1f - ao[0] - ao[1] - ao[6])),
            floatArrayOf(x2, y1, z2, light * (1f - ao[2] - ao[1] - ao[5]))
        )
    }

    private fun addQuad(
        buffer: DrawBuffer,
        uMin: Float, vMin: Float, uMax: Float, vMax: Float,
        vertices: Array<FloatArray>
    ) {
        fun toColorValue(v: Float) = (v.coerceIn(0f, 1f) * 255).toInt()
        fun color(v: Float) = Color.of(toColorValue(v), toColorValue(v), toColorValue(v))

        fun addVertex(x: Float, y: Float, z: Float, u: Float, v: Float, light: Float) {
            buffer.addVertex(matrix, x, y, z)
                .setTexture(u, v)
                .setColor(color(light))
        }

        addVertex(vertices[0][0], vertices[0][1], vertices[0][2], uMin, vMax, vertices[0][3])
        addVertex(vertices[1][0], vertices[1][1], vertices[1][2], uMin, vMin, vertices[1][3])
        addVertex(vertices[2][0], vertices[2][1], vertices[2][2], uMax, vMin, vertices[2][3])
        addVertex(vertices[2][0], vertices[2][1], vertices[2][2], uMax, vMin, vertices[2][3])
        addVertex(vertices[3][0], vertices[3][1], vertices[3][2], uMax, vMax, vertices[3][3])
        addVertex(vertices[0][0], vertices[0][1], vertices[0][2], uMin, vMax, vertices[0][3])
    }

    private fun isBlocked(x: Int, y: Int, z: Int): Boolean {
        return world.getBlock(x, y, z) != null
    }

    private fun getNeighborOffset(face: Face): Triple<Int, Int, Int> = when (face) {
        Face.UP -> Triple(0, 1, 0)
        Face.DOWN -> Triple(0, -1, 0)
        Face.NORTH -> Triple(0, 0, -1)
        Face.SOUTH -> Triple(0, 0, 1)
        Face.WEST -> Triple(-1, 0, 0)
        Face.EAST -> Triple(1, 0, 0)
    }

    private fun faceAOOffsets(face: Face): Array<IntArray> = when (face) {
        Face.UP, Face.DOWN -> arrayOf(
            intArrayOf(1, 1, 0), intArrayOf(0, 1, 1), intArrayOf(-1, 1, 0), intArrayOf(0, 1, -1),
            intArrayOf(-1, 1, -1), intArrayOf(-1, 1, 1), intArrayOf(1, 1, 1), intArrayOf(1, 1, -1)
        )
        Face.NORTH, Face.SOUTH -> arrayOf(
            intArrayOf(0, 1, face.z()), intArrayOf(1, 0, face.z()), intArrayOf(0, -1, face.z()), intArrayOf(-1, 0, face.z()),
            intArrayOf(-1, -1, face.z()), intArrayOf(1, -1, face.z()), intArrayOf(1, 1, face.z()), intArrayOf(-1, 1, face.z())
        )
        Face.WEST, Face.EAST -> arrayOf(
            intArrayOf(face.x(), 1, 0), intArrayOf(face.x(), 0, 1), intArrayOf(face.x(), -1, 0), intArrayOf(face.x(), 0, -1),
            intArrayOf(face.x(), -1, -1), intArrayOf(face.x(), -1, 1), intArrayOf(face.x(), 1, 1), intArrayOf(face.x(), 1, -1)
        )
    }

    private fun Face.x() = when (this) {
        Face.WEST -> -1
        Face.EAST -> 1
        else -> 0
    }

    private fun Face.z() = when (this) {
        Face.NORTH -> -1
        Face.SOUTH -> 1
        else -> 0
    }

    fun rebuild(cubes: Array<Block?>) {
        this.cubes = cubes

        vertexBuffer = generateBuffer(texture).use {
            if (it.vertexCount > 0) it.build() else null
        }
    }

    override fun cleanup() {
        texture.cleanup()
        vertexBuffer?.cleanup()
    }
}
