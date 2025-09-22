package arc.asset

internal data class SimpleRuntimeAsset(
    override val bytes: ByteArray
) : RuntimeAsset {

    override val text: String = bytes.decodeToString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleRuntimeAsset

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

    object Factory : RuntimeAsset.Factory {

        override fun create(bytes: ByteArray): RuntimeAsset {
            return SimpleRuntimeAsset(bytes)
        }
    }
}
