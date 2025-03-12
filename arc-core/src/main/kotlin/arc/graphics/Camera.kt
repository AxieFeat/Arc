package arc.graphics

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.math.Point3d
import org.jetbrains.annotations.ApiStatus
import org.joml.Matrix4f
import org.joml.Quaternionf

/**
 * This interface represents a camera in 3d space.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Camera {

    /**
     * Height of application window.
     */
    @get:JvmName("windowHeight")
    val windowHeight: Float

    /**
     * Width of application window.
     */
    @get:JvmName("windowWidth")
    val windowWidth: Float

    /**
     * View matrix. You can use it for advanced transformations.
     */
    @get:JvmName("view")
    var view: Matrix4f

    /**
     * Projection matrix. You can use it for advanced transformations.
     */
    @get:JvmName("projection")
    var projection: Matrix4f

    /**
     * Position of camera in 3d space.
     */
    @get:JvmName("position")
    var position: Point3d

    /**
     * Rotation of camera.
     */
    @get:JvmName("rotation")
    var rotation: Quaternionf

    /**
     * Field of view of camera.
     */
    @get:JvmName("fov")
    var fov: Float

    /**
     * Z near for matrix.
     */
    @get:JvmName("zNear")
    var zNear: Float

    /**
     * Z far for matrix.
     */
    @get:JvmName("zFar")
    var zFar: Float

    /**
     * Set look point for camera.
     *
     * If you specify this parameter, additional rotation through the other methods will have no effect.
     * Use [resetLookAt] for disabling.
     *
     * @param target Target for view.
     */
    fun lookAt(target: Point3d)

    /**
     * Rotate camera.
     */
    fun rotate(yaw: Float, pitch: Float, roll: Float)

    /**
     * Update matrices of camera.
     */
    fun update()

    /**
     * Update aspect in camera with new height and width.
     */
    fun updateAspect(height: Int, width: Int)

    /**
     * Reset [lookAt].
     */
    fun resetLookAt()

    /**
     * Reset all setting in camera.
     */
    fun reset()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create new instance of [Camera].
         *
         * @param fov Field of view for camera.
         * @param height Window height.
         * @param width Window width.
         *
         * @return New instance pf [Camera].
         */
        fun create(fov: Float, height: Float, width: Float): Camera

    }

    companion object {

        /**
         * Create new instance of [Camera].
         *
         * @param fov Field of view for camera.
         * @param height Window height.
         * @param width Window width.
         *
         * @return New instance pf [Camera].
         */
        @JvmStatic
        fun create(fov: Float, height: Float, width: Float): Camera {
            return Arc.factory<Factory>().create(fov, height, width)
        }

    }

}