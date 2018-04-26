package io.rapidfs.core.file

import com.esotericsoftware.minlog.Log.*
import java.io.File
import com.google.common.base.Splitter
import com.sun.org.apache.xml.internal.security.utils.Base64
import io.rapidfs.core.security.StackTraceHandler
import io.rapidfs.core.security.serialization.ObjectSerializer
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import jdk.nashorn.internal.objects.NativeArray.forEach
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream
import java.nio.file.DirectoryIteratorException
import java.nio.file.DirectoryStream

open class DatabaseFactory {

    private val databaseInfo = mutableMapOf<String, Database>()

    private fun size() = databaseInfo.size

    fun load() {
        val fpath = File(".").canonicalPath
        val dirPath = "$fpath/databases/"
        val dir = File(dirPath)
        dir.mkdirs()
        trace("Created /databases/ folder")

        try {
            Files.newDirectoryStream(Paths.get(dirPath)).use { stream ->
                for (path in stream) {
                    val file = path.toFile()
                    val fileName = file.name.replace(".rapid", "")
                    var split = Splitter.on("_").split(fileName).toList()
                    val name = split[1]
                    val id = split[0].replace("db", "").toInt()

                    info("Reading ${file.name}, database: $name ($id)")

                    val database = Database(file, name, id)

                    for (line in file.readLines(StandardCharsets.UTF_8)) {
                        split = Splitter
                                .on(':')
                                .split(line)
                                .toList()
                        val result =
                                if (split[1].startsWith(ObjectSerializer.serializedPrefix))
                                    ObjectSerializer.deserialize(Base64.decode(split[1]
                                            .replace(ObjectSerializer.serializedPrefix, "")))
                                else split[1]


                        database.dynamicMap[split[0]] = result
                        database.writtenKeys.add(split[0])
                        debug("Read: ${split[0]}")
                    }

                    databaseInfo[name] = database
                }
            }
        } catch (e: Exception) {
            StackTraceHandler.handle(e)
        }

    }

    fun createDatabase(name: String): Database {
        val dirPath = File(".").canonicalPath + "/databases/"

        val path = "$dirPath/db${size()}_$name.rapid"
        val file = File(path)
        file.createNewFile()
        debug("Created file at $path")

        val database = Database(file, name, size())
        databaseInfo[name] = database
        debug("Created database $name (id: ${database.id})")

        return database
    }

    fun removeDatabase(name: String): Boolean {
        getDatabase(name) ?: return false
        databaseInfo.remove(name)
        return true
    }

    fun exist(name: String): Boolean {
        return databaseInfo.containsKey(name)
    }

    fun getDatabaseOrThrow(name: String): Database {
        return databaseInfo[name]
                ?: throw NullPointerException("database with name $name does not exist")
    }

    fun getDatabase(name: String): Database? {
        return if (databaseInfo.containsKey(name))
            databaseInfo[name]
        else null
    }

}