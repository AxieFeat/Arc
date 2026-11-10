package arc.util

internal fun checkedProduct(left: Int, right: Int): Int {
    val result = left.toLong() * right.toLong()
    if (result !in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()) throw ArithmeticException("integer overflow")
    return result.toInt()
}
