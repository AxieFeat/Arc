package arc.model.group

import org.joml.Vector3f
import java.util.*

internal data class SimpleElementGroup(
    override val name: String = "",
    override val cubes: Set<UUID> = emptySet(),
    override val pivot: Vector3f = Vector3f(),
) : ElementGroup {

    override fun copy(): ElementGroup {
        return SimpleElementGroup(
            name,
            cubes.toSet(),
            Vector3f(pivot)
        )
    }

    class Builder : ElementGroup.Builder {

        private var name: String = ""
        private var cubes = mutableSetOf<UUID>()
        private var pivot = Vector3f()

        override fun setName(name: String): ElementGroup.Builder {
            this.name = name

            return this
        }

        override fun addCube(vararg cube: UUID): ElementGroup.Builder {
            this.cubes.addAll(cube)

            return this
        }

        override fun setPivot(pivot: Vector3f): ElementGroup.Builder {
            this.pivot = pivot

            return this
        }

        override fun build(): ElementGroup {
            return SimpleElementGroup(name, cubes.toSet(), pivot)
        }
    }
}
