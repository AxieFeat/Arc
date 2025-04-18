package arc.model.group

import arc.math.Vec3f
import java.util.*

internal data class ArcElementGroup(
    override val name: String = "",
    override val cubes: Set<UUID> = emptySet(),
    override val pivot: Vec3f = Vec3f.of(0f, 0f, 0f),
) : ElementGroup {

    override fun copy(): ElementGroup {
        return ArcElementGroup(
            name,
            cubes.toSet(),
            pivot.copy()
        )
    }

    class Builder : ElementGroup.Builder {

        private var name: String = ""
        private var cubes = mutableSetOf<UUID>()
        private var pivot = Vec3f.of(0f, 0f, 0f)

        override fun setName(name: String): ElementGroup.Builder {
            this.name = name

            return this
        }

        override fun addCube(vararg cube: UUID): ElementGroup.Builder {
            this.cubes.addAll(cube)

            return this
        }

        override fun setPivot(x: Float, y: Float, z: Float): ElementGroup.Builder {
            this.pivot.apply {
                this.x = x
                this.y = y
                this.z = z
            }

            return this
        }

        override fun build(): ElementGroup {
            return ArcElementGroup(name, cubes.toSet(), pivot)
        }

    }

}