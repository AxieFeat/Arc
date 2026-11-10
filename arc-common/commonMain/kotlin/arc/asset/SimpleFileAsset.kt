package arc.asset

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.io.readString

internal data class SimpleFileAsset(
    override val file: Path
) : AbstractStringAsset(), FileAsset {

    override val bytes: ByteArray
        get() = SystemFileSystem.source(file).buffered().use { it.readByteArray() }

    override val text: String
        get() = SystemFileSystem.source(file).buffered().use { it.readString() }

    object Factory : FileAsset.Factory {

        override fun create(file: Path): FileAsset {
            return SimpleFileAsset(file)
        }
    }
}
