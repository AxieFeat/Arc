package arc.discord.entity

import arc.discord.IPCClient

/**
 * Constants representing various Discord client builds,
 * such as Stable, Canary, Public Test Build (PTB)
 */
enum class DiscordBuild(private val endpoint: String? = null) {

    /**
     * Constant for the current Discord Canary release.
     */
    CANARY("//canary.discordapp.com/api"),

    /**
     * Constant for the current Discord Public Test Build or PTB release.
     */
    PTB("//ptb.discordapp.com/api"),

    /**
     * Constant for the current stable Discord release.
     */
    STABLE("//discordapp.com/api"),

    /**
     * 'Wildcard' build constant used in [IPCClient.connect] to signify that the build to target is not important, and
     * that the first valid build will be used.
     *
     *
     *
     * Other than this exact function, there is no use for this value.
     */
    ANY;

    companion object {
        /**
         * Gets a [DiscordBuild] matching the specified endpoint.
         *
         *
         *
         * This is only internally implemented.
         *
         * @param endpoint The endpoint to get from.
         *
         * @return The DiscordBuild corresponding to the endpoint, or
         * [DiscordBuild.ANY] if none match.
         */
        fun from(endpoint: String): DiscordBuild {
            for (value in entries) {
                if (value.endpoint != null && value.endpoint == endpoint) {
                    return value
                }
            }
            return ANY
        }
    }
}