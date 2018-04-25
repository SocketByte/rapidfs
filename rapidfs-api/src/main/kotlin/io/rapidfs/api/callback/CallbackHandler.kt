package io.rapidfs.api.callback

import io.rapidfs.api.ClientAdapter
import java.util.concurrent.ExecutionException
import io.rapidfs.api.RapidClient
import io.rapidfs.shared.RapidPacket
import io.rapidfs.shared.RapidPacketCallback
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors


class CallbackHandler private constructor(private val connection: RapidClient) : Runnable {

    val completableFuture = CompletableFuture<RapidPacketCallback>()

    var packet: RapidPacket? = null
    var callbackAction: Callback? = null
    var callbackId: Long = 0

    fun sendAndPush() {
        connection.client.sendTCP(packet)

        ClientAdapter
                .getCallbackCatcher()
                .push(callbackId, this)
    }

    override fun run() {
        sendAndPush()
        if (callbackAction != null)
            callback(callbackAction!!)
    }

    fun get(): RapidPacketCallback? {
        return completableFuture.get()
    }

    private fun callback(callbackAction: Callback) {
        val callback: RapidPacket?

        callback = completableFuture.get()

        callbackAction.execute(callback)
    }

    companion object {
        private val executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors())!!

        fun make(connection: RapidClient, packet: RapidPacket, callbackAction: Callback): CallbackHandler {
            val callbackHandler = make(connection, packet)
            callbackHandler.callbackAction = callbackAction

            executorService.submit(callbackHandler)
            return callbackHandler
        }

        fun make(connection: RapidClient, packet: RapidPacket): CallbackHandler {
            val callbackHandler = CallbackHandler(connection)
            callbackHandler.callbackAction = null
            callbackHandler.callbackId = packet.callbackId
            callbackHandler.packet = packet
            return callbackHandler
        }
    }

}