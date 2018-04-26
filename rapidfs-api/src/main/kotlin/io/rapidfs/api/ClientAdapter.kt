package io.rapidfs.api

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import io.rapidfs.api.callback.CallbackManager
import io.rapidfs.shared.RapidPacket
import io.rapidfs.shared.RapidPacketCallback


object ClientAdapter : Listener() {

    private val callbackCatcher = CallbackManager()

    fun getCallbackCatcher(): CallbackManager {
        return callbackCatcher
    }

    override fun received(connection: Connection?, packet: Any?) {
        if (packet !is RapidPacketCallback)
            return
        println("Received callback: $packet")
        callbackCatcher.complete(packet)
    }
}