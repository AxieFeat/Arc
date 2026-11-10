package arc.graphics

import arc.Arc
import arc.annotations.TypeFactory
import arc.culling.Frustum
import arc.math.Ray
import arc.annotations.InternalApi
import korlibs.math.geom.Matrix4
import korlibs.math.geom.Vector3F
import korlibs.math.geom.Quaternion
import kotlin.jvm.JvmStatic

/**
 * This interface represents a camera in 3d space.
 */
interface Camera {

    /**
     * Height of an application window.
     */
    val windowHeight: Float

    /**
     * Width of an application window.
     */
    val windowWidth: Float

    /**
     * Frustum culling for camera.
     */
    val frustum: Frustum

    /**
     * Ray of view in this camera.
     */
    val ray: Ray

    /**
     * View matrix. You can use it for advanced transformations.
     */
    var view: Matrix4

    /**
     * Projection matrix. You can use it for advanced transformations.
     */
    var projection: Matrix4

    /**
     * Combined view and projection matrices.
     */
    val combined: Matrix4

    /**
     * Position of camera in 3d space.
     */
    var position: Vector3F

    /**
     * Rotation of camera.
     */
    var rotation: Quaternion

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
    fun rotate(pitch: Float, yaw: Float, roll: Float)

    /**
     * Update matrices of camera.
     */
    fun update()

    /**
     * Update an aspect in camera with new height and width of a window.
     */
    fun updateAspect(width: Int, height: Int)

    /**
     * Reset all settings in the camera.
     */
    fun reset()

    @InternalApi
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
        fun of(fov: Float, width: Float, height: Float): Camera {
            return Arc.single<Factory>().create(fov, width, height)
        }
    }
}
