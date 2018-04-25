package io.rapidfs.shared

import java.io.Serializable
import java.util.*
import java.util.concurrent.ThreadLocalRandom

abstract class RapidPacket : Serializable {
    val callbackId = ThreadLocalRandom
            .current()
            .nextLong(Long.MIN_VALUE, Long.MAX_VALUE)
}