package arc.demo.world.block

import arc.model.Model

data class Block(
    val name: String,
    val model: Model,
    val opaque: Boolean = true,
    val isAir: Boolean = false
)
