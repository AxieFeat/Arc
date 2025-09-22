package arc.texture

import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable

/**
 * This interface represents a general texture.
 */
interface TextureLike : Bindable, Cleanable {

    /**
     * ID of this texture in a render system.
     */
    val id: Int
}
