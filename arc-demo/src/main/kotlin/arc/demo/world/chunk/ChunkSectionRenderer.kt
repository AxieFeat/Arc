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
        if (buffer.isDone) {
            val buf = buffer.join()

            if (buf != null) {
                if (vertexBuffer == null) {
                    vertexBuffer = buf.build()
                }
                // Текстура: предполагается единый атлас для всех блоков (как и раньше).
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

            // Прогнозируем ёмкость без заведомого перерасхода: базовая оценка на секцию
            // В среднем далеко не все грани видимы, поэтому возьмём умеренную величину.
            val estimatedTriangles = 16 * 16 * 16 /*voxels*/ * 3 /*~среднее видимых граней*/ * 2 /*треугольника на грань*/
            val buffer = drawer.begin(DrawerMode.TRIANGLES, vertexFormat, max(estimatedTriangles, 8192))

            val chunk = section.chunk
            val baseX = chunk.x * 16
            val baseY = section.y * 16
            val baseZ = chunk.z * 16

            // Временный буфер под вершины одного квада, чтобы не аллоцировать массивы
            val v = FloatArray(4 * 4) // x,y,z,light на 4 вершины

            for (ly in 0..15) {
                val worldY = baseY + ly
                for (lz in 0..15) {
                    val worldZ = baseZ + lz
                    for (lx in 0..15) {
                        val worldX = baseX + lx

                        val paletteIndex = section.getBlockIndex(lx, ly, lz)
                        if (paletteIndex.toInt() == 0) continue // AIR по палитре

                        val block = chunk.getBlockByPaletteIndex(paletteIndex)
                        val model = block.model

                        // Для каждого куба модели — добавляем видимые грани
                        model.cubes.forEach { cube ->
                            // Локальные границы куба с учётом мирового оффсета
                            val x1 = cube.from.x + worldX
                            val y1 = cube.from.y + worldY
                            val z1 = cube.from.z + worldZ
                            val x2 = cube.to.x + worldX
                            val y2 = cube.to.y + worldY
                            val z2 = cube.to.z + worldZ

                            cube.faces.forEach { (face, cubeFace) ->
                                val n = face.normal
                                val nx = n.x.toInt()
                                val ny = n.y.toInt()
                                val nz = n.z.toInt()

                                // Куллинг по соседу
                                if (isBlocked(worldX + nx, worldY + ny, worldZ + nz)) return@forEach

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

                                val ao = if (ambientOcclusion) calculateAO(face, worldX, worldY, worldZ) else null

                                // Заполняем 4 вершины квада без выделения массивов
                                when (face) {
                                    Face.UP -> {
                                        setV(v, 0, x1, y2, z1, light, ao, 2, 3, 4)
                                        setV(v, 1, x1, y2, z2, light, ao, 2, 1, 5)
                                        setV(v, 2, x2, y2, z2, light, ao, 0, 1, 6)
                                        setV(v, 3, x2, y2, z1, light, ao, 0, 3, 7)
                                    }
                                    Face.DOWN -> {
                                        setV(v, 0, x1, y1, z2, light, ao, 2, 1, 5)
                                        setV(v, 1, x1, y1, z1, light, ao, 2, 3, 4)
                                        setV(v, 2, x2, y1, z1, light, ao, 0, 3, 7)
                                        setV(v, 3, x2, y1, z2, light, ao, 0, 1, 6)
                                    }
                                    Face.NORTH -> {
                                        setV(v, 0, x1, y1, z1, light, ao, 2, 3, 4)
                                        setV(v, 1, x1, y2, z1, light, ao, 0, 3, 7)
                                        setV(v, 2, x2, y2, z1, light, ao, 0, 1, 6)
                                        setV(v, 3, x2, y1, z1, light, ao, 2, 1, 5)
                                    }
                                    Face.SOUTH -> {
                                        setV(v, 0, x2, y1, z2, light, ao, 2, 1, 5)
                                        setV(v, 1, x2, y2, z2, light, ao, 0, 1, 6)
                                        setV(v, 2, x1, y2, z2, light, ao, 0, 3, 7)
                                        setV(v, 3, x1, y1, z2, light, ao, 2, 3, 4)
                                    }
                                    Face.WEST -> {
                                        setV(v, 0, x1, y1, z2, light, ao, 1, 2, 5)
                                        setV(v, 1, x1, y2, z2, light, ao, 0, 1, 6)
                                        setV(v, 2, x1, y2, z1, light, ao, 0, 3, 7)
                                        setV(v, 3, x1, y1, z1, light, ao, 2, 3, 4)
                                    }
                                    Face.EAST -> {
                                        setV(v, 0, x2, y1, z1, light, ao, 2, 3, 4)
                                        setV(v, 1, x2, y2, z1, light, ao, 0, 3, 7)
                                        setV(v, 2, x2, y2, z2, light, ao, 0, 1, 6)
                                        setV(v, 3, x2, y1, z2, light, ao, 2, 1, 5)
                                    }
                                }

                                addQuad(buffer, uMin, vMin, uMax, vMax, v)
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
        if (buffer.isDone) {
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

    // Упаковываем координаты + освещение в один временный массив без аллокаций
    private fun setV(dst: FloatArray, i: Int, x: Float, y: Float, z: Float, baseLight: Float, ao: FloatArray?, a: Int, b: Int, c: Int) {
        val light = if (ao == null) baseLight else baseLight * (1f - ao[a] - ao[b] - ao[c])
        val o = i * 4
        dst[o] = x
        dst[o + 1] = y
        dst[o + 2] = z
        dst[o + 3] = light
    }

    private fun addQuad(
        buffer: DrawBuffer,
        uMin: Float, vMin: Float, uMax: Float, vMax: Float,
        v: FloatArray // 4 вершины по 4 значения (x,y,z,light)
    ) {
        fun toColorValue(f: Float) = (f.coerceIn(0f, 1f) * 255f).toInt()
        fun color(f: Float) = Color.of(toColorValue(f), toColorValue(f), toColorValue(f))

        fun add(i: Int, u: Float, vTex: Float) {
            val o = i * 4
            buffer.addVertex(v[o], v[o + 1], v[o + 2])
                .setTexture(u, vTex)
                .setColor(color(v[o + 3]))
        }

        // Треугольник 1
        add(0, uMin, vMax)
        add(1, uMin, vMin)
        add(2, uMax, vMin)
        // Треугольник 2
        add(2, uMax, vMin)
        add(3, uMax, vMax)
        add(0, uMin, vMax)
    }

    private fun isBlocked(x: Int, y: Int, z: Int): Boolean {
        val neighbor = world.getBlock(x, y, z)
        return !neighbor.isAir && neighbor.opaque
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

        private fun max(a: Int, b: Int): Int {
            return if (a > b) a else b
        }

        private val vertexFormat = VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.UV)
            .add(VertexFormatElement.COLOR)
            .build()

        private const val aoFactor = 0.15f

        private val drawer = Application.find().renderSystem.drawer
    }
}
