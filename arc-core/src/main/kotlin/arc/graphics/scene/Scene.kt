package arc.graphics.scene

import arc.graphics.Camera

/**
 * This interface represents a renderable scene of the engine.
 */
interface Scene {

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
    var isShowCursor: Boolean

    /**
     * Render this scene.
     */
    fun render()
}
