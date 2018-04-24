package io.rapidfs.core.listener

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import io.rapidfs.core.RapidFS
import io.rapidfs.core.command.CommandHandler
import io.rapidfs.core.packet.RapidPacketCommand

object CommandListener : Listener() {
    override fun received(connection: Connection?, packet: Any?) {
        if (packet !is RapidPacketCommand)
            return

        CommandHandler.executeCommand(packet.command)
    }
}