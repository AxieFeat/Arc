package arc.texture

import arc.util.pattern.Bindable
import arc.util.pattern.Cleanable

/**
 * This interface represents general texture.
 */
interface TextureLike : Bindable, Cleanable {

    /**
     * ID of this texture in render system.
     */
    val id: Int

}