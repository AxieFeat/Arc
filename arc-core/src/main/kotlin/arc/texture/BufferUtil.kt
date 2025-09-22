package arc.texture

import java.nio.ByteBuffer

internal object BufferUtil {

    /**
     * Create direct buffer from byte array.
     */
    @JvmStatic
    fun ByteArray.createDirectByteBuffer(): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(this.size)
        buffer.put(this)
        buffer.flip()
        return buffer
    }
}
