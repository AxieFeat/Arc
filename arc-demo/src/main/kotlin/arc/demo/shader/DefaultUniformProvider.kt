package arc.demo.shader

import arc.demo.VoxelGame
import arc.shader.AbstractUniformProvider

object DefaultUniformProvider : AbstractUniformProvider() {

    init {
        addUniform("projectionMatrix") {
            it.setUniform("projectionMatrix", VoxelGame.application.renderSystem.scene.camera.projection)
        }

        addUniform("viewMatrix") {
            it.setUniform("viewMatrix", VoxelGame.application.renderSystem.scene.camera.view)
        }

        addUniform("Sampler") {
            it.setUniform("sampler", VoxelGame.application.renderSystem.texture.id)
        }
    }

}