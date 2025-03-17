package arc.demo.cube

import arc.Application
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.texture.TextureAtlas
import org.joml.Matrix4f

class CubeArray(
    private val application: Application,
    private val atlas: TextureAtlas,
    private val format: VertexFormat,
) {
    private val buffer = application.renderSystem.drawer.begin(DrawerMode.TRIANGLES, format, Int.MAX_VALUE / 4)

    private val texCoords = listOf(
        Pair(2, 1)
    )
    private val positions = floatArrayOf(
        -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
        -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f
    )
    private val indices = intArrayOf(
        3, 2, 6, 6, 7, 3
    )
    private val uvPattern = arrayOf(
        intArrayOf(3, 2, 1, 1, 0, 3)
    )

    fun addCube(x: Float, y: Float, z: Float) {
        val matrix = Matrix4f().translate(x, y, z)

        for (i in indices.indices step 6) {
            val (row, col) = texCoords[i / 6]
            val uv = floatArrayOf(
                atlas.u(row - 1, col - 1), atlas.v(row - 1, col - 1),
                atlas.u(row - 1, col), atlas.v(row - 1, col - 1),
                atlas.u(row - 1, col), atlas.v(row, col - 1),
                atlas.u(row - 1, col - 1), atlas.v(row, col - 1)
            )
            val order = uvPattern[0]

            repeat(6) { j ->
                buffer.addVertex(
                    matrix,
                    positions[indices[i + j] * 3],
                    positions[indices[i + j] * 3 + 1],
                    positions[indices[i + j] * 3 + 2]
                )
                    .setTexture(uv[order[j] * 2], uv[order[j] * 2 + 1])
                    .endVertex()
            }
        }
    }

    fun render() {
        application.renderSystem.drawer.draw(buffer)
    }

    fun end() {
        buffer.end()
    }
}