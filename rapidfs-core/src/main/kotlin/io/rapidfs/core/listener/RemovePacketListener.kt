package io.rapidfs.core.listener

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import io.rapidfs.core.RapidFS
import io.rapidfs.shared.RapidPacketCallback
import io.rapidfs.shared.RapidPacketGet
import io.rapidfs.shared.RapidPacketRemove
import io.rapidfs.shared.RapidResult

object RemovePacketListener : Listener() {

    override fun received(connection: Connection?, packet: Any?) {
        if (packet == null || packet !is RapidPacketRemove)
            return
        if (connection == null)
            return

        val key = packet.key
        val db = packet.database

        val database = RapidFS.databaseFactory.getDatabase(db)

        if (database == null) {
            val callback = RapidPacketCallback(RapidResult.ERROR, "database with name $db is null")

            connection.sendTCP(callback)
            return
        }

        val result = database.get(key)
        if (result == null) {
            val callback = RapidPacketCallback(RapidResult.ERROR, "key with name $key is null")

            connection.sendTCP(callback)
            return
        }

        database.remove(key)

        val callback = RapidPacketCallback(RapidResult.SUCCESS)
        connection.sendTCP(callback)
    }
}