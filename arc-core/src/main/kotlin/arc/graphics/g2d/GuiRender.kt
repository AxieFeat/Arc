package arc.graphics.g2d

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.graphics.Drawer
import arc.util.Color
import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents drawer for elements.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface GuiRender {

    /**
     * Drawer for rendering.
     */
    @get:JvmName("drawer")
    val drawer: Drawer

    /**
     * Fill area at display.
     *
     * @param x0 First x.
     * @param y0 First y.
     *
     * @param x1 Second x.
     * @param y1 Second y.
     *
     * @param z Layer number.
     *
     * @param color Color of area.
     */
    fun fill(x0: Float, y0: Float, x1: Float, y1: Float, z: Float, color: Color)

    /**
     * Render text at display.
     *
     * @param text Text to render.
     * @param x X position.
     * @param y Y position.
     * @param z Z position.
     * @param shadow Render shadow of text?
     */
    fun text(text: Component, x: Float, y: Float, z: Float, shadow: Boolean = true)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create new instance of [GuiRender].
         *
         * @param drawer Drawer of renderer.
         *
         * @return New instance of [GuiRender].
         */
        fun create(drawer: Drawer): GuiRender

    }

    companion object {

        /**
         * Create new instance of [GuiRender].
         *
         * @param drawer Drawer of renderer.
         *
         * @return New instance of [GuiRender].
         */
        @JvmStatic
        fun create(drawer: Drawer): GuiRender {
            return Arc.factory<Factory>().create(drawer)
        }

    }

}