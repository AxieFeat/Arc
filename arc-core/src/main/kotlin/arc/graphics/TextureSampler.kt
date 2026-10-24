package arc.graphics

import arc.util.pattern.Cleanable

enum class TextureSampling {
    NEAREST,
    LINEAR
}

/** Owned sampling state, independent of a borrowed texture's parameters. */
interface TextureSampler : Cleanable, ContextResource
