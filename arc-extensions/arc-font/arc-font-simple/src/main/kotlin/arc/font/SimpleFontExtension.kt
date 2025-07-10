package arc.font

import arc.util.provider.ObjectProvider
import arc.util.provider.register

/**
 * This object class represents factory provider for `arc-font` extension.
 */
object SimpleFontExtension {

    /**
     * Bootstrap factories of `arc-font` extension.
     *
     * @param provider Provider for configuring.
     */
    @JvmStatic
    fun bootstrap(provider: ObjectProvider) {
        provider.register<GlyphFont.Factory>(SimpleGlyphFont.Factory)
    }

}