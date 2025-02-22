package arc.discord

import org.json.JSONObject
import arc.discord.entity.Packet
import arc.discord.entity.User

/**
 * An implementable listener used to handle events caught by an [IPCClient].
 *
 * Can be attached to an IPCClient using [IPCClient.setListener].
 */
interface IPCListener {
    /**
     * Fired whenever an [IPCClient] sends a [Packet] to Discord.
     *
     * @param client The IPCClient sending the Packet.
     * @param packet The Packet being sent.
     */
    fun onPacketSent(client: IPCClient, packet: Packet) {}

    /**
     * Fired whenever an [IPCClient] receives a [Packet] to Discord.
     *
     * @param client The IPCClient receiving the Packet.
     * @param packet The Packet being received.
     */
    fun onPacketReceived(client: IPCClient, packet: Packet) {}

    /**
     * Fired whenever a RichPresence activity informs us that
     * a user has clicked a "join" button.
     *
     * @param client The IPCClient receiving the event.
     * @param secret The secret of the event, determined by the implementation and specified by the user.
     */
    fun onActivityJoin(client: IPCClient, secret: String) {}

    /**
     * Fired whenever a RichPresence activity informs us that
     * a user has clicked a "spectate" button.
     *
     * @param client The IPCClient receiving the event.
     * @param secret The secret of the event, determined by the implementation and specified by the user.
     */
    fun onActivitySpectate(client: IPCClient, secret: String) {}

    /**
     * Fired whenever a RichPresence activity informs us that
     * a user has clicked a "ask to join" button.
     *
     *
     *
     * As opposed to [.onActivityJoin],
     * this also provides packaged [User] data.
     *
     * @param client The IPCClient receiving the event.
     * @param secret The secret of the event, determined by the implementation and specified by the user.
     * @param user The user who clicked the clicked the event, containing data on the account.
     */
    fun onActivityJoinRequest(client: IPCClient, secret: String, user: User) {}

    /**
     * Fired whenever an [IPCClient] is ready and connected to Discord.
     *
     * @param client The now ready IPCClient.
     */
    fun onReady(client: IPCClient) {}

    /**
     * Fired whenever an [IPCClient] has closed.
     *
     * @param client The now closed IPCClient.
     * @param json A [JSONObject] with close data.
     */
    fun onClose(client: IPCClient, json: JSONObject) {}

    /**
     * Fired whenever an [IPCClient] has disconnected,
     * either due to bad data or an exception.
     *
     * @param client The now closed IPCClient.
     * @param t A [Throwable] responsible for the disconnection.
     */
    fun onDisconnect(client: IPCClient, t: Throwable) {}
}