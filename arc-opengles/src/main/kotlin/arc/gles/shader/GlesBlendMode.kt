package arc.gles.shader

import arc.gles.graphics.GlesRenderSystem
import arc.shader.BlendMode

internal data class GlesBlendMode(
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
                    GlesRenderSystem.disableBlend()
                    return
                }

                GlesRenderSystem.enableBlend()
            }

            GlesRenderSystem.blendEquation(this.blendFunc)
            if (this.separateBlend) {
                GlesRenderSystem.blendFuncSeparate(
                    this.srcColorFactor,
                    this.dstColorFactor,
                    this.srcAlphaFactor,
                    this.dstAlphaFactor
                )
            } else {
                GlesRenderSystem.blendFunc(this.srcColorFactor, this.dstColorFactor)
            }
        }
    }

    companion object {
        private var lastApplied: BlendMode? = null
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
            return GlesBlendMode(separateBlend, opaque, srcColorFactor, srcAlphaFactor, dstColorFactor, dstAlphaFactor, blendFunc)
        }

    }

}