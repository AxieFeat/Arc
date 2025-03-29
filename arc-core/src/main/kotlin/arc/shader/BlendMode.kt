package arc.shader

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents blend setting for shader.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface BlendMode {

    @get:JvmName("srcColorFactor")
    val srcColorFactor: Int

    @get:JvmName("srcAlphaFactor")
    val srcAlphaFactor: Int

    @get:JvmName("dstColorFactor")
    val dstColorFactor: Int

    @get:JvmName("dstAlphaFactor")
    val dstAlphaFactor: Int

    @get:JvmName("blendFunc")
    val blendFunc: Int

    @get:JvmName("separateBlend")
    val separateBlend: Boolean

    @get:JvmName("opaque")
    val opaque: Boolean

    /**
     * Apply blend setting in current context.
     */
    fun apply()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(
            separateBlend: Boolean,
            opaque: Boolean,
            srcColorFactor: Int,
            srcAlphaFactor: Int,
            dstColorFactor: Int,
            dstAlphaFactor: Int,
            blendFunc: Int,
        ): BlendMode

    }

    companion object {

        /**
         * Create new instance of [BlendMode].
         *
         * @return New instance of [BlendMode].
         */
        @JvmStatic
        fun create(
            separateBlend: Boolean = false,
            opaque: Boolean = true,
            srcColorFactor: Int = 1,
            srcAlphaFactor: Int = 0,
            dstColorFactor: Int = 1,
            dstAlphaFactor: Int = 0,
            blendFunc: Int = 32774,
        ): BlendMode {
            return Arc.factory<Factory>().create(separateBlend, opaque, srcColorFactor, srcAlphaFactor, dstColorFactor, dstAlphaFactor, blendFunc)
        }

    }

}