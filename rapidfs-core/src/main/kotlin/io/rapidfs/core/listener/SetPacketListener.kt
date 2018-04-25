package io.rapidfs.core.listener

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import io.rapidfs.shared.RapidPacketSet

object SetPacketListener : Listener() {

    override fun received(connection: Connection?, packet: Any?) {
        if (packet != null && packet !is RapidPacketSet)
            return


    }
}