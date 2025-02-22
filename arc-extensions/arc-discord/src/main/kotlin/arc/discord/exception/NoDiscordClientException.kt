package arc.discord.exception

import arc.discord.IPCClient

/**
 * An exception thrown when an [IPCClient]
 * when the client cannot find the proper application to use for RichPresence when
 * attempting to [IPCClient.connect].
 *
 *
 *
 * This purely and always means the IPCClient in question (specifically the client ID)
 * is *invalid* and features using this library cannot be accessed using the instance.
 */
class NoDiscordClientException : Exception()