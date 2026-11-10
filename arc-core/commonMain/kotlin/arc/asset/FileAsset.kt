package arc.asset

import kotlin.jvm.JvmStatic

import arc.Arc.single
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.annotations.InternalApi
import kotlinx.io.files.Path

/**
 * This interface represents an asset that can be stored in a file system.
 *
 * Although this interface is marked as immutable,
 * in fact the getters can return different values if the file contents have been changed.
 */
@ImmutableType
interface FileAsset : StringAsset {

    /**
     * Path of this asset.
     */
    val file: Path

    /**
     * Bytes of this file. Not use too often because this value is not cached.
     */
    override val bytes: ByteArray

    /**
     * Read file bytes as string. Not use too often because this value is not cached.
     */
    override val text: String

    @InternalApi
    @TypeFactory
    interface Factory {

        fun create(file: Path): FileAsset
    }

    companion object {

        /**
         * Create instance of [FileAsset] from a file.
         *
         * @param file Path of asset.
         *
         * @return New instance of [FileAsset].
         */
        @JvmStatic
        fun from(file: Path): FileAsset {
            return single<Factory>().create(file)
        }
    }
}
