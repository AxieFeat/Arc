package arc.gles.graphics

import arc.graphics.BlendEquation
import arc.graphics.BlendFactor
import arc.graphics.CompareFunction
import arc.graphics.StencilOperation
import org.lwjgl.opengles.GLES.getCapabilities
import org.lwjgl.opengles.GLES20.GL_ALWAYS
import org.lwjgl.opengles.GLES20.GL_BACK
import org.lwjgl.opengles.GLES20.GL_BLEND
import org.lwjgl.opengles.GLES20.GL_COLOR_CLEAR_VALUE
import org.lwjgl.opengles.GLES20.GL_COLOR_WRITEMASK
import org.lwjgl.opengles.GLES20.GL_CULL_FACE
import org.lwjgl.opengles.GLES20.GL_CULL_FACE_MODE
import org.lwjgl.opengles.GLES20.GL_DECR
import org.lwjgl.opengles.GLES20.GL_DEPTH_CLEAR_VALUE
import org.lwjgl.opengles.GLES20.GL_DEPTH_FUNC
import org.lwjgl.opengles.GLES20.GL_DEPTH_RANGE
import org.lwjgl.opengles.GLES20.GL_DEPTH_TEST
import org.lwjgl.opengles.GLES20.GL_DEPTH_WRITEMASK
import org.lwjgl.opengles.GLES20.GL_DITHER
import org.lwjgl.opengles.GLES20.GL_DST_ALPHA
import org.lwjgl.opengles.GLES20.GL_DST_COLOR
import org.lwjgl.opengles.GLES20.GL_EQUAL
import org.lwjgl.opengles.GLES20.GL_FRONT
import org.lwjgl.opengles.GLES20.GL_FRONT_FACE
import org.lwjgl.opengles.GLES20.GL_GEQUAL
import org.lwjgl.opengles.GLES20.GL_GREATER
import org.lwjgl.opengles.GLES20.GL_INCR
import org.lwjgl.opengles.GLES20.GL_INVERT
import org.lwjgl.opengles.GLES20.GL_KEEP
import org.lwjgl.opengles.GLES20.GL_LEQUAL
import org.lwjgl.opengles.GLES20.GL_LESS
import org.lwjgl.opengles.GLES20.GL_NEVER
import org.lwjgl.opengles.GLES20.GL_NOTEQUAL
import org.lwjgl.opengles.GLES20.GL_ONE
import org.lwjgl.opengles.GLES20.GL_ONE_MINUS_DST_ALPHA
import org.lwjgl.opengles.GLES20.GL_ONE_MINUS_DST_COLOR
import org.lwjgl.opengles.GLES20.GL_ONE_MINUS_SRC_ALPHA
import org.lwjgl.opengles.GLES20.GL_ONE_MINUS_SRC_COLOR
import org.lwjgl.opengles.GLES20.GL_PACK_ALIGNMENT
import org.lwjgl.opengles.GLES20.GL_POLYGON_OFFSET_FILL
import org.lwjgl.opengles.GLES20.GL_REPLACE
import org.lwjgl.opengles.GLES20.GL_SAMPLE_ALPHA_TO_COVERAGE
import org.lwjgl.opengles.GLES20.GL_SAMPLE_COVERAGE
import org.lwjgl.opengles.GLES20.GL_SAMPLE_COVERAGE_INVERT
import org.lwjgl.opengles.GLES20.GL_SAMPLE_COVERAGE_VALUE
import org.lwjgl.opengles.GLES20.GL_SCISSOR_BOX
import org.lwjgl.opengles.GLES20.GL_SCISSOR_TEST
import org.lwjgl.opengles.GLES20.GL_SRC_ALPHA
import org.lwjgl.opengles.GLES20.GL_SRC_COLOR
import org.lwjgl.opengles.GLES20.GL_STENCIL_CLEAR_VALUE
import org.lwjgl.opengles.GLES20.GL_STENCIL_FAIL
import org.lwjgl.opengles.GLES20.GL_STENCIL_FUNC
import org.lwjgl.opengles.GLES20.GL_STENCIL_PASS_DEPTH_FAIL
import org.lwjgl.opengles.GLES20.GL_STENCIL_PASS_DEPTH_PASS
import org.lwjgl.opengles.GLES20.GL_STENCIL_REF
import org.lwjgl.opengles.GLES20.GL_STENCIL_TEST
import org.lwjgl.opengles.GLES20.GL_STENCIL_VALUE_MASK
import org.lwjgl.opengles.GLES20.GL_STENCIL_WRITEMASK
import org.lwjgl.opengles.GLES20.GL_TEXTURE_2D
import org.lwjgl.opengles.GLES20.GL_TEXTURE_BINDING_2D
import org.lwjgl.opengles.GLES20.GL_UNPACK_ALIGNMENT
import org.lwjgl.opengles.GLES20.GL_VIEWPORT
import org.lwjgl.opengles.GLES20.GL_ZERO
import org.lwjgl.opengles.GLES20.glBindTexture
import org.lwjgl.opengles.GLES20.glClearColor
import org.lwjgl.opengles.GLES20.glClearDepthf
import org.lwjgl.opengles.GLES20.glClearStencil
import org.lwjgl.opengles.GLES20.glColorMask
import org.lwjgl.opengles.GLES20.glCullFace
import org.lwjgl.opengles.GLES20.glDepthFunc
import org.lwjgl.opengles.GLES20.glDepthMask
import org.lwjgl.opengles.GLES20.glDepthRangef
import org.lwjgl.opengles.GLES20.glDisable
import org.lwjgl.opengles.GLES20.glEnable
import org.lwjgl.opengles.GLES20.glFrontFace
import org.lwjgl.opengles.GLES20.glGetFloat
import org.lwjgl.opengles.GLES20.glGetFloatv
import org.lwjgl.opengles.GLES20.glGetInteger
import org.lwjgl.opengles.GLES20.glGetIntegerv
import org.lwjgl.opengles.GLES20.glIsEnabled
import org.lwjgl.opengles.GLES20.glPixelStorei
import org.lwjgl.opengles.GLES20.glSampleCoverage
import org.lwjgl.opengles.GLES20.glScissor
import org.lwjgl.opengles.GLES20.glViewport
import org.lwjgl.opengles.GLES30.GL_ACTIVE_TEXTURE
import org.lwjgl.opengles.GLES30.GL_ARRAY_BUFFER
import org.lwjgl.opengles.GLES30.GL_ARRAY_BUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_BLEND_COLOR
import org.lwjgl.opengles.GLES30.GL_BLEND_DST_ALPHA
import org.lwjgl.opengles.GLES30.GL_BLEND_DST_RGB
import org.lwjgl.opengles.GLES30.GL_BLEND_EQUATION_ALPHA
import org.lwjgl.opengles.GLES30.GL_BLEND_EQUATION_RGB
import org.lwjgl.opengles.GLES30.GL_BLEND_SRC_ALPHA
import org.lwjgl.opengles.GLES30.GL_BLEND_SRC_RGB
import org.lwjgl.opengles.GLES30.GL_CURRENT_PROGRAM
import org.lwjgl.opengles.GLES30.GL_DECR_WRAP
import org.lwjgl.opengles.GLES30.GL_DRAW_FRAMEBUFFER
import org.lwjgl.opengles.GLES30.GL_DRAW_FRAMEBUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_ELEMENT_ARRAY_BUFFER
import org.lwjgl.opengles.GLES30.GL_ELEMENT_ARRAY_BUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_FUNC_ADD
import org.lwjgl.opengles.GLES30.GL_FUNC_REVERSE_SUBTRACT
import org.lwjgl.opengles.GLES30.GL_FUNC_SUBTRACT
import org.lwjgl.opengles.GLES30.GL_INCR_WRAP
import org.lwjgl.opengles.GLES30.GL_MAX
import org.lwjgl.opengles.GLES30.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS
import org.lwjgl.opengles.GLES30.GL_MAX_UNIFORM_BUFFER_BINDINGS
import org.lwjgl.opengles.GLES30.GL_MIN
import org.lwjgl.opengles.GLES30.GL_PACK_ROW_LENGTH
import org.lwjgl.opengles.GLES30.GL_PACK_SKIP_PIXELS
import org.lwjgl.opengles.GLES30.GL_PACK_SKIP_ROWS
import org.lwjgl.opengles.GLES30.GL_PIXEL_PACK_BUFFER
import org.lwjgl.opengles.GLES30.GL_PIXEL_PACK_BUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_PIXEL_UNPACK_BUFFER
import org.lwjgl.opengles.GLES30.GL_PIXEL_UNPACK_BUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_RASTERIZER_DISCARD
import org.lwjgl.opengles.GLES30.GL_READ_FRAMEBUFFER
import org.lwjgl.opengles.GLES30.GL_READ_FRAMEBUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_RENDERBUFFER
import org.lwjgl.opengles.GLES30.GL_RENDERBUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_SAMPLER_BINDING
import org.lwjgl.opengles.GLES30.GL_STENCIL_BACK_FAIL
import org.lwjgl.opengles.GLES30.GL_STENCIL_BACK_FUNC
import org.lwjgl.opengles.GLES30.GL_STENCIL_BACK_PASS_DEPTH_FAIL
import org.lwjgl.opengles.GLES30.GL_STENCIL_BACK_PASS_DEPTH_PASS
import org.lwjgl.opengles.GLES30.GL_STENCIL_BACK_REF
import org.lwjgl.opengles.GLES30.GL_STENCIL_BACK_VALUE_MASK
import org.lwjgl.opengles.GLES30.GL_STENCIL_BACK_WRITEMASK
import org.lwjgl.opengles.GLES30.GL_TEXTURE0
import org.lwjgl.opengles.GLES30.GL_TEXTURE_2D_ARRAY
import org.lwjgl.opengles.GLES30.GL_TEXTURE_3D
import org.lwjgl.opengles.GLES30.GL_TEXTURE_BINDING_2D_ARRAY
import org.lwjgl.opengles.GLES30.GL_TEXTURE_BINDING_3D
import org.lwjgl.opengles.GLES30.GL_TEXTURE_BINDING_CUBE_MAP
import org.lwjgl.opengles.GLES30.GL_TEXTURE_CUBE_MAP
import org.lwjgl.opengles.GLES30.GL_UNIFORM_BUFFER
import org.lwjgl.opengles.GLES30.GL_UNIFORM_BUFFER_BINDING
import org.lwjgl.opengles.GLES30.GL_UNIFORM_BUFFER_SIZE
import org.lwjgl.opengles.GLES30.GL_UNIFORM_BUFFER_START
import org.lwjgl.opengles.GLES30.GL_UNPACK_ROW_LENGTH
import org.lwjgl.opengles.GLES30.GL_UNPACK_SKIP_PIXELS
import org.lwjgl.opengles.GLES30.GL_UNPACK_SKIP_ROWS
import org.lwjgl.opengles.GLES30.GL_VERTEX_ARRAY_BINDING
import org.lwjgl.opengles.GLES30.glActiveTexture
import org.lwjgl.opengles.GLES30.glBindBuffer
import org.lwjgl.opengles.GLES30.glBindBufferBase
import org.lwjgl.opengles.GLES30.glBindBufferRange
import org.lwjgl.opengles.GLES30.glBindFramebuffer
import org.lwjgl.opengles.GLES30.glBindRenderbuffer
import org.lwjgl.opengles.GLES30.glBindSampler
import org.lwjgl.opengles.GLES30.glBindVertexArray
import org.lwjgl.opengles.GLES30.glBlendColor
import org.lwjgl.opengles.GLES30.glBlendEquationSeparate
import org.lwjgl.opengles.GLES30.glBlendFuncSeparate
import org.lwjgl.opengles.GLES30.glGetInteger64i
import org.lwjgl.opengles.GLES30.glGetIntegeri
import org.lwjgl.opengles.GLES30.glStencilFuncSeparate
import org.lwjgl.opengles.GLES30.glStencilMaskSeparate
import org.lwjgl.opengles.GLES30.glStencilOpSeparate
import org.lwjgl.opengles.GLES30.glUseProgram
import org.lwjgl.opengles.GLES31.GL_MAX_SAMPLE_MASK_WORDS
import org.lwjgl.opengles.GLES31.GL_SAMPLE_MASK
import org.lwjgl.opengles.GLES31.GL_SAMPLE_MASK_VALUE
import org.lwjgl.opengles.GLES31.glSampleMaski

