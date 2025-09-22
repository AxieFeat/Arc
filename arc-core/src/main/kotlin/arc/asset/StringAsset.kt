package arc.asset

import arc.annotations.ImmutableType

/**
 * This interface represents a string-like asset.
 *
 * @see RuntimeAsset
 */
@ImmutableType
interface StringAsset : AssetLike {

    /**
     * String of this asset.
     */
    val text: String
}
