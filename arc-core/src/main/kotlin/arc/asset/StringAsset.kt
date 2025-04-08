package arc.asset

import arc.annotations.ImmutableType

/**
 * This interface represents string-like asset.
 *
 * @see RuntimeAsset
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface StringAsset : AssetLike {

    /**
     * String of this asset.
     */
    @get:JvmName("text")
    val text: String

}