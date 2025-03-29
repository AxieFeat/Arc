package arc.graphics

/**
 * This interface represents renderable scene of engine.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface Scene {

    /**
     * Drawer, that this scene use.
     */
    @get:JvmName("drawer")
    val drawer: Drawer

    /**
     * Camera of this scene.
     */
    @get:JvmName("camera")
    val camera: Camera

    /**
     * Count of fps in this scene.
     */
    @get:JvmName("fps")
    val fps: Int

    /**
     * Delta tick of this scene.
     */
    @get:JvmName("delta")
    val delta: Float

    /**
     * Is this scene using now.
     */
    @get:JvmName("inUse")
    val inUse: Boolean

    /**
     * Is this scene render something.
     */
    var isSkipRender: Boolean

    /**
     * Is cursor showed in window.
     */
    @get:JvmName("showCursor")
    var showCursor: Boolean

    /**
     * Render this scene.
     */
    fun render()

}