package arc.demo.shader

import arc.demo.VoxelGame
import arc.shader.AbstractUniformProvider

object DefaultUniformProvider : AbstractUniformProvider() {

    init {
        VoxelGame.screen?.camera?.let {
            addUniform("projectionMatrix") { shader, name ->
                shader.setUniform(name, it.projection)
            }
        }

        VoxelGame.screen?.camera?.let {
            addUniform("viewMatrix") { shader, name ->
                shader.setUniform(name, it.view)
            }
        }

        addUniform("Sampler") { shader, name ->
            shader.setUniform(name, VoxelGame.application.renderContext.texture.id)
        }
    }
}
