package arc.demo.screen

import arc.demo.shader.ShaderContainer
import arc.demo.shader.VertexFormatContainer
import arc.display.Display
import arc.files.classpath
import arc.graphics.DrawerMode
import arc.graphics.ModelHandler
import arc.input.KeyCode
import arc.lwamodel.LwaModel
import arc.lwamodel.animation.LwamAnimation
import arc.lwamodel.animation.LwamAnimator
import arc.lwamodel.animation.LwamKeyframe
import arc.lwamodel.cube.LwamCube
import arc.lwamodel.cube.LwamCubeFace
import arc.lwamodel.group.LwamElementGroup
import arc.lwamodel.texture.LwamTexture
import arc.math.AABB
import arc.math.Point2i
import arc.math.Point3d
import arc.math.Vec3f
import arc.model.Face
import arc.model.animation.AnimationChannel
import arc.model.animation.AnimationLoopMode
import arc.util.Color
import arc.util.InterpolationMode
import com.google.gson.Gson
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import org.joml.Vector3f
import java.util.*

object ModelRenderScene : Screen("main-menu") {

//    private val model = LwaModel.of(
//        elements = listOf(
//            LwamCube.of(
//                uuid = UUID.nameUUIDFromBytes("1".toByteArray()),
//                from = Point3d.of(-8.0, 0.0, -8.0),
//                to = Point3d.of(-3.0, 5.0, 7.0),
//                faces = mapOf(
//                    Face.NORTH to LwamCubeFace.of(
//                        uvMin = Point2i.of(10, 24),
//                        uvMax = Point2i.of(15, 29),
//                        texture = 0
//                    ),
//                    Face.EAST to LwamCubeFace.of(
//                        uvMin = Point2i.of(0, 0),
//                        uvMax = Point2i.of(15, 5),
//                        texture = 0
//                    ),
//                    Face.SOUTH to LwamCubeFace.of(
//                        uvMin = Point2i.of(20, 24),
//                        uvMax = Point2i.of(25, 29),
//                        texture = 0
//                    ),
//                    Face.WEST to LwamCubeFace.of(
//                        uvMin = Point2i.of(0, 5),
//                        uvMax = Point2i.of(15, 10),
//                        texture = 0
//                    ),
//                    Face.UP to LwamCubeFace.of(
//                        uvMin = Point2i.of(0, 10),
//                        uvMax = Point2i.of(5, 25),
//                        texture = 0
//                    ),
//                    Face.DOWN to LwamCubeFace.of(
//                        uvMin = Point2i.of(5, 10),
//                        uvMax = Point2i.of(10, 25),
//                        texture = 0
//                    )
//                )
//            ),
//            LwamCube.of(
//                uuid = UUID.nameUUIDFromBytes("2".toByteArray()),
//                from = Point3d.of(-8.0, 5.0, -3.0),
//                to = Point3d.of(-3.0, 19.0, 2.0),
//                faces = mapOf(
//                    Face.NORTH to LwamCubeFace.of(
//                        uvMin = Point2i.of(10, 10),
//                        uvMax = Point2i.of(15, 24),
//                        texture = 0
//                    ),
//                    Face.EAST to LwamCubeFace.of(
//                        uvMin = Point2i.of(15, 0),
//                        uvMax = Point2i.of(20, 14),
//                        texture = 0
//                    ),
//                    Face.SOUTH to LwamCubeFace.of(
//                        uvMin = Point2i.of(15, 14),
//                        uvMax = Point2i.of(20, 28),
//                        texture = 0
//                    ),
//                    Face.WEST to LwamCubeFace.of(
//                        uvMin = Point2i.of(20, 0),
//                        uvMax = Point2i.of(25, 14),
//                        texture = 0
//                    ),
//                    Face.UP to LwamCubeFace.of(
//                        uvMin = Point2i.of(20, 14),
//                        uvMax = Point2i.of(25, 19),
//                        texture = 0
//                    )
//                )
//            )
//        ),
//        groups = listOf(
//            LwamElementGroup.of(
//                name = "main",
//                elements = setOf(
//                    UUID.nameUUIDFromBytes("1".toByteArray()),
//                    UUID.nameUUIDFromBytes("2".toByteArray())
//                )
//            )
//        ),
//        animations = listOf(
//            LwamAnimation.of(
//                name = "animation",
//                loop = AnimationLoopMode.LOOP,
//                duration = 1000.0,
//                animators = setOf(
//                    LwamAnimator.of(
//                        target = "main",
//                        keyframes = setOf(
////                            LwamKeyframe.of(
////                                interpolation = InterpolationMode.LINEAR,
////                                channel = AnimationChannel.ROTATION,
////                                time = 500.0,
////                                dataPoints = Point3d.of(0.0, 0.0, -30.0)
////                            ),
//                            LwamKeyframe.of(
//                                interpolation = InterpolationMode.LINEAR,
//                                channel = AnimationChannel.POSITION,
//                                time = 250.0,
//                                dataPoints = Point3d.of(-11.0, 0.0, 0.0)
//                            ),
//                            LwamKeyframe.of(
//                                interpolation = InterpolationMode.LINEAR,
//                                channel = AnimationChannel.SCALE,
//                                time = 500.0,
//                                dataPoints = Point3d.of(1.0, 0.7, 1.0)
//                            )
//                        )
//                    )
//                )
//            )
//        ),
//        textures = listOf(
//            LwamTexture.of(
//                id = 0,
//                width = 32,
//                height = 32,
//                base64Image = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAUVJREFUWEdjdH7R+5+BimDjvJ8YpvFUVTHisoIR5AALcTGSnXDi5SuwHmS9IDGQA7gV5eDmfb3/iGHUAQRDgOTwR9JAcRR8aWsjOxH6J7FTngZADkBONMSGBihxjTqAaiFAbLCjq6OKA7ZceYiRCOf3TsZwE0d0A4bYC52ZlCdCkAMsVGTghp+484QB5ABpcQm42NOXLxhADlCQ4IaLPXjxlWHUAaAQQAckV0aURMFcxToMB+z5no8h5qMjj7s2pNQB7NLqcAt/Pr3JAHIAepoadQDdQwA9EdDVAdEtTBiJcO38HvolQpAD0AuxUQdQLQTQIxdUEKGXA2SlAXSDia0NsTkAW9XOodyHOxES2xaI3vPlP3ptiMsB6KEyvB2ALQRpFgLoli114cGI2x93izBaWFSJAmLTCqnqcKZOUg0iV/2AOwAAjj4EPxsEA/UAAAAASUVORK5CYII="
//            )
//        ),
//    )

