package arc.asset

import arc.Arc.single
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus
import java.io.File

/**
 * This interface represents an asset that can be stored in a file system.
 *
 * Although this interface is marked as immutable,
 * in fact the getters can return different values if the file contents have been changed.
 */
@ImmutableType
interface FileAsset : StringAsset {

    /**
     * File of this asset.
     */
    val file: File

    /**
     * Bytes of this file. Not use too often because this value is not cached.
     */
    override val bytes: ByteArray

    /**
     * Read file bytes as string. Not use too often because this value is not cached.
     */
    override val text: String

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(file: File): FileAsset

    }

    companion object {

        /**
         * Create instance of [FileAsset] from a file.
         *
         * @param file File of asset.
         *
         * @return New instance of [FileAsset].
         */
        @JvmStatic
        fun from(file: File): FileAsset {
            return single<Factory>().create(file)
        }

    }

}