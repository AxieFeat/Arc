package arc.asset

import java.io.File

internal data class SimpleFileAsset(
    override val file: File
) : FileAsset {

    override val bytes: ByteArray
        get() = file.readBytes()

    override val text: String
        get() = file.readText()

    object Factory : FileAsset.Factory {

        override fun create(file: File): FileAsset {
            return SimpleFileAsset(file)
        }
    }
}
