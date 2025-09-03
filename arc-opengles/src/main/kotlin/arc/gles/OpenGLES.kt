package arc.gles

/**
 * This object represents OpenGL implementation of engine.
 */
object OpenGLES {

    /**
     * Preload OpenGL implementations.
     */
    @JvmStatic
    fun preload() {
        GlesFactoryProvider.bootstrap()
    }

}