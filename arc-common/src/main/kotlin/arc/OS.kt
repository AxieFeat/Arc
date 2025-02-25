package arc

internal object OS {

    @JvmStatic
    fun execSafe(vararg command: String): Boolean {
        try {
            Runtime.getRuntime().exec(command)
            return true
        } catch (t: Throwable) {
            return false
        }
    }

}