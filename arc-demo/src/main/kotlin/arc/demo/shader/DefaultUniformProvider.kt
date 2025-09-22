package arc.demo.shader

import arc.demo.VoxelGame
import arc.shader.AbstractUniformProvider

object DefaultUniformProvider : AbstractUniformProvider() {

    init {
        addUniform("projectionMatrix") { shader, name ->
            shader.setUniform(name, VoxelGame.application.renderSystem.scene.camera.projection)
        }

        addUniform("viewMatrix") { shader, name ->
            shader.setUniform(name, VoxelGame.application.renderSystem.scene.camera.view)
        }

        addUniform("Sampler") { shader, name ->
            shader.setUniform(name, VoxelGame.application.renderSystem.texture.id)
        }
    }

}
