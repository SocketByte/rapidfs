package io.rapidfs.shared

import io.rapidfs.shared.RapidPacket

data class RapidPacketSet(var key: String, var value: Any)
    : RapidPacket()