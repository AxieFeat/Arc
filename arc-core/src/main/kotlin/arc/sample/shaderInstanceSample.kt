@file:Suppress("DuplicatedCode")
package arc.sample

import arc.asset.asRuntimeAsset
import arc.shader.ShaderInstance

internal fun shaderInstanceSample() {

    // First of all, you should know that shaders should be stored, and not created new ones every time, this is a very hard operation.
    // To create a ShaderInstance, you need Vertex and Fragment shaders, in addition to them, you need ShaderSettings, but this is not necessary and it can be empty.

    // In this example we store shaders in code, but don't do this in real projects!

    val vertexShader = """
    #version 410

    layout (location = 0) in vec3 Position;
    layout (location = 1) in vec4 Color;

    out vec4 vertexColor;

    void main()
    {
        gl_Position = vec4(Position, 1.0);

        vertexColor = Color;
    } 
    """.trimIndent().asRuntimeAsset()

    val fragmentShader = """
    #version 410

    in vec4 vertexColor;
    out vec4 FragColor;

    void main()
    {
        FragColor = vertexColor;
    }
    """.trimIndent().asRuntimeAsset()

    // Ok, lets create ShaderInstance.
    val shader = ShaderInstance.of(
        vertexShader,
        fragmentShader
    )

    // Shader instance created! But now we need compile it for using.
    shader.compileShaders()

    // Now shader instance is done for rendering. You can bind/unbind it, set uniforms and more.
}