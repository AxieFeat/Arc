package arc.graphics

import arc.annotations.MutableType
import arc.math.*
import org.joml.Matrix4f

/**
 * Represents a mutable 3D camera with configurable attributes and operations for 3D rendering.
 * The camera provides properties and methods for manipulating its position, rotation,
 * field of view, viewport size, and projection settings. It also allows the calculation
 * and retrieval of view and projection matrices for rendering purposes.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Camera {

    /**
     * Represents the 3D position of the camera in space.
     *
     * This property holds the current coordinates of the camera as a [Vec3f] vector,
     * which includes the X, Y, and Z components. Changing this position directly affects
     * the location of the camera in the rendered scene.
     *
     * It is typically used in combination with other camera properties and methods,
     * such as `direction`, `rotation`, or `lookAt`, to define the viewing perspective
     * or manipulate the camera's orientation and focus.
     *
     * After modifying the position, the `update` method should be called to recalculate
     * the view matrices and ensure consistent rendering.
     */
    @get:JvmName("position")
    var position: Vec3f

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
     * Represents the direction vector of the camera in 3D space.
     *
     * This property provides the current forward-facing direction of the camera, often used
     * for operations such as determining the view matrix, calculating where the camera is
     * looking, or performing raycasting calculations. It is typically a unit vector and is
     * subject to updates whenever the camera's orientation changes (e.g., through rotation
     * or other transformations).
     */
    @get:JvmName("direction")
    val direction: Vec3f

    /**
     * Represents the "up" vector for the camera in 3D space.
     *
     * This vector defines the upwards direction relative to the camera's orientation,
     * typically used to specify the camera's "roll" or vertical alignment. It plays
     * a crucial role in calculating the view matrix and ensuring the camera's rotation
     * and perspective adhere to the intended transformations.
     */
    @get:JvmName("up")
    val up: Vec3f

    /**
     * Represents the camera's projection matrix.
     * The projection matrix defines the transformation from 3D world coordinates to 2D screen coordinates.
     * It encapsulates attributes like field of view, aspect ratio, and near and far clipping planes.
     *
     * This value should be updated by invoking the camera's `update` method after modifying projection-related properties.
     */
    @get:JvmName("projection")
    val projection: Matrix4f

    /**
     * Represents the view matrix of the camera.
     *
     * The view matrix transforms world coordinates into the camera's local coordinate space,
     * defining the camera's orientation and position in the scene. It is recalculated
     * whenever the camera's attributes are updated.
     */
    @get:JvmName("view")
    val view: Matrix4f

    /**
     * The `combined` property represents the combined projection and view matrix
     * of the camera. This matrix is typically used for transforming world coordinates
     * into camera coordinates during rendering operations.
     */
    @get:JvmName("combined")
    val combined: Matrix4f

    /**
     * Represents the inverse combined matrix of the camera, which is the inverse of the combined transformation
     * matrix. This matrix is typically used for transforming coordinates from the screen space back to the world
     * space, such as when performing ray casting or determining object positions relative to the camera.
     */
    @get:JvmName("inv")
    var inv: Matrix4f

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
     * Represents the near clipping plane distance of the camera.
     *
     * The near clipping plane defines the minimum distance from the camera
     * at which objects are rendered. Objects closer than this distance will
     * not be visible. This parameter directly influences the projection matrix
     * of the camera and is used for frustum culling calculations.
     */
    @get:JvmName("near")
    var near: Float

    /**
     * The far clipping plane distance for the camera's frustum.
     *
     * This property defines the maximum distance from the camera at which objects are rendered.
     * Any object farther away than this distance will not be visible in the rendered scene.
     * Adjusting this value can affect perspective and depth precision in the rendered scene.
     */
    @get:JvmName("far")
    var far: Float

    /**
     * Indicates whether the camera is in perspective mode.
     *
     * When set to true, the camera uses a perspective projection, which produces a 3D effect
     * where objects appear smaller the farther they are from the camera. When set to false,
     * the camera uses an orthographic projection, which maintains the same size for objects
     * regardless of their distance from the camera.
     *
     * This variable affects the camera's projection and should be updated before recalculating
     * the matrices using the update method.
     */
    var perspective: Boolean

    /**
     * Defines the width of the viewport in world units for the camera's projection.
     * This value corresponds to the horizontal size of the rendering area, and is used
     * in conjunction with the height to determine the aspect ratio and the field of view
     * when rendering the scene.
     *
     * Modifying this value requires calling the `update()` method to recalculate the
     * camera's projection and view matrices, ensuring the changes are reflected in the
     * rendered output.
     */
    @get:JvmName("width")
    val width: Float

    /**
     * Represents the height property of the camera's viewport or projection.
     *
     * This value typically defines the vertical size of the camera's view in world units
     * or is used in calculations involving the camera's field of view, aspect ratio, or
     * viewport dimensions. It is often updated or accessed when resizing the viewport
     * or modifying the camera's projection.
     */
    @get:JvmName("height")
    val height: Float

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
     * Updates the state of the camera and recalculates its transformation matrices.
     *
     * This method computes the updated `view`, `projection`, and `combined` matrices
     * based on the camera's current parameters such as position, rotation, direction,
     * field of view, near and far clipping planes, and perspective settings. These matrices
     * are used for rendering operations and to transform world coordinates into
     * camera or clip space.
     *
     * It must be called whenever the camera's properties are modified to ensure
     * the matrices stay in sync with the current camera state.
     */
    fun update()

    /**
     * Adjusts the camera's viewport dimensions and updates its projection matrix.
     *
     * @param viewportWidth The new width of the viewport in world units.
     * @param viewportHeight The new height of the viewport in world units.
     */
    fun resize(viewportWidth: Float, viewportHeight: Float)

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