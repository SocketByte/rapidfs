package io.rapidfs.example

import io.rapidfs.api.RapidClient
import io.rapidfs.shared.RapidPacketDrop

fun main(args: Array<String>) {
    val client = RapidClient("127.0.0.1", 8190)
            .connect()

    client.createOrThrow("test")
    println("After create")

    val testPacket = RapidPacketDrop("test")

    client.setOrThrow("test", "key", "value")
    client.setOrThrow("test", "key2", testPacket)
    println("After set")

    println(client.getOrThrow("test", "key"))
    println("After get")

    val packet = client.getOrThrow("test", "key2") as RapidPacketDrop
    println("Result: ${packet.database}")
}