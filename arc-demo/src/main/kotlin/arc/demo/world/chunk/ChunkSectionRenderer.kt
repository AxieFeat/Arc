package arc.demo.world.chunk

import arc.Application
import arc.demo.world.block.Blocks
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.model.Face
import arc.texture.TextureAtlas
import arc.util.Color
import org.joml.Vector3f
import org.joml.component1
import org.joml.component2
import org.joml.component3
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class ChunkSectionRenderer(
    val section: ChunkSection,
    val ambientOcclusion: Boolean = true
) {

    private val world = section.chunk.world

    private var buffer: CompletableFuture<DrawBuffer?> = CompletableFuture<DrawBuffer?>()
    private var vertexBuffer: VertexBuffer? = null

    fun render() {
        if(buffer.isDone) {
            val buf = buffer.join()

            if(buf != null) {
                if(vertexBuffer == null) {
                    vertexBuffer = buf.build()
                }
                Blocks.blocksAtlas.bind()
                drawer.draw(vertexBuffer!!)
            }
        }
    }

    private fun generateBuffer(atlas: TextureAtlas): CompletableFuture<DrawBuffer?> {
        val completableFuture = CompletableFuture<DrawBuffer?>()

        executor.submit {

            if (section.isEmpty) {
                completableFuture.complete(null)
                return@submit
            }

            val blocks = section.chunk.getSectionBlocks(section.y)

            val buffer = drawer.begin(
                DrawerMode.TRIANGLES,
                vertexFormat,
                VertexFormatElement.POSITION.size +
                        VertexFormatElement.UV.size +
                        VertexFormatElement.COLOR.size
                        * 6 // Faces of cube
                        * 4096 // Blocks in one Section
                        * 16 // Count of section in one chunk
                        * 32 // Count of chunks  TODO: Player view distance here
            )

            for (y in 0..15) {
                for (z in 0..15) {
                    for (x in 0..15) {

                        val block = blocks[y][z][x]

                        if (block.model == null) continue

                        val worldX = section.chunk.x * 16 + x
                        val worldY = section.y * 16 + y
                        val worldZ = section.chunk.z * 16 + z

                        val model = block.model

                        model.cubes.forEach { cube ->
                            val (x1, y1, z1) = Vector3f(cube.from).add(
                                worldX.toFloat(),
                                worldY.toFloat(),
                                worldZ.toFloat()
                            )
                            val (x2, y2, z2) = Vector3f(cube.to).add(
                                worldX.toFloat(),
                                worldY.toFloat(),
                                worldZ.toFloat()
                            )

                            cube.faces.forEach { (face, cubeFace) ->
                                val (dx, dy, dz) = face.normal
                                if (isBlocked(
                                        worldX + dx.toInt(),
                                        worldY + dy.toInt(),
                                        worldZ + dz.toInt()
                                    )
                                ) return@forEach

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

                                val ao =
                                    if (ambientOcclusion) calculateAO(face, worldX, worldY, worldZ) else FloatArray(8)

                                val vertices = getFaceVertices(face, x1, y1, z1, x2, y2, z2, ao, light)

                                addQuad(buffer, uMin, vMin, uMax, vMax, vertices)
                            }
                        }
                    }
                }
            }

            completableFuture.complete(buffer)
        }

        return completableFuture
    }

    fun rebuild() {
        if(buffer.isDone) {
            buffer.join()?.cleanup()
        }
        vertexBuffer?.cleanup()
        vertexBuffer = null
        buffer = generateBuffer(Blocks.blocksAtlas)
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
            buffer.addVertex(x, y, z)
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
        return false
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

    companion object {
        private val executor = Executors.newFixedThreadPool(8)

        private val vertexFormat = VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.UV)
            .add(VertexFormatElement.COLOR)
            .build()

        private const val aoFactor = 0.15f

        private val drawer = Application.find().renderSystem.drawer
    }
}