@file:Suppress("DuplicatedCode", "UNUSED_EXPRESSION")
package arc.sample

import arc.Application
import arc.asset.shader.FragmentShader
import arc.asset.shader.ShaderData
import arc.asset.shader.VertexShader
import arc.shader.AbstractUniformProvider
import arc.shader.ShaderInstance
import java.io.File

/**
 * For creation uniform provider we recommend kotlin objects and using AbstractUniformProvider class.
 */
internal object UniformProviderSample : AbstractUniformProvider() {

    private val application = Application.find()

    // Now we can configure uniforms for providing to shaders.
    init {
        // In example, we can provide projection and view matrices from camera.

        // In first argument we set name of uniform in shader.
        addUniform("projectionMatrix") {

            // This block of code call in every time, when your bind shader.

            // Then we just call function of shader instance for setting uniform.
            it.setUniform("projectionMatrix", application.renderSystem.scene.camera.projection)
        }

        addUniform("viewMatrix") {
            it.setUniform("viewMatrix", application.renderSystem.scene.camera.view)
        }
    }

}

internal fun uniformProviderSample() {
    // In previous sample we create object UniformProviderSample, we will use it here.
    val provider = UniformProviderSample

    // Creation simple shader as example.
    val shader = ShaderInstance.of(
        VertexShader.from(File("example.vsh")),
        FragmentShader.from(File("example.fsh")),
        ShaderData.from(File("example.json"))
    ).also { it.compileShaders() }

    // Now we can add provider to our shader.
    // Please note that the number of providers can be any, but we do not recommend using too many.
    shader.addProvider(provider)

    // That's it, the provider is set up. Now every time you bind the shader, the uniforms will be automatically transferred to the shader. However, you probably have a question: how to specify which uniforms the shader accepts?
    // The answer to this question is very simple, that's what ShaderData is for.

    // Just add name of uniform to uniforms list in .json file of ShaderData.
    """
    {
        "uniforms": [
            "projectionMatrix",
            "viewMatrix"
        ]
    }
    """
}