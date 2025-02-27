package arc.assets

import arc.annotations.ImmutableType
import java.io.File

/**
 * Represents a general asset in the system.
 * All asset types implement or extend this interface to provide their respective implementations.
 *
 * Assets are considered immutable. This is enforced by the annotation [ImmutableType],
 * which ensures that all fields are read-only and cannot be modified after creation.
 *
 * Common asset examples include [TextureAsset], and [SoundAsset].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Asset {

    /**
     * The file associated with this asset.
     *
     * This property provides access to the underlying file that represents the asset.
     * The file is immutable and cannot be modified after the asset is created.
     */
    @get:JvmName("file")
    val file: File

}