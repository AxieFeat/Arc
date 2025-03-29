package arc

/**
 * Enum representing different operating system platforms.
 */
enum class OSPlatform(name: String) {
    WINDOWS("windows"),
    LINUX("linux"),
    MACOS("macos"),
    ANDROID("android"),

    UNKNOWN("unknown");

    companion object {

        /**
         * Get platform from id string value.
         *
         * @param name Name of platform.
         *
         * @return Platform from string. Can return [UNKNOWN] if not found.
         */
        fun fromString(name: String): OSPlatform {
            return entries.find { it.name == name } ?: UNKNOWN
        }

    }
}