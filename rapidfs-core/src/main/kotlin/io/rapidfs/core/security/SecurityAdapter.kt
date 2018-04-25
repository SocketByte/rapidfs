package io.rapidfs.core.security

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.minlog.Log.warn
import io.rapidfs.core.RapidFS
import io.rapidfs.core.security.SecurityProvider
import io.rapidfs.shared.RapidPacketCallback
import io.rapidfs.shared.RapidResult

object SecurityAdapter : Listener() {
    override fun received(connection: Connection?, packet: Any?) {
        if (connection == null || packet == null)
            return

        if (RapidFS.securityProvider.notAuthorized(packet, connection)) {
            connection.sendTCP(RapidPacketCallback(RapidResult.ERROR, "Insufficient permission"))
            connection.close()
        }
    }
}