package arc.font

import arc.ArcFactoryProvider
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
    fun bootstrap(provider: FactoryProvider = ArcFactoryProvider) {
        provider.register<GlyphFont.Factory>(ArcGlyphFont.Factory)
    }

}