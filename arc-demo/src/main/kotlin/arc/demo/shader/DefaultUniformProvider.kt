package arc.demo.shader

import arc.demo.VoxelGame
import arc.shader.AbstractUniformProvider
import org.joml.Matrix4f

object DefaultUniformProvider : AbstractUniformProvider() {

    init {
        addUniform("projectionMatrix") { shader, name ->
            VoxelGame.screen?.camera?.let {
                shader.setUniform(name, it.projection)
            }
        }

        addUniform("viewMatrix") { shader, name ->
            VoxelGame.screen?.camera?.let {
                shader.setUniform(name, it.view)
            }
        }

        addUniform("modelMatrix") { shader, name ->
            shader.setUniform(name, Matrix4f())
        }

        addUniform("Sampler") { shader, name ->
            shader.setUniform(name, 0)
        }
    }
}
