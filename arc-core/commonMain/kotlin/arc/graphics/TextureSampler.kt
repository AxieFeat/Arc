package arc.graphics

import arc.util.pattern.Cleanable

/**
 * Texture minification and magnification filters.
 */
enum class TextureSampling {

    /**
     * Samples the closest texel.
     */
    NEAREST,

    /**
     * Interpolates neighboring texels.
     */
    LINEAR
}

/**
 * Owned sampling state, independent of a borrowed texture's parameters.
 */
interface TextureSampler : Cleanable, ContextResource
