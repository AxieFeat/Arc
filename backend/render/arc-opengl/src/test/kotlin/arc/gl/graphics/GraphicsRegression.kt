package arc.gl.graphics

import arc.ArcObjectProvider
import arc.asset.RuntimeAsset
import arc.gl.OpenGL
import arc.graphics.BlendState
import arc.graphics.ColorWriteMask
import arc.graphics.CompareFunction
import arc.graphics.CullMode
import arc.graphics.DepthState
import arc.graphics.DrawBuffer
import arc.graphics.DrawerMode
import arc.graphics.PixelRect
import arc.graphics.PolygonMode
import arc.graphics.RasterState
import arc.graphics.StencilOperation
import arc.graphics.StencilState
import arc.graphics.vertex.VertexFormat
import arc.graphics.vertex.VertexFormatElement
import arc.shader.FrameBuffer
import arc.shader.ShaderInstance
import java.nio.ByteBuffer
import java.nio.ByteOrder
import org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR
import org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE
import org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE
import org.lwjgl.glfw.GLFW.GLFW_VISIBLE
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDefaultWindowHints
import org.lwjgl.glfw.GLFW.glfwDestroyWindow
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwTerminate
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.GL_BLEND
import org.lwjgl.opengl.GL11.GL_COLOR_LOGIC_OP
import org.lwjgl.opengl.GL11.GL_COLOR_WRITEMASK
import org.lwjgl.opengl.GL11.GL_CULL_FACE
import org.lwjgl.opengl.GL11.GL_CULL_FACE_MODE
import org.lwjgl.opengl.GL11.GL_DEPTH_FUNC
import org.lwjgl.opengl.GL11.GL_DEPTH_TEST
import org.lwjgl.opengl.GL11.GL_DEPTH_WRITEMASK
import org.lwjgl.opengl.GL11.GL_NO_ERROR
import org.lwjgl.opengl.GL11.GL_POLYGON_MODE
import org.lwjgl.opengl.GL11.GL_RENDERER
import org.lwjgl.opengl.GL11.GL_RGBA
import org.lwjgl.opengl.GL11.GL_SCISSOR_BOX
import org.lwjgl.opengl.GL11.GL_SCISSOR_TEST
import org.lwjgl.opengl.GL11.GL_STENCIL_FAIL
import org.lwjgl.opengl.GL11.GL_STENCIL_FUNC
import org.lwjgl.opengl.GL11.GL_STENCIL_PASS_DEPTH_FAIL
import org.lwjgl.opengl.GL11.GL_STENCIL_REF
import org.lwjgl.opengl.GL11.GL_STENCIL_TEST
import org.lwjgl.opengl.GL11.GL_STENCIL_WRITEMASK
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.GL_TEXTURE_BINDING_2D
import org.lwjgl.opengl.GL11.GL_TRIANGLES
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.GL_VERSION
import org.lwjgl.opengl.GL11.GL_VIEWPORT
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glClearStencil
import org.lwjgl.opengl.GL11.glDeleteTextures
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL11.glGenTextures
import org.lwjgl.opengl.GL11.glGetError
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL11.glGetIntegerv
import org.lwjgl.opengl.GL11.glGetString
import org.lwjgl.opengl.GL11.glIsEnabled
import org.lwjgl.opengl.GL11.glReadPixels
import org.lwjgl.opengl.GL13.GL_ACTIVE_TEXTURE
import org.lwjgl.opengl.GL13.GL_SAMPLE_ALPHA_TO_COVERAGE
import org.lwjgl.opengl.GL13.GL_SAMPLE_COVERAGE
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL14.GL_BLEND_DST_RGB
import org.lwjgl.opengl.GL14.GL_BLEND_SRC_RGB
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER_BINDING
import org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING
import org.lwjgl.opengl.GL15.glBindBuffer
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL15.glGetBufferSubData
import org.lwjgl.opengl.GL20.GL_COMPILE_STATUS
import org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM
import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_LINK_STATUS
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL20.glAttachShader
import org.lwjgl.opengl.GL20.glCompileShader
import org.lwjgl.opengl.GL20.glCreateProgram
import org.lwjgl.opengl.GL20.glCreateShader
import org.lwjgl.opengl.GL20.glDeleteProgram
import org.lwjgl.opengl.GL20.glDeleteShader
import org.lwjgl.opengl.GL20.glGetProgramInfoLog
import org.lwjgl.opengl.GL20.glGetProgrami
import org.lwjgl.opengl.GL20.glGetShaderInfoLog
import org.lwjgl.opengl.GL20.glGetShaderi
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL20.glLinkProgram
import org.lwjgl.opengl.GL20.glShaderSource
import org.lwjgl.opengl.GL20.glUniform1f
import org.lwjgl.opengl.GL20.glUseProgram
import org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0
import org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER_BINDING
import org.lwjgl.opengl.GL30.GL_RASTERIZER_DISCARD
import org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER_BINDING
import org.lwjgl.opengl.GL30.GL_VERTEX_ARRAY_BINDING
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glDeleteVertexArrays
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER
import org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER_BINDING
import org.lwjgl.opengl.GL32.GL_SAMPLE_MASK
import org.lwjgl.opengl.GL33.GL_SAMPLER_BINDING
import org.lwjgl.opengl.GL33.glBindSampler
import org.lwjgl.opengl.GL33.glDeleteSamplers
import org.lwjgl.opengl.GL33.glGenSamplers

