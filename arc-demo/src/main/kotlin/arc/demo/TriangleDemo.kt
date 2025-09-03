package arc.demo

import arc.Application
import arc.ArcObjectProvider
import arc.asset.asRuntimeAsset
import arc.gl.OpenGL
import arc.gles.OpenGLES
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
    OpenGLES.preload()

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
       #version 300 es
precision highp float;

out vec4 vertexColor;

void main() {
    // Захардкоженный треугольник
    vec3 positions[3] = vec3[3](
        vec3(0.0,  0.5, 0.0),
        vec3(-0.5, -0.5, 0.0),
        vec3(0.5, -0.5, 0.0)
    );
    
    vec4 colors[3] = vec4[3](
        vec4(1.0, 0.0, 0.0, 1.0), // красный
        vec4(0.0, 1.0, 0.0, 1.0), // зеленый
        vec4(0.0, 0.0, 1.0, 1.0)  // синий
    );
    
    gl_Position = vec4(positions[gl_VertexID], 1.0);
    vertexColor = colors[gl_VertexID];
}

        """.trimIndent().asRuntimeAsset()

    val fragmentShader = """
        #version 300 es
precision mediump float;

in vec4 vertexColor;
out vec4 FragColor;

void main() {
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

