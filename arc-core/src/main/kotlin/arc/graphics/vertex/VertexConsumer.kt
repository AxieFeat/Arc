package arc.graphics.vertex

import arc.annotations.MutableType
import arc.util.Color
import org.joml.Matrix4f

/**
 * Interface for creating and managing vertex data in a 3D space.
 */
@MutableType
interface VertexConsumer {

    /**
     * Add vertex.
     *
     * @param x X position.
     * @param y Y position.
     * @param z Z position.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun addVertex(x: Float, y: Float, z: Float): VertexConsumer

    /**
     * Add vertex.
     *
     * @param matrix Matrix for transformation vertex.
     * @param x X position.
     * @param y Y position.
     * @param z Z position.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun addVertex(matrix: Matrix4f, x: Float, y: Float, z: Float): VertexConsumer

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
     * Set UV of current vertex.
     *
     * @param u Horizontal value.
     * @param v Vertical value.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setTexture(u: Float, v: Float): VertexConsumer

    /**
     * Set offsets for current vertex.
     *
     * @param x X value.
     * @param y Y value.
     * @param z Z value.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setTranslation(x: Float, y: Float, z: Float): VertexConsumer

    /**
     * Set normal coords for current vertex.
     *
     * @param x X value.
     * @param y Y value.
     * @param z Z value.
     *
     * @return Current instance of [VertexConsumer].
     */
    fun setNormal(x: Float, y: Float, z: Float): VertexConsumer

    /**
     * End current vertex.
     */
    fun endVertex(): VertexConsumer

}