package arc.lwamodel

import arc.annotations.ImmutableType
import arc.model.Element

/**
 * This interface represents element in LWAM format (It supports lighting).
 */
@ImmutableType
interface LwamElement : Element {

    /**
     * The light emission level of an element.
     */
    val lightLevel: Byte

    /**
     * The light color of this element in model.
     */
    val lightColor: Int

}