internal fun BlendFactor.glesValue(): Int = when (this) {
    BlendFactor.ZERO -> GL_ZERO
    BlendFactor.ONE -> GL_ONE
    BlendFactor.SOURCE_COLOR -> GL_SRC_COLOR
    BlendFactor.ONE_MINUS_SOURCE_COLOR -> GL_ONE_MINUS_SRC_COLOR
    BlendFactor.DESTINATION_COLOR -> GL_DST_COLOR
    BlendFactor.ONE_MINUS_DESTINATION_COLOR -> GL_ONE_MINUS_DST_COLOR
    BlendFactor.SOURCE_ALPHA -> GL_SRC_ALPHA
    BlendFactor.ONE_MINUS_SOURCE_ALPHA -> GL_ONE_MINUS_SRC_ALPHA
    BlendFactor.DESTINATION_ALPHA -> GL_DST_ALPHA
    BlendFactor.ONE_MINUS_DESTINATION_ALPHA -> GL_ONE_MINUS_DST_ALPHA
}

internal fun BlendEquation.glesValue(): Int = when (this) {
    BlendEquation.ADD -> GL_FUNC_ADD
    BlendEquation.SUBTRACT -> GL_FUNC_SUBTRACT
    BlendEquation.REVERSE_SUBTRACT -> GL_FUNC_REVERSE_SUBTRACT
    BlendEquation.MIN -> GL_MIN
    BlendEquation.MAX -> GL_MAX
}

