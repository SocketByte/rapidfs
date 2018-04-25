package io.rapidfs.shared

import io.rapidfs.shared.RapidPacket

data class RapidPacketGet(var key: String)
    : RapidPacket()
