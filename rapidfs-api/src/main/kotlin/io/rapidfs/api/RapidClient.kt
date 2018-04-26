package io.rapidfs.api

import com.esotericsoftware.kryonet.Client
import io.rapidfs.api.callback.Callback
import io.rapidfs.api.callback.CallbackHandler
import io.rapidfs.shared.*

class RapidClient(private val address: String,
                  private val port: Int,
                  private val password: String = "") {

    lateinit var client: Client

    fun connect(timeout: Int = 4000): RapidClient {
        this.client = Client()
        this.client.start()
        this.client.connect(timeout, address, port)

        val kryo = client.kryo
        kryo.register(ArrayList::class.java)
        kryo.register(RapidPacket::class.java)
        kryo.register(RapidResult::class.java)
        kryo.register(RapidPacketAuth::class.java)
        kryo.register(RapidPacketCommand::class.java)
        kryo.register(RapidPacketSet::class.java)
        kryo.register(RapidPacketGet::class.java)
        kryo.register(RapidPacketDrop::class.java)
        kryo.register(RapidPacketCreate::class.java)
        kryo.register(RapidPacketRemove::class.java)
        kryo.register(RapidPacketCallback::class.java)

        this.client.addListener(ClientAdapter)
        return this
    }

    fun disconnect() {
        this.client.stop()
    }

    fun reconnect() {
        this.client.reconnect()
    }

    fun send(command: String) {
        val packet = RapidPacketCommand(command)
        client.sendTCP(packet)
    }

    fun get(database: String, key: String, callback: Callback) {
        val get = RapidPacketGet(database, key)
        CallbackHandler.make(this, get, callback)
    }

    fun getOrThrow(database: String, key: String): Any {
        val get = RapidPacketGet(database, key)
        val handler = CallbackHandler.make(this, get)

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
    }

    fun set(database: String, key: String, value: Any) {
        val set = RapidPacketSet(database, key, value)
        client.sendTCP(set)
    }

    fun setOrThrow(database: String, key: String, value: Any) {
        val set = RapidPacketSet(database, key, value)
        val handler = CallbackHandler.make(this, set)

        val packet = handler.get()
            ?: throw NullPointerException("callback can not be null")

        if (packet.result == RapidResult.ERROR)
            throw RapidException(packet.message)
    }

    fun createWithCallback(database: String, callback: Callback) {
        val create = RapidPacketCreate(database)
        CallbackHandler.make(this, create, callback)
    }

    fun create(database: String) {
        val create = RapidPacketCreate(database)
        client.sendTCP(create)
    }

    fun createOrThrow(database: String) {
        val create = RapidPacketCreate(database)
        val handler = CallbackHandler.make(this, create)

        val packet = handler.get()
                ?: throw NullPointerException("callback can not be null")

        if (packet.result == RapidResult.ERROR)
            throw RapidException(packet.message)
    }

    fun dropWithCallback(database: String, callback: Callback) {
        val drop = RapidPacketDrop(database)
        CallbackHandler.make(this, drop, callback)
    }

    fun drop(database: String) {
        val drop = RapidPacketDrop(database)
        client.sendTCP(drop)
    }

    fun dropOrThrow(database: String) {
        val drop = RapidPacketDrop(database)
        val handler = CallbackHandler.make(this, drop)

        val packet = handler.get()
                ?: throw NullPointerException("callback can not be null")

        if (packet.result == RapidResult.ERROR)
            throw RapidException(packet.message)
    }

    fun removeWithCallback(database: String, key: String, callback: Callback) {
        val remove = RapidPacketRemove(database, key)
        CallbackHandler.make(this, remove, callback)
    }

    fun remove(database: String, key: String) {
        val remove = RapidPacketRemove(database, key)
        client.sendTCP(remove)
    }

    fun removeOrThrow(database: String, key: String) {
        val remove = RapidPacketRemove(database, key)
        val handler = CallbackHandler.make(this, remove)

        val packet = handler.get()
                ?: throw NullPointerException("callback can not be null")

        if (packet.result == RapidResult.ERROR)
            throw RapidException(packet.message)
    }


}