/** Invoke :arc-opengl:graphicsRegression on a desktop with OpenGL 4.1. No optional test framework is required. */
object GraphicsRegression {

    @JvmStatic
    fun main(args: Array<String>) {
        check(glfwInit()) { "GLFW initialization failed; a desktop session is required" }
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        val window = glfwCreateWindow(32, 32, "Arc graphics regression", 0, 0)
        check(window != 0L) { "An OpenGL 4.1 core context is unavailable" }
        try {
            glfwMakeContextCurrent(window)
            createCapabilities()
            println("Graphics regression: ${glGetString(GL_VERSION)}; ${glGetString(GL_RENDERER)}")
            ArcObjectProvider.install()
            ArcObjectProvider.bootstrap()
            OpenGL.preload()
            streaming()
            exceptionalStateRestoration()
            targetAndExactMask()
            legacyFramebufferComposition()
            passTiming()
            check(glGetError() == GL_NO_ERROR) { "Graphics operations left an OpenGL error" }
            println(
                "PASS: active-range streaming, exceptional restoration, scene preservation, empty scissor, exact stencil mask, target resize/cleanup"
            )
        } finally {
            glfwDestroyWindow(window)
            glfwTerminate()
        }
    }

    private fun streaming() {
        val format = VertexFormat.builder().add(VertexFormatElement.POSITION).build()
        val cpu = DrawBuffer.of(DrawerMode.TRIANGLES, format, 64)
        cpu.build().use { check(it.vertices == 0 && it.activeBytes == 0) }
        cpu.addVertex(1f, 2f, 3f)
        val gpu = cpu.build()
        check(cpu.activeBytes == 12 && cpu.byteBuffer.remaining() == 12)
        check(gpu.activeBytes == 12 && gpu.vertices == 1)
        cpu.build().use { check(it.vertices == 1) }
        cpu.addVertex(4f, 5f, 6f)
        cpu.build().use { check(it.vertices == 2 && it.activeBytes == 24) }
        val id = gpu.id
        val upload = direct(1600).position(4)
        repeat(300) { upload.putFloat(it.toFloat()) }
        upload.flip().position(4)
        val position = upload.position()
        val limit = upload.limit()
        gpu.write(upload, 100)
        check(gpu.id == id && gpu.size >= 1200 && gpu.activeBytes == 1200)
        check(upload.position() == position && upload.limit() == limit)
        gpu.bind()
        val readback = direct(1200)
        glGetBufferSubData(GL_ARRAY_BUFFER, 0, readback)
        repeat(300) { check(readback.getFloat(it * 4) == it.toFloat()) { "Upload mismatch at $it" } }
        gpu.unbind()
        upload.limit(16)
        gpu.write(upload, 1)
        check(gpu.id == id && gpu.vertices == 1 && gpu.activeBytes == 12)
        check(runCatching { gpu.write(upload, 2) }.isFailure)
        gpu.cleanup()
        gpu.cleanup()
        check(runCatching { gpu.write(upload, 1) }.isFailure)
        cpu.clear()
        cpu.build().use { check(it.vertices == 0) }
        cpu.cleanup()
        cpu.cleanup()
        check(runCatching { cpu.clear() }.isFailure)
    }

