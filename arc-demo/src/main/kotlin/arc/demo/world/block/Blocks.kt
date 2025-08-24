package arc.demo.world.block

import arc.math.AABB
import arc.model.Face
import arc.model.Model
import arc.model.cube.Cube
import arc.model.cube.CubeFace
import arc.model.texture.ModelTexture
import arc.texture.TextureAtlas
import org.joml.Vector3f

object Blocks {

    val AIR = object : Block(
        id = 0,
        model = null,
        aabb = null,
    ) {}

    @JvmStatic
    val STONE = object : Block(
        id = 1,
        model = defaultModel(),
        aabb = AABB.Companion.of(
            min = Vector3f(0f, 0f, 0f),
            max = Vector3f(1f, 1f, 1f)
        )
    ) {}

    // TODO: Generate real atlas of all blocks
    val blocksAtlas: TextureAtlas = ModelTexture.builder()
        .setImage("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAAAAAA6mKC9AAAAZElEQVR42jWNsQ0AMQjEGJcB3HsEr/xSkncBOgkfUyXIguoo4mEXGdkFFvBdlGmpwKCAWIWOeYFladCK4n2BXZZn6vR3dsXRfLnKWfFoXmtUrX81AiboaZ9u4I1G8KdqPICl6AdyLn2NfcJFIAAAAABJRU5ErkJggg==")
        .build()
        .toAtlasTexture()

    private fun defaultModel() = Model.Companion.builder()
        .addCube(
            Cube.Companion.builder()
                .setFrom(0f, 0f, 0f)
                .setTo(1f, 1f, 1f)
                .addFace(Face.NORTH, defaultFace())
                .addFace(Face.SOUTH, defaultFace())
                .addFace(Face.WEST, defaultFace())
                .addFace(Face.EAST, defaultFace())
                .addFace(Face.UP, defaultFace())
                .addFace(Face.DOWN, defaultFace())
                .build()
        )
        .setTexture(
            ModelTexture.Companion.builder()
                .setImage("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAAAAAA6mKC9AAAAZElEQVR42jWNsQ0AMQjEGJcB3HsEr/xSkncBOgkfUyXIguoo4mEXGdkFFvBdlGmpwKCAWIWOeYFladCK4n2BXZZn6vR3dsXRfLnKWfFoXmtUrX81AiboaZ9u4I1G8KdqPICl6AdyLn2NfcJFIAAAAABJRU5ErkJggg==")
                .build()
        )
        .build()

    private fun defaultFace(): CubeFace {
        return CubeFace.Companion.builder()
            .setUvMin(0, 0)
            .setUvMax(16, 16)
            .build()
    }

}