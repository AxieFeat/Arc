package arc.shader

import arc.Application
import arc.graphics.RenderSystem

internal data class SimpleBlendMode(
    override val separateBlend: Boolean,
    override val opaque: Boolean,
    override val srcColorFactor: Int,
    override val srcAlphaFactor: Int,
    override val dstColorFactor: Int,
    override val dstAlphaFactor: Int,
    override val blendFunc: Int
) : BlendMode {

    override fun apply() {
        if(this != lastApplied) {
            if (lastApplied == null || this.opaque != lastApplied!!.opaque) {
                lastApplied = this

                if (this.opaque) {
                    renderSystem.disableBlend()
                    return
                }

                renderSystem.enableBlend()
            }

            renderSystem.blendEquation(this.blendFunc)
            if (this.separateBlend) {
                renderSystem.blendFuncSeparate(
                    this.srcColorFactor,
                    this.dstColorFactor,
                    this.srcAlphaFactor,
                    this.dstAlphaFactor
                )
            } else {
                renderSystem.blendFunc(this.srcColorFactor, this.dstColorFactor)
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
            return SimpleBlendMode(separateBlend, opaque, srcColorFactor, srcAlphaFactor, dstColorFactor, dstAlphaFactor, blendFunc)
        }
    }

    companion object {

        private var lastApplied: BlendMode? = null
        private val renderSystem: RenderSystem by lazy { Application.find().renderSystem }
    }
}
