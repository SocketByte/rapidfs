package io.rapidfs.shared

import io.rapidfs.shared.RapidPacket
import io.rapidfs.shared.RapidResult

data class RapidPacketCallback (
        var result: RapidResult,
        var message: String = "no message provided",
        val data: MutableList<Any> = mutableListOf())
    : RapidPacket()
