package arc.asset

internal data class ArcRuntimeAsset(
    override val bytes: ByteArray
) : RuntimeAsset {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArcRuntimeAsset

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

    override val text: String
        get() = bytes.decodeToString()

    object Factory : RuntimeAsset.Factory {
        override fun create(bytes: ByteArray): RuntimeAsset {
            return ArcRuntimeAsset(bytes)
        }
    }

}