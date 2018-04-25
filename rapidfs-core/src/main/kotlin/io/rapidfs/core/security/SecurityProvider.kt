package io.rapidfs.core.security

import com.esotericsoftware.kryonet.Connection
import io.rapidfs.shared.RapidPacketAuth

class SecurityProvider {
    private val authorizedClients = mutableListOf<Connection>()

    fun authorize(connection: Connection) =
            this.authorizedClients.add(connection)

    fun deauthorize(connection: Connection) =
            this.authorizedClients.remove(connection)

    fun notAuthorized(packet: Any, connection: Connection): Boolean {
        return !this.authorizedClients.contains(connection) && packet !is RapidPacketAuth
    }
}