internal fun CompareFunction.glesValue(): Int = when (this) {
    CompareFunction.NEVER -> GL_NEVER
    CompareFunction.LESS -> GL_LESS
    CompareFunction.EQUAL -> GL_EQUAL
    CompareFunction.LESS_OR_EQUAL -> GL_LEQUAL
    CompareFunction.GREATER -> GL_GREATER
    CompareFunction.NOT_EQUAL -> GL_NOTEQUAL
    CompareFunction.GREATER_OR_EQUAL -> GL_GEQUAL
    CompareFunction.ALWAYS -> GL_ALWAYS
}

internal fun StencilOperation.glesValue(): Int = when (this) {
    StencilOperation.KEEP -> GL_KEEP
    StencilOperation.ZERO -> GL_ZERO
    StencilOperation.REPLACE -> GL_REPLACE
    StencilOperation.INCREMENT_CLAMP -> GL_INCR
    StencilOperation.DECREMENT_CLAMP -> GL_DECR
    StencilOperation.INVERT -> GL_INVERT
    StencilOperation.INCREMENT_WRAP -> GL_INCR_WRAP
    StencilOperation.DECREMENT_WRAP -> GL_DECR_WRAP
}

internal fun glesToggle(capability: Int, enabled: Boolean) {
    if (enabled) glEnable(capability) else glDisable(capability)
}

