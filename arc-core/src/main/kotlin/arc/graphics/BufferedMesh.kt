package arc.graphics

/**
 * This interface represents a mesh that does not render instantly,
 * but stores everything in a buffer and only renders at the end of the frame.
 */
interface BufferedMesh : Mesh