@file:Suppress("DuplicatedCode", "UNUSED_EXPRESSION")
package arc.sample

import arc.asset.shader.FragmentShader
import arc.asset.shader.ShaderData
import arc.asset.shader.VertexShader
import arc.shader.ShaderInstance
import java.io.File

internal fun shaderInstanceSample() {

    // First of all, you should know that shaders should be stored, and not created new ones every time, this is a very hard operation.
    // To create a ShaderInstance, you need Vertex and Fragment shaders, in addition to them, you need ShaderData, but this is not necessary and it can be empty.


    // We assume that you have files with such contents:

    // example.vsh
    """
    #version 410

    layout (location = 0) in vec3 Position;
    layout (location = 1) in vec4 Color;

    out vec4 vertexColor;

    void main()
    {
        gl_Position = vec4(Position, 1.0);

        vertexColor = Color;
    } 
    """

    // example.fsh
    """
    #version 410

    in vec4 vertexColor;
    out vec4 FragColor;

    void main()
    {
        FragColor = vertexColor;
    }
    """

    // example.json
    """
    {}
    """

    // Remember that the shader content here is just an example, you should not store it in your code. Shaders should be written in files.

    // Ok, lets create ShaderInstance. In this example we use File class, but we recommend use functions of LocationSpace class.
    val shader = ShaderInstance.of(
        VertexShader.from(File("example.vsh")),
        FragmentShader.from(File("example.fsh")),
        ShaderData.from(File("example.json"))
    )

    // Shader instance created! But now we need compile it for using.
    shader.compileShaders()

    // Now shader instance is done for rendering. You can bind/unbind it, set uniforms and more.
}