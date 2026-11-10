package arc.buffer

import com.ditchoom.buffer.BaseJvmBuffer
import com.ditchoom.buffer.PlatformBuffer
import io.github.oshai.kotlinlogging.KotlinLogging
import java.nio.ByteBuffer

private val logger = KotlinLogging.logger {}

/**
 * Returns a view of the remaining bytes without changing the caller's position.
 */
fun PlatformBuffer.nioView(): ByteBuffer {
    if (this is BaseJvmBuffer) return byteBuffer.duplicate()

    logger.debug { "Copying PlatformBuffer to ByteBuffer: ${this::class}" }

    val originalPosition = position()
    return try {
        ByteBuffer.wrap(readByteArray(remaining()))
    } finally {
        position(originalPosition)
    }
}
