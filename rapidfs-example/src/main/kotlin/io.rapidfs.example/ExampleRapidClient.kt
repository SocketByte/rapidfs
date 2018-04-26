package io.rapidfs.example

import io.rapidfs.api.RapidClient
import io.rapidfs.shared.RapidPacketDrop
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    val client = RapidClient("127.0.0.1", 8190)
            .connect()

    var start = System.currentTimeMillis()

    for (x in 0..100) {
        client.createOrThrow("test$x")

        for (i in 0..100) {
            client.set("test$x", "key$i", "${i*2}_test")
        }
    }

    var end = System.currentTimeMillis()
    var time = end - start

    println("Adding to DB: ${TimeUnit.MILLISECONDS.toSeconds(time)}s")

    start = System.currentTimeMillis()

    for (x in 0..100) {
        for (i in 0..100) {
            client.send("remove -db test$x -k key$i -nu")
        }
        client.send("finish -db test$x")
    }

    end = System.currentTimeMillis()
    time = end - start

    println("Removing from DB with -nu: ${TimeUnit.MILLISECONDS.toSeconds(time)}s")
}