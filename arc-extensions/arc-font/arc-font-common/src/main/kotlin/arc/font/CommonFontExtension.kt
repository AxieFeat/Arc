package arc.font

import arc.util.factory.FactoryProvider
import arc.util.factory.register

/**
 * This object class represents factory provider for `arc-font` extension.
 */
object CommonFontExtension {

    /**
     * Bootstrap factories of `arc-font` extension.
     *
     * @param provider Provider for configuring.
     */
    @JvmStatic
    fun bootstrap(provider: FactoryProvider) {
        provider.register<GlyphFont.Factory>(ArcGlyphFont.Factory)
    }

}