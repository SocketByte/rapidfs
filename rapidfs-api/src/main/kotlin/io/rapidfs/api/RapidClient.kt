package io.rapidfs.api

import com.esotericsoftware.kryonet.Client
import io.rapidfs.api.callback.Callback
import io.rapidfs.api.callback.CallbackHandler
import io.rapidfs.shared.*

class RapidClient(private val address: String,
                  private val port: Int,
                  private val password: String = "") {

    lateinit var client: Client

    fun connect(timeout: Int = 4000) {
        this.client = Client()
        this.client.connect(timeout, address, port)
    }

    fun disconnect() {
        this.client.stop()
    }

    fun reconnect() {
        this.client.reconnect()
    }

    fun get(database: String, key: String, callback: Callback) {
        val get = RapidPacketGet(key, database)
        CallbackHandler.make(this, get, callback)
                .sendAndPush()
    }

    fun getOrThrow(database: String, key: String): Any {
        val get = RapidPacketGet(key, database)
        val handler = CallbackHandler.make(this, get)

        handler.sendAndPush()
        val packet = handler.get()
                ?: throw NullPointerException("callback can not be null")

        if (packet.result == RapidResult.ERROR)
            throw RapidException(packet.message)

        if (packet.data.size == 0)
            throw RapidException("callback data can not be 0")

        return packet.data[0]
    }

    fun setWithCallback(database: String, key: String, value: Any, callback: Callback) {
        val set = RapidPacketSet(database, key, value)
        CallbackHandler.make(this, set, callback)
                .sendAndPush()
    }

    fun set(database: String, key: String, value: Any) {
        val set = RapidPacketSet(database, key, value)
        client.sendTCP(set)
    }

    fun setOrThrow(database: String, key: String, value: Any) {
        val set = RapidPacketSet(database, key, value)
        val handler = CallbackHandler.make(this, set)

        handler.sendAndPush()
        val packet = handler.get()
            ?: throw NullPointerException("callback can not be null")

        if (packet.result == RapidResult.ERROR)
            throw RapidException(packet.message)
    }

    fun createWithCallback(database: String, callback: Callback) {
        val create = RapidPacketCreate(database)
        CallbackHandler.make(this, create, callback)
                .sendAndPush()
    }

    fun create(database: String) {
        val create = RapidPacketCreate(database)
        client.sendTCP(create)
    }

    fun createOrThrow(database: String) {
        val create = RapidPacketCreate(database)
        val handler = CallbackHandler.make(this, create)

        handler.sendAndPush()
        val packet = handler.get()
                ?: throw NullPointerException("callback can not be null")

        if (packet.result == RapidResult.ERROR)
            throw RapidException(packet.message)
    }

    fun dropWithCallback(database: String, callback: Callback) {
        val drop = RapidPacketDrop(database)
        CallbackHandler.make(this, drop, callback)
                .sendAndPush()
    }

    fun drop(database: String) {
        val drop = RapidPacketDrop(database)
        client.sendTCP(drop)
    }

    fun dropOrThrow(database: String) {
        val drop = RapidPacketDrop(database)
        val handler = CallbackHandler.make(this, drop)

        handler.sendAndPush()
        val packet = handler.get()
                ?: throw NullPointerException("callback can not be null")

        if (packet.result == RapidResult.ERROR)
            throw RapidException(packet.message)
    }

    fun removeWithCallback(database: String, key: String, callback: Callback) {
        val remove = RapidPacketRemove(database, key)
        CallbackHandler.make(this, remove, callback)
                .sendAndPush()
    }

    fun remove(database: String, key: String) {
        val remove = RapidPacketRemove(database, key)
        client.sendTCP(remove)
    }

    fun removeOrThrow(database: String, key: String) {
        val remove = RapidPacketRemove(database, key)
        val handler = CallbackHandler.make(this, remove)

        handler.sendAndPush()
        val packet = handler.get()
                ?: throw NullPointerException("callback can not be null")

        if (packet.result == RapidResult.ERROR)
            throw RapidException(packet.message)
    }


}