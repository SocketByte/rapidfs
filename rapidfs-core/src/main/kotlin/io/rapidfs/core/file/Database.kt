package io.rapidfs.core.file

import com.esotericsoftware.minlog.Log.debug
import io.rapidfs.core.security.serialization.ObjectSerializer
import java.io.*
import java.io.FileWriter
import java.io.BufferedWriter
import java.nio.charset.StandardCharsets
import java.util.*


open class Database(val file: File,
               val name: String,
               val id: Int = 0,
               private val buffer: Int = 8192) {

    val dynamicMap = mutableMapOf<String, Any?>()
    val writtenKeys = mutableListOf<String>()

    private fun update(remove: Boolean = false) {
        if (remove)
            file.writeText("", StandardCharsets.UTF_8)
        for ((key, value) in dynamicMap) {
            if (!remove && writtenKeys.contains(key))
                continue

            val result =
                    if (value is Serializable)
                        ObjectSerializer.serializedPrefix +
                                String(Base64.getEncoder().encode(ObjectSerializer.serialize(value)))
                    else value.toString()
            val toWrite = "$key:$result\n"
            file.appendText(toWrite, StandardCharsets.UTF_8)

            if (!remove)
                writtenKeys.add(key)

            debug("Database #$id", "Write: ${toWrite.replace("\n", "")}")
        }
    }

    fun set(key: String, value: Any?) {
        this.dynamicMap[key] = value
        update()

        debug("Database #$id", "Set '$key' = '$value'")
    }

    fun get(key: String): Any? {
        return this.dynamicMap[key]
    }

    fun remove(key: String): Boolean {
        this.dynamicMap.remove(key)
        update(true)

        debug("Database #$id", "Removed '$key'")

        return !this.dynamicMap.containsKey(key)
    }

    fun remove(value: Any?): Boolean {
        this.dynamicMap.forEach {
            (key, otherValue) ->
                if (value != null && value == otherValue)
                    return remove(key)
        }
        return false
    }

    fun clear() {
        this.dynamicMap.clear()

        update()
        debug("Database #$id", "Cleared")
    }

}