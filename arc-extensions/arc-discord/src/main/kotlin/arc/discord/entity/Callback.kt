package arc.discord.entity

import arc.discord.IPCClient
import java.util.function.Consumer

/**
 * A callback for asynchronous logic when dealing processes that
 * would normally block the calling thread.
 *
 *
 *
 * This is most visibly implemented in [IPCClient].
 */
class Callback @JvmOverloads constructor(
    private val success: Consumer<Packet>? = null,
    private val failure: Consumer<String>? = null
) {

    /**
     * Constructs a Callback with a success [Consumer] *and*
     * a failure [Consumer] that occurs when the process it is
     * attached to executes without or with error (respectively).
     *
     * @param success The Consumer to launch after a successful process.
     * @param failure The Consumer to launch if the process has an error.
     */
    /**
     * Constructs an empty Callback.
     */

    /**
     * @param success The Runnable to launch after a successful process.
     * @param failure The Consumer to launch if the process has an error.
     */
    @Deprecated("")
    constructor(
        success: Runnable,
        failure: Consumer<String>?
    ) : this(
        Consumer<Packet> { p: Packet? -> success.run() },
        failure
    )

    /**
     * @param success The Runnable to launch after a successful process.
     */
    @Deprecated("")
    constructor(success: Runnable) : this(
        Consumer<Packet> { p: Packet? -> success.run() },
        null
    )

    val isEmpty: Boolean
        /**
         * Gets whether or not this Callback is "empty" which is more precisely
         * defined as not having a specified success [Consumer] and/or a
         * failure [Consumer].<br></br>
         * This is only true if the Callback is constructed with the parameter-less
         * constructor ([.Callback]) or another constructor that leaves
         * one or both parameters `null`.
         *
         * @return `true` if and only if the
         */
        get() = success == null && failure == null

    /**
     * Launches the success [Consumer].
     */
    fun succeed(packet: Packet) {
        success?.accept(packet)
    }

    /**
     * Launches the failure [Consumer] with the
     * provided message.
     *
     * @param message The message to launch the failure consumer with.
     */
    fun fail(message: String) {
        failure?.accept(message)
    }
}