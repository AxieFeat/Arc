package arc.demo.screen

import arc.demo.shader.ShaderContainer
import arc.demo.shader.VertexFormatContainer
import arc.graphics.DrawerMode
import arc.graphics.VertexBuffer
import arc.input.KeyCode
import arc.lwamodel.LWAModel
import arc.lwamodel.cube.LWAModelCube
import arc.lwamodel.cube.LWAModelCubeFace
import arc.lwamodel.texture.LWAModelTexture
import arc.math.Point2i
import arc.math.Point3d
import arc.model.Face
import arc.shader.ShaderInstance
import arc.texture.Texture
import org.joml.Vector3f
import java.util.*

object ModelRenderScene : Screen("main-menu") {

    private val model = LWAModel.of(
        elements = listOf(
            LWAModelCube.of(
                from = Point3d.of(-8.0, 0.0, -8.0),
                to = Point3d.of(-3.0, 5.0, 7.0),
                faces = mapOf(
                    Face.NORTH to LWAModelCubeFace.of(
                        uvMin = Point2i.of(10, 24),
                        uvMax = Point2i.of(15, 29),
                        texture = 0
                    ),
                    Face.EAST to LWAModelCubeFace.of(
                        uvMin = Point2i.of(0, 0),
                        uvMax = Point2i.of(15, 5),
                        texture = 0
                    ),
                    Face.SOUTH to LWAModelCubeFace.of(
                        uvMin = Point2i.of(20, 24),
                        uvMax = Point2i.of(25, 29),
                        texture = 0
                    ),
                    Face.WEST to LWAModelCubeFace.of(
                        uvMin = Point2i.of(0, 5),
                        uvMax = Point2i.of(15, 10),
                        texture = 0
                    ),
                    Face.UP to LWAModelCubeFace.of(
                        uvMin = Point2i.of(0, 10),
                        uvMax = Point2i.of(5, 25),
                        texture = 0
                    ),
                    Face.DOWN to LWAModelCubeFace.of(
                        uvMin = Point2i.of(5, 10),
                        uvMax = Point2i.of(10, 25),
                        texture = 0
                    )
                )
            ),
            LWAModelCube.of(
                from = Point3d.of(-8.0, 5.0, -3.0),
                to = Point3d.of(-3.0, 19.0, 2.0),
                faces = mapOf(
                    Face.NORTH to LWAModelCubeFace.of(
                        uvMin = Point2i.of(10, 10),
                        uvMax = Point2i.of(15, 24),
                        texture = 0
                    ),
                    Face.EAST to LWAModelCubeFace.of(
                        uvMin = Point2i.of(15, 0),
                        uvMax = Point2i.of(20, 14),
                        texture = 0
                    ),
                    Face.SOUTH to LWAModelCubeFace.of(
                        uvMin = Point2i.of(15, 14),
                        uvMax = Point2i.of(20, 28),
                        texture = 0
                    ),
                    Face.WEST to LWAModelCubeFace.of(
                        uvMin = Point2i.of(20, 0),
                        uvMax = Point2i.of(25, 14),
                        texture = 0
                    ),
                    Face.UP to LWAModelCubeFace.of(
                        uvMin = Point2i.of(20, 14),
                        uvMax = Point2i.of(25, 19),
                        texture = 0
                    )
                )
            )
        ),
        textures = listOf(
            LWAModelTexture.of(
                id = 0,
                width = 32,
                height = 32,
                base64Image = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAUVJREFUWEdjdH7R+5+BimDjvJ8YpvFUVTHisoIR5AALcTGSnXDi5SuwHmS9IDGQA7gV5eDmfb3/iGHUAQRDgOTwR9JAcRR8aWsjOxH6J7FTngZADkBONMSGBihxjTqAaiFAbLCjq6OKA7ZceYiRCOf3TsZwE0d0A4bYC52ZlCdCkAMsVGTghp+484QB5ABpcQm42NOXLxhADlCQ4IaLPXjxlWHUAaAQQAckV0aURMFcxToMB+z5no8h5qMjj7s2pNQB7NLqcAt/Pr3JAHIAepoadQDdQwA9EdDVAdEtTBiJcO38HvolQpAD0AuxUQdQLQTQIxdUEKGXA2SlAXSDia0NsTkAW9XOodyHOxES2xaI3vPlP3ptiMsB6KEyvB2ALQRpFgLoli114cGI2x93izBaWFSJAmLTCqnqcKZOUg0iV/2AOwAAjj4EPxsEA/UAAAAASUVORK5CYII="
            )
        )
    )

