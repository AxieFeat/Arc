package arc.asset

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents an immutable stack of assets.
 *
 * @param T The type of asset contained in the stack, which must implement the [AssetLike] interface.
 */
@ImmutableType
interface AssetStack<T : AssetLike> : Iterable<T> {

    /**
     * Set of all assets in this stack.
     */
    val assets: Set<T>

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun <T : AssetLike> create(assets: Set<T>): AssetStack<T>

    }

    companion object {

        /**
         * Empty [AssetStack].
         */
        @JvmField
        val EMPTY = of(emptySet())

        /**
         * Create a new stack of assets.
         *
         * @param assets Assets of stack.
         *
         * @return New instance of [AssetStack].
         */
        @JvmStatic
        fun <T : AssetLike> of(assets: Set<T> = setOf()): AssetStack<T> {
            return Arc.factory<Factory>().create(assets)
        }

        /**
         * Create a new stack of assets.
         *
         * @param asset Assets of stack.
         *
         * @return New instance of [AssetStack].
         */
        @JvmStatic
        fun <T : AssetLike> of(vararg asset: T): AssetStack<T> = of(asset.toSet())

    }

}