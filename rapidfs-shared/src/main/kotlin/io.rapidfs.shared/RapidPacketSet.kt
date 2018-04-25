package io.rapidfs.shared

import io.rapidfs.shared.RapidPacket

data class RapidPacketSet(var database: String = "", var key: String = "", var value: Any = "")
    : RapidPacket()