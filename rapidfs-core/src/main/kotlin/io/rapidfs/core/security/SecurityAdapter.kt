package io.rapidfs.core.security

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.minlog.Log.info
import com.esotericsoftware.minlog.Log.warn
import io.rapidfs.core.RapidFS
import io.rapidfs.core.security.SecurityProvider
import io.rapidfs.shared.RapidPacketCallback
import io.rapidfs.shared.RapidResult

object SecurityAdapter : Listener() {

    override fun connected(p0: Connection?) {
        info("Connected: ${p0?.remoteAddressTCP} ID: ${p0?.id}")
    }

    override fun disconnected(p0: Connection?) {
        info("Disconnected: ${p0?.remoteAddressTCP} ID: ${p0?.id}")
    }

    override fun received(connection: Connection?, packet: Any?) {
        if (connection == null || packet == null)
            return

        if (RapidFS.properties.containsKey("password")
                || RapidFS.properties.getProperty("password").isNullOrEmpty())
            return

        if (RapidFS.securityProvider.notAuthorized(packet, connection)) {
            connection.sendTCP(RapidPacketCallback(RapidResult.ERROR, "Insufficient permission"))
            connection.close()
        }
    }
}