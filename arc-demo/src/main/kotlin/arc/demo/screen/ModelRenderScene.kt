package arc.demo.screen

import arc.demo.shader.ShaderContainer
import arc.graphics.ModelHandler
import arc.input.KeyCode
import arc.math.Point2i
import arc.math.Point3d
import arc.model.Face
import arc.model.Model
import arc.model.animation.Animation
import arc.model.cube.Cube
import arc.model.cube.CubeFace
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture
import com.google.gson.Gson
import com.google.gson.JsonObject
import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator
import org.joml.Vector3f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack
import java.util.*

object ModelRenderScene : Screen("main-menu") {

    private val noise = PerlinNoiseGenerator.newBuilder().setSeed(3301).setInterpolation(Interpolation.COSINE).build();

    private val defaultFaces = mapOf(
        Face.NORTH to CubeFace.of(
            uvMin = Point2i.of(0, 0),
            uvMax = Point2i.of(16, 16),
            texture = 0
        ),
        Face.EAST to CubeFace.of(
            uvMin = Point2i.of(0, 0),
            uvMax = Point2i.of(16, 16),
            texture = 0
        ),
        Face.SOUTH to CubeFace.of(
            uvMin = Point2i.of(0, 0),
            uvMax = Point2i.of(16, 16),
            texture = 0
        ),
        Face.WEST to CubeFace.of(
            uvMin = Point2i.of(0, 0),
            uvMax = Point2i.of(16, 16),
            texture = 0
        ),
        Face.UP to CubeFace.of(
            uvMin = Point2i.of(0, 0),
            uvMax = Point2i.of(16, 16),
            texture = 0
        ),
        Face.DOWN to CubeFace.of(
            uvMin = Point2i.of(0, 0),
            uvMax = Point2i.of(16, 16),
            texture = 0
        )
    )

    private val model = Model.of(
        elements = listOf(
            Cube.of(
                from = Point3d.of(0.0, 0.0, 0.0),
                to = Point3d.of(1.0, 1.0, 1.0),
                faces = defaultFaces
            ),
            Cube.of(
                from = Point3d.of(0.0, 1.0, 1.0),
                to = Point3d.of(1.0, 2.0, 2.0),
                faces = defaultFaces
            ),
            Cube.of(
                from = Point3d.of(0.0, 1.0, 0.0),
                to = Point3d.of(1.0, 2.0, 1.0),
                faces = defaultFaces
            )
        ),
        textures = listOf(
            ModelTexture.of(
                id = 0,
                width = 16,
                height = 16,
                base64Image = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAAAAAA6mKC9AAAAZElEQVR42jWNsQ0AMQjEGJcB3HsEr/xSkncBOgkfUyXIguoo4mEXGdkFFvBdlGmpwKCAWIWOeYFladCK4n2BXZZn6vR3dsXRfLnKWfFoXmtUrX81AiboaZ9u4I1G8KdqPICl6AdyLn2NfcJFIAAAAABJRU5ErkJggg=="
            )
        ),
    )

//    private val model = loadModel(classpath("arc/model.bbmodel").readText())
    private val modelHandler = ModelHandler.of(drawer, generateTerrain(128, 128))

    private val front = Vector3f()
    private val right = Vector3f()
    private val up = Vector3f()

    private var sensitivity = 0f
    private var speed = 0f

    private val lightCells = buildList {
        val gridSize = 8
        val cellSize = 1f / gridSize
        val center = (gridSize - 1) / 2f

        for (y in 0 until gridSize) {
            for (x in 0 until gridSize) {
                val dist = kotlin.math.sqrt((x - center) * (x - center) + (y - center) * (y - center))
                val maxDist = kotlin.math.sqrt(center * center + center * center)

                val strength = if (dist <= center) {
                    1f - (dist / maxDist)
                } else {
                    0f
                }

                add(
                    LightCell(
                        uvMin = x * cellSize to y * cellSize,
                        uvMax = (x + 1) * cellSize to (y + 1) * cellSize,
                        color = floatArrayOf(1f * strength, 0f, 0f, strength * 0.3f)
                    )
                )
            }
        }
    }


    init {
        camera.fov = 65f
        camera.zNear = 0.1f
        camera.zFar = 10000f
        camera.update()

        application.renderSystem.enableCull()
        application.renderSystem.enableDepthTest()

        application.window.isVsync = true

        uploadLightCells(ShaderContainer.positionTexLight.id, lightCells)
    }

