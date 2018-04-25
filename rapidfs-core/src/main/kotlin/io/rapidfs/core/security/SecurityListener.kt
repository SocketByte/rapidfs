package io.rapidfs.core.security

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import io.rapidfs.core.RapidFS
import io.rapidfs.shared.RapidPacketAuth

object SecurityListener : Listener() {
    override fun received(connection: Connection?, packet: Any?) {
        if (packet !is RapidPacketAuth)
            return

        RapidFS.securityProvider.authorize(connection
                ?: throw NullPointerException("connection can not be null"))
    }
}