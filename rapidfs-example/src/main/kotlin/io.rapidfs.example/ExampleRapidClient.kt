package io.rapidfs.example

import io.rapidfs.api.Database
import io.rapidfs.api.RapidClient
import io.rapidfs.shared.RapidPacketDrop
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    val client = RapidClient("127.0.0.1", 8190)
            .connect()

    val database = Database(client, "test")
            .createOrThrow()

    println("Set")
    database.setOrThrow("key", "value", true)
    database.update()

    println("Remove")
    database.removeOrThrow("key", true)
    database.update(true)

    println("Drop")
    database.dropOrThrow()

    Thread.sleep(1000)

}