    override fun doRender() {
        handleInput()

        val aabb = modelHandler.aabb

        if(camera.frustum.isBoxInFrustum(aabb)) {
            modelHandler.render(ShaderContainer.positionTexLight)
        }
    }

    private fun handleInput() {
        this.front.set(0f, 0f, -1f).rotate(camera.rotation).normalize()
        this.right.set(1f, 0f, 0f).rotate(camera.rotation).normalize()
        this.up.set(0f, 1f, 0f).rotate(camera.rotation).normalize()

        var newX = camera.position.x
        var newY = camera.position.y
        var newZ = camera.position.z

        speed = if (application.keyboard.isPressed(KeyCode.KEY_LCONTROL)) {
            1f
        } else {
            5f
        } * delta

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

    override fun onFpsUpdate(fps: Int) {
        name = "FPS: $fps, Frame time: $frameTime ms"
    }

    private fun loadModel(jsonString: String): Model {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)

        val elements = mutableListOf<Cube>()
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

            val faces = mutableMapOf<Face, CubeFace>()
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
                    faces[face] = CubeFace.of(uvMin, uvMax, texture.asInt)
                }
            }

            elements.add(Cube.of(uuid, from, to, faces))
        }

        val groups = mutableListOf<ElementGroup>()
        val outliner = jsonObject.getAsJsonArray("outliner")
        for (item in outliner) {
            val itemObj = item.asJsonObject
            val uuid = UUID.fromString(itemObj.getAsJsonPrimitive("uuid").asString)
            val name = itemObj.getAsJsonPrimitive("name").asString
            val children = itemObj.getAsJsonArray("children")
            val elementsSet = children.map { UUID.fromString(it.asString) }.toSet()
            groups.add(
                ElementGroup.of(
                    uuid = uuid,
                    name = name,
                    elements = elementsSet
                )
            )
        }

        val textures = mutableListOf<ModelTexture>()
        val texturesArray = jsonObject.getAsJsonArray("textures")
        for (texture in texturesArray) {
            val textureObj = texture.asJsonObject
            val id = textureObj.getAsJsonPrimitive("id").asInt
            val width = textureObj.getAsJsonPrimitive("width").asInt
            val height = textureObj.getAsJsonPrimitive("height").asInt
            val base64Image = textureObj.getAsJsonPrimitive("source").asString.split(",")[1] // Base64 строка без префикса data:image/png;base64,
            textures.add(ModelTexture.of(id, width, height, base64Image))
        }

        val animations = mutableListOf<Animation>()
