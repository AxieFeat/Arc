package arc.input

import arc.Arc
import arc.annotations.TypeFactory
import arc.input.keyboard.KeyboardInput
import arc.input.mouse.MouseInput
import org.jetbrains.annotations.ApiStatus

/**
 * This interface represents input engine.
 */
interface InputEngine {

    /**
     * Mouse input in this engine.
     */
    val mouse: MouseInput

    /**
     * Keyboard input in this engine.
     */
    val keyboard: KeyboardInput

    @ApiStatus.Internal
    @TypeFactory
    interface Factory {

        fun create(): InputEngine

    }

    companion object {

        /**
         * Find specific implementation of [InputEngine] in current context.
         *
         * @return Instance of [InputEngine].
         */
        @JvmStatic
        fun find(): InputEngine {
            return Arc.factory<Factory>().create()
        }

    }

}