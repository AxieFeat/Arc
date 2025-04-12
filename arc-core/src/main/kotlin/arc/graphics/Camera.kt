package arc.graphics

import arc.Arc
import arc.annotations.TypeFactory
import arc.culling.Frustum
import arc.math.Point3d
import arc.math.Ray
import org.jetbrains.annotations.ApiStatus
import org.joml.Matrix4f
import org.joml.Quaternionf

/**
 * This interface represents a camera in 3d space.
 */
interface Camera {

    /**
     * Height of application window.
     */
    val windowHeight: Float

    /**
     * Width of application window.
     */
    val windowWidth: Float

    /**
     * Frustum culling for camera.
     */
    val frustum: Frustum

    /**
     * Ray of view of this camera.
     */
    val ray: Ray

    /**
     * View matrix. You can use it for advanced transformations.
     */
    var view: Matrix4f

    /**
     * Projection matrix. You can use it for advanced transformations.
     */
    var projection: Matrix4f

    /**
     * Combined view and projection matrices.
     */
    val combined: Matrix4f

    /**
     * Position of camera in 3d space.
     */
    var position: Point3d

    /**
     * Rotation of camera.
     */
    var rotation: Quaternionf

    /**
     * Field of view of camera.
     */
    var fov: Float

    /**
     * Z near for matrix.
     */
    var zNear: Float

    /**
     * Z far for matrix.
     */
    var zFar: Float

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
    fun updateAspect(width: Int, height: Int)

    /**
     * Reset all setting in camera.
     */
    fun reset()

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(fov: Float, width: Float, height: Float): Camera

    }

    companion object {

        /**
         * Create new instance of [Camera].
         *
         * @param fov Field of view for camera.
         * @param width Window width.
         * @param height Window height.
         *
         * @return New instance of [Camera].
         */
        @JvmStatic
        fun create(fov: Float, width: Float, height: Float): Camera {
            return Arc.factory<Factory>().create(fov, height, width)
        }

    }

}