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
     */
    @get:JvmName("file")
    val file: File

}