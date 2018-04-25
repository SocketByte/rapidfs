package io.rapidfs.shared

import io.rapidfs.shared.RapidPacket

data class RapidPacketCommand (var command: String = "")
    : RapidPacket()