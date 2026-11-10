package arc.texture

import com.ditchoom.buffer.BufferFactory
import com.ditchoom.buffer.Default
import com.ditchoom.buffer.PlatformBuffer

internal object BufferUtil {
    fun ByteArray.createDirectBuffer(): PlatformBuffer {
        val buffer = BufferFactory.Default.allocate(size)
        buffer.writeBytes(this)
        buffer.resetForRead()
        return buffer
    }
}
