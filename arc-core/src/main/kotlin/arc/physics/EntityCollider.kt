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

}