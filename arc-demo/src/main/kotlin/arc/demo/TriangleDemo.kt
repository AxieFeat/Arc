package arc.demo

import arc.Application
import arc.ArcObjectProvider
import arc.asset.asRuntimeAsset
import arc.gl.OpenGL
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.RenderSystem
import arc.graphics.vertex.VertexBuffer
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.ShaderInstance
import arc.util.Color

fun main() {
    // Bootstrap the implementations
    ArcObjectProvider.install()
    ArcObjectProvider.bootstrap()

    // Preload OpenGL
    OpenGL.preload()

    // Find application in current context (OpenGL implementation)
    val application: Application = Application.find()
    application.init()

    // Get shader and compile it
    val shader: ShaderInstance = getShaderInstance()
    shader.compileShaders()

    val renderSystem: RenderSystem = application.renderSystem

    // Create buffer with our vertices
    val buffer: VertexBuffer = createBuffer(application)

    while (!application.window.shouldClose()) {
        // Begin render frame
        renderSystem.beginFrame()

        shader.bind()
        renderSystem.drawer.draw(buffer)
        shader.unbind()

        // End frame
        renderSystem.endFrame()
    }
}

private fun getShaderInstance(): ShaderInstance {
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

    return ShaderInstance.of(
        vertexShader,
        fragmentShader
    )
}

private fun createBuffer(application: Application): VertexBuffer {
    val buffer: DrawBuffer = application.renderSystem.drawer.begin(
        DrawerMode.TRIANGLES,
        VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .add(VertexFormatElement.COLOR)
            .build()
    )

    buffer.addVertex(0f, 0.5f, 0f).setColor(Color.BLUE)
    buffer.addVertex(-0.5f, -0.5f, 0f).setColor(Color.RED)
    buffer.addVertex(0.5f, -0.5f, 0f).setColor(Color.GREEN)

    return buffer.build()
}

