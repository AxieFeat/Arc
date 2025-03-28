package arc.asset

import arc.annotations.ImmutableType
import java.io.File

/**
 * Represents a general asset in the engine.
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