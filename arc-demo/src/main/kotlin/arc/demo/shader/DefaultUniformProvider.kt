package arc.demo.shader

import arc.demo.Game
import arc.shader.AbstractUniformProvider

object DefaultUniformProvider : AbstractUniformProvider() {

    init {
        addUniform("projectionMatrix") {
            it.setUniform("projectionMatrix", Game.application.renderSystem.scene.camera.projection)
        }

        addUniform("viewMatrix") {
            it.setUniform("viewMatrix", Game.application.renderSystem.scene.camera.view)
        }

        addUniform("Sampler") {
            it.setUniform("sampler", Game.application.renderSystem.texture.id)
        }
    }

}