package arc.gl.graphics

import org.lwjgl.opengl.GL41
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

object GLAllocation {

    @Synchronized
    fun generateDisplayLists(range: Int): Int {
        return GL41.glGenLists(range)
    }

    @Synchronized
    fun deleteDisplayLists(list: Int, range: Int) {
        GL41.glDeleteLists(list, range)
    }

    @Synchronized
    fun deleteDisplayLists(list: Int) {
        GL41.glDeleteLists(list, 1)
    }


    @Synchronized
    fun createDirectByteBuffer(capacity: Int): ByteBuffer {
        return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder())
    }

    fun createDirectIntBuffer(capacity: Int): IntBuffer {
        return createDirectByteBuffer(capacity shl 2).asIntBuffer()
    }

    fun createDirectFloatBuffer(capacity: Int): FloatBuffer {
        return createDirectByteBuffer(capacity shl 2).asFloatBuffer()
    }
}