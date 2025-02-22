package arc.discord

import arc.discord.entity.*
import org.json.JSONException
import org.json.JSONObject
import arc.discord.entity.pipe.Pipe
import arc.discord.entity.pipe.PipeStatus
import arc.discord.exception.NoDiscordClientException
import java.io.Closeable
import java.io.IOException
import java.lang.management.ManagementFactory
import kotlin.concurrent.Volatile

/**
 * Represents a Discord IPC Client that can send and receive
 * Rich Presence data.
 *
 *
 *
 * The ID provided should be the **client ID of the particular
 * application providing Rich Presence**, which can be found
 * [here](https://discordapp.com/developers/applications/me).
 *
 *
 *
 * When initially created using [.IPCClient] the client will
 * be inactive awaiting a call to [.connect].<br></br>
 * After the call, this client can send and receive Rich Presence data
 * to and from discord via [.sendRichPresence] and
 * [.setListener] respectively.
 *
 *
 *
 * Please be mindful that the client created is initially unconnected,
 * and calling any methods that exchange data between this client and
 * Discord before a call to [.connect] will cause
 * an [IllegalStateException] to be thrown.<br></br>
 * This also means that the IPCClient cannot tell whether the client ID
 * provided is valid or not before a handshake.
 */
class IPCClient(
    private val clientId: Long
) : Closeable {
    
    private val callbacks: MutableMap<String, Callback> = mutableMapOf()

    @Volatile
    private var pipe: Pipe? = null
    private var listener: IPCListener? = null
    private var readThread: Thread? = null

    /**
     * Sets this IPCClient's [IPCListener] to handle received events.
     *
     *
     *
     * A single IPCClient can only have one of these set at any given time.<br></br>
     * Setting this `null` will remove the currently active one.
     *
     *
     *
     * This can be set safely before a call to [.connect]
     * is made.
     *
     * @param listener The [IPCListener] to set for this IPCClient.
     *
     * @see IPCListener
     */
    fun setListener(listener: IPCListener) {
        this.listener = listener
        pipe?.listener = listener
    }

    /**
     * Opens the connection between the IPCClient and Discord.
     *
     *
     *
     * **This must be called before any data is exchanged between the
     * IPCClient and Discord.**
     *
     * @param preferredOrder the priority order of client builds to connect to
     *
     * @throws IllegalStateException
     * There is an open connection on this IPCClient.
     * @throws NoDiscordClientException
     * No client of the provided [build type][DiscordBuild](s) was found.
     */
    @Throws(NoDiscordClientException::class)
    fun connect(vararg preferredOrder: DiscordBuild) {
        checkConnected(false)
        callbacks.clear()
        pipe = null

        pipe = Pipe.openPipe(this, clientId, callbacks, *preferredOrder)

        listener?.onReady(this)
        startReading()
    }

    /**
     * Sends a [RichPresence] to the Discord client.
     *
     *
     *
     * This is where the IPCClient will officially display
     * a Rich Presence in the Discord client.
     *
     *
     *
     * Sending this again will overwrite the last provided
     * [RichPresence].
     *
     * @param presence The [RichPresence] to send.
     * @param callback A [Callback] to handle success or error
     *
     * @throws IllegalStateException
     * If a connection was not made prior to invoking
     * this method.
     *
     * @see RichPresence
     */
    /**
     * Sends a [RichPresence] to the Discord client.
     *
     *
     *
     * This is where the IPCClient will officially display
     * a Rich Presence in the Discord client.
     *
     *
     *
     * Sending this again will overwrite the last provided
     * [RichPresence].
     *
     * @param presence The [RichPresence] to send.
     *
     * @throws IllegalStateException
     * If a connection was not made prior to invoking
     * this method.
     *
     * @see RichPresence
     */
    @JvmOverloads
    fun sendRichPresence(presence: RichPresence?, callback: Callback? = null) {
        checkConnected(true)

        pipe?.send(
            Packet.OpCode.FRAME, JSONObject()
                .put("cmd", "SET_ACTIVITY")
                .put(
                    "args", JSONObject()
                        .put("pid", pID)
                        .put("activity", presence?.toJson())
                ), callback
        )
    }

    /**
     * Adds an event [Event] to this IPCClient.<br></br>
     * If the provided [Event] is added more than once,
     * it does nothing.
     * Once added, there is no way to remove the subscription
     * other than [closing][.close] the connection
     * and creating a new one.
     *
     * @param sub The event [Event] to add.
     * @param callback The [Callback] to handle success or failure
     *
     * @throws IllegalStateException
     * If a connection was not made prior to invoking
     * this method.
     */
    /**
     * Adds an event [Event] to this IPCClient.<br></br>
     * If the provided [Event] is added more than once,
     * it does nothing.
     * Once added, there is no way to remove the subscription
     * other than [closing][.close] the connection
     * and creating a new one.
     *
     * @param sub The event [Event] to add.
     *
     * @throws IllegalStateException
     * If a connection was not made prior to invoking
     * this method.
     */
    @JvmOverloads
    fun subscribe(sub: Event, callback: Callback? = null) {
        checkConnected(true)
        check(sub.isSubscribable) { "Cannot subscribe to $sub event!" }

        pipe?.send(
            Packet.OpCode.FRAME, JSONObject()
                .put("cmd", "SUBSCRIBE")
                .put("evt", sub.name), callback
        )
    }

    val status: PipeStatus
        /**
         * Gets the IPCClient's current [PipeStatus].
         *
         * @return The IPCClient's current [PipeStatus].
         */
        get() {
            if (pipe == null) return PipeStatus.UNINITIALIZED

            return pipe!!.status
        }

    /**
     * Attempts to close an open connection to Discord.<br></br>
     * This can be reopened with another call to [.connect].
     *
     * @throws IllegalStateException
     * If a connection was not made prior to invoking
     * this method.
     */
    override fun close() {
        checkConnected(true)

        pipe?.close()
    }

    val discordBuild: DiscordBuild?
        /**
         * Gets the IPCClient's [DiscordBuild].
         *
         *
         *
         * This is always the first specified DiscordBuild when
         * making a call to [.connect],
         * or the first one found if none or [DiscordBuild.ANY]
         * is specified.
         *
         *
         *
         * Note that specifying ANY doesn't mean that this will return
         * ANY. In fact this method should **never** return the
         * value ANY.
         *
         * @return The [DiscordBuild] of this IPCClient, or null if not connected.
         */
        get() {
            if (pipe == null) return null

            return pipe!!.discordBuild
        }

    /**
     * Constants representing events that can be subscribed to
     * using [.subscribe].
     *
     *
     *
     * Each event corresponds to a different function as a
     * component of the Rich Presence.<br></br>
     * A full breakdown of each is available
     * [here](https://discordapp.com/developers/docs/rich-presence/how-to).
     */
    enum class Event
        (val isSubscribable: Boolean) {
        NULL(false),  // used for confirmation
        READY(false),
        ERROR(false),
        ACTIVITY_JOIN(true),
        ACTIVITY_SPECTATE(true),
        ACTIVITY_JOIN_REQUEST(true),

        /**
         * A backup key, only important if the
         * IPCClient receives an unknown event
         * type in a JSON payload.
         */
        UNKNOWN(false);

        companion object {
            fun of(str: String?): Event {
                if (str == null) return NULL
                for (s in entries) {
                    if (s != UNKNOWN && s.name.equals(str, ignoreCase = true)) return s
                }
                return UNKNOWN
            }
        }
    }


    // Private methods
    /**
     * Makes sure that the client is connected (or not) depending on if it should
     * for the current state.
     *
     * @param connected Whether to check in the context of the IPCClient being
     * connected or not.
     */
    private fun checkConnected(connected: Boolean) {
        check(!(connected && status != PipeStatus.CONNECTED)) {
            String.format(
                "IPCClient (ID: %d) is not connected!",
                clientId
            )
        }
        check(!(!connected && status == PipeStatus.CONNECTED)) {
            String.format(
                "IPCClient (ID: %d) is already connected!",
                clientId
            )
        }
    }

    /**
     * Initializes this IPCClient's [readThread][IPCClient.readThread]
     * and calls the first [Pipe.read].
     */
    private fun startReading() {
        readThread = Thread {
            try {
                var p: Packet
                while ((pipe!!.read().also { p = it }).op != Packet.OpCode.CLOSE) {
                    val json: JSONObject = p.json!!
                    val event = Event.of(json.optString("evt", null))
                    val nonce = json.optString("nonce", null)
                    when (event) {
                        Event.NULL -> if (nonce != null && callbacks.containsKey(nonce)) callbacks.remove(
                            nonce
                        )?.succeed(p)

                        Event.ERROR -> if (nonce != null && callbacks.containsKey(nonce)) callbacks.remove(
                            nonce
                        )?.fail(json.getJSONObject("data").optString("message", null))

                       else -> {}
                    }
                    if (listener != null && json.has("cmd") && json.getString("cmd") == "DISPATCH") {
                        try {
                            val data = json.getJSONObject("data")
                            when (Event.of(json.getString("evt"))) {
                                Event.ACTIVITY_JOIN -> listener?.onActivityJoin(
                                    this,
                                    data.getString("secret")
                                )

                                Event.ACTIVITY_SPECTATE -> listener?.onActivitySpectate(
                                    this,
                                    data.getString("secret")
                                )

                                Event.ACTIVITY_JOIN_REQUEST -> {
                                    val u = data.getJSONObject("user")
                                    val user =
                                        User(
                                            u.getString("username"),
                                            u.getString("discriminator"),
                                            u.getString("id").toLong(),
                                            u.optString("avatar", null)
                                        )
                                    listener?.onActivityJoinRequest(
                                        this,
                                        data.optString("secret", null),
                                        user
                                    )
                                }

                                else -> {}
                            }
                        } catch (ignore: Exception) {
                        }
                    }
                }
                pipe?.status = PipeStatus.DISCONNECTED
                listener?.onClose(this, p.json!!)
            } catch (ex: IOException) {
                pipe?.status = PipeStatus.DISCONNECTED
                listener?.onDisconnect(this, ex)
            } catch (ex: JSONException) {
                pipe?.status = PipeStatus.DISCONNECTED
                listener?.onDisconnect(this, ex)
            }
        }

        readThread!!.start()
    }

    companion object {

        // Private static methods
        private val pID: Int
            /**
             * Finds the current process ID.
             *
             * @return The current process ID.
             */
            get() {
                val pr = ManagementFactory.getRuntimeMXBean().name
                return pr.substring(0, pr.indexOf('@')).toInt()
            }
    }
}