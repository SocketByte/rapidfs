package io.rapidfs.api.callback

import io.rapidfs.shared.RapidPacket
import io.rapidfs.shared.RapidPacketCallback
import java.util.LinkedHashMap


class CallbackManager {

    private val callbacks = LinkedHashMap<Long, CallbackHandler>()

    fun complete(packet: RapidPacketCallback) {
        if (!callbacks.containsKey(packet.callbackId))
            return
        callbacks[packet.callbackId]
                ?.completableFuture
                ?.complete(packet)
    }

    fun push(callbackId: Long, future: CallbackHandler) {
        callbacks[callbackId] = future
    }


}