package io.rapidfs.core.listener

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import io.rapidfs.core.RapidFS
import io.rapidfs.shared.RapidPacketCallback
import io.rapidfs.shared.RapidPacketGet
import io.rapidfs.shared.RapidResult

object GetPacketListener : Listener() {

    override fun received(connection: Connection?, packet: Any?) {
        if (packet == null || packet !is RapidPacketGet)
            return
        if (connection == null)
            return

        val key = packet.key
        val db = packet.database

        val database = RapidFS.databaseFactory.getDatabase(db)

        if (database == null) {
            val callback = RapidPacketCallback(RapidResult.ERROR, "database with name $db is null")
            callback.callbackId = packet.callbackId
            connection.sendTCP(callback)
            return
        }

        val result = database.get(key)
        if (result == null) {
            val callback = RapidPacketCallback(RapidResult.ERROR, "key with name $key is null")
            callback.callbackId = packet.callbackId
            connection.sendTCP(callback)
            return
        }

        val callback = RapidPacketCallback(RapidResult.SUCCESS)
        callback.callbackId = packet.callbackId
        callback.data.add(result)
        connection.sendTCP(callback)
    }
}