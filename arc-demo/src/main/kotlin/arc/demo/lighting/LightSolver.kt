package arc.demo.lighting

import arc.demo.world.World

class LightSolver(
    val world: World,
    val channel: LightChannel,
) {

    @OptIn(ExperimentalUnsignedTypes::class)
    val lighting = UByteArray(16 * 16 * 16)

    fun add(x: Int, y: Int, z: Int, value: UByte) {

    }

}