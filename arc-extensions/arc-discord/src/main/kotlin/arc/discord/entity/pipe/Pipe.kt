package arc.discord.entity.pipe

import org.json.JSONException
import org.json.JSONObject
import arc.discord.IPCClient
import arc.discord.IPCListener
import arc.discord.entity.Callback
import arc.discord.entity.DiscordBuild
import arc.discord.entity.Packet
import arc.discord.exception.NoDiscordClientException
import java.io.IOException
import java.util.*

abstract class Pipe internal constructor(
    val ipcClient: IPCClient,
    private val callbacks: MutableMap<String, Callback>
) {
    var status: PipeStatus = PipeStatus.CONNECTING
    var listener: IPCListener? = null
    private var build: DiscordBuild? = null

    /**
     * Sends json with the given [Packet.OpCode].
     *
     * @param op The [Packet.OpCode] to send data with.
     * @param data The data to send.
     * @param callback callback for the response
     */
    fun send(
        op: Packet.OpCode,
        data: JSONObject,
        callback: Callback?
    ) {
        try {
            val nonce = generateNonce()
            val p = Packet(op, data.put("nonce", nonce))
            if (callback != null && !callback.isEmpty) callbacks[nonce] = callback
            write(p.toBytes())
            listener?.onPacketSent(ipcClient, p)
        } catch (ex: IOException) {
            status = PipeStatus.DISCONNECTED
        }
    }

    /**
     * Blocks until reading a [Packet] or until the
     * read thread encounters bad data.
     *
     * @return A valid [Packet].
     *
     * @throws IOException
     * If the pipe breaks.
     * @throws JSONException
     * If the read thread receives bad data.
     */
    @Throws(IOException::class, JSONException::class)
    abstract fun read(): Packet

    @Throws(IOException::class)
    abstract fun write(b: ByteArray)

    @Throws(IOException::class)
    abstract fun close()

    val discordBuild: DiscordBuild?
        get() = build

    companion object {
        private const val VERSION = 1

        @Throws(NoDiscordClientException::class)
        fun openPipe(
            ipcClient: IPCClient,
            clientId: Long,
            callbacks: MutableMap<String, Callback>,
            vararg preferredOrder: DiscordBuild?
        ): Pipe {
            var preferredOrder: Array<out DiscordBuild?> = preferredOrder
            if (preferredOrder.isEmpty()) preferredOrder =
                arrayOf<DiscordBuild?>(DiscordBuild.ANY)

            var pipe: Pipe? = null

            // store some files so we can get the preferred client
            val open = arrayOfNulls<Pipe>(DiscordBuild.entries.size)
            for (i in 0..9) {
                try {
                    val location = getPipeLocation(i)
                    pipe = createPipe(ipcClient, callbacks, location)

                    pipe.send(
                        Packet.OpCode.HANDSHAKE,
                        JSONObject().put("v", VERSION).put("client_id", clientId.toString()),
                        null
                    )

                    val p: Packet = pipe.read() // this is a valid client at this point

                    pipe.build = DiscordBuild.from(
                        p.json!!.getJSONObject("data")
                            .getJSONObject("config")
                            .getString("api_endpoint")
                    )

                    // we're done if we found our first choice
                    if (pipe.build == preferredOrder[0] || DiscordBuild.ANY == preferredOrder[0]) {
                        break
                    }

                    open[pipe.build!!.ordinal] = pipe // didn't find first choice yet, so store what we have
                    open[DiscordBuild.ANY.ordinal] =
                        pipe // also store in 'any' for use later

                    pipe.build = null
                    pipe = null
                } catch (ex: IOException) {
                    pipe = null
                } catch (ex: JSONException) {
                    pipe = null
                }
            }

            if (pipe == null) {
                // we already know we don't have our first pick
                // check each of the rest to see if we have that
                for (i in 1 until preferredOrder.size) {
                    val cb: DiscordBuild = preferredOrder[i]!!
                    if (open[cb.ordinal] != null) {
                        pipe = open[cb.ordinal]
                        open[cb.ordinal] = null
                        if (cb == DiscordBuild.ANY)  // if we pulled this from the 'any' slot, we need to figure out which build it was
                        {
                            for (k in open.indices) {
                                if (open[k] === pipe) {
                                    pipe!!.build = DiscordBuild.entries.get(k)
                                    open[k] = null // we don't want to close this
                                }
                            }
                        } else pipe!!.build = cb

                        break
                    }
                }
                if (pipe == null) {
                    throw NoDiscordClientException()
                }
            }
            // close unused files, except skip 'any' because its always a duplicate
            for (i in open.indices) {
                if (i == DiscordBuild.ANY.ordinal) continue
                if (open[i] != null) {
                    try {
                        open[i]!!.close()
                    } catch (ignore: IOException) {
                    }
                }
            }

            pipe.status = PipeStatus.CONNECTED

            return pipe
        }

        private fun createPipe(
            ipcClient: IPCClient,
            callbacks: MutableMap<String, Callback>,
            location: String
        ): Pipe {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())

            return if (osName.contains("win")) {
                WindowsPipe(ipcClient, callbacks, location)
            } else if (osName.contains("linux") || osName.contains("mac")) {
                try {
                    UnixPipe(ipcClient, callbacks, location)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            } else {
                throw RuntimeException("Unsupported OS: $osName")
            }
        }

        /**
         * Generates a nonce.
         *
         * @return A random [UUID].
         */
        private fun generateNonce(): String {
            return UUID.randomUUID().toString()
        }

        // a list of system property keys to get IPC file from different unix systems.
        private val unixPaths = arrayOf("XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP")

        /**
         * Finds the IPC location in the current system.
         *
         * @param i Index to try getting the IPC at.
         *
         * @return The IPC location.
         */
        private fun getPipeLocation(i: Int): String {
            if (System.getProperty("os.name").contains("Win")) return "\\\\?\\pipe\\discord-ipc-$i"
            var tmppath: String? = null
            for (str in unixPaths) {
                tmppath = System.getenv(str)
                if (tmppath != null) break
            }
            if (tmppath == null) tmppath = "/tmp"
            return "$tmppath/discord-ipc-$i"
        }
    }
}