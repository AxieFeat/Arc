package arc.asset

import kotlin.jvm.JvmStatic

import arc.Arc.single
import arc.annotations.TypeFactory
import arc.annotations.InternalApi

/**
 * This interface represents some asset, that available only at runtime.
 *
 * NOTE: This type of asset supports text asset. But the factory supports only bytes.
 * That is, when trying to get a string, you may get an unexpected result.
 */
interface RuntimeAsset : StringAsset {

    @InternalApi
    @TypeFactory
    interface Factory {

        fun create(bytes: ByteArray): RuntimeAsset
    }

    companion object {

        /**
         * Create instance of [RuntimeAsset] from bytes.
         *
         * @param bytes Bytes of asset.
         *
         * @return New instance of [RuntimeAsset].
         */
        @JvmStatic
        fun from(bytes: ByteArray): RuntimeAsset {
            return single<Factory>().create(bytes)
        }

        /**
         * Create instance of [RuntimeAsset] from string.
         *
         * @param text String of asset (Will be serialized to bytes).
         *
         * @return New instance of [RuntimeAsset].
         */
        @JvmStatic
        fun from(text: String): RuntimeAsset = from(text.encodeToByteArray())
    }
}
