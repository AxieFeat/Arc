package arc.particle

import arc.Arc
import arc.annotations.MutableType
import arc.annotations.TypeFactory
import arc.graphics.g3d.Entity
import org.jetbrains.annotations.ApiStatus
import java.util.UUID

/**
 * Represents a stack of particles in 3D space.
 *
 * This interface provides functionality to manage and render a group of particles
 * with shared attributes such as spread, speed, count, and their associated entity.
 * Each `ParticleStack` has a unique identifier and operates with a base entity that defines
 * the visual and interactive properties shared by all particles in the stack.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@MutableType
interface ParticleStack {

    /**
     * Unique id of particle stack.
     */
    @get:JvmName("uuid")
    val uuid: UUID

    /**
     * Basic entity of this particle.
     * It will be copied for all other particles.
     */
    @get:JvmName("entity")
    val entity: Entity

    /**
     * Particle spread to the sides.
     */
    @get:JvmName("spread")
    val spread: Float

    /**
     * Speed of moving particles.
     */
    @get:JvmName("speed")
    val speed: Float

    /**
     * Count of particles.
     */
    @get:JvmName("count")
    val count: Int

    /**
     * Renders the particles.
     *
     * @param delta Delta time since the last frame, used for frame-based animations.
     */
    fun render(delta: Float)

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        /**
         * Creates a new instance of a ParticleStack with the given parameters.
         *
         * @param uuid The unique identifier for the ParticleStack.
         * @param entity The base entity associated with the particles in the stack.
         * @param spread The spread of the particles to the sides.
         * @param speed The speed at which the particles move.
         * @param count The number of particles in the stack.
         *
         * @return A new instance of ParticleStack configured with the provided parameters.
         */
        fun create(uuid: UUID, entity: Entity, spread: Float, speed: Float, count: Int): ParticleStack

    }

    companion object {

        /**
         * Creates a new instance of a ParticleStack with the given parameters.
         *
         * @param uuid The unique identifier for the ParticleStack.
         * @param entity The base entity associated with the particles in the stack.
         * @param spread The spread of the particles to the sides.
         * @param speed The speed at which the particles move.
         * @param count The number of particles in the stack.
         *
         * @return A new instance of ParticleStack configured with the provided parameters.
         */
        @JvmStatic
        fun create(uuid: UUID, entity: Entity, spread: Float, speed: Float, count: Int): ParticleStack {
            return Arc.factory<Factory>().create(uuid, entity, spread, speed, count)
        }

    }

}