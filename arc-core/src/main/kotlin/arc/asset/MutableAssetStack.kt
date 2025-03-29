package arc.asset

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a mutable stack of assets.
 *
 * @param T The type of asset contained in the stack, which must implement the [Asset] interface.
 */
@MutableType
interface MutableAssetStack<T : Asset> : AssetStack<T> {

    /**
     * Add new asset to stack.
     *
     * @param asset Asset to add.
     */
    fun add(asset: T)

    /**
     * Remove asset from stack.
     *
     * @param asset Asset to remove.
     */
    fun remove(asset: T)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun <T : Asset> create(assets: Set<T>): MutableAssetStack<T>

    }

    companion object {

        /**
         * Empty [MutableAssetStack].
         */
        @JvmField
        val EMPTY = of(emptySet())

        /**
         * Create new stack of assets.
         *
         * @param assets Assets of stack.
         *
         * @return New instance of [MutableAssetStack].
         */
        @JvmStatic
        fun <T : Asset> of(assets: Set<T> = setOf()): MutableAssetStack<T> {
            return Arc.factory<Factory>().create(assets)
        }

    }

}