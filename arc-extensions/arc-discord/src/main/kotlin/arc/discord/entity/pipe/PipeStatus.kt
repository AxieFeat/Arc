package arc.discord.entity.pipe

import arc.discord.entity.Packet

/**
 * Constants representing various status that an [IPCClient] can have.
 */
enum class PipeStatus {
    /**
     * Status for when the IPCClient when no attempt to connect has been made.
     *
     *
     *
     * All IPCClients are created starting with this status,
     * and it never returns for the lifespan of the client.
     */
    UNINITIALIZED,

    /**
     * Status for when the Pipe is attempting to connect.
     *
     *
     *
     * This will become set whenever the #connect() method is called.
     */
    CONNECTING,

    /**
     * Status for when the Pipe is connected with Discord.
     *
     *
     *
     * This is only present when the connection is healthy, stable,
     * and reading good data without exception.<br></br>
     * If the environment becomes out of line with these principles
     * in any way, the IPCClient in question will become
     * [PipeStatus.DISCONNECTED].
     */
    CONNECTED,

    /**
     * Status for when the Pipe has received an [Packet.OpCode.CLOSE].
     *
     *
     *
     * This signifies that the reading thread has safely and normally shut
     * and the client is now inactive.
     */
    CLOSED,

    /**
     * Status for when the Pipe has unexpectedly disconnected, either because
     * of an exception, and/or due to bad data.
     *
     *
     *
     * When the status of an Pipe becomes this, a call to
     * [IPCListener.onDisconnect] will be made if one
     * has been provided to the IPCClient.
     *
     *
     *
     * Note that the IPCClient will be inactive with this status, after which a
     * call to [IPCClient.connect] can be made to "reconnect" the
     * IPCClient.
     */
    DISCONNECTED
}