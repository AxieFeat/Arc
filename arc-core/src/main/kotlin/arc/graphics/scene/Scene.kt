package arc.graphics.scene

import arc.graphics.Camera
import arc.graphics.Drawer

/**
 * This interface represents a renderable scene of the engine.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface Scene {

    /**
     * Drawer that this scene uses.
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
     * Is cursor showed in a window.
     */
    @get:JvmName("isShowCursor")
    @set:JvmName("setShowCursor")
    var isShowCursor: Boolean

    /**
     * Render this scene.
     */
    fun render()

}