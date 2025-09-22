package arc.gl.shader

import arc.gl.graphics.GlRenderSystem
import arc.shader.BlendMode

internal data class GlBlendMode(
    override val separateBlend: Boolean,
    override val opaque: Boolean,
    override val srcColorFactor: Int,
    override val srcAlphaFactor: Int,
    override val dstColorFactor: Int,
    override val dstAlphaFactor: Int,
    override val blendFunc: Int,
) : BlendMode {

    override fun apply() {
        if(this != lastApplied) {
            if (lastApplied == null || this.opaque != lastApplied!!.opaque) {
                lastApplied = this

                if (this.opaque) {
                    GlRenderSystem.disableBlend()
                    return
                }

                GlRenderSystem.enableBlend()
            }

            GlRenderSystem.blendEquation(this.blendFunc)
            if (this.separateBlend) {
                GlRenderSystem.blendFuncSeparate(
                    this.srcColorFactor,
                    this.dstColorFactor,
                    this.srcAlphaFactor,
                    this.dstAlphaFactor
                )
            } else {
                GlRenderSystem.blendFunc(this.srcColorFactor, this.dstColorFactor)
            }
        }
    }

    object Factory : BlendMode.Factory {
        override fun create(
            separateBlend: Boolean,
            opaque: Boolean,
            srcColorFactor: Int,
            srcAlphaFactor: Int,
            dstColorFactor: Int,
            dstAlphaFactor: Int,
            blendFunc: Int
        ): BlendMode {
            return GlBlendMode(separateBlend, opaque, srcColorFactor, srcAlphaFactor, dstColorFactor, dstAlphaFactor, blendFunc)
        }

    }

    companion object {

        private var lastApplied: BlendMode? = null
    }
}
