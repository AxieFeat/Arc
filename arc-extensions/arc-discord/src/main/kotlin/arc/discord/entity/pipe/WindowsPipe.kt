package arc.discord.entity.pipe

import org.json.JSONException
import org.json.JSONObject
import arc.discord.IPCClient
import arc.discord.entity.Callback
import arc.discord.entity.Packet
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile

class WindowsPipe internal constructor(
    ipcClient: IPCClient,
    callbacks: MutableMap<String, Callback>,
    location: String?
) : Pipe(ipcClient, callbacks) {
    private var file: RandomAccessFile? = null

    init {
        try {
            this.file = RandomAccessFile(location, "rw")
        } catch (e: FileNotFoundException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray) {
        file!!.write(b)
    }

    @Throws(IOException::class, JSONException::class)
    override fun read(): Packet {
        while (file!!.length() == 0L && status == PipeStatus.CONNECTED) {
            try {
                Thread.sleep(50)
            } catch (ignored: InterruptedException) {
            }
        }

        if (status == PipeStatus.DISCONNECTED) throw IOException("Disconnected!")

        if (status == PipeStatus.CLOSED) return Packet(
            Packet.OpCode.CLOSE,
            null
        )

        val op: Packet.OpCode = Packet.OpCode.entries.toTypedArray()[Integer.reverseBytes(
            file!!.readInt()
        )]
        val len = Integer.reverseBytes(file!!.readInt())
        val d = ByteArray(len)

        file!!.readFully(d)
        val p: Packet = Packet(
            op, JSONObject(
                String(d)
            )
        )
        listener?.onPacketReceived(ipcClient, p)
        return p
    }

    @Throws(IOException::class)
    override fun close() {
        send(Packet.OpCode.CLOSE, JSONObject(), null)
        status = PipeStatus.CLOSED
        file!!.close()
    }
}