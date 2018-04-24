package io.rapidfs.core.file.properties

import com.esotericsoftware.minlog.Log.warn
import io.rapidfs.core.security.StackTraceHandler
import java.util.*
import jdk.nashorn.internal.objects.ArrayBufferView.buffer
import java.io.*


class ConfigResolver {

    private val fileName = "server.properties"

    private lateinit var inputStream: FileInputStream
    private lateinit var fileWriter: FileWriter

    lateinit var properties: Properties
        private set

    fun load() {
        this.properties = Properties()

        val file = File(fileName)
        if (!file.exists())
            file.createNewFile()

        this.inputStream = FileInputStream(file)
        this.fileWriter = FileWriter(file, true)
        this.properties.load(inputStream)
    }

    fun save() = this.properties.store(fileWriter, null)

}