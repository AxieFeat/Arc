package arc.font

import net.kyori.adventure.text.Component
import org.joml.Matrix4f

/**
 * This interface represents wrapped Kyori component.
 */
interface WrappedComponent{

    /**
     * Wrapping component.
     */
    val component: Component

    /**
     * Matrix for transformation text in 3d space.
     */
    val matrix: Matrix4f

    /**
     * Size of text.
     */
    val size: Int

    /**
     * Width of this text in pixels.
     */
    val width: Int

    /**
     * Height of this text in pixels.
     */
    val height: Int

}