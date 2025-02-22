package arc

import arc.annotations.MutableType
import arc.graphics.RenderSystem
import arc.window.Window

/**
 * Represents the core interface for an engine, providing access to its essential
 * properties and functionalities, such as the window, frame rate, rendering system,
 * and related configurations. This interface is mutable.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Engine {

    /**
     * Window of engine.
     */
    @get:JvmName("window")
    val window: Window

    /**
     * Game frame rate.
     */
    @get:JvmName("fps")
    val fps: Int

    /**
     * Render system of engine.
     */
    @get:JvmName("renderSystem")
    val renderSystem: RenderSystem

    /**
     * Limit of [fps].
     */
    @get:JvmName("fpsLimit")
    var fpsLimit: Int

    /**
     * Is render enabled. If false - it will not render anything.
     */
    var isSkipRendering: Boolean

}