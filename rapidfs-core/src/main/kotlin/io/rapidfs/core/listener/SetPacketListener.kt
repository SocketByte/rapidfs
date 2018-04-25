package io.rapidfs.core.listener

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import io.rapidfs.core.RapidFS
import io.rapidfs.shared.RapidPacketCallback
import io.rapidfs.shared.RapidPacketGet
import io.rapidfs.shared.RapidPacketSet
import io.rapidfs.shared.RapidResult

object SetPacketListener : Listener() {

    override fun received(connection: Connection?, packet: Any?) {
        if (packet == null || packet !is RapidPacketSet)
            return
        if (connection == null)
            return

        val key = packet.key
        val value = packet.value
        val db = packet.database

        val database = RapidFS.databaseFactory.getDatabase(db)

        if (database == null) {
            val callback = RapidPacketCallback(RapidResult.ERROR, "database with name $db is null")
            callback.callbackId = packet.callbackId
            connection.sendTCP(callback)
            return
        }

        database.set(key, value)

        val callback = RapidPacketCallback(RapidResult.SUCCESS)
        callback.callbackId = packet.callbackId
        connection.sendTCP(callback)
    }
}