package arc.gl

/**
 * This object represents OpenGL implementation of engine.
 */
object OpenGL {

    /**
     * Preload OpenGL implementations.
     */
    @JvmStatic
    fun preload() {
        GlFactoryProvider.bootstrap()
    }

}