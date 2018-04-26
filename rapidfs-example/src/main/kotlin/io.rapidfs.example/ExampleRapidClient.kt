package io.rapidfs.example

import io.rapidfs.api.RapidClient
import io.rapidfs.shared.RapidPacketDrop
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    val client = RapidClient("127.0.0.1", 8190)
            .connect()

    var addQueries = 0.0
    var remQueries = 0.0

    for (x in 0..100) {
        client.createOrThrow("test$x")
    }

    var start = System.currentTimeMillis()

    for (x in 0..100) {
        for (i in 0..100) {
            client.send("set -db test$x -k key$i -v ${i*2}_test -nu")
            addQueries++
        }
        Thread.sleep(3)
        client.send("update -db test$x")
    }

    var end = System.currentTimeMillis()
    var time = end - start

    println("Adding to DB: ${TimeUnit.MILLISECONDS.toMillis(time)}ms")
    println("Add Throughput:  ${addQueries / TimeUnit.MILLISECONDS.toMillis(time).toDouble()} per ms")

    start = System.currentTimeMillis()

    for (x in 0..100) {
        for (i in 0..100) {
            client.send("remove -db test$x -k key$i -nu")
            remQueries++
        }
        Thread.sleep(3)
        client.send("update -db test$x -rem")
    }

    end = System.currentTimeMillis()
    time = end - start

    println("Removing from DB: ${TimeUnit.MILLISECONDS.toMillis(time)}ms")
    println("Remove Throughput:  ${remQueries / TimeUnit.MILLISECONDS.toMillis(time).toDouble()} per ms")
}