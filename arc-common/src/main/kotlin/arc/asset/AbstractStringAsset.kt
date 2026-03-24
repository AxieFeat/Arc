package arc.asset

abstract class AbstractStringAsset : StringAsset {

    override val length: Int
        get() = text.length

    override fun get(index: Int): Char = text[index]

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = text.subSequence(startIndex, endIndex)
}
