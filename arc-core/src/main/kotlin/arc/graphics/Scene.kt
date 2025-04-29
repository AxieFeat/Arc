package arc.graphics

/**
 * This interface represents renderable scene of engine.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
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
     * Delta tick of this scene.
     */
    val delta: Float

    /**
     * Is cursor showed in window.
     */
    @get:JvmName("isShowCursor")
    @set:JvmName("setShowCursor")
    var isShowCursor: Boolean

    /**
     * Render this scene.
     */
    fun render()

}