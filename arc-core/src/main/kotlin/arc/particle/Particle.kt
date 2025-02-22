package arc.particle

import arc.Arc
import arc.annotations.ImmutableType
import arc.annotations.TypeFactory
import arc.graphics.g3d.Entity
import arc.util.Identifiable
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * This interface represents single particle in 3D space.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
interface Particle : Identifiable {

    /**
     * Unique id of this particle.
     */
    @get:JvmName("uuid")
    override val uuid: UUID

    /**
     * Entity of this particle.
     */
    @get:JvmName("entity")
    val entity: Entity

    /**
     * Time to leave in milliseconds.
     */
    @get:JvmName("ttl")
    val ttl: Long

    /**
     * Render particle.
     *
     * @param delta Delta of frame for animations.
     */
    fun render(delta: Float)

    @TypeFactory
    @ApiStatus.Internal
    interface Factory {

        /**
         * Create new single particle.
         *
         * @param uuid Unique id of this particle.
         * @param entity Entity of particle.
         * @param ttl Time to leave of particle.
         *
         * @return New instance of [Particle].
         */
        fun create(uuid: UUID, entity: Entity, ttl: Long): Particle

    }

    companion object {

        /**
         * Create new single particle.
         *
         * @param uuid Unique id of this particle.
         * @param entity Entity of particle.
         * @param ttl Time to leave of particle.
         *
         * @return New instance of [Particle].
         */
        @JvmStatic
        fun create(uuid: UUID, entity: Entity, ttl: Long): Particle {
            return Arc.factory<Factory>().create(uuid, entity, ttl)
        }

    }

}