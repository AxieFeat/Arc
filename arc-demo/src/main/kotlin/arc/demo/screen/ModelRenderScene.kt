package arc.demo.screen

import arc.demo.shader.ShaderContainer
import arc.graphics.ModelRender
import arc.input.KeyCode
import arc.lwamodel.LwaModel
import arc.lwamodel.animation.LwamAnimation
import arc.lwamodel.animation.LwamAnimator
import arc.lwamodel.animation.LwamKeyframe
import arc.lwamodel.cube.LwamCube
import arc.lwamodel.cube.LwamCubeFace
import arc.lwamodel.group.LwamElementGroup
import arc.lwamodel.texture.LwamTexture
import arc.math.Point2i
import arc.math.Point3d
import arc.model.Face
import arc.model.animation.AnimationChannel
import arc.model.animation.AnimationLoopMode
import arc.util.InterpolationMode
import org.joml.Vector3f
import java.util.*

object ModelRenderScene : Screen("main-menu") {

    private val model = LwaModel.of(
        elements = listOf(
            LwamCube.of(
                uuid = UUID.nameUUIDFromBytes("1".toByteArray()),
                from = Point3d.of(-8.0, 0.0, -8.0),
                to = Point3d.of(-3.0, 5.0, 7.0),
                faces = mapOf(
                    Face.NORTH to LwamCubeFace.of(
                        uvMin = Point2i.of(10, 24),
                        uvMax = Point2i.of(15, 29),
                        texture = 0
                    ),
                    Face.EAST to LwamCubeFace.of(
                        uvMin = Point2i.of(0, 0),
                        uvMax = Point2i.of(15, 5),
                        texture = 0
                    ),
                    Face.SOUTH to LwamCubeFace.of(
                        uvMin = Point2i.of(20, 24),
                        uvMax = Point2i.of(25, 29),
                        texture = 0
                    ),
                    Face.WEST to LwamCubeFace.of(
                        uvMin = Point2i.of(0, 5),
                        uvMax = Point2i.of(15, 10),
                        texture = 0
                    ),
                    Face.UP to LwamCubeFace.of(
                        uvMin = Point2i.of(0, 10),
                        uvMax = Point2i.of(5, 25),
                        texture = 0
                    ),
                    Face.DOWN to LwamCubeFace.of(
                        uvMin = Point2i.of(5, 10),
                        uvMax = Point2i.of(10, 25),
                        texture = 0
                    )
                )
            ),
            LwamCube.of(
                uuid = UUID.nameUUIDFromBytes("2".toByteArray()),
                from = Point3d.of(-8.0, 5.0, -3.0),
                to = Point3d.of(-3.0, 19.0, 2.0),
                faces = mapOf(
                    Face.NORTH to LwamCubeFace.of(
                        uvMin = Point2i.of(10, 10),
                        uvMax = Point2i.of(15, 24),
                        texture = 0
                    ),
                    Face.EAST to LwamCubeFace.of(
                        uvMin = Point2i.of(15, 0),
                        uvMax = Point2i.of(20, 14),
                        texture = 0
                    ),
                    Face.SOUTH to LwamCubeFace.of(
                        uvMin = Point2i.of(15, 14),
                        uvMax = Point2i.of(20, 28),
                        texture = 0
                    ),
                    Face.WEST to LwamCubeFace.of(
                        uvMin = Point2i.of(20, 0),
                        uvMax = Point2i.of(25, 14),
                        texture = 0
                    ),
                    Face.UP to LwamCubeFace.of(
                        uvMin = Point2i.of(20, 14),
                        uvMax = Point2i.of(25, 19),
                        texture = 0
                    )
                )
            )
        ),
        groups = listOf(
            LwamElementGroup.of(
                name = "main",
                elements = setOf(
                    UUID.nameUUIDFromBytes("1".toByteArray()),
                    UUID.nameUUIDFromBytes("2".toByteArray())
                )
            )
        ),
        animations = listOf(
            LwamAnimation.of(
                name = "animation",
                loop = AnimationLoopMode.LOOP,
                duration = 1000.0,
                animators = setOf(
                    LwamAnimator.of(
                        target = "main",
                        keyframes = setOf(
//                            LwamKeyframe.of(
//                                interpolation = InterpolationMode.LINEAR,
//                                channel = AnimationChannel.ROTATION,
//                                time = 500.0,
//                                dataPoints = Point3d.of(0.0, 0.0, -30.0)
//                            ),
                            LwamKeyframe.of(
                                interpolation = InterpolationMode.LINEAR,
                                channel = AnimationChannel.POSITION,
                                time = 250.0,
                                dataPoints = Point3d.of(-11.0, 0.0, 0.0)
                            ),
                            LwamKeyframe.of(
                                interpolation = InterpolationMode.LINEAR,
                                channel = AnimationChannel.SCALE,
                                time = 500.0,
                                dataPoints = Point3d.of(1.0, 0.7, 1.0)
                            )
                        )
                    )
                )
            )
        ),
        textures = listOf(
            LwamTexture.of(
                id = 0,
                width = 32,
                height = 32,
                base64Image = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAUVJREFUWEdjdH7R+5+BimDjvJ8YpvFUVTHisoIR5AALcTGSnXDi5SuwHmS9IDGQA7gV5eDmfb3/iGHUAQRDgOTwR9JAcRR8aWsjOxH6J7FTngZADkBONMSGBihxjTqAaiFAbLCjq6OKA7ZceYiRCOf3TsZwE0d0A4bYC52ZlCdCkAMsVGTghp+484QB5ABpcQm42NOXLxhADlCQ4IaLPXjxlWHUAaAQQAckV0aURMFcxToMB+z5no8h5qMjj7s2pNQB7NLqcAt/Pr3JAHIAepoadQDdQwA9EdDVAdEtTBiJcO38HvolQpAD0AuxUQdQLQTQIxdUEKGXA2SlAXSDia0NsTkAW9XOodyHOxES2xaI3vPlP3ptiMsB6KEyvB2ALQRpFgLoli114cGI2x93izBaWFSJAmLTCqnqcKZOUg0iV/2AOwAAjj4EPxsEA/UAAAAASUVORK5CYII="
            )
        ),
    )

