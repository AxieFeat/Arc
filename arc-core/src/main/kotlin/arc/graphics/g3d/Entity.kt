package arc.graphics.g3d

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.g3d.model.Model
import arc.math.Point3d
import arc.util.Copyable
import arc.util.Identifiable
import org.jetbrains.annotations.ApiStatus
import org.joml.Quaternionf
import java.util.UUID

/**
 * Represents an entity within a 3D environment.
 *
 * An entity is a mutable object that has attributes such as position, scale, rotation,
 * and model representation.
 *
 * You can store entities in [ChunkSection].
 *
 * @see ChunkSection
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface Entity : Copyable<Entity>, Identifiable {

    /**
     * Represents the unique identifier of an entity.
     *
     * The `id` property is an integral value that uniquely identifies an instance of an `Entity`.
     * It is commonly used for entity management tasks such as retrieval, comparison, and storage,
     * particularly in game engine systems and chunk-based environments.
     */
    @get:JvmName("uuid")
    override val uuid: UUID

    /**
     * Represents the 3D model associated with this entity.
     *
     * The `model` variable holds a reference to the 3D model defining the visual and structural
     * representation of the entity. It includes details such as associated assets, textures,
     * elements, and other model-related attributes. The model is an essential component for rendering
     * and manipulation in 3D space, ensuring accurate representation of the entity in the environment.
     *
     * This property is mutable, allowing the visual representation of the entity to be changed
     * dynamically during the application's lifecycle.
     */
    @get:JvmName("model")
    var model: Model

    /**
     * Represents the scaling factor of the entity.
     *
     * This variable defines how much the entity's size is scaled relative to its original dimensions.
     * A value greater than 1 increases the size, a value less than 1 decreases it, and a value of 1
     * retains the original size.
     */
    @get:JvmName("scale")
    var scale: Double

    /**
     * Represents the position of the entity in a 3D coordinate system.
     *
     * The position is defined using a [Point3d] object, which specifies the x, y, and z coordinates.
     * This variable can be used to track or update the spatial location of the entity.
     */
    @get:JvmName("position")
    var position: Point3d

    /**
     * Represents the current rotation of the entity in 3D space.
     *
     * This property defines the entity's orientation using a quaternion.
     * Quaternions are used to efficiently handle 3D rotations, avoiding
     * issues such as gimbal lock and enabling smooth interpolations.
     */
    @get:JvmName("rotation")
    var rotation: Quaternionf

    /**
     * Represents the 3D offset position of the entity within the world.
     *
     * This variable indicates the relative position of the entity in a
     * three-dimensional space, using the [Point3d] format. It is used
     * to determine the precise coordinates of the entity's offset.
     */
    @get:JvmName("offset")
    var offset: Point3d

    /**
     * Indicates whether the entity is viewable.
     *
     * This property is typically used to determine if the entity is currently visible
     * or rendered in the given context, such as within a camera's perspective or in the current scene.
     */
    var isViewable: Boolean

    /**
     * Determines if this entity collides with another specified entity.
     *
     * @param other The other entity to check for collision.
     *
     * @return True if this entity collides with the specified entity, false otherwise.
     */
    fun isCollide(other: Entity): Boolean

    /**
     * Creates and returns a new instance of the `Entity` class with identical properties.
     *
     * This method is used to create a duplicate of the current `Entity` object.
     * The returned `Entity` is distinct from the original, allowing for modifications
     * without affecting the original instance.
     *
     * @return A new instance of `Entity` with the same properties as the original.
     */
    override fun copy(): Entity

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Create new instance [Entity].
         *
         * @param uuid Unique ID of this entity.
         * @param model Model of this entity.
         *
         * @return New instance of [Entity].
         */
        fun create(uuid: UUID, model: Model): Entity

    }

    companion object {

        /**
         * Create new instance [Entity].
         *
         * @param uuid Unique ID of this entity.
         * @param model Model of this entity.
         *
         * @return New instance of [Entity].
         */
        @JvmStatic
        fun create(uuid: UUID, model: Model): Entity {
            return Arc.factory<Factory>().create(uuid, model)
        }

    }

}