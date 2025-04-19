package arc.model

import arc.math.Vec3f
import arc.model.animation.Animation
import arc.model.cube.Cube
import arc.model.group.ElementGroup
import arc.model.texture.ModelTexture

internal data class ArcModel(
    override val cubes: List<Cube> = emptyList(),
    override val groups: List<ElementGroup> = emptyList(),
    override val animations: List<Animation> = emptyList(),
    override val texture: ModelTexture
) : Model {

    override fun hasNearFace(cube: Cube, face: Face): Boolean {
        val epsilon = 0.001f
        val from = cube.from
        val to = cube.to

        fun rangesOverlap(aMin: Float, aMax: Float, bMin: Float, bMax: Float): Boolean {
            return aMin < bMax - epsilon && aMax > bMin + epsilon
        }

        for (other in cubes) {
            if (other == cube) continue

            val oFrom = other.from
            val oTo = other.to

            when (face) {
                Face.NORTH -> {
                    if (Vec3f.of(0f, 0f, from.z).epsilonEquals(Vec3f.of(0f, 0f, oTo.z), epsilon) &&
                        rangesOverlap(from.x, to.x, oFrom.x, oTo.x) &&
                        rangesOverlap(from.y, to.y, oFrom.y, oTo.y)
                    ) return true
                }

                Face.SOUTH -> {
                    if (Vec3f.of(0f, 0f, to.z).epsilonEquals(Vec3f.of(0f, 0f, oFrom.z), epsilon) &&
                        rangesOverlap(from.x, to.x, oFrom.x, oTo.x) &&
                        rangesOverlap(from.y, to.y, oFrom.y, oTo.y)
                    ) return true
                }

                Face.WEST -> {
                    if (Vec3f.of(from.x, 0f, 0f).epsilonEquals(Vec3f.of(oTo.x, 0f, 0f), epsilon) &&
                        rangesOverlap(from.z, to.z, oFrom.z, oTo.z) &&
                        rangesOverlap(from.y, to.y, oFrom.y, oTo.y)
                    ) return true
                }

                Face.EAST -> {
                    if (Vec3f.of(to.x, 0f, 0f).epsilonEquals(Vec3f.of(oFrom.x, 0f, 0f), epsilon) &&
                        rangesOverlap(from.z, to.z, oFrom.z, oTo.z) &&
                        rangesOverlap(from.y, to.y, oFrom.y, oTo.y)
                    ) return true
                }

                Face.DOWN -> {
                    if (Vec3f.of(0f, from.y, 0f).epsilonEquals(Vec3f.of(0f, oTo.y, 0f), epsilon) &&
                        rangesOverlap(from.x, to.x, oFrom.x, oTo.x) &&
                        rangesOverlap(from.z, to.z, oFrom.z, oTo.z)
                    ) return true
                }

                Face.UP -> {
                    if (Vec3f.of(0f, to.y, 0f).epsilonEquals(Vec3f.of(0f, oFrom.y, 0f), epsilon) &&
                        rangesOverlap(from.x, to.x, oFrom.x, oTo.x) &&
                        rangesOverlap(from.z, to.z, oFrom.z, oTo.z)
                    ) return true
                }
                else -> {}
            }
        }

        return false
    }

    override fun greedy() {

    }

    override fun reset() {
        // TODO ? May be not need.
    }

    override fun copy(): Model {
        return ArcModel(
            cubes.map { it.copy() },
            groups.map { it.copy() },
            animations.map { it.copy() },
            texture
        )
    }

    class Builder : Model.Builder {

        private val cubes = mutableListOf<Cube>()
        private val groups = mutableListOf<ElementGroup>()
        private val animations = mutableListOf<Animation>()

        private var isTextureSet = false
        private lateinit var texture: ModelTexture

        override fun addCube(vararg cube: Cube): Model.Builder {
            this.cubes.addAll(cube)

            return this
        }

        override fun addGroup(vararg group: ElementGroup): Model.Builder {
            this.groups.addAll(group)

            return this
        }

        override fun addAnimation(vararg animation: Animation): Model.Builder {
            this.animations.addAll(animation)

            return this
        }

        override fun setTexture(texture: ModelTexture): Model.Builder {
            this.texture = texture
            this.isTextureSet = true

            return this
        }

        override fun build(): Model {
            if (!isTextureSet) throw IllegalStateException("Can not build model, texture not set")

            return ArcModel(cubes, groups, animations, texture)
        }

    }

}