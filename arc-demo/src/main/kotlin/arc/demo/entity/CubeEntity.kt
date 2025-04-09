package arc.demo.entity

import arc.Application
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.shader.ShaderInstance
import arc.texture.TextureAtlas
import org.joml.Math
import org.joml.Matrix4f

class CubeEntity(
    private val application: Application,
    private val atlas: TextureAtlas,
    vertexFormat: VertexFormat,
) {

    private val texCoords = listOf(
        Pair(1, 1),
        Pair(1, 1),
        Pair(1, 1),
        Pair(1, 1),
        Pair(2, 1),
        Pair(1, 2)
    )
    private val positions = floatArrayOf(
        -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
        -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f
    )
    private val indices = intArrayOf(
        0, 1, 2, 2, 3, 0, 5, 4, 7, 7, 6, 5, 4, 0, 3, 3, 7, 4,
        1, 5, 6, 6, 2, 1, 3, 2, 6, 6, 7, 3, 4, 5, 1, 1, 0, 4
    )
    private val uvPattern = arrayOf(
        intArrayOf(3, 2, 1, 1, 0, 3),
        intArrayOf(0, 1, 2, 2, 3, 0)
    )

    private val cubeMatrix = Matrix4f()

    private val buffer = application.renderSystem.drawer.begin(DrawerMode.TRIANGLES, vertexFormat, 200).also {
        for (i in indices.indices step 6) {
            val (row, col) = texCoords[i / 6]

            // TODO fix for new atlas.
//            val uv = floatArrayOf(
//                atlas.u(row - 1, col - 1), atlas.v(row - 1, col - 1),
//                atlas.u(row - 1, col), atlas.v(row - 1, col - 1),
//                atlas.u(row - 1, col), atlas.v(row, col - 1),
//                atlas.u(row - 1, col - 1), atlas.v(row, col - 1)
//            )
            val order = uvPattern[if (i / 6 < 4) 0 else 1]

            repeat(6) { j ->
                it.addVertex(
                    cubeMatrix,
                    positions[indices[i + j] * 3],
                    positions[indices[i + j] * 3 + 1],
                    positions[indices[i + j] * 3 + 2]
                )
//                    .setTexture(uv[order[j] * 2], uv[order[j] * 2 + 1]) // TODO
            }
        }
    }

    private var vertexBuffer = buffer.build()

    fun render(shader: ShaderInstance) {
        shader.bind()
        atlas.bind()

        application.renderSystem.enableDepthTest()
        application.renderSystem.drawer.draw(vertexBuffer)
        application.renderSystem.disableDepthTest()

        atlas.unbind()
        shader.unbind()
    }

    fun rotate(x: Float, y: Float, z: Float) {
        cubeMatrix.setRotationXYZ(
            Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)
        ).normal()
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        cubeMatrix.translate(x, y, z)
    }

    fun setScale(x: Float, y: Float = x, z: Float = x) {
        cubeMatrix.scale(x, y, z)
    }
}