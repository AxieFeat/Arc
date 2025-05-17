package arc.audio

import arc.audio.format.SoundLoaderFactory
import arc.util.factory.FactoryProvider
import arc.util.factory.register

/**
 * This object class represents factory provider for `arc-audio` extension.
 */
object AlAudioExtension {

    /**
     * Bootstrap factories of `arc-audio` extension.
     *
     * @param provider Provider for configuring.
     */
    @JvmStatic
    fun bootstrap(provider: FactoryProvider) {
        provider.register<SoundEngine.Factory>(AlSoundEngine.Factory)
        provider.register<SoundLoader.Factory>(SoundLoaderFactory)
    }

}