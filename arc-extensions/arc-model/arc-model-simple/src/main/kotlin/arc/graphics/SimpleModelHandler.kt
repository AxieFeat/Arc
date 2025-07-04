package arc.graphics

import arc.math.AABB
import arc.model.Model
import arc.model.cube.Cube
import arc.shader.ShaderInstance
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

internal data class SimpleModelHandler(
    override val drawer: Drawer,
    override val model: Model
) : ModelHandler {

    private val matrix = Matrix4f()
    private val rotation = Quaternionf()

    private var cachedAabb = AABB.of(Vector3f(), Vector3f())
    private var matrixDirty = true

    override val aabb: AABB
        get() {
            if (matrixDirty) {
                recalculateAABB()
                matrixDirty = false
            }
            return cachedAabb
        }

    private val texture = model.texture.toAtlasTexture()

    private val modelSections = run {
        if(model.groups.isEmpty()) return@run listOf(SimpleModelSection(drawer, "auto-generated", texture, model, model.cubes))

        val result = mutableListOf<SimpleModelSection>()

        model.groups.forEach { group ->
            val cubes = mutableListOf<Cube>()

            group.cubes.forEach { element ->
                cubes.add(model.cubes.find { it.uuid == element }!!)
            }

            result.add(SimpleModelSection(drawer, group.name, texture, model, cubes))
        }

        return@run result
    }

    override fun tick(delta: Float) {
        modelSections.forEach { it.tick(delta) }
    }

    override fun render(shader: ShaderInstance) {
        shader.bind()
        texture.bind()
        modelSections.forEach { it.render(matrix) }
        texture.unbind()
        shader.unbind()
    }

    override fun playAnimation(name: String) {
        val animation = model.animations.find { it.name == name }

        animation?.animators?.forEach { animator ->
            val target = modelSections.find { it.groupName == animator.target }

            target?.playAnimation(animation)
        }
    }

    override fun stopAnimation(name: String) {
        val animation = model.animations.find { it.name == name }

        animation?.animators?.forEach { animator ->
            val target = modelSections.find { it.groupName == animator.target }

            target?.stopAnimation()
        }
    }

    override fun rotate(x: Float, y: Float, z: Float) {
//        rotation.rotateXYZ(
//            Math.toRadians(x),
//            Math.toRadians(y),
//            Math.toRadians(z)
//        )
        matrix.rotateXYZ(
            Math.toRadians(x),
            Math.toRadians(y),
            Math.toRadians(z)
        ).normal()

//        matrix.rotation(rotation)
        matrixDirty = true
    }

    override fun scale(x: Float, y: Float, z: Float) {
        matrix.scale(x, y, z)
        matrixDirty = true
    }

    override fun position(x: Float, y: Float, z: Float) {
        matrix.translate(x, y, z)
        matrixDirty = true
    }

    private fun recalculateAABB() {
        var minX = Float.POSITIVE_INFINITY
        var minY = Float.POSITIVE_INFINITY
        var minZ = Float.POSITIVE_INFINITY

        var maxX = Float.NEGATIVE_INFINITY
        var maxY = Float.NEGATIVE_INFINITY
        var maxZ = Float.NEGATIVE_INFINITY

        val temp = Vector3f()

        model.cubes.forEach { element ->
            val x0 = element.from.x
            val y0 = element.from.y
            val z0 = element.from.z
            val x1 = element.to.x
            val y1 = element.to.y
            val z1 = element.to.z

            val corners = listOf(
                Vector3f(x0, y0, z0), Vector3f(x1, y0, z0),
                Vector3f(x1, y1, z0), Vector3f(x0, y1, z0),
                Vector3f(x0, y0, z1), Vector3f(x1, y0, z1),
                Vector3f(x1, y1, z1), Vector3f(x0, y1, z1)
            )

            corners.forEach { corner ->
                matrix.transformPosition(corner, temp)

                minX = Math.min(minX, temp.x)
                minY = Math.min(minY, temp.y)
                minZ = Math.min(minZ, temp.z)

                maxX = Math.max(maxX, temp.x)
                maxY = Math.max(maxY, temp.y)
                maxZ = Math.max(maxZ, temp.z)
            }
        }


        cachedAabb = AABB.of(
            min = Vector3f(minX, minY, minZ),
            max = Vector3f(maxX, maxY, maxZ),
        )
    }

    override fun cleanup() {
        modelSections.forEach {
            it.buffer.cleanup()
        }
    }

    object Factory : ModelHandler.Factory {
        override fun create(drawer: Drawer, model: Model): ModelHandler {
            return SimpleModelHandler(drawer, model)
        }
    }
}
