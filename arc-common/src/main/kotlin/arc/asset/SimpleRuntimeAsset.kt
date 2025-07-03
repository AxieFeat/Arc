package arc.asset

internal data class SimpleRuntimeAsset(
    override val bytes: ByteArray
) : RuntimeAsset {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleRuntimeAsset

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

    override val text: String = bytes.decodeToString()

    object Factory : RuntimeAsset.Factory {
        override fun create(bytes: ByteArray): RuntimeAsset {
            return SimpleRuntimeAsset(bytes)
        }
    }

}