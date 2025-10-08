package arc.gles.window

object MacEGLBridge {

    init {
        System.loadLibrary("MacEGLBridge")
    }

    @JvmStatic
    external fun createMetalLayer(nsWindowPtr: Long): Long
}