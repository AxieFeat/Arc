package arc.graphics

import arc.annotations.MutableType
import arc.math.Point3d
import arc.math.Ray
import arc.math.Vec3f

/**
 * Interface that defines the properties and behavior of a 3D camera.
 *
 * A `Camera` object represents a virtual camera in 3D space, providing properties for position,
 * rotation, field of view, and raycasting. The interface also includes methods to control camera
 * orientation. This is commonly used in graphics rendering and 3D scene management.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Camera {

    /**
     * Represents the 3D position of the camera in space.
     *
     * This property holds the current coordinates of the camera as a [Point3d] vector,
     * which includes the X, Y, and Z components. Changing this position directly affects
     * the location of the camera in the rendered scene.
     */
    @get:JvmName("position")
    var position: Point3d

    /**
     * Represents the rotation of the camera in 3D space.
     *
     * This property defines the orientation of the camera, typically described using a 3D vector
     * containing rotation values around the X, Y, and Z axes. The rotation affects the direction
     * the camera is facing and determines how the objects in the scene are viewed.
     *
     * Changes to this property require recalculating the camera's view matrix by invoking the `update()` method.
     */
    @get:JvmName("rotation")
    var rotation: Vec3f

    /**
     * Represents the field of view (FOV) of the camera in degrees.
     *
     * This variable determines the extent of the observable world visible
     * to the camera at any given moment along the vertical axis. A higher
     * FOV value results in a wider perspective, while a lower value provides
     * a more zoomed-in view.
     *
     * Changes to this variable should generally be followed by a call to
     * the `update` method to recalculate the projection and view matrix of
     * the camera, ensuring the changes are applied correctly.
     */
    @get:JvmName("fov")
    var fov: Float

    /**
     * Represents a 3D ray originating from the camera.
     *
     * The `ray` property defines a mathematical construct commonly used in graphics applications to
     * represent an infinite line with an origin and direction in 3D space. This ray is typically derived
     * from the camera's position, orientation, and projection, allowing for operations such as
     * raycasting and picking in a 3D scene. It is often used to determine intersections with objects
     * in the scene for spatial queries.
     *
     * The values for this property are usually recalculated when the camera's projection or view
     * matrix is updated.
     */
    @get:JvmName("ray")
    val ray: Ray

    /**
     * Adjusts the camera to look at a specific target in 3D space.
     *
     * This method updates the camera's direction to focus on the given target point,
     * recalculating the necessary state to orient the camera properly toward the target.
     *
     * @param vec3f The 3D vector representing the target point in space that the camera should look at.
     */
    fun lookAt(vec3f: Vec3f)

}