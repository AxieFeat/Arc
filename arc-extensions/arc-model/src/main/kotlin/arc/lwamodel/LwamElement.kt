package arc.lwamodel

import arc.annotations.ImmutableType
import arc.model.Element

/**
 * This interface represents element in LWAM format (It supports lighting).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface LwamElement : Element {

    /**
     * The light emission level of an element.
     */
    @get:JvmName("lightLevel")
    val lightLevel: Byte

    /**
     * The light color of this element in model.
     */
    @get:JvmName("lightColor")
    val lightColor: Int

}