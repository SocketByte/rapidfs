package io.rapidfs.core.listener

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import io.rapidfs.core.RapidFS
import io.rapidfs.shared.*

object DropPacketListener : Listener() {

    override fun received(connection: Connection?, packet: Any?) {
        if (packet == null || packet !is RapidPacketDrop)
            return
        if (connection == null)
            return

        val db = packet.database

        val result = RapidFS.databaseFactory.removeDatabase(db)
        if (!result) {
            val callback = RapidPacketCallback(RapidResult.ERROR, "database with name $db does not exist")
            callback.callbackId = packet.callbackId
            connection.sendTCP(callback)
            return
        }

        val callback = RapidPacketCallback(RapidResult.SUCCESS)
        callback.callbackId = packet.callbackId
        connection.sendTCP(callback)
    }
}