    private val modelTexture = model.textures.first().toTexture()

    private val buffer = generateBuffer()

    private val front = Vector3f()
    private val right = Vector3f()
    private val up = Vector3f()

    private var sensitivity = 0f
    private var speed = 0f

    init {
        camera.fov = 65f
        camera.zNear = 0.0001f
        camera.zFar = 10000000000000000000000000000000000f
        camera.position = Point3d.of(0.0, 0.0, 0.0)
        camera.update()

        application.renderSystem.enableCull()
        application.renderSystem.enableDepthTest()
    }

    override fun doRender() {
        handleInput()

        renderModel(ShaderContainer.positionTex)
    }

    private fun handleInput() {
        this.front.set(0f, 0f, -1f).rotate(camera.rotation).normalize()
        this.right.set(1f, 0f, 0f).rotate(camera.rotation).normalize()
        this.up.set(0f, 1f, 0f).rotate(camera.rotation).normalize()

        var newX = camera.position.x
        var newY = camera.position.y
        var newZ = camera.position.z

        speed = if (application.keyboard.isPressed(KeyCode.KEY_LCONTROL)) {
            1f * delta
        } else {
            5f * delta
        }

        if (application.keyboard.isPressed(KeyCode.KEY_W)) {
            newX += front.x * speed
            newY += front.y * speed
            newZ += front.z * speed
        }
        if (application.keyboard.isPressed(KeyCode.KEY_S)) {
            newX -= front.x * speed
            newY -= front.y * speed
            newZ -= front.z * speed
        }
        if (application.keyboard.isPressed(KeyCode.KEY_A)) {
            newX -= right.x * speed
            newY -= right.y * speed
            newZ -= right.z * speed
        }
        if (application.keyboard.isPressed(KeyCode.KEY_D)) {
            newX += right.x * speed
            newY += right.y * speed
            newZ += right.z * speed
        }
        if (application.keyboard.isPressed(KeyCode.KEY_SPACE)) {
            newX += up.x * speed
            newY += up.y * speed
            newZ += up.z * speed
        }
        if (application.keyboard.isPressed(KeyCode.KEY_LSHIFT)) {
            newX -= up.x * speed
            newY -= up.y * speed
            newZ -= up.z * speed
        }

        camera.position.x = newX
        camera.position.y = newY
        camera.position.z = newZ

        if (application.mouse.isPressed(KeyCode.MOUSE_LEFT)) {
            this.sensitivity = 65f * delta
            camera.rotate(
                -application.mouse.displayVec.x * sensitivity,
                -application.mouse.displayVec.y * sensitivity,
                0f
            )
        }

        camera.update()
    }

    private fun renderModel(shader: ShaderInstance) {
        shader.bind()
        modelTexture.bind()
        application.renderSystem.drawer.draw(buffer)
        modelTexture.unbind()
        shader.unbind()
    }

    private fun generateBuffer(): VertexBuffer {
        val buffer = application.renderSystem.drawer.begin(DrawerMode.TRIANGLES, VertexFormatContainer.positionTex, model.elements.size * 6 * 30)

        model.elements.forEach { cube ->
            if (cube is LWAModelCube) {

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

                    buffer.addVertex(vertices[0].first, vertices[0].second, vertices[0].third)
                        .setTexture(uMin, vMax)
                        .endVertex()

                    buffer.addVertex(vertices[1].first, vertices[1].second, vertices[1].third)
                        .setTexture(uMin, vMin)
                        .endVertex()

                    buffer.addVertex(vertices[2].first, vertices[2].second, vertices[2].third)
                        .setTexture(uMax, vMin)
                        .endVertex()

                    buffer.addVertex(vertices[2].first, vertices[2].second, vertices[2].third)
                        .setTexture(uMax, vMin)
                        .endVertex()

                    buffer.addVertex(vertices[3].first, vertices[3].second, vertices[3].third)
                        .setTexture(uMax, vMax)
                        .endVertex()

                    buffer.addVertex(vertices[0].first, vertices[0].second, vertices[0].third)
                        .setTexture(uMin, vMax)
                        .endVertex()
                }
            }
        }

        buffer.end()
        return buffer.build()
    }


    private fun LWAModelTexture.toTexture(): Texture {
        val image = Base64.getDecoder().decode(base64Image)

        return Texture.from(
            bytes = image
        )
    }

    override fun onFpsUpdate(fps: Int) {
        name = "FPS: $fps"
    }
}