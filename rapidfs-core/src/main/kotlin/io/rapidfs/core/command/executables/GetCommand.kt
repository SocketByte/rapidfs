package io.rapidfs.core.command.executables

import com.esotericsoftware.minlog.Log.info
import io.rapidfs.core.RapidFS
import io.rapidfs.core.command.Command
import io.rapidfs.core.command.CommandBuilder
import io.rapidfs.core.command.ExecutableCommand

object GetCommand : ExecutableCommand() {
    override fun execute(command: Command) {
        val dbArgument = command.getArgument("-db")
        val keyArgument = command.getArgument("-k")

        val db = dbArgument?.content
        val key = keyArgument?.content

        val database = RapidFS.databaseFactory.getDatabase(db!!)

        info("Result from database ${database?.name} and key $key: \n${database?.get(key!!).toString()}")
    }

    override fun getBuilder(): CommandBuilder {
        return CommandBuilder("get")
                .addArgument("-db")
                .addArgument("-k")
                .setExecutable(this)
    }
}