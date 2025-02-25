//package arc.graphics
//
//import arc.annotations.ImmutableType
//import arc.math.AABB
//
///**
// * This interface represents a mesh, which is an abstract representation of a 3D object
// * consisting of vertices, normals, texture coordinates, indices, and weights. A mesh
// * can be rendered and cleaned up when no longer needed.
// */
//@Suppress("INAPPLICABLE_JVM_NAME")
//@ImmutableType
//interface Mesh {
//
//    /**
//     * Represents the Axis-Aligned Bounding Box (AABB) of the mesh.
//     * The AABB defines the minimum and maximum extents of the mesh
//     * in 3D space, encapsulating all of its geometry within a rectangular
//     * prism aligned to the coordinate axes.
//     */
//    @get:JvmName("aabb")
//    val aabb: AABB
//
//    /**
//     * An array of floats representing the positions of vertices in the 3D space.
//     * Each group of three consecutive values corresponds to the X, Y, and Z
//     * coordinates of a single vertex in the mesh.
//     */
//    @get:JvmName("positions")
//    val positions: FloatArray
//
//    /**
//     * An array of floats representing the normals of vertices in the 3D space.
//     * Each group of three consecutive values corresponds to the X, Y, and Z components
//     * of the normal vector for a single vertex. Normal vectors are often used in 3D rendering
//     * to determine how light interacts with the surface of an object.
//     */
//    @get:JvmName("normals")
//    val normals: FloatArray
//
//    /**
//     * An array of floats representing the texture coordinates associated with the vertices of the mesh.
//     * Each pair of consecutive values corresponds to the U and V texture coordinates for a single vertex,
//     * where U represents the horizontal axis and V represents the vertical axis in a 2D texture map.
//     * These coordinates are used for mapping a texture onto the surface of the mesh.
//     */
//    @get:JvmName("textCoords")
//    val textCoords: FloatArray
//
//    /**
//     * An array of integers representing the indices of vertices in the mesh. Each group of three integers
//     * defines a single triangle in 3D space by specifying the indices of the vertices that form the triangle.
//     * These indices correspond to the positions in the [positions] array of the mesh.
//     */
//    @get:JvmName("indices")
//    val indices: IntArray
//
//    /**
//     * Array of weights used in the mesh to influence the geometry,
//     * typically for features like skeletal animations or blend shaping.
//     * Each weight corresponds to a vertex or a specific influence factor
//     * in the mesh structure.
//     */
//    @get:JvmName("weights")
//    val weights: FloatArray
//
//    /**
//     * Renders the mesh using the available vertex and index buffers.
//     * This method processes the mesh data (positions, normals, texture coordinates, indices, etc.)
//     * and submits the appropriate rendering commands to the graphics pipeline.
//     *
//     * This method is typically called within a rendering context where the mesh
//     * is bound to a shader and other rendering settings have already been configured.
//     */
//    fun render()
//
//    /**
//     * Releases the resources associated with the mesh.
//     *
//     * This method is responsible for freeing any allocated resources,
//     * such as vertex buffers, index buffers, or any other graphics-related
//     * data, to prevent memory leaks.
//     *
//     * It should be called when the mesh is no longer needed or before
//     * the application exits.
//     */
//    fun cleanup()
//
//}