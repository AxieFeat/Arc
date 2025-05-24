@file:Suppress("DuplicatedCode")
package arc.sample

import arc.Application
import arc.asset.FileAsset
import arc.shader.AbstractUniformProvider
import arc.shader.ShaderInstance
import arc.shader.ShaderSettings
import java.io.File

/**
 * For creation uniform provider we recommend kotlin objects and using AbstractUniformProvider class.
 */
internal object UniformProviderSample : AbstractUniformProvider() {

    private val application = Application.find()

    // Now we can configure uniforms for providing to shaders.
    init {
        // For example, we can provide projection and view matrices from camera.

        // In first argument we set name of uniform in shader.
        addUniform("projectionMatrix") {

            // This block of code calls in every time, when your bind shader.

            // Then we just call function of shader instance for setting uniform.
            it.setUniform("projectionMatrix", application.renderSystem.scene.camera.projection)
        }

        addUniform("viewMatrix") {
            it.setUniform("viewMatrix", application.renderSystem.scene.camera.view)
        }
    }

}

internal fun uniformProviderSample() {
    // In the previous sample we create object UniformProviderSample, we will use it here.
    val provider = UniformProviderSample

    // Creation simple shader as an example.
    val shader = ShaderInstance.of(
        FileAsset.from(File("example.vsh")),
        FileAsset.from(File("example.fsh")),

        // It's very important part of providing uniforms shaders.
        // Is ShaderSettings your need put names of uniforms, that's this shader will use.
        ShaderSettings.of(
            uniforms = listOf("projectionMatrix", "viewMatrix"),
        )
    ).also { it.compileShaders() }

    // Now we can add provider to our shader.
    // Please note that the number of providers can be any, but we do not recommend using too many.
    shader.addProvider(provider)
}