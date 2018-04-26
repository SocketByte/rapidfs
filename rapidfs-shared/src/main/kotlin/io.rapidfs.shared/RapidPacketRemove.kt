package io.rapidfs.shared

data class RapidPacketRemove(var database: String = "",
                             var key: String = "",
                             var noUpdate: Boolean = false)
    : RapidPacket()
