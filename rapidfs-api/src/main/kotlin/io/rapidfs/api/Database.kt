package io.rapidfs.api

import io.rapidfs.api.callback.Callback

data class Database (private val client: RapidClient,
                     private val databaseName: String) {

    fun update(removeAction: Boolean = false) {
        client.update(databaseName, removeAction)
    }

    fun get(key: String, callback: Callback) {
        return client.get(databaseName, key, callback)
    }

    fun getOrThrow(key: String) = client.getOrThrow(databaseName, key)

    fun setWithCallback(key: String, value: Any, callback: Callback, noUpdate: Boolean = false) {
        client.setWithCallback(databaseName, key, value, callback, noUpdate)
    }

    fun set(key: String, value: Any, noUpdate: Boolean = false) {
        client.set(databaseName, key, value, noUpdate)
    }

    fun setOrThrow(key: String, value: Any, noUpdate: Boolean = false) {
        client.setOrThrow(databaseName, key, value, noUpdate)
    }

    fun createWithCallback(callback: Callback): Database {
        client.createWithCallback(databaseName, callback)
        return this
    }

    fun create(): Database {
        client.create(databaseName)
        return this
    }

    fun createOrThrow(): Database {
        client.createOrThrow(databaseName)
        return this
    }

    fun dropWithCallback(callback: Callback) {
        client.dropWithCallback(databaseName, callback)
    }

    fun drop() {
        client.drop(databaseName)
    }

    fun dropOrThrow() {
        client.dropOrThrow(databaseName)
    }

    fun removeWithCallback(key: String, callback: Callback, noUpdate: Boolean = false) {
        client.removeWithCallback(databaseName, key, callback, noUpdate)
    }

    fun remove(key: String, noUpdate: Boolean = false) {
        client.remove(databaseName, key, noUpdate)
    }

    fun removeOrThrow(key: String, noUpdate: Boolean = false) {
        client.removeOrThrow(databaseName, key, noUpdate)
    }

}