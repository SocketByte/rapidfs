package io.rapidfs.core.security

import com.esotericsoftware.kryonet.Connection

class SecurityProvider {
    private val authorizedClients = mutableListOf<Connection>()

    fun authorize(connection: Connection) =
            this.authorizedClients.add(connection)

    fun deauthorize(connection: Connection) =
            this.authorizedClients.remove(connection)

    fun isAuthorized(connection: Connection): Boolean =
            this.authorizedClients.contains(connection)
}