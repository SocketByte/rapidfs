package io.rapidfs.shared

import io.rapidfs.shared.RapidPacket

data class RapidPacketAuth(var password: String = "")
    : RapidPacket()