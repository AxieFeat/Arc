package arc.assets

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * Represents an immutable stack of assets.
 *
 * @param T The type of asset contained in the stack, which must implement the [Asset] interface.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface AssetStack<T : Asset> : Iterable<T> {

    /**
     * Set of all assets in this stack.
     */
    @get:JvmName("assets")
    val assets: Set<T>

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create new stack of assets.
         *
         * @param assets Assets of stack.
         *
         * @return New instance of [AssetStack].
         */
        fun <T : Asset> create(assets: Set<T>): AssetStack<T>

    }

    companion object {

        /**
         * Empty [AssetStack].
         */
        @JvmField
        val EMPTY = of(emptySet())

        /**
         * Create new stack of assets.
         *
         * @param assets Assets of stack.
         *
         * @return New instance of [AssetStack].
         */
        @JvmStatic
        fun <T : Asset> of(assets: Set<T> = setOf()): AssetStack<T> {
            return Arc.factory<Factory>().create(assets)
        }

    }

}