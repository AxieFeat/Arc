package arc.graphics.vertex

import arc.annotations.MutableType
import arc.math.Point3i
import arc.util.Color

/**
 * Interface for creating and managing vertex data in a 3D space.
 *
 * A `VertexConsumer` defines a mutable structure for manipulating vertex attributes such as position,
 * color, texture coordinates, and normals. This interface is intended for real-time rendering systems
 * or other applications requiring precise control over vertex data. Multiple methods are provided
 * to modify the attributes of individual vertices in a customizable way. All methods in this interface
 * return the current instance, facilitating method chaining.
 */
@MutableType
interface VertexConsumer {

    /**
     * Add vertex.
     *
     * @param point Location of point.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun addVertex(point: Point3i): VertexConsumer

    /**
     * Add vertex.
     *
     * @param x X position.
     * @param y Y position.
     * @param z Z position.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun addVertex(x: Int, y: Int, z: Int): VertexConsumer

    /**
     * Set color of current vertex.
     *
     * @param red Red value.
     * @param green Green value.
     * @param blue Blue value.
     * @param alpha Alpha value (transparency).
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setColor(red: Int, green: Int, blue: Int, alpha: Int): VertexConsumer

    /**
     * Set color of current vertex.
     *
     * @param color Color to set.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setColor(color: Color): VertexConsumer

    /**
     * Disable color for current vertex.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun noColor(): VertexConsumer

    /**
     * Set first UV of current vertex.
     *
     * @param u Horizontal value.
     * @param v Vertical value.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setUv(u: Int, v: Int): VertexConsumer

    /**
     * Set second UV of current vertex.
     *
     * @param u Horizontal value.
     * @param v Vertical value.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setUv1(u: Int, v: Int): VertexConsumer

    /**
     * Set third UV of current vertex.
     *
     * @param u Horizontal value.
     * @param v Vertical value.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setUv2(u: Int, v: Int): VertexConsumer

    /**
     * Set normal for current vertex.
     *
     * @param point Values of normal.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setNormal(point: Point3i): VertexConsumer

    /**
     * Set normal for current vertex.
     *
     * @param x X value.
     * @param y Y value.
     * @param z Z value.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setNormal(x: Int, y: Int, z: Int): VertexConsumer

    /**
     * Set offsets for current vertex.
     *
     * @param x X value.
     * @param y Y value.
     * @param z Z value.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setTranslation(x: Double, y: Double, z: Double): VertexConsumer

}