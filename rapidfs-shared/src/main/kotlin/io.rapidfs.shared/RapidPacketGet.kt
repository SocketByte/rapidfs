package io.rapidfs.shared

import io.rapidfs.shared.RapidPacket

data class RapidPacketGet(var database: String = "", var key: String = "")
    : RapidPacket()