//        val animationsArray = jsonObject.getAsJsonArray("animations")
//        for (anim in animationsArray) {
//            val animObj = anim.asJsonObject
//            val animationUUID = UUID.fromString(animObj.getAsJsonPrimitive("uuid").asString)
//            val name = animObj.getAsJsonPrimitive("name").asString
//            val duration = animObj.getAsJsonPrimitive("length").asDouble
//            val loopMode = AnimationLoopMode.LOOP // TODO
//            val animators = mutableSetOf<LwamAnimator>()
//            val animatorsObj = animObj.getAsJsonObject("animators")
//            animatorsObj.entrySet().forEach { (key, value) ->
//                val animatorObj = value.asJsonObject
//                val target = animatorObj.getAsJsonPrimitive("name").asString
//                val keyframes = mutableSetOf<LwamKeyframe>()
//                val keyframesArray = animatorObj.getAsJsonArray("keyframes")
//                for (keyframe in keyframesArray) {
//                    val channel = AnimationChannel.valueOf(keyframe.asJsonObject.getAsJsonPrimitive("channel").asString.uppercase())
//                    val time = keyframe.asJsonObject.getAsJsonPrimitive("time").asDouble
//                    val interpolation = InterpolationMode.valueOf(keyframe.asJsonObject.getAsJsonPrimitive("interpolation").asString.uppercase())
//                    val uuid = keyframe.asJsonObject.getAsJsonPrimitive("uuid").asString
//                    val dataPoints = keyframe.asJsonObject.getAsJsonArray("data_points").first().asJsonObject
//                    keyframes.add(
//                        LwamKeyframe.of(
//                            uuid = UUID.fromString(uuid),
//                            channel = channel,
//                            time = time,
//                            dataPoints = Point3d.of(dataPoints.getAsJsonPrimitive("x").asDouble, dataPoints.getAsJsonPrimitive("y").asDouble, dataPoints.getAsJsonPrimitive("z").asDouble),
//                            interpolation = interpolation,
//                        )
//                    )
//                }
//                animators.add(
//                    LwamAnimator.of(
//                        uuid = UUID.fromString(key),
//                        target = target,
//                        keyframes = keyframes
//                    )
//                )
//            }
//            animations.add(
//                LwamAnimation.of(
//                    name = name,
//                    uuid = animationUUID,
//                    loop = loopMode,
//                    startDelay = 0.0,
//                    loopDelay = 0.0,
//                    duration = duration,
//                    animators = animators
//                )
//            )
//        }

        return Model.of(elements, groups, animations, textures)
    }

    fun generateTerrain(width: Int, depth: Int): Model {
        val elements = mutableListOf<Cube>()

        for (x in 0 until width) {
            for (z in 0 until depth) {
                val height = (perlin(x.toDouble() / 10.0, z.toDouble() / 10.0) * 6).toInt().coerceAtLeast(1)

                for (y in 0 until height) {
                    val faces = mutableMapOf<Face, CubeFace>()

                    fun isAir(nx: Int, ny: Int, nz: Int): Boolean {
                        val nh = (perlin(nx.toDouble() / 10.0, nz.toDouble() / 10.0) * 6).toInt()
                        return ny >= nh
                    }

                    if (isAir(x, y, z - 1)) faces[Face.NORTH] = defaultFace()
                    if (isAir(x + 1, y, z)) faces[Face.EAST] = defaultFace()
                    if (isAir(x, y, z + 1)) faces[Face.SOUTH] = defaultFace()
                    if (isAir(x - 1, y, z)) faces[Face.WEST] = defaultFace()
                    if (isAir(x, y + 1, z)) faces[Face.UP] = defaultFace()
                    if (isAir(x, y - 1, z)) faces[Face.DOWN] = defaultFace()

                    if (faces.isNotEmpty()) {
                        elements.add(
                            Cube.of(
                                from = Point3d.of(x.toDouble(), y.toDouble(), z.toDouble()),
                                to = Point3d.of(x + 1.0, y + 1.0, z + 1.0),
                                faces = faces
                            )
                        )
                    }
                }
            }
        }

        return Model.of(
            elements = elements,
            textures = listOf(
                ModelTexture.of(
                    id = 0,
                    width = 16,
                    height = 16,
                    base64Image = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAAAAAA6mKC9AAAAZElEQVR42jWNsQ0AMQjEGJcB3HsEr/xSkncBOgkfUyXIguoo4mEXGdkFFvBdlGmpwKCAWIWOeYFladCK4n2BXZZn6vR3dsXRfLnKWfFoXmtUrX81AiboaZ9u4I1G8KdqPICl6AdyLn2NfcJFIAAAAABJRU5ErkJggg==" // та же текстура, что и в примере
                )
            )
        )
    }

    private fun defaultFace(): CubeFace {
        return CubeFace.of(
            uvMin = Point2i.of(0, 0),
            uvMax = Point2i.of(16, 16),
            texture = 0
        )
    }

    private fun perlin(x: Double, y: Double): Double {
        val value = noise.evaluateNoise(x, y)

        return (value + 1.0) / 2.0
    }

    data class LightCell(
        val uvMin: Pair<Float, Float>,
        val uvMax: Pair<Float, Float>,
        val color: FloatArray
    )

    fun uploadLightCells(programId: Int, lightCells: List<LightCell>) {
        val maxCells = 64
        val clampedCells = lightCells.take(maxCells)

        glUseProgram(programId)

        val uLightCellCountLoc = glGetUniformLocation(programId, "u_LightCellCount")
        glUniform1i(uLightCellCountLoc, clampedCells.size)

        MemoryStack.stackPush().use { stack ->
            for (i in clampedCells.indices) {
                val cell = clampedCells[i]

                val uvMinLoc = glGetUniformLocation(programId, "u_LightCells[$i].uvMin")
                val uvMaxLoc = glGetUniformLocation(programId, "u_LightCells[$i].uvMax")
                val colorLoc = glGetUniformLocation(programId, "u_LightCells[$i].color")

                glUniform2f(uvMinLoc, cell.uvMin.first, cell.uvMin.second)
                glUniform2f(uvMaxLoc, cell.uvMax.first, cell.uvMax.second)
                glUniform4f(colorLoc, cell.color[0], cell.color[1], cell.color[2], cell.color[3])
            }
        }
    }




}