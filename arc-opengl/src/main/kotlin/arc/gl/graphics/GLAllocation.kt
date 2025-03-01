package arc.gl.graphics

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GLUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

object GLAllocation {
    /**
     * Generates the specified number of display lists and returns the first index.
     */
    @Synchronized
    fun generateDisplayLists(range: Int): Int {
        val i = GL11.glGenLists(range)

        if (i == 0) {
            val j = GL11.glGetError()
            var s = "No error code reported"

            if (j != 0) {
                s = GLU.gluErrorString(j)
            }

            throw IllegalStateException("glGenLists returned an ID of 0 for a count of $range, GL error ($j): $s")
        } else {
            return i
        }
    }

    @Synchronized
    fun deleteDisplayLists(list: Int, range: Int) {
        GL11.glDeleteLists(list, range)
    }

    @Synchronized
    fun deleteDisplayLists(list: Int) {
        GL11.glDeleteLists(list, 1)
    }

    /**
     * Creates and returns a direct byte buffer with the specified capacity. Applies native ordering to speed up access.
     */
    @Synchronized
    fun createDirectByteBuffer(capacity: Int): ByteBuffer {
        return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder())
    }

    /**
     * Creates and returns a direct int buffer with the specified capacity. Applies native ordering to speed up access.
     */
    fun createDirectIntBuffer(capacity: Int): IntBuffer {
        return createDirectByteBuffer(capacity shl 2).asIntBuffer()
    }

    /**
     * Creates and returns a direct float buffer with the specified capacity. Applies native ordering to speed up
     * access.
     */
    fun createDirectFloatBuffer(capacity: Int): FloatBuffer {
        return createDirectByteBuffer(capacity shl 2).asFloatBuffer()
    }
}