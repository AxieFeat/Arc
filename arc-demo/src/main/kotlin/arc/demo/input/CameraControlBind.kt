package arc.demo.input

import arc.demo.VoxelGame
import arc.input.Binding
import arc.input.KeyCode

object CameraControlBind : Binding {

    override val id: String = "arc.demo.camera-control"
    override val key: KeyCode = KeyCode.KEY_E

    var status: Boolean = true
        private set(value) {
            field = value

            VoxelGame.application.renderSystem.scene.isShowCursor = !value
        }

    override fun onPress(key: KeyCode) {
        status = !status
    }

}