    private val modelRender = ModelRender.of(model)

    private val front = Vector3f()
    private val right = Vector3f()
    private val up = Vector3f()

    private var sensitivity = 0f
    private var speed = 0f

    init {
        camera.fov = 65f
        camera.zNear = 0.0001f
        camera.zFar = 1000f
        camera.rotate(0f, 90f, 0f)
        camera.position = Point3d.of(15.0, 10.0, 0.0)
        camera.update()

        application.renderSystem.enableCull()
        application.renderSystem.enableDepthTest()

        application.window.isVsync = true

//        modelRender.playAnimation("animation")
    }

    override fun doRender() {
        handleInput()

//        modelRender.tick(partial)

        modelRender.render(ShaderContainer.positionTexLight)
    }

    private fun handleInput() {
        this.front.set(0f, 0f, -1f).rotate(camera.rotation).normalize()
        this.right.set(1f, 0f, 0f).rotate(camera.rotation).normalize()
        this.up.set(0f, 1f, 0f).rotate(camera.rotation).normalize()

        var newX = camera.position.x
        var newY = camera.position.y
        var newZ = camera.position.z

        speed = if (application.keyboard.isPressed(KeyCode.KEY_LCONTROL)) {
            1f * 0.01f
        } else {
            5f * 0.01f
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
            this.sensitivity = 65f * 0.01f
            camera.rotate(
                -application.mouse.displayVec.x * sensitivity,
                -application.mouse.displayVec.y * sensitivity,
                0f
            )
        }

        camera.update()
    }

    override fun onFpsUpdate(fps: Int) {
        name = "FPS: $fps, Frame time: $frameTime ms"
    }
}