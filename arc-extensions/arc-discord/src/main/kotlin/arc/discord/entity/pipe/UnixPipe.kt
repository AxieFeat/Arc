package arc.discord.entity.pipe

import org.json.JSONException
import org.json.JSONObject
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import arc.discord.IPCClient
import arc.discord.entity.Callback
import arc.discord.entity.Packet
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.file.Paths

class UnixPipe internal constructor(
    ipcClient: IPCClient,
    callbacks: MutableMap<String, Callback>,
    location: String
) : Pipe(ipcClient, callbacks) {
    private val socket: AFUNIXSocket = AFUNIXSocket.newInstance()

    init {
        socket.connect(AFUNIXSocketAddress.of(Paths.get(location)))
    }

    @Throws(IOException::class, JSONException::class)
    override fun read(): Packet {
        val `is`: InputStream = socket.inputStream

        while (`is`.available() == 0 && status == PipeStatus.CONNECTED) {
            try {
                Thread.sleep(50)
            } catch (ignored: InterruptedException) {
            }
        }

        /*byte[] buf = new byte[is.available()];
        is.read(buf, 0, buf.length);
        LOGGER.info(new String(buf));

        if (true) return null;*/
        if (status == PipeStatus.DISCONNECTED) throw IOException("Disconnected!")

        if (status == PipeStatus.CLOSED) return Packet(
            Packet.OpCode.CLOSE,
            null
        )

        // Read the op and length. Both are signed ints
        var d = ByteArray(8)
        `is`.read(d)
        val bb = ByteBuffer.wrap(d)

        val op: Packet.OpCode =
            Packet.OpCode.entries.toTypedArray()[Integer.reverseBytes(bb.getInt())]
        d = ByteArray(Integer.reverseBytes(bb.getInt()))

        `is`.read(d)
        val p: Packet = Packet(
            op, JSONObject(
                String(d)
            )
        )
        listener?.onPacketReceived(ipcClient, p)
        return p
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray) {
        socket.outputStream.write(b)
    }

    @Throws(IOException::class)
    override fun close() {
        send(Packet.OpCode.CLOSE, JSONObject(), null)
        status = PipeStatus.CLOSED
        socket.close()
    }
}