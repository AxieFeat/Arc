package arc.audio

import arc.audio.format.SoundLoaderFactory
import arc.util.provider.ObjectProvider
import arc.util.provider.register

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
    fun bootstrap(provider: ObjectProvider) {
        provider.register<SoundEngine.Provider>(AlSoundEngine.Provider)
        provider.register<SoundLoader.Factory>(SoundLoaderFactory)
    }

}