package arc.demo.screen

import arc.asset.asRuntimeAsset
import arc.demo.shader.ShaderContainer
import arc.graphics.DrawerMode
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.input.KeyCode
import arc.input.mouse
import arc.input.onPress
import arc.input.onRelease
import arc.input.onScroll
import arc.shader.FrameBuffer
import arc.shader.ShaderInstance
import org.joml.Vector2f

object FractalScreen : Screen("fractal") {

    private val drawer = application.renderSystem.drawer

    var needUpdate = true

    val frameBuffer = FrameBuffer.create(
        width = application.window.width,
        height = application.window.height,
        useDepth = false
    )

    val shader = ShaderInstance.of(
        vertexShader = """
            #version 410

            layout (location = 0) in vec3 Position;

            void main() {
                gl_Position = vec4(Position, 1.0);
            }
        """.trimIndent().asRuntimeAsset(),
        fragmentShader = """
            #version 410

            uniform vec2 iResolution;
            uniform vec2 center;
            uniform float scale;
            uniform float iTime;
            uniform vec2 juliaC;
            uniform int maxIter;

            out vec4 FragColor;
            
            void main() {
                vec2 fragCoord = gl_FragCoord.xy;
            
                vec2 uv = (fragCoord - iResolution * 0.5) * scale + center;
                vec2 z = uv;
                vec2 c = juliaC;
            
                int iter = 0;
            
                for (int i = 0; i < maxIter; i++) {
                    if (dot(z, z) > 4.0) break;
                    z = vec2(
                        z.x * z.x - z.y * z.y + c.x,
                        2.0 * z.x * z.y + c.y
                    );
                    iter++;
                }
            
                float zn = dot(z, z);
                float smoothIter = (iter == maxIter) 
                    ? float(maxIter) 
                    : float(iter) - log2(log2(zn)) + 4.0;
            
                float norm = log(1.0 + smoothIter) / log(float(maxIter));
            
                vec3 color;
                if (iter == maxIter) {
                    color = vec3(0.0);
                } else {
                    float t = norm;
                    
                    color.r = 0.2 * sin(10.0 * t + 1.0) + 0.3;
                    color.g = 0.8 + 0.2 * cos(15.0 * t);      
                    color.b = 0.3 + 0.2 * sin(12.0 * t + 2.0);
                
                    color = clamp(color, 0.0, 1.0);
                    color *= smoothstep(0.0, 1.0, pow(norm, 0.8));
                }
            
                vec3 background = vec3(0.05, 0.07, 0.09);
                color = mix(background, color, 1.0 - exp(-3.0 * norm));
            
                FragColor = vec4(color, 1.0);
            }
        """.trimIndent().asRuntimeAsset()
    ).also { it.compileShaders() }

    val buffer = drawer.begin(
        DrawerMode.TRIANGLES,
        VertexFormat.builder()
            .add(VertexFormatElement.POSITION)
            .build()
    ).use { buffer ->
        buffer.addVertex(-1f, 1f, 0f)
        buffer.addVertex(-1f, -1f, 0f)
        buffer.addVertex(1f, -1f, 0f)

        buffer.addVertex(-1f, 1f, 0f)
        buffer.addVertex(1f, -1f, 0f)
        buffer.addVertex(1f, 1f, 0f)

        buffer.build()
    }

    var center = Vector2f(0f, 0f)
    var scale = 1f / application.window.height.toFloat()

    var dragging = false

    init {
        application.mouse.onScroll { x, y ->
            val zoomFactor = 1.1f
            val oldScale = scale

            scale *= if (y > 0) 1f / zoomFactor else zoomFactor

            val mousePos = application.mouse.displayVec
            val windowWidth = application.window.width.toFloat()
            val windowHeight = application.window.height.toFloat()

            val mouseWorldBefore = Vector2f(
                (mousePos.x - windowWidth / 2f) * oldScale + center.x,
                (windowHeight / 2f - mousePos.y) * oldScale + center.y
            )

            val mouseWorldAfter = Vector2f(
                (mousePos.x - windowWidth / 2f) * scale + center.x,
                (windowHeight / 2f - mousePos.y) * scale + center.y
            )

            center.add(mouseWorldBefore).sub(mouseWorldAfter)

            needUpdate = true
        }

        application.mouse.onPress(KeyCode.MOUSE_LEFT) {
            dragging = true
        }

        application.mouse.onRelease(KeyCode.MOUSE_LEFT) {
            dragging = false
            application.mouse.reset()
        }
    }

    override fun doRender() {
        if(frameBuffer.resize(application.window.width, application.window.height)) {
            needUpdate = true
        }

        if (dragging) {
            center.sub(application.mouse.displayVec.x * scale, -application.mouse.displayVec.y * scale)
            needUpdate = true
        }

        if(needUpdate) {
            frameBuffer.bind(viewport = false)
            frameBuffer.clear()

            shader.bind()

            val width = application.window.width.toFloat()
            val height = application.window.height.toFloat()

            shader.setUniform("iResolution", Vector2f(width, height))
            shader.setUniform("center", center)
            shader.setUniform("scale", scale)
            shader.setUniform("iTime", 0.0f)
            shader.setUniform("juliaC", Vector2f(-1.2f, 0.15015f))
            shader.setUniform("maxIter", 15000)

            drawer.draw(buffer)

            shader.unbind()
            frameBuffer.unbind()

            needUpdate = false
        }

        ShaderContainer.blitScreen.bind()
        frameBuffer.render()
    }

    override fun onFpsUpdate(fps: Int) {
        name = "FPS: $fps, Frame time: $frameTime ms"
    }

}