/** One native snapshot per lexical pass. Ordinary draws and state setters never query the driver. */
internal class GlesRenderStateSnapshot(textureUnits: Set<Int>?) {

    private val viewport = integers(GL_VIEWPORT, 4)
    private val scissor = integers(GL_SCISSOR_BOX, 4)
    private val sampleCoverage = glGetFloat(GL_SAMPLE_COVERAGE_VALUE)
    private val sampleInvert = glGetInteger(GL_SAMPLE_COVERAGE_INVERT) != 0
    private val sampleMasks = if (getCapabilities().GLES31)
        IntArray(glGetInteger(GL_MAX_SAMPLE_MASK_WORDS)) { glGetIntegeri(GL_SAMPLE_MASK_VALUE, it) } else IntArray(0)
    private val enabled = intArrayOf(
        GL_BLEND, GL_DEPTH_TEST, GL_CULL_FACE, GL_SCISSOR_TEST,
        GL_STENCIL_TEST, GL_POLYGON_OFFSET_FILL, GL_DITHER, GL_RASTERIZER_DISCARD,
        GL_SAMPLE_ALPHA_TO_COVERAGE, GL_SAMPLE_COVERAGE,
    ).toList().plus(if (getCapabilities().GLES31) listOf(GL_SAMPLE_MASK) else emptyList()).associateWith(::glIsEnabled)
    private val program = glGetInteger(GL_CURRENT_PROGRAM)
    private val vertexArray = glGetInteger(GL_VERTEX_ARRAY_BINDING)
    private val arrayBuffer = glGetInteger(GL_ARRAY_BUFFER_BINDING)
    private val elementBuffer = glGetInteger(GL_ELEMENT_ARRAY_BUFFER_BINDING)
    private val uniformBuffer = glGetInteger(GL_UNIFORM_BUFFER_BINDING)
    private val uniformBindings = Array(glGetInteger(GL_MAX_UNIFORM_BUFFER_BINDINGS)) { index ->
        longArrayOf(
            glGetIntegeri(GL_UNIFORM_BUFFER_BINDING, index).toLong(),
            glGetInteger64i(GL_UNIFORM_BUFFER_START, index),
            glGetInteger64i(GL_UNIFORM_BUFFER_SIZE, index)
        )
    }
    private val pixelPackBuffer = glGetInteger(GL_PIXEL_PACK_BUFFER_BINDING)
    private val pixelUnpackBuffer = glGetInteger(GL_PIXEL_UNPACK_BUFFER_BINDING)
    private val drawFramebuffer = glGetInteger(GL_DRAW_FRAMEBUFFER_BINDING)
    private val readFramebuffer = glGetInteger(GL_READ_FRAMEBUFFER_BINDING)
    private val renderbuffer = glGetInteger(GL_RENDERBUFFER_BINDING)
    private val blendSource = glGetInteger(GL_BLEND_SRC_RGB)
    private val blendDestination = glGetInteger(GL_BLEND_DST_RGB)
    private val blendSourceAlpha = glGetInteger(GL_BLEND_SRC_ALPHA)
    private val blendDestinationAlpha = glGetInteger(GL_BLEND_DST_ALPHA)
    private val blendEquation = glGetInteger(GL_BLEND_EQUATION_RGB)
    private val blendAlphaEquation = glGetInteger(GL_BLEND_EQUATION_ALPHA)
    private val blendColor = floats(GL_BLEND_COLOR, 4)
    private val depthMask = glGetInteger(GL_DEPTH_WRITEMASK) != 0
    private val depthFunction = glGetInteger(GL_DEPTH_FUNC)
    private val depthRange = floats(GL_DEPTH_RANGE, 2)
    private val cullFace = glGetInteger(GL_CULL_FACE_MODE)
    private val frontFace = glGetInteger(GL_FRONT_FACE)
    private val colorMask = integers(GL_COLOR_WRITEMASK, 4)
    private val clearColor = floats(GL_COLOR_CLEAR_VALUE, 4)
    private val clearDepth = glGetFloat(GL_DEPTH_CLEAR_VALUE)
    private val clearStencil = glGetInteger(GL_STENCIL_CLEAR_VALUE)
    private val stencilFront = stencil(false)
    private val stencilBack = stencil(true)
    private val activeTexture = glGetInteger(GL_ACTIVE_TEXTURE)
    private val pixelStore = intArrayOf(
        GL_PACK_ALIGNMENT, GL_UNPACK_ALIGNMENT, GL_PACK_ROW_LENGTH, GL_UNPACK_ROW_LENGTH,
        GL_PACK_SKIP_PIXELS, GL_UNPACK_SKIP_PIXELS, GL_PACK_SKIP_ROWS, GL_UNPACK_SKIP_ROWS,
    ).associateWith(::glGetInteger)
    private val textures = run {
        val count = glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS)
        val units = textureUnits?.also { requested ->
            require(requested.all { it in 0 until count }) { "Texture unit is outside 0 until $count" }
        }?.plus(activeTexture - GL_TEXTURE0)?.sorted() ?: (0 until count).toList()
        units.map { unit ->
            glActiveTexture(GL_TEXTURE0 + unit)
            TextureBindings(
                unit, TEXTURE_BINDINGS.map { (target, binding) -> target to glGetInteger(binding) },
                glGetInteger(GL_SAMPLER_BINDING)
            )
        }
    }.also { glActiveTexture(activeTexture) }

    fun restore() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, drawFramebuffer)
        glBindFramebuffer(GL_READ_FRAMEBUFFER, readFramebuffer)
        glBindRenderbuffer(GL_RENDERBUFFER, renderbuffer)
        glUseProgram(program)
        glBindVertexArray(vertexArray)
        glBindBuffer(GL_ARRAY_BUFFER, arrayBuffer)
        // Element binding belongs to a VAO. In a core context VAO zero has no element binding to change.
        if (vertexArray != 0 || elementBuffer != 0) glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBuffer)
        uniformBindings.forEachIndexed { index, range ->
            if (range[0] != 0L && range[2] > 0) glBindBufferRange(GL_UNIFORM_BUFFER, index, range[0].toInt(), range[1], range[2])
            else glBindBufferBase(GL_UNIFORM_BUFFER, index, range[0].toInt())
        }
        glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer)
        glBindBuffer(GL_PIXEL_PACK_BUFFER, pixelPackBuffer)
        glBindBuffer(GL_PIXEL_UNPACK_BUFFER, pixelUnpackBuffer)
        textures.forEach { bindings ->
            glActiveTexture(GL_TEXTURE0 + bindings.unit)
            bindings.targets.forEach { (target, texture) -> glBindTexture(target, texture) }
            glBindSampler(bindings.unit, bindings.sampler)
        }
        glActiveTexture(activeTexture)
        glViewport(viewport[0], viewport[1], viewport[2], viewport[3])
        glScissor(scissor[0], scissor[1], scissor[2], scissor[3])
        enabled.forEach(::glesToggle)
        glSampleCoverage(sampleCoverage, sampleInvert)
        sampleMasks.forEachIndexed { index, value -> glSampleMaski(index, value) }
        glBlendFuncSeparate(blendSource, blendDestination, blendSourceAlpha, blendDestinationAlpha)
        glBlendEquationSeparate(blendEquation, blendAlphaEquation)
        glBlendColor(blendColor[0], blendColor[1], blendColor[2], blendColor[3])
        glDepthMask(depthMask)
        glDepthFunc(depthFunction)
        glDepthRangef(depthRange[0], depthRange[1])
        glCullFace(cullFace)
        glFrontFace(frontFace)
        glColorMask(colorMask[0] != 0, colorMask[1] != 0, colorMask[2] != 0, colorMask[3] != 0)
        glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3])
        glClearDepthf(clearDepth)
        glClearStencil(clearStencil)
        restoreStencil(GL_FRONT, stencilFront)
        restoreStencil(GL_BACK, stencilBack)
        pixelStore.forEach(::glPixelStorei)
    }

    private data class TextureBindings(val unit: Int, val targets: List<Pair<Int, Int>>, val sampler: Int)

    companion object {

        private val TEXTURE_BINDINGS = listOf(
            GL_TEXTURE_2D to GL_TEXTURE_BINDING_2D,
            GL_TEXTURE_3D to GL_TEXTURE_BINDING_3D,
            GL_TEXTURE_CUBE_MAP to GL_TEXTURE_BINDING_CUBE_MAP,
            GL_TEXTURE_2D_ARRAY to GL_TEXTURE_BINDING_2D_ARRAY,
        )

        private fun integers(parameter: Int, count: Int) = IntArray(count).also { glGetIntegerv(parameter, it) }
        private fun floats(parameter: Int, count: Int) = FloatArray(count).also { glGetFloatv(parameter, it) }

        private fun stencil(back: Boolean): IntArray = if (back) intArrayOf(
            GL_STENCIL_BACK_FUNC, GL_STENCIL_BACK_REF, GL_STENCIL_BACK_VALUE_MASK,
            GL_STENCIL_BACK_WRITEMASK, GL_STENCIL_BACK_FAIL, GL_STENCIL_BACK_PASS_DEPTH_FAIL,
            GL_STENCIL_BACK_PASS_DEPTH_PASS,
        ).map(::glGetInteger).toIntArray() else intArrayOf(
            GL_STENCIL_FUNC, GL_STENCIL_REF, GL_STENCIL_VALUE_MASK, GL_STENCIL_WRITEMASK,
            GL_STENCIL_FAIL, GL_STENCIL_PASS_DEPTH_FAIL, GL_STENCIL_PASS_DEPTH_PASS,
        ).map(::glGetInteger).toIntArray()

        private fun restoreStencil(face: Int, state: IntArray) {
            glStencilFuncSeparate(face, state[0], state[1], state[2])
            glStencilMaskSeparate(face, state[3])
            glStencilOpSeparate(face, state[4], state[5], state[6])
        }
    }
}
