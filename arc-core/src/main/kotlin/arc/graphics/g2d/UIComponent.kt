package arc.graphics.g2d

import arc.annotations.MutableType
import arc.math.Point2d

/**
 * Represents an interactive, modifiable graphical component that can be rendered on a screen.
 *
 * Instances of this interface define properties for position, scale, and visibility,
 * and provide a method for rendering the component using a graphical renderer.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface UIComponent {

    /**
     * Position of this element in screen.
     */
    @get:JvmName("position")
    var position: Point2d

    /**
     * Scale of this element.
     */
    @get:JvmName("scale")
    var scale: Double

    /**
     * Is this element viewable.
     */
    var isViewable: Boolean

    /**
     * Render this element via [GuiRender].
     *
     * @param guiRender Gui renderer.
     */
    fun render(guiRender: GuiRender)

}