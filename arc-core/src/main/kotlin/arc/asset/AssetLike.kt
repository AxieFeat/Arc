package arc.asset

import arc.annotations.ImmutableType

/**
 * Represents a very basic asset of engine.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface AssetLike {

    /**
     * Bytes of this asset. It can be anything.
     */
    @get:JvmName("bytes")
    val bytes: ByteArray

}