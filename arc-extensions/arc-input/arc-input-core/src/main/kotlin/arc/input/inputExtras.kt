@file:JvmSynthetic

package arc.input

import arc.Application
import arc.input.keyboard.KeyboardInput
import arc.input.mouse.MouseInput

@JvmSynthetic
private val engine = InputEngine.find()

/**
 * Get mouse input in current input engine.
 */
@get:JvmSynthetic
val Application.mouse: MouseInput
    get() = engine.mouse

/**
 * Get keyboard input in current input engine.
 */
@get:JvmSynthetic
val Application.keyboard: KeyboardInput
    get() = engine.keyboard