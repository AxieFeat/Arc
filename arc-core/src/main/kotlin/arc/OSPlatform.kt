package arc

/**
 * Enum representing different operating system platforms.
 */
enum class OSPlatform(val id: String) {

    WINDOWS("windows"),
    LINUX("linux"),
    MACOS("macos"),
    ANDROID("android"),

    UNKNOWN("unknown");

    companion object {

        /**
         * Get platform from id string value.
         *
         * @param id ID of a platform.
         *
         * @return Platform from string. Can return [UNKNOWN] if not found.
         */
        @JvmStatic
        fun fromString(id: String): OSPlatform {
            return entries.find { it.id == id } ?: UNKNOWN
        }

    }
}