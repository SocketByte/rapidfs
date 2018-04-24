package io.rapidfs.core

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryonet.Server
import com.esotericsoftware.minlog.Log
import com.esotericsoftware.minlog.Log.*
import io.rapidfs.core.command.CommandDatabase
import io.rapidfs.core.command.CommandHandler
import io.rapidfs.core.file.properties.ConfigResolver
import io.rapidfs.core.security.StackTraceHandler
import io.rapidfs.core.security.cryptography.Alghoritm
import io.rapidfs.core.security.cryptography.encrypt
import java.io.FileReader
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import io.rapidfs.core.command.Locker
import io.rapidfs.core.command.exceptions.ArgumentRequiredException
import io.rapidfs.core.command.exceptions.CommandNotFoundException
import io.rapidfs.core.command.executables.TestCommand
import io.rapidfs.core.file.DatabaseFactory
import io.rapidfs.core.listener.CommandListener
import io.rapidfs.core.listener.SecurityListener
import io.rapidfs.core.packet.RapidPacket
import io.rapidfs.core.packet.RapidPacketAuth
import io.rapidfs.core.packet.RapidPacketCommand
import io.rapidfs.core.security.SecurityProvider
import java.util.*
import java.io.InputStreamReader
import java.io.File




object RapidFS {

    private var WRITE_BUFFER = 8192
    private var READ_BUFFER = 8192

    lateinit var server:           Server            private set
    lateinit var mavenModel:       Model             private set
    lateinit var configResolver:   ConfigResolver    private set
    lateinit var properties:       Properties        private set
    lateinit var securityProvider: SecurityProvider  private set

    lateinit var databaseFactory:  DatabaseFactory   private set

    @JvmStatic
    fun main(args: Array<String>) {
        configResolver = ConfigResolver()
        configResolver.load()
        properties = configResolver.properties

        when (properties.getProperty("log_level", "INFO")) {
            "INFO" -> set(LEVEL_INFO)
            "DEBUG" -> set(LEVEL_DEBUG)
            "TRACE" -> set(LEVEL_TRACE)
            "ERROR" -> set(LEVEL_ERROR)
            "WARN" -> set(LEVEL_WARN)
            "NONE" -> set(LEVEL_NONE)
            else -> set(LEVEL_INFO)
        }

        val reader = MavenXpp3Reader()
        mavenModel = if (File("pom.xml").exists())
            reader.read(FileReader("pom.xml"))
        else reader.read(
                    InputStreamReader(
                            RapidFS::class.java.getResourceAsStream(
                                    "/META-INF/maven/io.rapidfs/rapidfs-core/pom.xml"
                            )
                    )
            )
        debug("Created a maven model (from pom.xml)")

        WRITE_BUFFER = properties
                .getProperty("write_buffer", "8192").toInt()
        WRITE_BUFFER = properties
                .getProperty("read_buffer", "8192").toInt()

        server = Server(WRITE_BUFFER, READ_BUFFER)
        info("Created a server instance")

        debug("Write buffer: $WRITE_BUFFER bytes per object")
        debug("Read buffer: $READ_BUFFER bytes per object")

        server.start()
        info("Started server successfully")

        val port = properties
                .getProperty("port", "8190").toInt()
        server.bind(port)
        debug("Bind port: $port")

        val kryo = server.kryo
        kryo.register(RapidPacket::class.java)
        kryo.register(RapidPacketAuth::class.java)
        kryo.register(RapidPacketCommand::class.java)
        info("Registered all packet classes")

        securityProvider = SecurityProvider()
        info("Created security provider")

        server.addListener(SecurityListener)
        server.addListener(CommandListener)
        debug("Registered SecurityListener")
        info("Registered all listeners")

        databaseFactory = DatabaseFactory()
        info("Created database factory")

        info("Ready")
        println()

        databaseFactory.load()
        if (databaseFactory.exist("test")) {
            val database = databaseFactory.getDatabase("test")

            info("test  -> ${database.get("test")}")
            info("test2 -> ${database.get("test2")}")

            val command = database.get("object") as RapidPacketCommand
            info("object -> password: ${command.command}")
        }
        else {
            val database = databaseFactory.createDatabase("test")

            database.set("test", "something")
            database.set("test2", 54)

            val command = RapidPacketCommand("test password")
            database.set("object", command)
        }

        CommandDatabase.addCommand(TestCommand)

        while (true) {
            if (!Locker.isLocked()) {
                print("/>")

                val scanner = Scanner(System.`in`)
                val input = scanner.nextLine()

                try {
                    CommandHandler.executeCommand(input)
                } catch (e: CommandNotFoundException) {
                    severe("Unknown command")

                    Locker.setLocked(false)
                } catch (e: ArgumentRequiredException) {
                    severe("Argument content can not be null [${e.message}]")

                    Locker.setLocked(false)
                } catch (e: NullPointerException) {
                    StackTraceHandler.handle(e)

                    Locker.setLocked(false)
                }

            }
        }

    }

}