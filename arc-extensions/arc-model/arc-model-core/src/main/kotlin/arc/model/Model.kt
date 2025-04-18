package arc.model

import arc.Arc
import arc.math.Point3i
import arc.model.animation.Animation
import arc.model.cube.Cube
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture
import arc.model.cube.CubeFace
import arc.util.pattern.Copyable
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents model in LWAM format.
 *
 * LWAM (Lightweight Arc Model) - its format of models, that based on BB Model format, but:
 *  * Any method for storaging, it can be binary file also.
 *  * Without unnecessary parameters, only those necessary for rendering.
 *  * Support for colored lightning in elements.
 *
 * Due to its advantages over BB Model, this format is more preferable for use,
 * and it is also very good for transferring over the network.
 */
interface Model : Copyable<Model> {

    /**
     * All cubes in this model.
     */
    val cubes: List<Cube>

    /**
     * All groups in this model.
     */
    val groups: List<ElementGroup>

    /**
     * All animations in this model.
     */
    val animations: List<Animation>

    /**
     * Texture of this model.
     */
    val texture: ModelTexture

    /**
     * Translate positions of all cubes in this model by offsets.
     *
     * @param offsetX X offset.
     * @param offsetY Y offset.
     * @param offsetZ Z offset.
     */
    fun translate(offsetX: Float, offsetY: Float, offsetZ: Float) {
        cubes.forEach { it.translate(offsetX, offsetY, offsetZ) }
    }

    /**
     * Is some cube has near cube for some facing.
     *
     * @param cube Cube for check.
     * @param face Face for check.
     *
     * @return `true` if a [face] of a [cube] has another face next to it that is larger or the same size. Otherwise, `false.
     */
    fun hasNearFace(cube: Cube, face: Face): Boolean

    /**
     * Cull faces in model, that not viewable (Close by other cube faces).
     * It will ignore faces, where [CubeFace.isCullable] set to false.
     */
    fun cullFaces() {
        cubes.forEach { cube ->
            cube.removeFaceIf {
                hasNearFace(cube, it.key) && it.value.isCullable
            }
        }
    }

    /**
     * Use greedy meshing for this model.
     */
    fun greedy()

    /**
     * Reset model to default state.
     *
     * Example resetting model after animation.
     */
    fun reset()

    @ApiStatus.Internal
    interface Builder : arc.util.pattern.Builder<Model> {

        fun addCube(vararg cube: Cube): Builder

        fun addGroup(vararg group: ElementGroup): Builder

        fun addAnimation(vararg animation: Animation): Builder

        fun setTexture(texture: ModelTexture): Builder

    }

    companion object {

        /**
         * Create new instance of [Model] via builder.
         *
         * @return New instance of [Builder].
         */
        @JvmStatic
        fun builder(): Builder {
            return Arc.factory<Builder>()
        }

    }

}