    private fun exceptionalStateRestoration() {
        val context = GlRenderContext
        val program = program()
        val vao = glGenVertexArrays()
        val buffers = IntArray(3) { glGenBuffers() }
        val texture = glGenTextures()
        val sampler = glGenSamplers()
        val target = context.createRenderTarget(16, 16, true)
        val uiTarget = context.createRenderTarget(8, 8)
        try {
            context.bindTarget(target)
            glUseProgram(program)
            glBindVertexArray(vao)
            glBindBuffer(GL_ARRAY_BUFFER, buffers[0])
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[1])
            glBindBuffer(GL_UNIFORM_BUFFER, buffers[2])
            glActiveTexture(GL_TEXTURE0 + 1)
            glBindTexture(GL_TEXTURE_2D, texture)
            glBindSampler(1, sampler)
            context.setViewport(2, 3, 7, 5)
            context.setScissor(PixelRect(1, 2, 5, 6))
            context.setDepth(DepthState(true, false, CompareFunction.GREATER))
            context.setCull(CullMode.FRONT)
            context.setBlend(BlendState.STRAIGHT_ALPHA)
            context.setColorWrite(ColorWriteMask(true, false, true, false))
            context.setStencil(
                StencilState(
                    true, CompareFunction.NOT_EQUAL, 3, 7, 5,
                    StencilOperation.INCREMENT_WRAP, StencilOperation.REPLACE, StencilOperation.KEEP
                )
            )
            context.setRaster(RasterState(PolygonMode.LINE, true, true, true, true, true, setOf(0)))
            val expected = state()
            val failure = runCatching {
                context.beginScopedPass().use {
                    context.bindTarget(uiTarget)
                    glUseProgram(0)
                    glBindVertexArray(0)
                    glBindBuffer(GL_ARRAY_BUFFER, 0)
                    glBindBuffer(GL_UNIFORM_BUFFER, 0)
                    glBindTexture(GL_TEXTURE_2D, 0)
                    glBindSampler(1, 0)
                    glActiveTexture(GL_TEXTURE0)
                    context.setScissor(null)
                    context.setViewport(0, 0, 8, 8)
                    context.setDepth(DepthState())
                    context.setCull(CullMode.NONE)
                    context.setBlend(BlendState.DISABLED)
                    context.setColorWrite(ColorWriteMask.NONE)
                    context.setStencil(StencilState())
                    context.setRaster(RasterState())
                    throw IllegalArgumentException("Expected painter failure")
                }
            }.exceptionOrNull()
            check(failure?.message == "Expected painter failure")
            val actual = state()
            val difference = expected.filter { (key, value) -> actual[key] != value }
            check(expected == actual) { "Graphics state changed after exceptional pass: expected differences=$difference; actual=$actual" }
            val outer = context.beginScopedPass()
            val inner = context.beginScopedPass()
            check(runCatching { outer.close() }.isFailure)
            inner.close()
            outer.close()
            outer.close()
        } finally {
            context.setRaster(RasterState())
            context.bindTarget(null)
            glUseProgram(0)
            glBindVertexArray(0)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindBuffer(GL_UNIFORM_BUFFER, 0)
            glActiveTexture(GL_TEXTURE0 + 1)
            glBindTexture(GL_TEXTURE_2D, 0)
            glBindSampler(1, 0)
            glActiveTexture(GL_TEXTURE0)
            glDeleteProgram(program)
            glDeleteVertexArrays(vao)
            buffers.forEach(::glDeleteBuffers)
            glDeleteTextures(texture)
            glDeleteSamplers(sampler)
            target.cleanup()
            uiTarget.cleanup()
        }
    }

    private fun targetAndExactMask() {
        val context = GlRenderContext
        val scene = context.createRenderTarget(16, 16, true)
        val layer = context.createRenderTarget(8, 8)
        val program = program()
        val vao = glGenVertexArrays()
        try {
            context.bindTarget(scene)
            context.setViewport(0, 0, 16, 16)
            context.setScissor(null)
            context.setDepth(DepthState())
            context.setCull(CullMode.NONE)
            context.setBlend(BlendState.DISABLED)
            context.setColorWrite(ColorWriteMask.ALL)
            context.setStencil(StencilState())
            glClearStencil(0)
            context.clearTarget(1f, 0f, 0f, 1f, stencil = true)
            context.beginScopedPass().use {
                context.bindTarget(layer)
                context.clearTarget(0f, 1f, 0f, 1f)
            }
            check(pixel(12, 12) == listOf(255, 0, 0, 255)) { "UI target changed the scene target" }
            context.setScissor(PixelRect(0, 0, 0, 16))
            context.clearTarget(0f, 0f, 1f, 1f)
            check(pixel(1, 1) == listOf(255, 0, 0, 255)) { "Empty scissor did not reject rendering" }
            context.setScissor(null)
            glUseProgram(program)
            glBindVertexArray(vao)
            context.setColorWrite(ColorWriteMask.NONE)
            context.setStencil(StencilState(true, reference = 1, pass = StencilOperation.REPLACE))
            glUniform1f(glGetUniformLocation(program, "extent"), 1f)
            glDrawArrays(GL_TRIANGLES, 0, 3)
            context.setColorWrite(ColorWriteMask.ALL)
            context.setStencil(StencilState(true, CompareFunction.EQUAL, 1, writeMask = 0))
            glUniform1f(glGetUniformLocation(program, "extent"), 3f)
            glDrawArrays(GL_TRIANGLES, 0, 3)
            check(pixel(1, 1) == listOf(0, 255, 0, 255)) { "Mask interior was not rendered" }
            check(pixel(14, 14) == listOf(255, 0, 0, 255)) { "Exact triangle mask leaked outside its path" }
            context.bindTarget(null)
            check(layer.resize(12, 10) && layer.width == 12 && layer.height == 10)
            check(!layer.resize(12, 10))
            layer.colorTexture.cleanup()
            check(layer.colorTexture.id > 0) { "Borrowed texture cleanup destroyed its target" }
        } finally {
            context.bindTarget(null)
            glUseProgram(0)
            glBindVertexArray(0)
            glDeleteProgram(program)
            glDeleteVertexArrays(vao)
            scene.cleanup()
            scene.cleanup()
            layer.cleanup()
            layer.cleanup()
        }
    }

    private fun state(): Map<Int, List<Int>> = buildMap {
        val scalars = intArrayOf(
            GL_CURRENT_PROGRAM, GL_VERTEX_ARRAY_BINDING,
            GL_ARRAY_BUFFER_BINDING, GL_ELEMENT_ARRAY_BUFFER_BINDING, GL_UNIFORM_BUFFER_BINDING,
            GL_DRAW_FRAMEBUFFER_BINDING, GL_READ_FRAMEBUFFER_BINDING, GL_ACTIVE_TEXTURE,
            GL_TEXTURE_BINDING_2D, GL_SAMPLER_BINDING, GL_DEPTH_FUNC, GL_DEPTH_WRITEMASK,
            GL_CULL_FACE_MODE, GL_BLEND_SRC_RGB, GL_BLEND_DST_RGB, GL_STENCIL_FUNC,
            GL_STENCIL_REF, GL_STENCIL_WRITEMASK, GL_STENCIL_FAIL, GL_STENCIL_PASS_DEPTH_FAIL
        )
        scalars.forEach { put(it, listOf(glGetInteger(it))) }
        intArrayOf(
            GL_BLEND, GL_DEPTH_TEST, GL_CULL_FACE, GL_SCISSOR_TEST, GL_STENCIL_TEST,
            GL_RASTERIZER_DISCARD, GL_COLOR_LOGIC_OP, GL_SAMPLE_ALPHA_TO_COVERAGE, GL_SAMPLE_COVERAGE,
            GL_SAMPLE_MASK, GL_CLIP_DISTANCE0
        )
            .forEach { put(it, listOf(if (glIsEnabled(it)) 1 else 0)) }
        intArrayOf(GL_VIEWPORT, GL_SCISSOR_BOX, GL_COLOR_WRITEMASK).forEach { parameter ->
            put(parameter, IntArray(4).also { glGetIntegerv(parameter, it) }.toList())
        }
        put(GL_POLYGON_MODE, IntArray(2).also { glGetIntegerv(GL_POLYGON_MODE, it) }.toList())
    }

    private fun legacyFramebufferComposition() {
        val context = GlRenderContext
        val legacy = FrameBuffer.create(4, 4, true)
        val scene = context.createRenderTarget(16, 16)
        val shader = ShaderInstance.of(
            RuntimeAsset.from("#version 410 core\nlayout(location=0) in vec3 position;void main(){gl_Position=vec4(position,1);}"),
            RuntimeAsset.from("#version 410 core\nout vec4 color;void main(){color=vec4(0,1,0,1);}")
        )
        shader.compileShaders()
        try {
            check(legacy.resize(8, 6) && legacy.textureWidth == 8 && legacy.textureHeight == 6)
            context.bindTarget(null)
            context.setViewport(0, 0, 32, 32)
            context.setScissor(null)
            context.setStencil(StencilState())
            context.setColorWrite(ColorWriteMask.ALL)
            context.setDepth(DepthState())
            context.setCull(CullMode.NONE)
            context.setBlend(BlendState.DISABLED)
            context.clearTarget(1f, 0f, 0f, 1f)
            context.bindTarget(scene)
            context.setViewport(2, 3, 9, 10)
            context.clearTarget(1f, 0f, 0f, 1f)
            context.bindShader(shader)
            val expected = state()
            legacy.render(16, 16)
            check(expected == state()) { "Legacy framebuffer composition did not restore the application's state" }
            check(pixel(5, 5) == listOf(255, 0, 0, 255)) { "Legacy composition cleared the application target" }
            context.bindTarget(null)
            check(pixel(20, 20) == listOf(255, 0, 0, 255)) { "Legacy composition implicitly cleared outside its viewport" }
            check(pixel(5, 5) == listOf(0, 255, 0, 255)) { "Legacy framebuffer geometry was not rendered" }
        } finally {
            context.bindTarget(null)
            glUseProgram(0)
            shader.cleanup()
            shader.cleanup()
            legacy.cleanup()
            legacy.cleanup()
            scene.cleanup()
        }
    }

    private fun pixel(x: Int, y: Int): List<Int> {
        val bytes = direct(4)
        glReadPixels(x, y, 1, 1, GL_RGBA, GL_UNSIGNED_BYTE, bytes)
        return (0..3).map { bytes[it].toInt() and 255 }
    }

    private fun program(): Int {
        fun shader(type: Int, source: String): Int {
            val shader = glCreateShader(type)
            glShaderSource(shader, source)
            glCompileShader(shader)
            check(glGetShaderi(shader, GL_COMPILE_STATUS) != 0) { glGetShaderInfoLog(shader) }
            return shader
        }

        val vertex = shader(
            GL_VERTEX_SHADER, """
            #version 410 core
            uniform float extent;
            void main() {
                vec2 p = gl_VertexID == 0 ? vec2(-1,-1) : gl_VertexID == 1 ? vec2(extent,-1) : vec2(-1,extent);
                gl_Position = vec4(p,0,1);
            }
        """.trimIndent()
        )
        val fragment = shader(
            GL_FRAGMENT_SHADER, """
            #version 410 core
            out vec4 color;
            void main() { color = vec4(0,1,0,1); }
        """.trimIndent()
        )
        val program = glCreateProgram()
        glAttachShader(program, vertex)
        glAttachShader(program, fragment)
        glLinkProgram(program)
        glDeleteShader(vertex)
        glDeleteShader(fragment)
        check(glGetProgrami(program, GL_LINK_STATUS) != 0) { glGetProgramInfoLog(program) }
        return program
    }

    private fun passTiming() {
        repeat(30) { GlRenderContext.beginScopedPass(setOf(0)).use { } }
        val times = LongArray(100) {
            val start = System.nanoTime()
            GlRenderContext.beginScopedPass(setOf(0)).use { }
            System.nanoTime() - start
        }.sorted()
        println(
            "Scoped pass unit0 snapshot+restore CPU: p50=${times[49] / 1_000_000.0}ms p95=${times[94] / 1_000_000.0}ms p99=${times[98] / 1_000_000.0}ms (100 samples; not full UI benchmark)"
        )
    }

    private fun direct(bytes: Int) = ByteBuffer.allocateDirect(bytes).order(ByteOrder.nativeOrder())
}
