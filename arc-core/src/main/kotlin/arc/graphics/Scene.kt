package arc.graphics

/**
 * This interface represents renderable scene of engine.
 */
interface Scene {

    /**
     * Drawer, that this scene use.
     */
    val drawer: Drawer

    /**
     * Camera of this scene.
     */
    val camera: Camera

    /**
     * Count of fps in this scene.
     */
    val fps: Int

    /**
     * Partial tick of this scene.
     */
    val partial: Float

    /**
     * Is this scene using now.
     */
    val inUse: Boolean

    /**
     * Is this scene render something.
     */
    var isSkipRender: Boolean

    /**
     * Is cursor showed in window.
     */
    var showCursor: Boolean

    /**
     * Render this scene.
     */
    fun render()

}