    private val model = loadModel(classpath("arc/model.bbmodel").readText())
    private val modelHandler = ModelHandler.of(model)

    private val display = Display.from(
        AABB.of(
            min = Vec3f.of(0f, 0f, 0f),
            max = Vec3f.of(10f, 25f, 0f),
        )
    )

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

        modelHandler.playAnimation("animation")
    }

    override fun doRender() {
        handleInput()

        display.update(camera)

        val aabb = modelHandler.aabb

        display.bind()

        drawer.begin(DrawerMode.TRIANGLES, VertexFormatContainer.positionColor, 256).use { buffer ->
            buffer.addVertex(-1f, 1f, 0f).setColor(Color.WHITE)
            buffer.addVertex(-1f, -1f, 0f).setColor(Color.WHITE)
            buffer.addVertex(1f, -1f, 0f).setColor(Color.WHITE)

            buffer.addVertex(-1f, 1f, 0f).setColor(Color.WHITE)
            buffer.addVertex(1f, -1f, 0f).setColor(Color.WHITE)
            buffer.addVertex(1f, 1f, 0f).setColor(Color.WHITE)

            buffer.build().use {
                ShaderContainer.positionColor.bind()

                drawer.draw(it)

                ShaderContainer.positionColor.unbind()
            }
        }
        modelHandler.render(ShaderContainer.positionTexLight)
        display.unbind()
//
        display.render(ShaderContainer.positionTex)

//        modelHandler.tick(partial)
//        val aabb = modelHandler.aabb
//
//        ShaderContainer.positionColor.bind()
//        drawer.begin(DrawerMode.LINES, VertexFormatContainer.positionColor).use { buffer ->
//            val (x0, y0, z0) = aabb.min
//            val (x1, y1, z1) = aabb.max
//
//            buffer.addVertex(x0, y0, z0).setColor(Color.RED)
//            buffer.addVertex(x1, y0, z0).setColor(Color.RED)
//
//            buffer.addVertex(x1, y0, z0).setColor(Color.RED)
//            buffer.addVertex(x1, y0, z1).setColor(Color.RED)
//
//            buffer.addVertex(x1, y0, z1).setColor(Color.RED)
//            buffer.addVertex(x0, y0, z1).setColor(Color.RED)
//
//            buffer.addVertex(x0, y0, z1).setColor(Color.RED)
//            buffer.addVertex(x0, y0, z0).setColor(Color.RED)
//
//
//            buffer.addVertex(x0, y1, z0).setColor(Color.RED)
//            buffer.addVertex(x1, y1, z0).setColor(Color.RED)
//
//            buffer.addVertex(x1, y1, z0).setColor(Color.RED)
//            buffer.addVertex(x1, y1, z1).setColor(Color.RED)
//
//            buffer.addVertex(x1, y1, z1).setColor(Color.RED)
//            buffer.addVertex(x0, y1, z1).setColor(Color.RED)
//
//            buffer.addVertex(x0, y1, z1).setColor(Color.RED)
//            buffer.addVertex(x0, y1, z0).setColor(Color.RED)
//
//
//            buffer.addVertex(x0, y0, z0).setColor(Color.RED)
//            buffer.addVertex(x0, y1, z0).setColor(Color.RED)
//
//            buffer.addVertex(x1, y0, z0).setColor(Color.RED)
//            buffer.addVertex(x1, y1, z0).setColor(Color.RED)
//
//            buffer.addVertex(x1, y0, z1).setColor(Color.RED)
//            buffer.addVertex(x1, y1, z1).setColor(Color.RED)
//
//            buffer.addVertex(x0, y0, z1).setColor(Color.RED)
//            buffer.addVertex(x0, y1, z1).setColor(Color.RED)
//
//            buffer.build().use {
//                drawer.draw(it)
//            }
//        }
//        drawer.begin(DrawerMode.LINES, VertexFormatContainer.positionColor).use { buffer ->
//
//            val (origin, direction) = camera.ray
//
//            buffer.addVertex(origin.x, origin.y, origin.z).setColor(Color.YELLOW)
//            buffer.addVertex(direction.x, direction.y, direction.z).setColor(Color.YELLOW)
//
//            buffer.build().use {
//                drawer.draw(it)
//            }
//        }
//        ShaderContainer.positionColor.unbind()

//
//        if(camera.frustum.isBoxInFrustum(aabb)) {
//            modelHandler.render(ShaderContainer.positionTexLight)
//        }
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

    private fun loadModel(jsonString: String): LwaModel {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)

        val elements = mutableListOf<LwamCube>()
        val elementsArray = jsonObject.getAsJsonArray("elements")
        for (element in elementsArray) {
            val elementObj = element.asJsonObject
            val uuid = UUID.fromString(elementObj.getAsJsonPrimitive("uuid").asString)
            val from = elementObj.getAsJsonArray("from").let {
                Point3d.of(it[0].asDouble, it[1].asDouble, it[2].asDouble)
            }
            val to = elementObj.getAsJsonArray("to").let {
                Point3d.of(it[0].asDouble, it[1].asDouble, it[2].asDouble)
            }

            val faces = mutableMapOf<Face, LwamCubeFace>()
            val facesObj = elementObj.getAsJsonObject("faces")
            facesObj.entrySet().forEach { (key, value) ->
                val texture = value.asJsonObject.getAsJsonPrimitive("texture")

                if(texture != null) {
                    val face = when (key) {
                        "north" -> Face.NORTH
                        "east" -> Face.EAST
                        "south" -> Face.SOUTH
                        "west" -> Face.WEST
                        "up" -> Face.UP
                        "down" -> Face.DOWN
                        else -> throw IllegalArgumentException("Unknown face: $key")
                    }
                    val uv = value.asJsonObject.getAsJsonArray("uv")
                    val uvMin = Point2i.of(uv[0].asInt, uv[1].asInt)
                    val uvMax = Point2i.of(uv[2].asInt, uv[3].asInt)
                    faces[face] = LwamCubeFace.of(uvMin, uvMax, texture.asInt)
                }
            }

            elements.add(LwamCube.of(uuid, from, to, faces))
        }

        val groups = mutableListOf<LwamElementGroup>()
        val outliner = jsonObject.getAsJsonArray("outliner")
        for (item in outliner) {
            val itemObj = item.asJsonObject
            val uuid = UUID.fromString(itemObj.getAsJsonPrimitive("uuid").asString)
            val name = itemObj.getAsJsonPrimitive("name").asString
            val children = itemObj.getAsJsonArray("children")
            val elementsSet = children.map { UUID.fromString(it.asString) }.toSet()
            groups.add(
                LwamElementGroup.of(
                    uuid = uuid,
                    name = name,
                    elements = elementsSet
                )
            )
        }

        val textures = mutableListOf<LwamTexture>()
        val texturesArray = jsonObject.getAsJsonArray("textures")
        for (texture in texturesArray) {
            val textureObj = texture.asJsonObject
            val id = textureObj.getAsJsonPrimitive("id").asInt
            val width = textureObj.getAsJsonPrimitive("width").asInt
            val height = textureObj.getAsJsonPrimitive("height").asInt
            val base64Image = textureObj.getAsJsonPrimitive("source").asString.split(",")[1] // Base64 строка без префикса data:image/png;base64,
            textures.add(LwamTexture.of(id, width, height, base64Image))
        }

        val animations = mutableListOf<LwamAnimation>()
        val animationsArray = jsonObject.getAsJsonArray("animations")
        for (anim in animationsArray) {
            val animObj = anim.asJsonObject
            val animationUUID = UUID.fromString(animObj.getAsJsonPrimitive("uuid").asString)
            val name = animObj.getAsJsonPrimitive("name").asString
            val duration = animObj.getAsJsonPrimitive("length").asDouble
            val loopMode = AnimationLoopMode.LOOP // TODO
            val animators = mutableSetOf<LwamAnimator>()
            val animatorsObj = animObj.getAsJsonObject("animators")
            animatorsObj.entrySet().forEach { (key, value) ->
                val animatorObj = value.asJsonObject
                val target = animatorObj.getAsJsonPrimitive("name").asString
                val keyframes = mutableSetOf<LwamKeyframe>()
                val keyframesArray = animatorObj.getAsJsonArray("keyframes")
                for (keyframe in keyframesArray) {
                    val channel = AnimationChannel.valueOf(keyframe.asJsonObject.getAsJsonPrimitive("channel").asString.uppercase())
                    val time = keyframe.asJsonObject.getAsJsonPrimitive("time").asDouble
                    val interpolation = InterpolationMode.valueOf(keyframe.asJsonObject.getAsJsonPrimitive("interpolation").asString.uppercase())
                    val uuid = keyframe.asJsonObject.getAsJsonPrimitive("uuid").asString
                    val dataPoints = keyframe.asJsonObject.getAsJsonArray("data_points").first().asJsonObject
                    keyframes.add(
                        LwamKeyframe.of(
                            uuid = UUID.fromString(uuid),
                            channel = channel,
                            time = time,
                            dataPoints = Point3d.of(dataPoints.getAsJsonPrimitive("x").asDouble, dataPoints.getAsJsonPrimitive("y").asDouble, dataPoints.getAsJsonPrimitive("z").asDouble),
                            interpolation = interpolation,
                        )
                    )
                }
                animators.add(
                    LwamAnimator.of(
                        uuid = UUID.fromString(key),
                        target = target,
                        keyframes = keyframes
                    )
                )
            }
            animations.add(
                LwamAnimation.of(
                    name = name,
                    uuid = animationUUID,
                    loop = loopMode,
                    startDelay = 0.0,
                    loopDelay = 0.0,
                    duration = duration,
                    animators = animators
                )
            )
        }

        return LwaModel.of(elements, groups, animations, textures)
    }
}