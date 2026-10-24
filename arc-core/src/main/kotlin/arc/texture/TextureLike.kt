package arc.texture

import arc.graphics.ContextResource
import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable

/**
 * This interface represents a general texture.
 */
interface TextureLike : Bindable, Cleanable, ContextResource {

    /**
     * ID of this texture in a render system.
     */
    val id: Int
}
