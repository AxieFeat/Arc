package arc.gl.model

import arc.graphics.ModelHandler
import arc.math.AABB
import arc.math.Vec3f
import arc.model.Element
import arc.model.Model
import arc.model.cube.Cube
import arc.model.texture.ModelTexture
import arc.shader.ShaderInstance
import arc.texture.Texture
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.*

internal data class GlModelHandler(
    override val model: Model
) : ModelHandler {

    private val matrix = Matrix4f()
    private val rotation = Quaternionf()

    private val cachedAabb = AABB.of(Vec3f.ZERO, Vec3f.ZERO)
    private var matrixDirty = true

    override val aabb: AABB
        get() {
            if (matrixDirty) {
                recalculateAABB()
                matrixDirty = false
            }
            return cachedAabb
        }

    private val textures = model.textures.map { it.toTexture() }

    private val modelSections = run {
        val result = mutableSetOf<GlModelSection>()

        model.groups.forEach { group ->
            val elements = mutableSetOf<Element>()

            group.elements.forEach { element ->
                elements.add(model.elements.find { it.uuid == element }!!)
            }

            result.add(GlModelSection(group.name, model, elements))
        }

        return@run result
    }

    override fun tick(partialTick: Float) {
        modelSections.forEach { it.tick(partialTick) }
    }

    override fun render(shader: ShaderInstance) {
        shader.bind()
        textures.forEach { it.bind() }
        modelSections.forEach { it.render(matrix) }
        textures.forEach { it.unbind() }
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

        model.elements.filterIsInstance<Cube>().forEach { element ->
            val x0 = element.from.x.toFloat()
            val y0 = element.from.y.toFloat()
            val z0 = element.from.z.toFloat()
            val x1 = element.to.x.toFloat()
            val y1 = element.to.y.toFloat()
            val z1 = element.to.z.toFloat()

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

        cachedAabb.min = Vec3f.of(minX, minY, minZ)
        cachedAabb.max = Vec3f.of(maxX, maxY, maxZ)
    }


    private fun ModelTexture.toTexture(): Texture {
        val image = Base64.getDecoder().decode(base64Image)

        return Texture.from(
            bytes = image
        )
    }

    object Factory : ModelHandler.Factory {
        override fun create(model: Model): ModelHandler {
            return GlModelHandler(model)
        }
    }
}
