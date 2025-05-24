package arc.asset

import arc.annotations.ImmutableType

/**
 * Represents a very basic asset of the engine.
 */
@ImmutableType
interface AssetLike {

    /**
     * Bytes of this asset. It can be anything.
     */
    val bytes: ByteArray

}