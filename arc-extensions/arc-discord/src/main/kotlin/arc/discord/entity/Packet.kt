package arc.discord.entity

import arc.discord.IPCClient
import arc.discord.IPCListener
import arc.discord.entity.Packet.OpCode
import org.json.JSONObject
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

/**
 * A data-packet received from Discord via an [IPCClient].<br></br>
 * These can be handled via an implementation of [IPCListener].
 */
class Packet(
    /**
     * Gets the [OpCode] value of this [Packet].
     *
     * @return This Packet's OpCode.
     */
    val op: OpCode,

    /**
     * Gets the [JSONObject] value as a part of this [Packet].
     *
     * @return The JSONObject value of this Packet.
     */
    val json: JSONObject?
) {

    /**
     * Converts this [Packet] to a `byte` array.
     *
     * @return This Packet as a `byte` array.
     */
    fun toBytes(): ByteArray {
        val d = json.toString().toByteArray(StandardCharsets.UTF_8)
        val packet = ByteBuffer.allocate(d.size + 2 * Integer.BYTES)
        packet.putInt(Integer.reverseBytes(op.ordinal))
        packet.putInt(Integer.reverseBytes(d.size))
        packet.put(d)
        return packet.array()
    }

    override fun toString(): String {
        return "Pkt:$op$json"
    }

    /**
     * Discord response OpCode values that are
     * sent with response data to and from Discord
     * and the [IPCClient]
     * connected.
     */
    enum class OpCode {
        HANDSHAKE, FRAME, CLOSE, PING, PONG
    }
}