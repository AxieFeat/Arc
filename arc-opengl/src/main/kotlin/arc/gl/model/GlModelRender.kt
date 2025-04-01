package arc.gl.model

import arc.graphics.ModelRender
import arc.model.Element
import arc.model.Model
import arc.model.texture.ModelTexture
import arc.shader.ShaderInstance
import arc.texture.Texture
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import java.util.*

internal data class GlModelRender(
    override val model: Model
) : ModelRender {

    private val matrix = Matrix4f()
    private val rotation = Quaternionf()

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
        rotation.rotateXYZ(
            Math.toRadians(x),
            Math.toRadians(y),
            Math.toRadians(z)
        )

        matrix.rotation(rotation)
    }

    override fun scale(x: Float, y: Float, z: Float) {
        matrix.scale(x, y, z)
    }

    override fun position(x: Float, y: Float, z: Float) {
        matrix.translate(x, y, z)
    }

    private fun ModelTexture.toTexture(): Texture {
        val image = Base64.getDecoder().decode(base64Image)

        return Texture.from(
            bytes = image
        )
    }

    object Factory : ModelRender.Factory {
        override fun create(model: Model): ModelRender {
            return GlModelRender(model)
        }
    }

}