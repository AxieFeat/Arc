package arc.asset

import arc.Arc
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a mutable stack of assets.
 *
 * @param T The type of asset contained in the stack, which must implement the [AssetLike] interface.
 */
interface MutableAssetStack<T : AssetLike> : AssetStack<T> {

    /**
     * Add new asset to the stack.
     *
     * @param asset Asset to add.
     */
    fun add(asset: T)

    /**
     * Remove asset from the stack.
     *
     * @param asset Asset to remove.
     */
    fun remove(asset: T)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun <T : AssetLike> create(assets: Set<T>): MutableAssetStack<T>

    }

    companion object {

        /**
         * Empty [MutableAssetStack].
         */
        @JvmField
        val EMPTY = of(emptySet())

        /**
         * Create a new stack of assets.
         *
         * @param assets Assets of stack.
         *
         * @return New instance of [MutableAssetStack].
         */
        @JvmStatic
        fun <T : AssetLike> of(assets: Set<T> = setOf()): MutableAssetStack<T> {
            return Arc.factory<Factory>().create(assets)
        }

        /**
         * Create a new stack of assets.
         *
         * @param asset Assets of stack.
         *
         * @return New instance of [MutableAssetStack].
         */
        @JvmStatic
        fun <T : AssetLike> of(vararg asset: T): MutableAssetStack<T> = of(asset.toSet())

    }

}