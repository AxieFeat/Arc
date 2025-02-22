package arc.physics

import arc.graphics.g3d.Entity

/**
 * Interface representing the behavior required for detecting and processing collisions
 * between entities within a 3D environment. The `EntityCollider` manages collision
 * detection based on entities' positions and models, as well as handles the responses
 * triggered by collisions.
 */
interface EntityCollider {

    /**
     * Checks if two entities collide based on their positions and models.
     *
     * @param entity1 First entity to check collision.
     * @param entity2 Second entity to check collision.
     *
     * @return True if the entities collide, false otherwise.
     */
    fun checkCollision(entity1: Entity, entity2: Entity): Boolean

    /**
     * Processes the collision event between two entities.
     * Handles collision effects like position adjustment or triggering associated actions.
     *
     * @param entity1 First entity involved in collision.
     * @param entity2 Second entity involved in collision.
     */
    fun processCollision(entity1: Entity, entity2: Entity)

    /**
     * Updates the collision state and processes any necessary adjustments or actions
     * within the 3D environment.
     *
     * The `update` function is responsible for checking and handling collisions
     * between entities. It ensures that the state of each entity is appropriately adjusted
     * based on detected collisions, applying necessary effects such as positional changes,
     * rotation updates, or other triggered responses.
     *
     * Implementations of this method should manage the lifecycle of the collision detection
     * and response processes, maintaining the integrity and stability of the environment.
     */
    fun update()

}