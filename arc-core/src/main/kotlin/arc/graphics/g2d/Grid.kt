package arc.graphics.g2d

import arc.annotations.MutableType

/**
 * This interface represents a full screen size grid for arranging items on the window.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Grid {

    /**
     * Renderable components of this grid.
     */
    @get:JvmName("components")
    val UIComponents: List<UIComponent>

    /**
     * Rows count of this grid.
     */
    @get:JvmName("rows")
    val rows: Int

    /**
     * Columns count of this grid.
     */
    @get:JvmName("columns")
    val columns: Int

    /**
     * Add component to this grid.
     *
     * @param row Row number (X)
     * @param column Column number (X)
     * @param UIComponent Component to add.
     */
    fun add(row: Int, column: Int, UIComponent: UIComponent)

    /**
     * Remove component from this grid.
     *
     * @param UIComponent Component to remove.
     */
    fun remove(UIComponent: UIComponent)

    /**
     * Render all components of this grid via [GuiRender].
     *
     * @param guiRender Gui renderer.
     */
    fun render(guiRender: GuiRender)

}