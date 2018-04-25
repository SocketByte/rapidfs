package io.rapidfs.api.callback

import io.rapidfs.shared.RapidPacketCallback

interface Callback {
    fun execute(callback: RapidPacketCallback)
}