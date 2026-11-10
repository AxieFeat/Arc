package arc.model.group

import korlibs.math.geom.Vector3F
import kotlin.uuid.Uuid

internal data class SimpleElementGroup(
    override val name: String = "",
    override val cubes: Set<Uuid> = emptySet(),
    override val pivot: Vector3F = Vector3F(0f, 0f, 0f),
) : ElementGroup {

    override fun copy(): ElementGroup {
        return SimpleElementGroup(
            name,
            cubes.toSet(),
            pivot
        )
    }

    class Builder : ElementGroup.Builder {

        private var name: String = ""
        private var cubes = mutableSetOf<Uuid>()
        private var pivot = Vector3F(0f, 0f, 0f)

        override fun setName(name: String): ElementGroup.Builder {
            this.name = name

            return this
        }

        override fun addCube(vararg cube: Uuid): ElementGroup.Builder {
            this.cubes.addAll(cube)

            return this
        }

        override fun setPivot(pivot: Vector3F): ElementGroup.Builder {
            this.pivot = pivot

            return this
        }

        override fun build(): ElementGroup {
            return SimpleElementGroup(name, cubes.toSet(), pivot)
        }
    }
}
