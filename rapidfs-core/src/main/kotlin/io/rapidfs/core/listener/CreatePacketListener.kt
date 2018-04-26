package io.rapidfs.core.listener

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import io.rapidfs.core.RapidFS
import io.rapidfs.shared.RapidPacketCallback
import io.rapidfs.shared.RapidPacketCreate
import io.rapidfs.shared.RapidPacketGet
import io.rapidfs.shared.RapidResult

object CreatePacketListener : Listener() {

    override fun received(connection: Connection?, packet: Any?) {
        if (packet == null || packet !is RapidPacketCreate)
            return
        if (connection == null)
            return

        val db = packet.database

        RapidFS.databaseFactory.createDatabase(db)

        val callback = RapidPacketCallback(RapidResult.SUCCESS)
        callback.callbackId = packet.callbackId
        connection.sendTCP(callback)
    }
}